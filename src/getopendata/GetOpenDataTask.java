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

    private final String historyCsvFileName;
    private final String resultCsvFileName;
    private final FileWriter logFileWriter;
    private Map<Integer, String> itemIdMap;
    private Map<Integer, String> siteMap;
    private Date specificDate;

    public GetOpenDataTask(String historyCsvFileName, String resultCsvFileName,
            FileWriter logFileWriter, Map itemIdMap, Map siteMap, Date specificDate) {
        this.historyCsvFileName = historyCsvFileName;
        this.resultCsvFileName = resultCsvFileName;
        this.logFileWriter = logFileWriter;
        this.itemIdMap = itemIdMap;
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
                    if (itemIdMap.containsKey(airQualityData.getItemId())
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

            //補上dummy值
            LogUtils.log(logFileWriter, String.format("%1$s\tNow have %2$d data", TimestampUtils.getTimestampStr(), airQualityDataMap.values().size()));
            for (int siteId : siteMap.keySet()) {
                for (int itemId : itemIdMap.keySet()) {
                    if (!airQualityDataMap.containsKey(siteId + "," + itemId)) {

                        String dataTimeStr = TimestampUtils.dateToStr(specificDate);
                        DateFormat dataFromat = new SimpleDateFormat("yyyy/M/d");
                        Date monitorDate = dataFromat.parse(dataTimeStr);
                        AirQualityData dummyAirQualityData = new AirQualityData(siteId, siteMap.get(siteId), itemId, itemIdMap.get(itemId), monitorDate);

                        airQualityDataMap.put(siteId + "," + itemId, dummyAirQualityData);
                    }
                }
            }
            LogUtils.log(logFileWriter, String.format("%1$s\tAfter filling up with dummy data, now have %2$d data", TimestampUtils.getTimestampStr(), airQualityDataMap.values().size()));

            //清除錯誤值
            clearAllErrorValue(siteMap, itemIdMap, airQualityDataMap);

            //建立紀錄檔
            LogUtils.log(logFileWriter, String.format("%1$s\tNow start writing data into file", TimestampUtils.getTimestampStr()));

            File csvDataFile = new File(resultCsvFileName);

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

            ///////////////////////////////////
            //建立itemMap
            Map<String, Integer> itemMap = new HashMap();
            itemMap.put("SO2", 1);
            itemMap.put("CO", 2);
            itemMap.put("O3", 3);
            itemMap.put("PM10", 4);
            itemMap.put("NOx", 5);

            itemMap.put("NO", 6);
            itemMap.put("NO2", 7);
            itemMap.put("AMB_TEMP", 14);
            itemMap.put("RAINFALL", 23);
            itemMap.put("PM2.5", 33);

            itemMap.put("RH", 38);

            ArrayList<AirQualityRecordData> airQualityDataList = new ArrayList();

            //讀歷年資料檔        
            ArrayList<AirQualityRecordData> historyAirQualityDataList = Step.readFile(historyCsvFileName, logFileWriter);
            addList(airQualityDataList, historyAirQualityDataList);

            //讀已經補過缺漏值的資料
            ArrayList<AirQualityRecordData> recentAirQualityDataList = Step.readFile(resultCsvFileName, logFileWriter);
            addList(airQualityDataList, recentAirQualityDataList);

            //新抓的資料轉成AirQualityRecordData格式
            ArrayList<AirQualityRecordData> newAirQualityDataList = new ArrayList();
            for (AirQualityData airQualityData : airQualityDataMap.values()) {
                newAirQualityDataList.add(new AirQualityRecordData(airQualityData.getRecordStr()));
            }
            addList(airQualityDataList, newAirQualityDataList);

            //建立hashMap<String,AirQualityData>   測站 日期 測項 -> airQualityData
            Map<String, AirQualityRecordData> airQualityRecordDataMap = Step.generateAirQualityDataMap(airQualityDataList, logFileWriter);

            //開始補值
            int numOfNotFilledValueLastTime = Integer.MAX_VALUE;
            int numOfNotFilledValue;
            int roundCount = 0;
            while (true) {
                roundCount++;
                LogUtils.log(logFileWriter, String.format("%1$s\tStart filling data, round %2$d", TimestampUtils.getTimestampStr(), roundCount));
                numOfNotFilledValue = Step.fillUpAirQualityData(airQualityRecordDataMap, airQualityDataList, logFileWriter);
                LogUtils.log(logFileWriter, String.format("%1$s\tRound %2$d still have %3$d not filled value", TimestampUtils.getTimestampStr(), roundCount, numOfNotFilledValue));

                if (numOfNotFilledValue != numOfNotFilledValueLastTime) {
                    numOfNotFilledValueLastTime = numOfNotFilledValue;
                } else {
                    LogUtils.log(logFileWriter, String.format("%1$s\tCannot fill sothing more values, break", TimestampUtils.getTimestampStr()));
                    break;
                }
            }

            //建立紀錄檔
            LogUtils.log(logFileWriter, String.format("%1$s\tNow start writing data into file", TimestampUtils.getTimestampStr()));
            FileWriter csvResultFileWriter = Step.createFileWriter(resultCsvFileName, false);

            //寫檔
            Step.writeFile(csvResultFileWriter, airQualityDataList, logFileWriter);

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

    private static String stripExtension(String str) {
        // Handle null case specially.

        if (str == null) {
            return null;
        }

        // Get position of last '.'.
        int pos = str.lastIndexOf(".");

        // If there wasn't any '.' just return the string as is.
        if (pos == -1) {
            return str;
        }

        // Otherwise return the string, up to the dot.
        return str.substring(0, pos);
    }

    public static void clearAllErrorValue(Map<Integer, String> siteMap, Map<Integer, String> itemIdMap, Map<String, AirQualityData> airQualityDataMap) {

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

    private void addList(ArrayList<AirQualityRecordData> destAirQualityDataList, ArrayList<AirQualityRecordData> srcAirQualityDataList) {
        for (AirQualityRecordData airQualityRecordData : srcAirQualityDataList) {
            destAirQualityDataList.add(airQualityRecordData);
        }
    }

}
