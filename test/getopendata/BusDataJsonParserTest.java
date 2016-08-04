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
public class BusDataJsonParserTest {

    public BusDataJsonParserTest() {
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
    public void testGetBusDataList() throws Exception {
        System.out.println("getBusDataList");
        String jsonStr = new Scanner(new File("./testdata/busData.json")).useDelimiter("\\Z").next();
        String expResult = "2016-02-02 14:49:28, 1, 1100.0, , 222233738.0, 337-FP, 1, 0, 111520.0, , 0, 121.557663, 25.042852, 1.020452, 174.100006, 0, ";
        ArrayList<AirQualityData> resultBusEventDataList = AirQualityJsonParser.getAirQualityDataList(jsonStr);
        String result = resultBusEventDataList.get(0).toString();
        assertEquals(expResult, result);

    }

    /**
     * Test of getBusEventDataList method, of class BusDataJsonParser.
     */
    @Test
    public void testGetBusEventDataList() throws Exception {
        System.out.println("getBusEventDataList");
        String jsonStr = new Scanner(new File("./testdata/busEventData.json")).useDelimiter("\\Z").next();
        String expResult = "337-FP, 2016-02-02 21:22:17, 1, 1100.0, 222233738.0, 1, 0, 111520.0, 1, 41619, 0";
        ArrayList<BusEventData> resultBusEventDataList = AirQualityJsonParser.getBusEventDataList(jsonStr);
        String result = resultBusEventDataList.get(0).toString();
        assertEquals(expResult, result);
    }

    /**
     * Test of getRouteDataList method, of class BusDataJsonParser.
     */
    @Test
    public void testGetRouteDataList() throws Exception {
        System.out.println("getRouteDataList");
        String jsonStr = new Scanner(new File("./testdata/routeData.json")).useDelimiter("\\Z").next();
        String expResult = "104620.0, 645副經三總";
        ArrayList<RouteData> resultRouteDataList = AirQualityJsonParser.getRouteDataList(jsonStr);
        String result = resultRouteDataList.get(0).toString();
        assertEquals(expResult, result);
    }

    /**
     * Test of getStopLocationDataList method, of class BusDataJsonParser.
     */
    @Test
    public void testGetStopLocationDataList() throws Exception {
        System.out.println("getStopLocationDataList");
        String jsonStr = new Scanner(new File("./testdata/stopLocationData.json")).useDelimiter("\\Z").next();
        String expResult = "148536.0, 蘆洲站";
        ArrayList<StopLocationData> resultStopLocationDataList = AirQualityJsonParser.getStopLocationDataList(jsonStr);
        String result = resultStopLocationDataList.get(0).toString();
        assertEquals(expResult, result);
    }

    /**
     * Test of getProviderDataList method, of class BusDataJsonParser.
     */
    @Test
    public void testGetProviderDataList() throws Exception {
        System.out.println("getProviderDataList");
        String jsonStr = new Scanner(new File("./testdata/providerData.json")).useDelimiter("\\Z").next();
        String expResult = "5500.0, 中興巴士";
        ArrayList<ProviderData> resultProviderDataList = AirQualityJsonParser.getProviderDataList(jsonStr);
        String result = resultProviderDataList.get(0).toString();
        assertEquals(expResult, result);
    }

}
