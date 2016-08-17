/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package getopendata;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author kizax
 */
public class StepTest {

    public StepTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of fillUpAirQualityData method, of class Step.
     */
    @Test
    public void testFillUpAirQualityData() {
        System.out.println("getAirQualityDataList");
        String logFileName = "./record/log.txt";
        FileWriter logFileWriter = Step.createFileWriter(logFileName, true);

        //讀檔        
        ArrayList<AirQualityRecordData> airQualityDataList = Step.readFile("./testdata/fillUpAirQualityDataTestdata.csv", logFileWriter);

        //建立hashMap<String,AirQualityData>   測站 日期 測項 -> airQualityData
        Map<String, AirQualityRecordData> airQualityDataMap = Step.generateAirQualityDataMap(airQualityDataList, logFileWriter);

        //開始補值
        Step.fillUpAirQualityData(airQualityDataMap, airQualityDataList, logFileWriter);

        String expResult, result;
        expResult = "Erlin,2015/1/2,PM2.5,5.0,5.0,6.0,5.0,5.0,4.0,4.0,4.0,4.0,,,5.0,5.0,4.0,3.0,2.0,2.0,2.0,3.0,3.0,4.0,3.0,5.0,5.0";
        result = airQualityDataList.get(1).getRecordStr();
        assertEquals(expResult, result);

        expResult = "Erlin,2015/1/9,PM2.5,8.0,7.0,7.0,5.0,5.0,5.0,6.0,5.0,6.0,4.0,5.0,4.0,3.0,3.0,5.0,4.0,4.0,3.0,3.0,4.0,5.0,5.0,6.0,5.0";
        result = airQualityDataList.get(8).getRecordStr();
        assertEquals(expResult, result);

        expResult = "Erlin,2015/1/24,PM2.5,6.0,6.0,5.0,5.0,4.0,4.0,4.0,5.0,5.0,5.0,5.0,5.0,5.0,4.0,3.0,2.0,3.0,3.0,5.0,6.0,7.0,7.0,8.0,9.0";
        result = airQualityDataList.get(23).getRecordStr();
        assertEquals(expResult, result);

        expResult = "Erlin,2015/2/27,PM2.5,5.0,5.0,6.0,4.0,3.0,2.0,9.0,8.0,5.0,5.0,5.0,5.0,4.0,3.0,3.0,7.0,8.0,2.0,2.0,3.0,3.0,3.0,4.0,4.0";
        result = airQualityDataList.get(84).getRecordStr();
        assertEquals(expResult, result);

    }

