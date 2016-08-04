package getopendata;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Main {

    private static final String logFileName = "./record/log.txt";

    public static void main(String[] args) {

        File logFile = new File(logFileName);

        if (!logFile.getParentFile().exists()) {
            logFile.getParentFile().mkdirs();
        }

        FileWriter logFileWriter = null;
        try {
            if (!logFile.exists()) {
                logFile.createNewFile();
                logFileWriter = new FileWriter(logFile, true);

            } else {
                logFileWriter = new FileWriter(logFile, true);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        Date date = new Date();

        LogUtils.log(logFileWriter, String.format("%1$s\tStart air quality open data downloader!", TimestampUtils.getTimestampStr()));

        Map<Integer, String> itemMap = new HashMap();
        itemMap.put(1, "SO2");
        itemMap.put(2, "CO");
        itemMap.put(3, "O3");
        itemMap.put(4, "PM10");
        itemMap.put(5, "NOx");
        
        itemMap.put(6, "NO");
        itemMap.put(7, "NO2");
        itemMap.put(14, "AMB_TEMP");
        itemMap.put(23, "RAINFALL");
        itemMap.put(33, "PM2.5");
        
        itemMap.put(38, "RH");

        int offset = 0;
        Thread getOpenDataTaskThread = new Thread(new GetOpenDataTask(logFileWriter, offset, itemMap));
        getOpenDataTaskThread.start();

    }
}
