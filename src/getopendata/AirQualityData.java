/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package getopendata;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author kizax
 */
public class AirQualityData {

    public static final String NOT_SET = "";

    private int siteId;
    private String siteName;
    private int itemId;
    private String itemName;
    private String itemEngName;
    private String itemUnit;
    private Date monitorDate;
    private String monitorValue00;
    private String monitorValue01;
    private String monitorValue02;
    private String monitorValue03;
    private String monitorValue04;
    private String monitorValue05;
    private String monitorValue06;
    private String monitorValue07;
    private String monitorValue08;
    private String monitorValue09;
    private String monitorValue10;
    private String monitorValue11;
    private String monitorValue12;
    private String monitorValue13;
    private String monitorValue14;
    private String monitorValue15;
    private String monitorValue16;
    private String monitorValue17;
    private String monitorValue18;
    private String monitorValue19;
    private String monitorValue20;
    private String monitorValue21;
    private String monitorValue22;
    private String monitorValue23;

    public AirQualityData(int siteId, String siteName, int itemId, String itemName,
            String itemEngName, String itemUnit, Date monitorDate,
            String monitorValue00, String monitorValue01, String monitorValue02, String monitorValue03,
            String monitorValue04, String monitorValue05, String monitorValue06, String monitorValue07,
            String monitorValue08, String monitorValue09, String monitorValue10, String monitorValue11,
            String monitorValue12, String monitorValue13, String monitorValue14, String monitorValue15,
            String monitorValue16, String monitorValue17, String monitorValue18, String monitorValue19,
            String monitorValue20, String monitorValue21, String monitorValue22, String monitorValue23) {

        this.siteId = siteId;
        this.siteName = siteName;
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemEngName = itemEngName;
        this.itemUnit = itemUnit;
        this.monitorDate = monitorDate;
        this.monitorValue00 = monitorValue00;
        this.monitorValue01 = monitorValue01;
        this.monitorValue02 = monitorValue02;
        this.monitorValue03 = monitorValue03;
        this.monitorValue04 = monitorValue04;
        this.monitorValue05 = monitorValue05;
        this.monitorValue06 = monitorValue06;
        this.monitorValue07 = monitorValue07;
        this.monitorValue08 = monitorValue08;
        this.monitorValue09 = monitorValue09;
        this.monitorValue10 = monitorValue10;
        this.monitorValue11 = monitorValue11;
        this.monitorValue12 = monitorValue12;
        this.monitorValue13 = monitorValue13;
        this.monitorValue14 = monitorValue14;
        this.monitorValue15 = monitorValue15;
        this.monitorValue16 = monitorValue16;
        this.monitorValue17 = monitorValue17;
        this.monitorValue18 = monitorValue18;
        this.monitorValue19 = monitorValue19;
        this.monitorValue20 = monitorValue20;
        this.monitorValue21 = monitorValue21;
        this.monitorValue22 = monitorValue22;
        this.monitorValue23 = monitorValue23;

    }

    public AirQualityData(int siteId, String siteName, int itemId,
            String itemEngName, Date monitorDate) {

        this.siteId = siteId;
        this.siteName = siteName;
        this.itemId = itemId;
        this.itemName = "";
        this.itemEngName = itemEngName;
        this.itemUnit = "";
        this.monitorDate = monitorDate;
        this.monitorValue00 = NOT_SET;
        this.monitorValue01 = NOT_SET;
        this.monitorValue02 = NOT_SET;
        this.monitorValue03 = NOT_SET;
        this.monitorValue04 = NOT_SET;
        this.monitorValue05 = NOT_SET;
        this.monitorValue06 = NOT_SET;
        this.monitorValue07 = NOT_SET;
        this.monitorValue08 = NOT_SET;
        this.monitorValue09 = NOT_SET;
        this.monitorValue10 = NOT_SET;
        this.monitorValue11 = NOT_SET;
        this.monitorValue12 = NOT_SET;
        this.monitorValue13 = NOT_SET;
        this.monitorValue14 = NOT_SET;
        this.monitorValue15 = NOT_SET;
        this.monitorValue16 = NOT_SET;
        this.monitorValue17 = NOT_SET;
        this.monitorValue18 = NOT_SET;
        this.monitorValue19 = NOT_SET;
        this.monitorValue20 = NOT_SET;
        this.monitorValue21 = NOT_SET;
        this.monitorValue22 = NOT_SET;
        this.monitorValue23 = NOT_SET;

    }

    @Override
    public String toString() {
        String vdDataStr = String.format("%1$d, %2$s, %3$d, %4$s, %5$s, %6$s, %7$s, "
                + "%8$s, %9$s, %10$s, %11$s, "
                + "%12$s, %13$s, %14$s, %15$s, "
                + "%16$s, %17$s, %18$s, %19$s, "
                + "%20$s, %21$s, %22$s, %23$s, "
                + "%24$s, %25$s, %26$s, %27$s, "
                + "%28$s, %29$s, %30$s, %31$s", 
                getSiteId(), getSiteName(), getItemId(), getItemName(), getItemEngName(), 
                getItemUnit(), getMonitorDateStr(), 
                monitorValue00, monitorValue01, monitorValue02, monitorValue03,
                monitorValue04, monitorValue05, monitorValue06, monitorValue07,
                monitorValue08, monitorValue09, monitorValue10, monitorValue11,
                monitorValue12, monitorValue13, monitorValue14, monitorValue15,
                monitorValue16, monitorValue17, monitorValue18, monitorValue19,
                monitorValue20, monitorValue21, monitorValue22, monitorValue23);
        return vdDataStr;
    }

