/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package getopendata;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author kizax
 */
public class Step {

    public static ArrayList<AirQualityRecordData> readFile(String fileName, FileWriter logFileWriter) {
        ArrayList<AirQualityRecordData> airQualityDataList = new ArrayList<>();
        try {
            LogUtils.log(logFileWriter, String.format("%1$s\tStart reading the record file", TimestampUtils.getTimestampStr()));

            File file = new File(fileName);

            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream(file), "UTF8"));

            int lineCount = 0;
            while (bufferedReader.ready()) {
                String recordStr = bufferedReader.readLine();
                lineCount++;
                try {
                    AirQualityRecordData airQualityData = new AirQualityRecordData(recordStr);
                    airQualityData.setLineNum(lineCount);
                    airQualityDataList.add(airQualityData);

                } catch (ParseException ex) {
                    LogUtils.log(logFileWriter, String.format("%1$s\tLine %2$d has ParseException", TimestampUtils.getTimestampStr(), lineCount));
                }
            }

            bufferedReader.close();

            LogUtils.log(logFileWriter, String.format("%1$s\tSuccessfully reading the record file", TimestampUtils.getTimestampStr(), lineCount));

        } catch (FileNotFoundException ex) {
            LogUtils.log(logFileWriter, String.format("%1$s\tThere is no file in %2$s", TimestampUtils.getTimestampStr(), fileName));
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

        return airQualityDataList;
    }

    public static Map<String, AirQualityRecordData> generateAirQualityDataMap(ArrayList<AirQualityRecordData> airQualityDataList, FileWriter logFileWriter) {
        //建立hashMap<String,AirQualityData>   測站 日期 測項 -> airQualityData
        Map<String, AirQualityRecordData> airQualityDataMap = new HashMap();
        for (AirQualityRecordData airQualityData : airQualityDataList) {
            String key = String.format("%1$s %2$s %3$s",
                    airQualityData.getSiteName(), airQualityData.getMonitorDateStr(),
                    airQualityData.getItemName());
            airQualityDataMap.put(key, airQualityData);
        }

        return airQualityDataMap;
    }

    public static int fillUpAirQualityData(Map<String, AirQualityRecordData> airQualityDataMap,
            ArrayList<AirQualityRecordData> airQualityDataList, FileWriter logFileWriter) {

        int numOfNotFilledValue = 0;

        String key;
        for (AirQualityRecordData airQualityData : airQualityDataList) {

            for (int index = 0; index < 24; index++) {
                if (airQualityData.getMonitorValue(index) == AirQualityRecordData.NOT_SET) {

//                    LogUtils.log(logFileWriter, String.format(
//                            "%1$s\tLine %2$d air quality data %3$s / %4$s at %5$s %6$d o'clock has leaked value",
//                            TimestampUtils.getTimestampStr(), airQualityData.getLineNum(), airQualityData.getSiteName(),
//                            airQualityData.getItemName(), airQualityData.getMonitorDateStr(), index));
                    //初始化參考值
                    float[] refValues = new float[10];
                    for (int i = 0; i < 10; i++) {
                        refValues[i] = AirQualityRecordData.NOT_SET;
                    }

                    //   同一測站同日前一小時
                    refValues[0] = getMonitorValue(airQualityData, airQualityData, index - 1, "前一小時", logFileWriter);

                    //前一日同時、前一日前一小時、前一日後一小時
                    Calendar yesterdayCalendar = Calendar.getInstance();
                    yesterdayCalendar.setTime(airQualityData.getMonitorDate());
                    yesterdayCalendar.add(Calendar.DATE, -1);
                    Date yesterday = yesterdayCalendar.getTime();
                    key = String.format("%1$s %2$s %3$s",
                            airQualityData.getSiteName(), TimestampUtils.dateToStr(yesterday),
                            airQualityData.getItemName());

                    AirQualityRecordData yesterdayAirQualityData = airQualityDataMap.get(key);
                    refValues[1] = getMonitorValue(airQualityData, yesterdayAirQualityData, index - 1, "前一天前一小時", logFileWriter);
                    refValues[2] = getMonitorValue(airQualityData, yesterdayAirQualityData, index, "前一天同一小時", logFileWriter);
                    refValues[3] = getMonitorValue(airQualityData, yesterdayAirQualityData, index + 1, "前一天後一小時", logFileWriter);

                    //前一週同時、前一週前一小時、前一週後一小時
                    Calendar lastWeekCalendar = Calendar.getInstance();
                    lastWeekCalendar.setTime(airQualityData.getMonitorDate());
                    lastWeekCalendar.add(Calendar.DATE, -7);
                    Date lastWeek = lastWeekCalendar.getTime();
                    key = String.format("%1$s %2$s %3$s",
                            airQualityData.getSiteName(), TimestampUtils.dateToStr(lastWeek),
                            airQualityData.getItemName());

                    AirQualityRecordData lastWeekAirQualityData = airQualityDataMap.get(key);
                    refValues[4] = getMonitorValue(airQualityData, lastWeekAirQualityData, index - 1, "前一週前一小時", logFileWriter);
                    refValues[5] = getMonitorValue(airQualityData, lastWeekAirQualityData, index, "前一週同一小時", logFileWriter);
                    refValues[6] = getMonitorValue(airQualityData, lastWeekAirQualityData, index + 1, "前一週後一小時", logFileWriter);

                    //前一年同週同時、前一年同週前一小時、前一年同週後一小時
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(airQualityData.getMonitorDate());
                    int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                    int weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);

                    Calendar prevYearCalendar = Calendar.getInstance();
                    prevYearCalendar.setTime(airQualityData.getMonitorDate());
                    prevYearCalendar.add(Calendar.YEAR, -1);
                    Date prevYear = prevYearCalendar.getTime();
                    int prevYearDayOfWeek = prevYearCalendar.get(Calendar.DAY_OF_WEEK);

                    int diff = dayOfWeek - prevYearDayOfWeek;
                    prevYearCalendar.add(Calendar.DATE, diff);
                    Date prevYearWithSameDayOfWeek = prevYearCalendar.getTime();

                    key = String.format("%1$s %2$s %3$s",
                            airQualityData.getSiteName(), TimestampUtils.dateToStr(prevYearWithSameDayOfWeek),
                            airQualityData.getItemName());

                    AirQualityRecordData prevYearAirQualityData = airQualityDataMap.get(key);
                    refValues[7] = getMonitorValue(airQualityData, prevYearAirQualityData, index - 1, "前一年同週前一小時", logFileWriter);
                    refValues[8] = getMonitorValue(airQualityData, prevYearAirQualityData, index, "前一年同週同一小時", logFileWriter);
                    refValues[9] = getMonitorValue(airQualityData, prevYearAirQualityData, index + 1, "前一年同週後一小時", logFileWriter);

//                    LogUtils.log(logFileWriter, String.format("%1$s\tLine %2$d air quality data %3$s / %4$s at %5$s %6$d o'clock call %7$s day's data",
//                            TimestampUtils.getTimestampStr(), airQualityData.getLineNum(), airQualityData.getSiteName(), 
//                            airQualityData.getItemName(), airQualityData.getMonitorDateStrWithDayOfWeek(), index, 
//                            TimestampUtils.dateToStrWithDayOfWeek(prevYearWithSameDayOfWeek)));
                    int validValueCount = 0;
                    float sum = 0;
                    for (int i = 0; i < 10; i++) {
                        if (refValues[i] != AirQualityRecordData.NOT_SET) {
                            sum += refValues[i];
                            validValueCount++;
                        }
                    }

                    if (validValueCount >= 5) {
                        float avg = sum / validValueCount;
                        airQualityData.setMonitorValue(avg, index);
                        LogUtils.log(logFileWriter, String.format("%1$s\tLine %2$d air quality data %3$s / %4$s at %5$s %6$d o'clock filled with %7$f",
                                TimestampUtils.getTimestampStr(), airQualityData.getLineNum(), airQualityData.getSiteName(), airQualityData.getItemName(), airQualityData.getMonitorDateStr(), index, avg));
                    } else {
                        numOfNotFilledValue++;
//                        LogUtils.log(logFileWriter, String.format("%1$s\tLine %2$d air quality data %3$s / %4$s at %5$s %6$d o'clock has leaked value but not filled",
//                                TimestampUtils.getTimestampStr(), airQualityData.getLineNum(), airQualityData.getSiteName(), airQualityData.getItemName(), airQualityData.getMonitorDateStr(), index));
//                        LogUtils.log(logFileWriter, String.format("%1$s\tValid value count: %2$d, %3$s",
//                                TimestampUtils.getTimestampStr(), validValueCount, convertArrayToStr(refValues)));

                    }
                }
            }

        }

        return numOfNotFilledValue;

    }

    public static ArrayList<String> listFilesForFolder(String folderName) {

        File folder = new File(folderName);

        ArrayList<String> fileNameList = new ArrayList();
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
            } else {
                String fileName = fileEntry.getName();
                String extension = "";

                int i = fileName.lastIndexOf('.');
                if (i > 0) {
                    extension = fileName.substring(i + 1);
                }

                if (extension.equals("csv")) {
                    fileNameList.add(fileName);
                }

            }
        }

        return fileNameList;
    }

    public static void writeFile(FileWriter resultFileWriter,
            ArrayList<AirQualityRecordData> airQualityRecordDataList, FileWriter logFileWriter) {
        try {
            //寫入檔頭BOM，避免EXCEL開啟變成亂碼
            byte[] bom = new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};
            resultFileWriter.write(new String(bom));

            //寫入紀錄檔
            int writingCount = 0;
            for (AirQualityRecordData airQualityData : airQualityRecordDataList) {
                writeCsvFile(resultFileWriter, airQualityData.getRecordStr());
                writingCount++;
            }

            LogUtils.log(logFileWriter, String.format("%1$s\tSuccessfully writing %2$d data into record file", TimestampUtils.getTimestampStr(), writingCount));
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void writeFile(FileWriter csvFileWriter,
            Collection<AirQualityData> airQualityDataList, FileWriter logFileWriter) {
        try {

            //寫入檔頭BOM，避免EXCEL開啟變成亂碼
            byte[] bom = new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};
            csvFileWriter.write(new String(bom));

            //寫入紀錄檔
            int writingCount = 0;
            for (AirQualityData airQualityData : airQualityDataList) {
                writeCsvFile(csvFileWriter, airQualityData.getRecordStr());
                writingCount++;
            }

            LogUtils.log(logFileWriter, String.format("%1$s\tSuccessfully writing %2$d data into record file", TimestampUtils.getTimestampStr(), writingCount));
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static FileWriter createFileWriter(String fileName, boolean append) {
        //建立log file
        File file = new File(fileName);

        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        FileWriter fileWriter = null;
        try {
            if (!file.exists()) {
                file.createNewFile();
                fileWriter = new FileWriter(file, append);
            } else {
                fileWriter = new FileWriter(file, append);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return fileWriter;
    }

    private static void writeCsvFile(FileWriter csvFileWriter, String record) {
        WriteThread writerThread = new WriteThread(csvFileWriter, record);
        writerThread.start();
    }

    private static float getMonitorValue(AirQualityRecordData airQualityData, AirQualityRecordData refAirQualityData, int index, String str, FileWriter logFileWriter) {

        float monitorValue = AirQualityRecordData.NOT_SET;
        try {
            monitorValue = refAirQualityData.getMonitorValue(index);
        } catch (ArrayIndexOutOfBoundsException e) {
        } catch (NullPointerException e) {
        }
        if (monitorValue == AirQualityRecordData.NOT_SET) {
//            LogUtils.log(logFileWriter, String.format("%1$s\tLine %2$d air quality data %3$s / %4$s at %5$s %6$d o'clock doesn't have %7$s value",
//                    TimestampUtils.getTimestampStr(), airQualityData.getLineNum(), airQualityData.getSiteName(),
//                    airQualityData.getItemName(), airQualityData.getMonitorDateStr(), index, str));
        }

        return monitorValue;
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
            if (null != pm10AirQualityData && null != pm25AirQualityData) {
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

    private static String convertArrayToStr(float[] floatArray) {
        String str = "";
        int count = 0;
        for (float f : floatArray) {
            str += ("[" + count + "] " + Float.toString(f) + " ");
            count++;
        }
        return str;
    }

    static void fillWithDummyData(Map<String, AirQualityData> airQualityDataMap, Map<Integer, String> siteMap, Map<Integer, String> itemIdMap, Date specificDate, FileWriter logFileWriter) {
        LogUtils.log(logFileWriter, String.format("%1$s\tNow have %2$d data", TimestampUtils.getTimestampStr(), airQualityDataMap.values().size()));
        for (int siteId : siteMap.keySet()) {
            for (int itemId : itemIdMap.keySet()) {
                if (!airQualityDataMap.containsKey(siteId + "," + itemId)) {
                    try {
                        String dataTimeStr = TimestampUtils.dateToStr(specificDate);
                        DateFormat dataFromat = new SimpleDateFormat("yyyy/M/d");
                        Date monitorDate = dataFromat.parse(dataTimeStr);
                        AirQualityData dummyAirQualityData = new AirQualityData(siteId, siteMap.get(siteId), itemId, itemIdMap.get(itemId), monitorDate);

                        airQualityDataMap.put(siteId + "," + itemId, dummyAirQualityData);
                    } catch (ParseException ex) {

                    }
                }
            }
        }
        LogUtils.log(logFileWriter, String.format("%1$s\tAfter filling up with dummy data, now have %2$d data", TimestampUtils.getTimestampStr(), airQualityDataMap.values().size()));

    }
}
