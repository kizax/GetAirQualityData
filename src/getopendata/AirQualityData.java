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
        String vdDataStr = String.format("%1$d, %2$s, %3$d, %4$s, %5$s, %6$s, %7$s, %8$s, %9$s, %10$s, %11$s, %12$s, %13$s, %14$s, %15$s, %16$s, %17$s, %18$s, %19$s, %20$s, %21$s, %22$s, %23$s, %24$s, %25$s, %26$s, %27$s, %28$s, %29$s, %30$s, %31$s", getSiteId(), getSiteName(), getItemId(), getItemName(), getItemEngName(), getItemUnit(), getMonitorDateStr(), getMonitorValue00(), getMonitorValue01(), getMonitorValue02(), getMonitorValue03(), getMonitorValue04(), getMonitorValue05(), getMonitorValue06(), getMonitorValue07(), getMonitorValue08(), getMonitorValue09(), getMonitorValue10(), getMonitorValue11(), getMonitorValue12(), getMonitorValue13(), getMonitorValue14(), getMonitorValue15(), getMonitorValue16(), getMonitorValue17(), getMonitorValue18(), getMonitorValue19(), getMonitorValue20(), getMonitorValue21(), getMonitorValue22(), getMonitorValue23());
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
                getMonitorValue00(), getMonitorValue01(), getMonitorValue02(), getMonitorValue03(),
                getMonitorValue04(), getMonitorValue05(), getMonitorValue06(), getMonitorValue07(),
                getMonitorValue08(), getMonitorValue09(), getMonitorValue10(), getMonitorValue11(),
                getMonitorValue12(), getMonitorValue13(), getMonitorValue14(), getMonitorValue15(),
                getMonitorValue16(), getMonitorValue17(), getMonitorValue18(), getMonitorValue19(),
                getMonitorValue20(), getMonitorValue21(), getMonitorValue22(), getMonitorValue23());
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
    public String getMonitorValue00() {
        return monitorValue00;
    }

    /**
     * @param monitorValue00 the monitorValue00 to set
     */
    public void setMonitorValue00(String monitorValue00) {
        this.monitorValue00 = monitorValue00;
    }

    /**
     * @return the monitorValue01
     */
    public String getMonitorValue01() {
        return monitorValue01;
    }

    /**
     * @param monitorValue01 the monitorValue01 to set
     */
    public void setMonitorValue01(String monitorValue01) {
        this.monitorValue01 = monitorValue01;
    }

    /**
     * @return the monitorValue02
     */
    public String getMonitorValue02() {
        return monitorValue02;
    }

    /**
     * @param monitorValue02 the monitorValue02 to set
     */
    public void setMonitorValue02(String monitorValue02) {
        this.monitorValue02 = monitorValue02;
    }

    /**
     * @return the monitorValue03
     */
    public String getMonitorValue03() {
        return monitorValue03;
    }

    /**
     * @param monitorValue03 the monitorValue03 to set
     */
    public void setMonitorValue03(String monitorValue03) {
        this.monitorValue03 = monitorValue03;
    }

    /**
     * @return the monitorValue04
     */
    public String getMonitorValue04() {
        return monitorValue04;
    }

    /**
     * @param monitorValue04 the monitorValue04 to set
     */
    public void setMonitorValue04(String monitorValue04) {
        this.monitorValue04 = monitorValue04;
    }

    /**
     * @return the monitorValue05
     */
    public String getMonitorValue05() {
        return monitorValue05;
    }

    /**
     * @param monitorValue05 the monitorValue05 to set
     */
    public void setMonitorValue05(String monitorValue05) {
        this.monitorValue05 = monitorValue05;
    }

    /**
     * @return the monitorValue06
     */
    public String getMonitorValue06() {
        return monitorValue06;
    }

    /**
     * @param monitorValue06 the monitorValue06 to set
     */
    public void setMonitorValue06(String monitorValue06) {
        this.monitorValue06 = monitorValue06;
    }

    /**
     * @return the monitorValue07
     */
    public String getMonitorValue07() {
        return monitorValue07;
    }

    /**
     * @param monitorValue07 the monitorValue07 to set
     */
    public void setMonitorValue07(String monitorValue07) {
        this.monitorValue07 = monitorValue07;
    }

    /**
     * @return the monitorValue08
     */
    public String getMonitorValue08() {
        return monitorValue08;
    }

    /**
     * @param monitorValue08 the monitorValue08 to set
     */
    public void setMonitorValue08(String monitorValue08) {
        this.monitorValue08 = monitorValue08;
    }

    /**
     * @return the monitorValue09
     */
    public String getMonitorValue09() {
        return monitorValue09;
    }

    /**
     * @param monitorValue09 the monitorValue09 to set
     */
    public void setMonitorValue09(String monitorValue09) {
        this.monitorValue09 = monitorValue09;
    }

    /**
     * @return the monitorValue10
     */
    public String getMonitorValue10() {
        return monitorValue10;
    }

    /**
     * @param monitorValue10 the monitorValue10 to set
     */
    public void setMonitorValue10(String monitorValue10) {
        this.monitorValue10 = monitorValue10;
    }

    /**
     * @return the monitorValue11
     */
    public String getMonitorValue11() {
        return monitorValue11;
    }

    /**
     * @param monitorValue11 the monitorValue11 to set
     */
    public void setMonitorValue11(String monitorValue11) {
        this.monitorValue11 = monitorValue11;
    }

    /**
     * @return the monitorValue12
     */
    public String getMonitorValue12() {
        return monitorValue12;
    }

    /**
     * @param monitorValue12 the monitorValue12 to set
     */
    public void setMonitorValue12(String monitorValue12) {
        this.monitorValue12 = monitorValue12;
    }

    /**
     * @return the monitorValue13
     */
    public String getMonitorValue13() {
        return monitorValue13;
    }

    /**
     * @param monitorValue13 the monitorValue13 to set
     */
    public void setMonitorValue13(String monitorValue13) {
        this.monitorValue13 = monitorValue13;
    }

    /**
     * @return the monitorValue14
     */
    public String getMonitorValue14() {
        return monitorValue14;
    }

    /**
     * @param monitorValue14 the monitorValue14 to set
     */
    public void setMonitorValue14(String monitorValue14) {
        this.monitorValue14 = monitorValue14;
    }

    /**
     * @return the monitorValue15
     */
    public String getMonitorValue15() {
        return monitorValue15;
    }

    /**
     * @param monitorValue15 the monitorValue15 to set
     */
    public void setMonitorValue15(String monitorValue15) {
        this.monitorValue15 = monitorValue15;
    }

    /**
     * @return the monitorValue16
     */
    public String getMonitorValue16() {
        return monitorValue16;
    }

    /**
     * @param monitorValue16 the monitorValue16 to set
     */
    public void setMonitorValue16(String monitorValue16) {
        this.monitorValue16 = monitorValue16;
    }

    /**
     * @return the monitorValue17
     */
    public String getMonitorValue17() {
        return monitorValue17;
    }

    /**
     * @param monitorValue17 the monitorValue17 to set
     */
    public void setMonitorValue17(String monitorValue17) {
        this.monitorValue17 = monitorValue17;
    }

    /**
     * @return the monitorValue18
     */
    public String getMonitorValue18() {
        return monitorValue18;
    }

    /**
     * @param monitorValue18 the monitorValue18 to set
     */
    public void setMonitorValue18(String monitorValue18) {
        this.monitorValue18 = monitorValue18;
    }

    /**
     * @return the monitorValue19
     */
    public String getMonitorValue19() {
        return monitorValue19;
    }

    /**
     * @param monitorValue19 the monitorValue19 to set
     */
    public void setMonitorValue19(String monitorValue19) {
        this.monitorValue19 = monitorValue19;
    }

    /**
     * @return the monitorValue20
     */
    public String getMonitorValue20() {
        return monitorValue20;
    }

    /**
     * @param monitorValue20 the monitorValue20 to set
     */
    public void setMonitorValue20(String monitorValue20) {
        this.monitorValue20 = monitorValue20;
    }

    /**
     * @return the monitorValue21
     */
    public String getMonitorValue21() {
        return monitorValue21;
    }

    /**
     * @param monitorValue21 the monitorValue21 to set
     */
    public void setMonitorValue21(String monitorValue21) {
        this.monitorValue21 = monitorValue21;
    }

    /**
     * @return the monitorValue22
     */
    public String getMonitorValue22() {
        return monitorValue22;
    }

    /**
     * @param monitorValue22 the monitorValue22 to set
     */
    public void setMonitorValue22(String monitorValue22) {
        this.monitorValue22 = monitorValue22;
    }

    /**
     * @return the monitorValue23
     */
    public String getMonitorValue23() {
        return monitorValue23;
    }

    /**
     * @param monitorValue23 the monitorValue23 to set
     */
    public void setMonitorValue23(String monitorValue23) {
        this.monitorValue23 = monitorValue23;
    }

}
