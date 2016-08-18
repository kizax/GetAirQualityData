package getopendata;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class Main {

    public static void main(String[] args) {

        String folderPath = ".";
        try {
            folderPath = args[0];
        } catch (ArrayIndexOutOfBoundsException e) {
        }

        String historyCsvFileName = folderPath + "/history/airQualityDataHistory.csv";
        String airQualityDataCsvFileName = folderPath + "/record/airQualityData.csv";
        String filledUpAirQualityDataCsvFileName = folderPath + "/record/airQualityData_afterFillValue.csv";
        String logFileName = folderPath + "/record/log.txt";

        //建立log file
        FileWriter logFileWriter = Step.createFileWriter(logFileName, true);
        LogUtils.log(logFileWriter, String.format("%1$s\tStart air quality open data downloader!", TimestampUtils.getTimestampStr()));

        //建立itemIdMap
        Map<Integer, String> itemIdMap = MapUtils.getItemIdMap();

        //建立siteMap
        Map<Integer, String> siteMap = MapUtils.getSiteMap();

        //執行 getOpenDataTask
        int dateBefore = -1;
        try {
            dateBefore = -Integer.valueOf(args[1]);
        } catch (ArrayIndexOutOfBoundsException e) {
        }

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, dateBefore);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date specificDate = calendar.getTime();

        Thread getOpenDataTaskThread = new Thread(new GetOpenDataTask(historyCsvFileName, 
                airQualityDataCsvFileName, filledUpAirQualityDataCsvFileName,
                logFileWriter, itemIdMap, siteMap, specificDate));
        getOpenDataTaskThread.start();

    }

}
