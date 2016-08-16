/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package getopendata;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.http.HttpResponse;
import org.json.JSONException;
import org.xml.sax.SAXException;

/**
 *
 * @author kizax
 */
public class GetOpenDataTask implements Runnable {

    private final String csvFileName;
    private final FileWriter logFileWriter;
    private Map<Integer, String> itemMap;
    private Map<Integer, String> siteMap;
    private Date specificDate;

    public GetOpenDataTask(String resultCsvFileName, FileWriter logFileWriter, Map itemMap, Map siteMap, Date specificDate) {
        this.csvFileName = resultCsvFileName;
        this.logFileWriter = logFileWriter;
        this.itemMap = itemMap;
        this.siteMap = siteMap;
        this.specificDate = specificDate;
    }

    @Override
    public void run() {
        try {

            int offset = 0;
            int limit = 1000;

            LogUtils.log(logFileWriter, String.format("%1$s\tTarget date: %2$s", TimestampUtils.getTimestampStr(), TimestampUtils.dateToStr(specificDate)));

            Map<String, AirQualityData> airQualityDataMap = new HashMap();

            while (true) {

                LogUtils.log(logFileWriter, String.format("%1$s\tBus data is downloading now, offset %2$d, limit %3$d.", TimestampUtils.getTimestampStr(), offset, limit));

                String airQualityDataUrl = String.format(DataLinks.airQualityDataUrl, offset, limit);
                LogUtils.log(logFileWriter, String.format("%1$s\tairQualityDataUrl: %2$s", TimestampUtils.getTimestampStr(), airQualityDataUrl));

                HttpResponse airQualityDataHttpResponse = HttpUtils.httpGet(airQualityDataUrl);

                String airQualityDataJsonStr = getStrFromResponse(airQualityDataHttpResponse);
                ArrayList<AirQualityData> airQualityDataList = AirQualityJsonParser.getAirQualityDataList(airQualityDataJsonStr);

                LogUtils.log(logFileWriter, String.format("%1$s\tNum of air quality data: %2$d", TimestampUtils.getTimestampStr(), airQualityDataList.size()));

                //看看是否已有紀錄，若還沒有，則放入airQualityDataMap
                int puttingCount = 0;
                for (AirQualityData airQualityData : airQualityDataList) {
                    if (itemMap.containsKey(airQualityData.getItemId())
                            && airQualityData.getMonitorDateStr().equals(TimestampUtils.dateToStr(specificDate))
                            && !airQualityDataMap.containsKey(airQualityData.getSiteId() + "," + airQualityData.getItemId())) {
                        airQualityDataMap.put(airQualityData.getSiteId() + "," + airQualityData.getItemId(), airQualityData);

                        puttingCount++;
                    }
                }
                LogUtils.log(logFileWriter, String.format("%1$s\tPut %2$d data into airQualityDataMap", TimestampUtils.getTimestampStr(), puttingCount));

                AirQualityData lastAirQualityData = airQualityDataList.get(airQualityDataList.size() - 1);
                LogUtils.log(logFileWriter, String.format("%1$s\tLast data's date is  %2$s", TimestampUtils.getTimestampStr(), lastAirQualityData.getMonitorDateStr()));
                if (lastAirQualityData.getMonitorDate().before(specificDate)) {
                    LogUtils.log(logFileWriter, String.format("%1$s\tStop catching data", TimestampUtils.getTimestampStr()));
                    break;
                } else {
                    offset += limit;
                    LogUtils.log(logFileWriter, String.format("%1$s\tContinually catch data", TimestampUtils.getTimestampStr()));
                }

            }

            //補值
            LogUtils.log(logFileWriter, String.format("%1$s\tNow have %2$d data", TimestampUtils.getTimestampStr(), airQualityDataMap.values().size()));
            for (int siteId : siteMap.keySet()) {
                for (int itemId : itemMap.keySet()) {
                    if (!airQualityDataMap.containsKey(siteId + "," + itemId)) {

                        String dataTimeStr = TimestampUtils.dateToStr(specificDate);
                        DateFormat dataFromat = new SimpleDateFormat("yyyy/M/d");
                        Date monitorDate = dataFromat.parse(dataTimeStr);
                        AirQualityData dummyAirQualityData = new AirQualityData(siteId, siteMap.get(siteId), itemId, itemMap.get(itemId), monitorDate);

                        airQualityDataMap.put(siteId + "," + itemId, dummyAirQualityData);
                    }
                }
            }
            LogUtils.log(logFileWriter, String.format("%1$s\tAfter filling up with dummy data, now have %2$d data", TimestampUtils.getTimestampStr(), airQualityDataMap.values().size()));

            //清除錯誤值
            clearAllErrorValue(siteMap, itemMap, airQualityDataMap);

            //建立紀錄檔
            LogUtils.log(logFileWriter, String.format("%1$s\tNow start writing data into file", TimestampUtils.getTimestampStr()));

            File csvDataFile = new File(csvFileName);

            if (!csvDataFile.getParentFile().exists()) {
                csvDataFile.getParentFile().mkdirs();
            }
            FileWriter csvFileWriter
                    = new FileWriter(csvDataFile, true);

            //寫入檔頭BOM，避免EXCEL開啟變成亂碼
            byte[] bom = new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};
            csvFileWriter.write(new String(bom));

            //寫入紀錄檔
            int writingCount = 0;
            for (AirQualityData airQualityData : airQualityDataMap.values()) {

                writeCsvFile(csvFileWriter, airQualityData.getRecordStr());

                writingCount++;

            }

            LogUtils.log(logFileWriter, String.format("%1$s\tSuccessfully writing %2$d data into record file", TimestampUtils.getTimestampStr(), writingCount));

            
            
            
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            LogUtils.log(logFileWriter, String.format("%1$s\t%2$s", TimestampUtils.getTimestampStr(), ex));
        } catch (SAXException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            LogUtils.log(logFileWriter, String.format("%1$s\t%2$s", TimestampUtils.getTimestampStr(), ex));
        } catch (ParseException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            LogUtils.log(logFileWriter, String.format("%1$s\t%2$s", TimestampUtils.getTimestampStr(), ex));
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(GetOpenDataTask.class.getName()).log(Level.SEVERE, null, ex);
            LogUtils.log(logFileWriter, String.format("%1$s\t%2$s", TimestampUtils.getTimestampStr(), ex));
        } catch (JSONException ex) {
            Logger.getLogger(GetOpenDataTask.class.getName()).log(Level.SEVERE, null, ex);
            LogUtils.log(logFileWriter, String.format("%1$s\t%2$s", TimestampUtils.getTimestampStr(), ex));
        }

    }

    private void writeCsvFile(FileWriter csvFileWriter, String record) {
        WriteThread writerThread = new WriteThread(csvFileWriter, record);
        writerThread.start();
    }

    private String getStrFromResponse(HttpResponse response) throws IOException {
        InputStream inputStream = response.getEntity().getContent();

        StringBuilder inputStringBuilder = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));

        String jsonStr = "";
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            inputStringBuilder.append(line);
            inputStringBuilder.append('\n');
            jsonStr += (line + '\n');
        }
        return jsonStr;
    }

    public static void clearAllErrorValue(Map<Integer, String> siteMap, Map<Integer, String> itemMap, Map<String, AirQualityData> airQualityDataMap) {

        final int AMB_TEMP_ITEM_ID = 14;
        final int CO_ITEM_ID = 2;
        final int NO_ITEM_ID = 6;
        final int NO2_ITEM_ID = 7;
        final int NOX_ITEM_ID = 5;
        final int O3_ITEM_ID = 3;
        final int PM10_ITEM_ID = 4;
        final int PM25_ITEM_ID = 33;
        final int RAINF_ITEM_ID = 23;
        final int RH_ITEM_ID = 38;
        final int SO2_ITEM_ID = 1;

        //AMB_T	<6.4 ,修正為missing
        //AMB_T	>43.5 ,修正為missing
        final float AMB_TEMP_UPPER_BOUND = 43.5f;
        final float AMB_TEMP_LOWER_BOUND = 6.4f;
        clearErrorValue(airQualityDataMap, siteMap, AMB_TEMP_ITEM_ID, AMB_TEMP_UPPER_BOUND, AMB_TEMP_LOWER_BOUND);

        //CO >50.5 修正為missing
        final float CO_UPPER_BOUND = 50.5f;
        clearErrorValue(airQualityDataMap, siteMap, CO_ITEM_ID, CO_UPPER_BOUND);

        //NO >505 修正為missing
        final float NO_UPPER_BOUND = 505f;
        clearErrorValue(airQualityDataMap, siteMap, NO_ITEM_ID, NO_UPPER_BOUND);

        //NO2 >505 修正為missing
        final float NO2_UPPER_BOUND = 505f;
        clearErrorValue(airQualityDataMap, siteMap, NO2_ITEM_ID, NO2_UPPER_BOUND);

        //NOX >505 修正為missing
        final float NOX_UPPER_BOUND = 505f;
        clearErrorValue(airQualityDataMap, siteMap, NOX_ITEM_ID, NOX_UPPER_BOUND);

        //O3 >505 修正為missing
        final float O3_UPPER_BOUND = 505f;
        clearErrorValue(airQualityDataMap, siteMap, O3_ITEM_ID, O3_UPPER_BOUND);

        //PM10 >10200 修正為missing
        final float PM10_UPPER_BOUND = 10200f;
        clearErrorValue(airQualityDataMap, siteMap, PM10_ITEM_ID, PM10_UPPER_BOUND);

        //PM2.5 >10200 修正為missing
        final float PM25_UPPER_BOUND = 10200f;
        clearErrorValue(airQualityDataMap, siteMap, PM25_ITEM_ID, PM25_UPPER_BOUND);

        //PM2.5 逐日同小時之PM2.5若>PM10, 修正為missing
        clearErrorValue(airQualityDataMap, siteMap);

        //rainf 若有"NR" , 修正為0
        clearErrorValue(airQualityDataMap, siteMap, RAINF_ITEM_ID);

        //RH <0 , 修正為missing
        //RH >100 , 修正為missing
        final float RH_UPPER_BOUND = 100f;
        final float RH_LOWER_BOUND = 0f;
        clearErrorValue(airQualityDataMap, siteMap, RH_ITEM_ID, RH_UPPER_BOUND, RH_LOWER_BOUND);

        //SO2 >505,  修正為missing
        final float SO2_UPPER_BOUND = 505f;
        clearErrorValue(airQualityDataMap, siteMap, SO2_ITEM_ID, SO2_UPPER_BOUND);

    }

    private static void clearErrorValue(Map<String, AirQualityData> airQualityDataMap, Map<Integer, String> siteMap, int itemId, float upperBound, float lowerBound) {
        final int HOURS_IN_DAY = 24;
        final String MISSING_STR = "";

        for (int siteId : siteMap.keySet()) {
            AirQualityData airQualityData = airQualityDataMap.get(siteId + "," + itemId);
            if (null != airQualityData) {
                for (int i = 0; i < HOURS_IN_DAY; i++) {
                    try {
                        float monitorValue = Float.valueOf(airQualityData.getMonitorValue(i));
                        if (monitorValue < lowerBound || monitorValue > upperBound) {
                            airQualityData.setMonitorValue(i, MISSING_STR);
                        }
                    } catch (NumberFormatException e) {
                    }
                }
            }
        }
    }

    private static void clearErrorValue(Map<String, AirQualityData> airQualityDataMap, Map<Integer, String> siteMap, int itemId, float upperBound) {
        final int HOURS_IN_DAY = 24;
        final String MISSING_STR = "";

        for (int siteId : siteMap.keySet()) {
            AirQualityData airQualityData = airQualityDataMap.get(siteId + "," + itemId);
            if (null != airQualityData) {
                for (int i = 0; i < HOURS_IN_DAY; i++) {
                    try {
                        float monitorValue = Float.valueOf(airQualityData.getMonitorValue(i));
                        if (monitorValue > upperBound) {
                            airQualityData.setMonitorValue(i, MISSING_STR);
                        }
                    } catch (NumberFormatException e) {
                    }
                }
            }
        }
    }

    private static void clearErrorValue(Map<String, AirQualityData> airQualityDataMap, Map<Integer, String> siteMap) {
        final int HOURS_IN_DAY = 24;
        final String MISSING_STR = "";
        final int PM10_ITEM_ID = 4;
        final int PM25_ITEM_ID = 33;

        //PM2.5 逐日同小時之PM2.5若>PM10, 修正為missing
        for (int siteId : siteMap.keySet()) {
            AirQualityData pm10AirQualityData = airQualityDataMap.get(siteId + "," + PM10_ITEM_ID);
            AirQualityData pm25AirQualityData = airQualityDataMap.get(siteId + "," + PM25_ITEM_ID);
            if (null != pm10AirQualityData) {
                for (int i = 0; i < HOURS_IN_DAY; i++) {
                    try {
                        float pm10 = Float.valueOf(pm10AirQualityData.getMonitorValue(i));
                        float pm25 = Float.valueOf(pm25AirQualityData.getMonitorValue(i));
                        if (pm25 > pm10) {
                            pm25AirQualityData.setMonitorValue(i, MISSING_STR);
                        }
                    } catch (NumberFormatException e) {
                    }
                }
            }
        }
    }

    private static void clearErrorValue(Map<String, AirQualityData> airQualityDataMap, Map<Integer, String> siteMap, int itemId) {
        final int HOURS_IN_DAY = 24;
        final String MISSING_STR = "0";

        //rainf 若有"NR" , 修正為0
        for (int siteId : siteMap.keySet()) {
            AirQualityData airQualityData = airQualityDataMap.get(siteId + "," + itemId);
            if (null != airQualityData) {
                for (int i = 0; i < HOURS_IN_DAY; i++) {
                    try {
                        String monitorValue = airQualityData.getMonitorValue(i);
                        if (monitorValue.contains("NR")) {
                            airQualityData.setMonitorValue(i, MISSING_STR);
                        }
                    } catch (NumberFormatException e) {
                    }
                }
            }
        }
    }

}
