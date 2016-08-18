/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package getopendata;

import java.util.ArrayList;
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
        String jsonStr, expResult, result;
        ArrayList<AirQualityData> resultBusEventDataList;

        jsonStr = "[{\"SiteId\":\"83\",\"SiteName\":\"麥寮\",\"ItemId\":\"144\",\"ItemName\":\"小時風向值\",\"ItemEngName\":\"WD_HR\",\"ItemUnit\":\"degrees\",\"MonitorDate\":\"2016-08-06\",\"MonitorValue00\":\"83\",\"MonitorValue01\":\"93\",\"MonitorValue02\":\"121\",\"MonitorValue03\":\"144\",\"MonitorValue04\":\"137\",\"MonitorValue05\":\"131\",\"MonitorValue06\":\"66\",\"MonitorValue07\":\"46\",\"MonitorValue08\":\"64\",\"MonitorValue09\":\"69\",\"MonitorValue10\":\"356\",\"MonitorValue11\":\"323\",\"MonitorValue12\":\"299\",\"MonitorValue13\":\"300\",\"MonitorValue14\":\"292\",\"MonitorValue15\":\"289\",\"MonitorValue16\":\"285\",\"MonitorValue17\":\"283\",\"MonitorValue18\":\"281\",\"MonitorValue19\":\"237\",\"MonitorValue20\":\"70\",\"MonitorValue21\":\"78\",\"MonitorValue22\":\"173\",\"MonitorValue23\":\"12\"},{\"SiteId\":\"83\",\"SiteName\":\"麥寮\",\"ItemId\":\"143\",\"ItemName\":\"小時風速值\",\"ItemEngName\":\"WS_HR\",\"ItemUnit\":\"m/sec\",\"MonitorDate\":\"2016-08-06\",\"MonitorValue00\":\"0.4\",\"MonitorValue01\":\"0.8\",\"MonitorValue02\":\"0.9\",\"MonitorValue03\":\"1.2\",\"MonitorValue04\":\"1\",\"MonitorValue05\":\"0.9\",\"MonitorValue06\":\"0.7\",\"MonitorValue07\":\"0.8\",\"MonitorValue08\":\"0.8\",\"MonitorValue09\":\"0.5\",\"MonitorValue10\":\"0.7\",\"MonitorValue11\":\"0.9\",\"MonitorValue12\":\"1.8\",\"MonitorValue13\":\"2.5\",\"MonitorValue14\":\"1.3\",\"MonitorValue15\":\"1.8\",\"MonitorValue16\":\"1.3\",\"MonitorValue17\":\"0.3\",\"MonitorValue18\":\"1.1\",\"MonitorValue19\":\"0.3\",\"MonitorValue20\":\"0.7\",\"MonitorValue21\":\"0.2\",\"MonitorValue22\":\"0.2\",\"MonitorValue23\":\"0.5\"}]";
        expResult = "83, 麥寮, 144, 小時風向值, WD_HR, degrees, 2016/8/6, 83, 93, 121, 144, 137, 131, 66, 46, 64, 69, 356, 323, 299, 300, 292, 289, 285, 283, 281, 237, 70, 78, 173, 12";
        resultBusEventDataList = AirQualityJsonParser.getAirQualityDataList(jsonStr);
        result = resultBusEventDataList.get(0).toString();
        assertEquals(expResult, result);

        jsonStr = "[{SiteId:\"80\",SiteName:\"關山\",ItemId:\"14\",ItemName:\"溫度\",ItemEngName:\"AMB_TEMP\",ItemUnit:\"℃\",MonitorDate:\"2016-08-17\",MonitorValue00:\"NR\",MonitorValue01:\"1\",MonitorValue02:\"NR\",MonitorValue03:\"2\",MonitorValue04:\"NR\",MonitorValue05:\"3\",MonitorValue06:\"NR\",MonitorValue07:\"4\",MonitorValue08:\"NR\",MonitorValue09:\"5\",MonitorValue10:\"NR\",MonitorValue11:\"6\",MonitorValue12:\"NR\",MonitorValue13:\"7\",MonitorValue14:\"NR\",MonitorValue15:\"8\",MonitorValue16:\"NR\",MonitorValue17:\"9\",MonitorValue18:\"NR\",MonitorValue19:\"10\",MonitorValue20:\"NR\",MonitorValue21:\"11\",MonitorValue22:\"NR\",MonitorValue23:\"12\"},{SiteId:\"80\",SiteName:\"關山\",ItemId:\"14\",ItemName:\"溫度\",ItemEngName:\"AMB_TEMP\",ItemUnit:\"℃\",MonitorDate:\"2016-08-17\",MonitorValue00:\"1\",MonitorValue01:\"NR\",MonitorValue02:\"2\",MonitorValue03:\"NR\",MonitorValue04:\"3\",MonitorValue05:\"NR\",MonitorValue06:\"4\",MonitorValue07:\"NR\",MonitorValue08:\"5\",MonitorValue09:\"NR\",MonitorValue10:\"6\",MonitorValue11:\"NR\",MonitorValue12:\"7\",MonitorValue13:\"NR\",MonitorValue14:\"8\",MonitorValue15:\"NR\",MonitorValue16:\"9\",MonitorValue17:\"NR\",MonitorValue18:\"10\",MonitorValue19:\"NR\",MonitorValue20:\"11\",MonitorValue21:\"NR\",MonitorValue22:\"12\",MonitorValue23:\"NR\"}]";
        expResult = "80, 關山, 14, 溫度, AMB_TEMP, ℃, 2016/8/17, NR, 1, NR, 2, NR, 3, NR, 4, NR, 5, NR, 6, NR, 7, NR, 8, NR, 9, NR, 10, NR, 11, NR, 12";
        resultBusEventDataList = AirQualityJsonParser.getAirQualityDataList(jsonStr);
        result = resultBusEventDataList.get(0).toString();
        assertEquals(expResult, result);
        
        expResult = "80, 關山, 14, 溫度, AMB_TEMP, ℃, 2016/8/17, 1, NR, 2, NR, 3, NR, 4, NR, 5, NR, 6, NR, 7, NR, 8, NR, 9, NR, 10, NR, 11, NR, 12, NR";
        resultBusEventDataList = AirQualityJsonParser.getAirQualityDataList(jsonStr);
        result = resultBusEventDataList.get(1).toString();
        assertEquals(expResult, result);

    }

}
