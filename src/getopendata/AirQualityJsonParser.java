/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package getopendata;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author kizax
 */
public class AirQualityJsonParser {

    public static ArrayList<AirQualityData> getAirQualityDataList(String jsonStr) throws SAXException, IOException, ParseException, ParserConfigurationException, JSONException {

        ArrayList<AirQualityData> airQualityDataList = new ArrayList<>();

        JSONObject jsonObj = new JSONObject(jsonStr);

        JSONObject resultObj = jsonObj.getJSONObject("result");

        JSONArray airQualityJsonArray = resultObj.getJSONArray("records");

        for (int i = 0; i < airQualityJsonArray.length(); i++) {
            JSONObject airQualityObj = (JSONObject) airQualityJsonArray.get(i);

            int siteId = Integer.valueOf(airQualityObj.getString("SiteId"));
            String siteName = airQualityObj.getString("SiteName");

            int itemId = Integer.valueOf(airQualityObj.getString("ItemId"));
            String itemName = airQualityObj.getString("ItemName");
            String itemEngName = airQualityObj.getString("ItemEngName");
            String itemUnit = airQualityObj.getString("ItemUnit");

            String dataTimeStr = airQualityObj.getString("MonitorDate");
            DateFormat dataFromat = new SimpleDateFormat("yyyy-MM-dd");
            Date monitorDate = dataFromat.parse(dataTimeStr);

            String monitorValue00 = airQualityObj.getString("MonitorValue00");
            String monitorValue01 = airQualityObj.getString("MonitorValue01");
            String monitorValue02 = airQualityObj.getString("MonitorValue02");
            String monitorValue03 = airQualityObj.getString("MonitorValue03");
            String monitorValue04 = airQualityObj.getString("MonitorValue04");
            String monitorValue05 = airQualityObj.getString("MonitorValue05");
            String monitorValue06 = airQualityObj.getString("MonitorValue06");
            String monitorValue07 = airQualityObj.getString("MonitorValue07");
            String monitorValue08 = airQualityObj.getString("MonitorValue08");
            String monitorValue09 = airQualityObj.getString("MonitorValue09");
            String monitorValue10 = airQualityObj.getString("MonitorValue10");
            String monitorValue11 = airQualityObj.getString("MonitorValue11");
            String monitorValue12 = airQualityObj.getString("MonitorValue12");
            String monitorValue13 = airQualityObj.getString("MonitorValue13");
            String monitorValue14 = airQualityObj.getString("MonitorValue14");
            String monitorValue15 = airQualityObj.getString("MonitorValue15");
            String monitorValue16 = airQualityObj.getString("MonitorValue16");
            String monitorValue17 = airQualityObj.getString("MonitorValue17");
            String monitorValue18 = airQualityObj.getString("MonitorValue18");
            String monitorValue19 = airQualityObj.getString("MonitorValue19");
            String monitorValue20 = airQualityObj.getString("MonitorValue20");
            String monitorValue21 = airQualityObj.getString("MonitorValue21");
            String monitorValue22 = airQualityObj.getString("MonitorValue22");
            String monitorValue23 = airQualityObj.getString("MonitorValue23");

            AirQualityData busData = new AirQualityData(siteId, siteName, itemId, itemName,
                    itemEngName, itemUnit, monitorDate,
                    monitorValue00, monitorValue01, monitorValue02, monitorValue03,
                    monitorValue04, monitorValue05, monitorValue06, monitorValue07,
                    monitorValue08, monitorValue09, monitorValue10, monitorValue11,
                    monitorValue12, monitorValue13, monitorValue14, monitorValue15,
                    monitorValue16, monitorValue17, monitorValue18, monitorValue19,
                    monitorValue20, monitorValue21, monitorValue22, monitorValue23);
            airQualityDataList.add(busData);
        }

        return airQualityDataList;
    }

}
