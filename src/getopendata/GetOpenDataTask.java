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
import java.text.ParseException;
import java.util.ArrayList;
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
    private final String airQualityDataCsvFileName;
    private final String filledUpAirQualityDataCsvFileName;
    private final FileWriter logFileWriter;
    private Map<Integer, String> itemIdMap;
    private Map<Integer, String> siteMap;
    private Date specificDate;

    public GetOpenDataTask(String historyCsvFileName, String airQualityDataCsvFileName, String filledUpAirQualityDataCsvFileName,
            FileWriter logFileWriter, Map itemIdMap, Map siteMap, Date specificDate) {
        this.historyCsvFileName = historyCsvFileName;
        this.airQualityDataCsvFileName = airQualityDataCsvFileName;
        this.filledUpAirQualityDataCsvFileName = filledUpAirQualityDataCsvFileName;
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
            Step.fillWithDummyData(airQualityDataMap, siteMap, itemIdMap, specificDate, logFileWriter);

            //清除錯誤值
            Step.clearAllErrorValue(siteMap, itemIdMap, airQualityDataMap);

            //建立紀錄檔
            LogUtils.log(logFileWriter, String.format("%1$s\tNow start writing data into file", TimestampUtils.getTimestampStr()));

            FileWriter csvFileWriter = Step.createFileWriter(airQualityDataCsvFileName, true);
            Step.writeFile(csvFileWriter, airQualityDataMap.values(), logFileWriter);

            //建立itemMap
            Map<String, Integer> itemMap = MapUtils.getItemMap();

            ArrayList<AirQualityRecordData> referenceAirQualityRecordDataList = new ArrayList();

            //讀歷年資料檔        
            ArrayList<AirQualityRecordData> historyAirQualityDataList = Step.readFile(historyCsvFileName, logFileWriter);
            addList(referenceAirQualityRecordDataList, historyAirQualityDataList);

            //讀已經補過缺漏值的資料
            ArrayList<AirQualityRecordData> recentAirQualityDataList = Step.readFile(filledUpAirQualityDataCsvFileName, logFileWriter);
            addList(referenceAirQualityRecordDataList, recentAirQualityDataList);

            //對參考資料建立hashMap<String,AirQualityData>   測站 日期 測項 -> airQualityData
            Map<String, AirQualityRecordData> referenceAirQualityRecordDataMap = Step.generateAirQualityDataMap(referenceAirQualityRecordDataList, logFileWriter);

            //新抓的資料轉成AirQualityRecordData格式
            ArrayList<AirQualityRecordData> newAirQualityDataList = new ArrayList();
            for (AirQualityData airQualityData : airQualityDataMap.values()) {
                newAirQualityDataList.add(new AirQualityRecordData(airQualityData.getRecordStr()));
            }

            //開始對新抓的資料補值
            int numOfNotFilledValueLastTime = Integer.MAX_VALUE;
            int numOfNotFilledValue;
            int roundCount = 0;
            while (true) {
                roundCount++;
                LogUtils.log(logFileWriter, String.format("%1$s\tStart filling data, round %2$d", TimestampUtils.getTimestampStr(), roundCount));
                numOfNotFilledValue = Step.fillUpAirQualityData(referenceAirQualityRecordDataMap, newAirQualityDataList, logFileWriter);
                LogUtils.log(logFileWriter, String.format("%1$s\tRound %2$d still have %3$d not filled value", TimestampUtils.getTimestampStr(), roundCount, numOfNotFilledValue));

                if (numOfNotFilledValue != numOfNotFilledValueLastTime) {
                    numOfNotFilledValueLastTime = numOfNotFilledValue;
                } else {
                    LogUtils.log(logFileWriter, String.format("%1$s\tCannot fill sothing more values, break", TimestampUtils.getTimestampStr()));
                    break;
                }
            }

            //將補完值的新資料附加到已經補過缺漏值的資料的最後面
            LogUtils.log(logFileWriter, String.format("%1$s\tNow start writing data into file", TimestampUtils.getTimestampStr()));
            FileWriter csvResultFileWriter = Step.createFileWriter(filledUpAirQualityDataCsvFileName, false);

            //寫檔
            Step.writeFile(csvResultFileWriter, newAirQualityDataList, logFileWriter);

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

    private void addList(ArrayList<AirQualityRecordData> destAirQualityDataList, ArrayList<AirQualityRecordData> srcAirQualityDataList) {
        for (AirQualityRecordData airQualityRecordData : srcAirQualityDataList) {
            destAirQualityDataList.add(airQualityRecordData);
        }
    }

}