    public String getRecordStr() {
        String recordStr = String.format("%1$s,%2$s,%3$s,"
                + "%4$s,%5$s,%6$s,%7$s,"
                + "%8$s,%9$s,%10$s,%11$s,"
                + "%12$s,%13$s,%14$s,%15$s,"
                + "%16$s,%17$s,%18$s,%19$s,"
                + "%20$s,%21$s,%22$s,%23$s,"
                + "%24$s,%25$s,%26$s,%27$s",
                getSiteName(), getMonitorDateStr(), getItemEngName(),
                monitorValue00, monitorValue01, monitorValue02, monitorValue03,
                monitorValue04, monitorValue05, monitorValue06, monitorValue07,
                monitorValue08, monitorValue09, monitorValue10, monitorValue11,
                monitorValue12, monitorValue13, monitorValue14, monitorValue15,
                monitorValue16, monitorValue17, monitorValue18, monitorValue19,
                monitorValue20, monitorValue21, monitorValue22, monitorValue23);
        return recordStr;
    }

    String getMonitorDateStr() {
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy/M/d"); //2016/1/15
        String timeStr = timeFormat.format(getMonitorDate());
        return timeStr;
    }

    /**
     * @return the siteId
     */
    public int getSiteId() {
        return siteId;
    }

    /**
     * @param siteId the siteId to set
     */
    public void setSiteId(int siteId) {
        this.siteId = siteId;
    }

    /**
     * @return the siteName
     */
    public String getSiteName() {
        return siteName;
    }

    /**
     * @param siteName the siteName to set
     */
    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    /**
     * @return the itemId
     */
    public int getItemId() {
        return itemId;
    }

    /**
     * @param itemId the itemId to set
     */
    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    /**
     * @return the itemName
     */
    public String getItemName() {
        return itemName;
    }

    /**
     * @param itemName the itemName to set
     */
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    /**
     * @return the itemEngName
     */
    public String getItemEngName() {
        return itemEngName;
    }

    /**
     * @param itemEngName the itemEngName to set
     */
    public void setItemEngName(String itemEngName) {
        this.itemEngName = itemEngName;
    }

    /**
     * @return the itemUnit
     */
    public String getItemUnit() {
        return itemUnit;
    }

    /**
     * @param itemUnit the itemUnit to set
     */
    public void setItemUnit(String itemUnit) {
        this.itemUnit = itemUnit;
    }

    /**
     * @return the monitorDate
     */
    public Date getMonitorDate() {
        return monitorDate;
    }

    /**
     * @param monitorDate the monitorDate to set
     */
    public void setMonitorDate(Date monitorDate) {
        this.monitorDate = monitorDate;
    }

    /**
     * @return the monitorValue00
     */
    public String getMonitorValue(int index) {

        switch (index) {
            case 0:
                return monitorValue00;
            case 1:
                return monitorValue01;
            case 2:
                return monitorValue02;
            case 3:
                return monitorValue03;
            case 4:
                return monitorValue04;
            case 5:
                return monitorValue05;

            case 6:
                return monitorValue06;
            case 7:
                return monitorValue07;
            case 8:
                return monitorValue08;
            case 9:
                return monitorValue09;
            case 10:
                return monitorValue10;

            case 11:
                return monitorValue11;
            case 12:
                return monitorValue12;
            case 13:
                return monitorValue13;
            case 14:
                return monitorValue14;
            case 15:
                return monitorValue15;

            case 16:
                return monitorValue16;
            case 17:
                return monitorValue17;
            case 18:
                return monitorValue18;
            case 19:
                return monitorValue19;
            case 20:
                return monitorValue20;

            case 21:
                return monitorValue21;
            case 22:
                return monitorValue22;
            case 23:
                return monitorValue23;
        }
        return null;
    }

    /**
     */
    public void setMonitorValue(int index, String monitorValue) {

        switch (index) {
            case 0:
                this.monitorValue00 = monitorValue;
            case 1:
                this.monitorValue01 = monitorValue;
            case 2:
                this.monitorValue02 = monitorValue;
            case 3:
                this.monitorValue03 = monitorValue;
            case 4:
                this.monitorValue04 = monitorValue;
            case 5:
                this.monitorValue05 = monitorValue;

            case 6:
                this.monitorValue06 = monitorValue;
            case 7:
                this.monitorValue07 = monitorValue;
            case 8:
                this.monitorValue08 = monitorValue;
            case 9:
                this.monitorValue09 = monitorValue;
            case 10:
                this.monitorValue10 = monitorValue;

            case 11:
                this.monitorValue11 = monitorValue;
            case 12:
                this.monitorValue12 = monitorValue;
            case 13:
                this.monitorValue13 = monitorValue;
            case 14:
                this.monitorValue14 = monitorValue;
            case 15:
                this.monitorValue15 = monitorValue;

            case 16:
                this.monitorValue16 = monitorValue;
            case 17:
                this.monitorValue17 = monitorValue;
            case 18:
                this.monitorValue18 = monitorValue;
            case 19:
                this.monitorValue19 = monitorValue;
            case 20:
                this.monitorValue20 = monitorValue;

            case 21:
                this.monitorValue21 = monitorValue;
            case 22:
                this.monitorValue22 = monitorValue;
            case 23:
                this.monitorValue23 = monitorValue;
        }
    }

}