    /**
     * Test of clearAllErrorValue method, of class Step.
     */
    @Test
    public void testClearAllErrorValue() {
        System.out.println("clearAllErrorValue");
        //建立itemIdMap
        Map<Integer, String> itemIdMap = MapUtils.getItemIdMap();

        //建立siteMap
        Map<Integer, String> siteMap = MapUtils.getSiteMap();

        System.out.println("getAirQualityDataList");
        String logFileName = "./record/log.txt";
        FileWriter logFileWriter = Step.createFileWriter(logFileName, true);

        //讀檔        
        ArrayList<AirQualityRecordData> airQualityRecordDataList = Step.readFile("./testdata/clearAllErrorValueTestData.csv", logFileWriter);
        Map<String, AirQualityData> airQualityDataMap = new HashMap<String, AirQualityData>();

        //看看是否已有紀錄，若還沒有，則放入airQualityDataMap
        for (AirQualityRecordData airQualityRecordData : airQualityRecordDataList) {
            AirQualityData airQualityData = new AirQualityData(airQualityRecordData);
            airQualityDataMap.put(airQualityData.getSiteId() + "," + airQualityData.getItemId(), airQualityData);
        }
        Step.clearAllErrorValue(siteMap, itemIdMap, airQualityDataMap);

        ArrayList<AirQualityData> airQualityDataList = new ArrayList();
        for (AirQualityData airQualityData : airQualityDataMap.values()) {
            airQualityDataList.add(airQualityData);
            System.out.println("result: " + airQualityData.getRecordStr());
        }

        String expResult, result;
        expResult = "新竹,2016/8/14,SO2,505.0,1.6,,1.6,1.2,1.1,,1.3,1.3,1.2,505.0,1.7,1.7,1.8,2.9,,3.5,,3.2,2.8,2.0,505.0,2.9,";
        result = airQualityDataList.get(0).getRecordStr();
        System.out.println("expect: " + expResult);
        System.out.println("result: " + result);
        assertEquals(expResult, result);

        expResult = "嘉義,2016/8/14,NO2,,505.0,8.0,7.8,10.0,13.0,,9.0,6.2,5.8,5.1,,,,3.7,,4.3,505.0,8.2,9.5,7.6,8.2,7.2,";
        result = airQualityDataList.get(1).getRecordStr();
        System.out.println("expect: " + expResult);
        System.out.println("result: " + result);
        assertEquals(expResult, result);

        expResult = "三重,2016/8/14,RH,,,0.0,,,,,,,,44.0,42.0,40.0,41.0,100.0,42.0,44.0,47.0,51.0,0.0,56.0,,59.0,";
        result = airQualityDataList.get(2).getRecordStr();
        System.out.println("expect: " + expResult);
        System.out.println("result: " + result);
        assertEquals(expResult, result);

        expResult = "臺東,2016/8/14,AMB_TEMP,,26.0,,26.0,6.4,26.0,,,6.4,30.0,,,,31.0,,31.0,,30.0,43.5,28.0,12.0,28.0,,28.0";
        result = airQualityDataList.get(3).getRecordStr();
        System.out.println("expect: " + expResult);
        System.out.println("result: " + result);
        assertEquals(expResult, result);

        expResult = "臺南,2016/8/14,NOx,20.0,20.0,505.0,,,16.0,17.0,16.0,12.0,,,,6.9,6.1,9.1,11.0,505.0,6.2,18.0,14.0,13.0,16.0,13.0,";
        result = airQualityDataList.get(4).getRecordStr();
        System.out.println("expect: " + expResult);
        System.out.println("result: " + result);
        assertEquals(expResult, result);

        expResult = "桃園,2016/8/14,O3,11.0,7.2,,,23.0,9.1,14.0,28.0,505.0,41.0,45.0,47.0,44.0,40.0,38.0,,,26.0,20.0,19.0,505.0,20.0,18.0,14.0";
        result = airQualityDataList.get(5).getRecordStr();
        System.out.println("expect: " + expResult);
        System.out.println("result: " + result);
        assertEquals(expResult, result);

        expResult = "宜蘭,2016/8/14,PM2.5,15.0,,16.0,15.0,16.0,15.0,9.0,,,,30.0,13.0,15.0,,,23.0,17.0,15.0,,,20.0,23.0,,";
        result = airQualityDataList.get(6).getRecordStr();
        System.out.println("expect: " + expResult);
        System.out.println("result: " + result);
        assertEquals(expResult, result);

        expResult = "西屯,2016/8/14,NO,,2.0,2.1,1.4,1.5,,1.8,2.2,,2.0,1.6,2.8,505.0,,1.7,1.9,1.8,1.3,1.1,1.3,1.3,1.6,1.3,505.0";
        result = airQualityDataList.get(7).getRecordStr();
        System.out.println("expect: " + expResult);
        System.out.println("result: " + result);
        assertEquals(expResult, result);

        expResult = "宜蘭,2016/8/14,PM10,,19.0,16.0,10200.0,25.0,27.0,,33.0,30.0,10200.0,30.0,,31.0,31.0,28.0,29.0,22.0,10200.0,17.0,19.0,20.0,,18.0,";
        result = airQualityDataList.get(8).getRecordStr();
        System.out.println("expect: " + expResult);
        System.out.println("result: " + result);
        assertEquals(expResult, result);

        expResult = "三重,2016/8/14,RAINFALL,0.0,,,,,,,,,,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,,0.0,0.0";
        result = airQualityDataList.get(9).getRecordStr();
        System.out.println("expect: " + expResult);
        System.out.println("result: " + result);
        assertEquals(expResult, result);

        expResult = "林口,2016/8/14,CO,50.5,,0.25,0.25,,,0.33,0.3,0.28,,0.17,0.14,0.13,0.12,0.15,0.14,50.5,0.16,0.18,0.18,0.19,,0.23,50.5";
        result = airQualityDataList.get(10).getRecordStr();
        System.out.println("expect: " + expResult);
        System.out.println("result: " + result);
        assertEquals(expResult, result);

    }

}
