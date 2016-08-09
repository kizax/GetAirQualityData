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

                //判斷是否所有紀錄都是該日期的紀錄
                boolean areAllDataInSpecificDate = true;
                for (AirQualityData airQualityData : airQualityDataList) {
                    if (!airQualityData.getMonitorDateStr().equals(TimestampUtils.dateToStr(specificDate))) {
                        areAllDataInSpecificDate = false;
                    }
                }

                if (puttingCount == 0 && offset != 2000) {
                    //根本沒抓到東西，再跑一次
                    int minute = 1;
                    LogUtils.log(logFileWriter, String.format("%1$s\tCold down %2$d minutes and recatch", TimestampUtils.getTimestampStr(), minute));
                    Thread.sleep(minute * 60 * 1000);
                }
                if (puttingCount == 0 && offset == 2000) {
                    break;
                }
                if (puttingCount != 0) {
                    offset += limit;
                }

            }

            //補值
            LogUtils.log(logFileWriter, String.format("%1$s\tNow have %2$d data", TimestampUtils.getTimestampStr(), airQualityDataMap.values().size()));
            for (int siteId : siteMap.keySet()) {
                for (int itemId : itemMap.keySet()) {
                    if (!airQualityDataMap.containsKey(siteId + "," + itemId)) {

                        Calendar yesterdayCalendar = Calendar.getInstance();
                        yesterdayCalendar.setTime(new Date());
                        yesterdayCalendar.add(Calendar.DATE, -1);
                        Date yesterday = yesterdayCalendar.getTime();

                        String dataTimeStr = TimestampUtils.dateToStr(yesterday);
                        DateFormat dataFromat = new SimpleDateFormat("yyyy/M/d");
                        Date monitorDate = dataFromat.parse(dataTimeStr);
                        AirQualityData dummyAirQualityData = new AirQualityData(siteId, siteMap.get(siteId), itemId, itemMap.get(itemId), monitorDate);

                        airQualityDataMap.put(siteId + "," + itemId, dummyAirQualityData);
                    }
                }
            }
            LogUtils.log(logFileWriter, String.format("%1$s\tAfter filling up with dummy data, now have %2$d data", TimestampUtils.getTimestampStr(), airQualityDataMap.values().size()));

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
        } catch (InterruptedException ex) {
            Logger.getLogger(GetOpenDataTask.class.getName()).log(Level.SEVERE, null, ex);
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

}
