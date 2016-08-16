package getopendata;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) {

        String folderPath = ".";
        try {
            folderPath = args[0];
        } catch (ArrayIndexOutOfBoundsException e) {
        }

        String resultCsvFileName = folderPath + "/record/airQualityData.csv";
        String logFileName = folderPath + "/record/log.txt";

        //建立log file
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

        LogUtils.log(logFileWriter, String.format("%1$s\tStart air quality open data downloader!", TimestampUtils.getTimestampStr()));

        //建立itemMap
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

        //建立siteMap
        Map<Integer, String> siteMap = new HashMap();
        siteMap.put(1, "基隆");
        siteMap.put(2, "汐止");
        siteMap.put(3, "萬里");
        siteMap.put(4, "新店");
        siteMap.put(5, "土城");
        siteMap.put(6, "板橋");
        siteMap.put(7, "新莊");
        siteMap.put(8, "菜寮");
        siteMap.put(9, "林口");
        siteMap.put(10, "淡水");
        siteMap.put(11, "士林");
        siteMap.put(12, "中山");
        siteMap.put(13, "萬華");
        siteMap.put(14, "古亭");
        siteMap.put(15, "松山");
        siteMap.put(16, "大同");
        siteMap.put(17, "桃園");
        siteMap.put(18, "大園");
        siteMap.put(19, "觀音");
        siteMap.put(20, "平鎮");
        siteMap.put(21, "龍潭");
        siteMap.put(22, "湖口");
        siteMap.put(23, "竹東");
        siteMap.put(24, "新竹");
        siteMap.put(25, "頭份");
        siteMap.put(26, "苗栗");
        siteMap.put(27, "三義");
        siteMap.put(28, "豐原");
        siteMap.put(29, "沙鹿");
        siteMap.put(30, "大里");
        siteMap.put(31, "忠明");
        siteMap.put(32, "西屯");
        siteMap.put(33, "彰化");
        siteMap.put(34, "線西");
        siteMap.put(35, "二林");
        siteMap.put(36, "南投");
        siteMap.put(37, "斗六");
        siteMap.put(38, "崙背");
        siteMap.put(39, "新港");
        siteMap.put(40, "朴子");
        siteMap.put(41, "臺西");
        siteMap.put(42, "嘉義");
        siteMap.put(43, "新營");
        siteMap.put(44, "善化");
        siteMap.put(45, "安南");
        siteMap.put(46, "臺南");
        siteMap.put(47, "美濃");
        siteMap.put(48, "橋頭");
        siteMap.put(49, "仁武");
        siteMap.put(50, "鳳山");
        siteMap.put(51, "大寮");
        siteMap.put(52, "林園");
        siteMap.put(53, "楠梓");
        siteMap.put(54, "左營");
        siteMap.put(56, "前金");
        siteMap.put(57, "前鎮");
        siteMap.put(58, "小港");
        siteMap.put(59, "屏東");
        siteMap.put(60, "潮州");
        siteMap.put(61, "恆春");
        siteMap.put(62, "臺東");
        siteMap.put(63, "花蓮");
        siteMap.put(64, "陽明");
        siteMap.put(65, "宜蘭");
        siteMap.put(66, "冬山");
        siteMap.put(67, "三重");
        siteMap.put(68, "中壢");
        siteMap.put(69, "竹山");
        siteMap.put(70, "永和");
        siteMap.put(71, "復興");
        siteMap.put(72, "埔里");
        siteMap.put(75, "馬祖");
        siteMap.put(77, "金門");
        siteMap.put(78, "馬公");
        siteMap.put(80, "關山");
        siteMap.put(83, "麥寮");

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
        calendar.set( Calendar.MILLISECOND, 0 );
        Date specificDate = calendar.getTime();

        Thread getOpenDataTaskThread = new Thread(new GetOpenDataTask(resultCsvFileName, logFileWriter, itemMap, siteMap, specificDate));
        getOpenDataTaskThread.start();

    }
}
