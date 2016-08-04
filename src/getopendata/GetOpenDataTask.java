/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package getopendata;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.Callable;
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
public class GetOpenDataTask implements Callable<Boolean> {

    private final String csvFileName = "./record/airQualityData.csv";
    private final FileWriter logFileWriter;
    private final int limit;
    private int offset;
    private Map itemMap;
    private Date specificDate;

    public GetOpenDataTask(FileWriter logFileWriter, int limit, int offset, Map itemMap, Date specificDate) {
        this.logFileWriter = logFileWriter;
        this.limit = limit;
        this.offset = offset;
        this.itemMap = itemMap;
        this.specificDate = specificDate;
    }

    @Override
    public Boolean call() throws Exception {

        LogUtils.log(logFileWriter, String.format("%1$s\tBus data is downloading now, offset %2$d, limit %3$d.", TimestampUtils.getTimestampStr(), offset, limit));

        try {
            String airQualityDataUrl = String.format(DataLinks.airQualityDataUrl, offset, limit);
            LogUtils.log(logFileWriter, String.format("%1$s\tairQualityDataUrl: %2$s", TimestampUtils.getTimestampStr(), airQualityDataUrl));

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
            FileWriter csvFileWriter
                    = new FileWriter(csvDataFile, true);

            //寫入檔頭BOM，避免EXCEL開啟變成亂碼
            byte[] bom = new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};
            csvFileWriter.write(new String(bom));

            //寫入紀錄檔
            for (AirQualityData airQualityData : airQualityDataList) {
                if (itemMap.containsKey(airQualityData.getItemId())
                        && airQualityData.getMonitorDateStr().equals(TimestampUtils.dateToStr(specificDate))) {
                    writeCsvFile(csvFileWriter, airQualityData.getRecordStr());
                }
            }

            LogUtils.log(logFileWriter, String.format("%1$s\tSuccessfully writing data into record file", TimestampUtils.getTimestampStr()));

            //判斷是否所有紀錄都是該日期的紀錄
            boolean areAllDataInSpecificDate = true;
            for (AirQualityData airQualityData : airQualityDataList) {
                if (!airQualityData.getMonitorDateStr().equals(TimestampUtils.dateToStr(specificDate))) {
                    areAllDataInSpecificDate = false;
                }
            }

            return areAllDataInSpecificDate;

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

        return false;
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
