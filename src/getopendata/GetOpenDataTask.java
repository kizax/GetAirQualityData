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
import java.util.Map;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.http.HttpResponse;
import org.json.JSONException;
import org.xml.sax.SAXException;

/**
 *
 * @author kizax
 */
public class GetOpenDataTask implements Runnable {

    private final String csvFileName = "./record/airQualityData.csv";
    private final FileWriter logFileWriter;
    private final int limit = 1000;
    private int offset;
    private Map itemMap;

    public GetOpenDataTask(FileWriter logFileWriter, int offset, Map itemMap) {
        this.logFileWriter = logFileWriter;
        this.offset = offset;
        this.itemMap = itemMap;
    }

    @Override
    public void run() {

        LogUtils.log(logFileWriter, String.format("%1$s\tBus data is downloading now.", TimestampUtils.getTimestampStr()));

        try {
            String airQualityDataUrl = String.format(DataLinks.airQualityDataUrl, offset, limit);
            HttpResponse airQualityDataHttpResponse = HttpUtils.httpGet(airQualityDataUrl);

            String airQualityDataJsonStr = getStrFromResponse(airQualityDataHttpResponse);
            ArrayList<AirQualityData> airQualityDataList = AirQualityJsonParser.getAirQualityDataList(airQualityDataJsonStr);

            LogUtils.log(logFileWriter, String.format("%1$s\tNum of air quality data: %2$d", TimestampUtils.getTimestampStr(), airQualityDataList.size()));

            //建立紀錄檔
            LogUtils.log(logFileWriter, String.format("%1$s\tNow start writing data into file", TimestampUtils.getTimestampStr()));

            File csvDataFile = new File(csvFileName);

            if (!csvDataFile.getParentFile().exists()) {
                csvDataFile.getParentFile().mkdirs();
            }
            FileWriter csvFileWriter = new FileWriter(csvDataFile, true);

            //寫入紀錄檔
            for (AirQualityData airQualityData : airQualityDataList) {
                if (itemMap.containsKey(airQualityData.getItemId())) {
                    writeCsvFile(csvFileWriter, airQualityData.getRecordStr());
                }
            }

            LogUtils.log(logFileWriter, String.format("%1$s\tSuccessfully writing data into record file", TimestampUtils.getTimestampStr()));

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

}
