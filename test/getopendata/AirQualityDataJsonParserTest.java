/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package getopendata;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
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
public class AirQualityDataJsonParserTest {

    public AirQualityDataJsonParserTest() {
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
     * Test of getBusDataList method, of class BusDataJsonParser.
     */
    @Test
    public void testGetAirQualityDataList() throws Exception {
        System.out.println("getAirQualityDataList");
        String jsonStr = new Scanner(new File("./testdata/airQualityData.json")).next();
        String expResult = "2016-02-02 14:49:28, 1, 1100.0, , 222233738.0, 337-FP, 1, 0, 111520.0, , 0, 121.557663, 25.042852, 1.020452, 174.100006, 0, ";
        ArrayList<AirQualityData> resultBusEventDataList = AirQualityJsonParser.getAirQualityDataList(jsonStr);
        String result = resultBusEventDataList.get(0).toString();
        assertEquals(expResult, result);

    }

}
