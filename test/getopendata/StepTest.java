/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package getopendata;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
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
        ArrayList<AirQualityRecordData> airQualityDataList = Step.readFile("./testdata/testdata.csv", logFileWriter);

        //建立hashMap<String,AirQualityData>   測站 日期 測項 -> airQualityData
        Map<String, AirQualityRecordData> airQualityDataMap = Step.generateAirQualityDataMap(airQualityDataList, logFileWriter);

        //開始補值
        Step.fillUpAirQualityData(airQualityDataMap, airQualityDataList, logFileWriter);

        String expResult = "Erlin,2015/1/2,PM2.5,5.0,5.0,6.0,5.0,5.0,4.0,4.0,4.0,4.0,,,5.0,5.0,4.0,3.0,2.0,2.0,2.0,3.0,3.0,4.0,3.0,5.0,5.0";
        String result = airQualityDataList.get(1).getRecordStr();
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
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}
