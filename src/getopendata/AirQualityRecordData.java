/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package getopendata;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author kizax
 */
public class AirQualityRecordData {

    public final static float NOT_SET = -99999;

    private String siteName;
    private Date monitorDate;
    private String itemName;
    private float[] monitorValues;
    private int lineNum;

    public AirQualityRecordData(String recordStr) throws ParseException, ArrayIndexOutOfBoundsException {
        String[] recordStrArray = recordStr.split(",");

        this.siteName = recordStrArray[0];

        String dataTimeStr = recordStrArray[1];
        DateFormat dataFromat = new SimpleDateFormat("yyyy/M/d");
//        DateFormat dataFromat = new SimpleDateFormat("yyyy-MM-dd");
        this.monitorDate = dataFromat.parse(dataTimeStr);

        this.itemName = recordStrArray[2];

        //初始化monitorValues
        monitorValues = new float[24];
        for (int count = 0; count < 24; count++) {
            monitorValues[count] = NOT_SET;
        }
        try {
            this.monitorValues[0] = strToFloat(recordStrArray[3]);
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        try {
            this.monitorValues[1] = strToFloat(recordStrArray[4]);
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        try {
            this.monitorValues[2] = strToFloat(recordStrArray[5]);
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        try {
            this.monitorValues[3] = strToFloat(recordStrArray[6]);
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        try {
            this.monitorValues[4] = strToFloat(recordStrArray[7]);
        } catch (ArrayIndexOutOfBoundsException e) {
        }

        try {
            this.monitorValues[5] = strToFloat(recordStrArray[8]);
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        try {
            this.monitorValues[6] = strToFloat(recordStrArray[9]);
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        try {
            this.monitorValues[7] = strToFloat(recordStrArray[10]);
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        try {
            this.monitorValues[8] = strToFloat(recordStrArray[11]);
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        try {
            this.monitorValues[9] = strToFloat(recordStrArray[12]);
        } catch (ArrayIndexOutOfBoundsException e) {
        }

        try {
            this.monitorValues[10] = strToFloat(recordStrArray[13]);
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        try {
            this.monitorValues[11] = strToFloat(recordStrArray[14]);
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        try {
            this.monitorValues[12] = strToFloat(recordStrArray[15]);
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        try {
            this.monitorValues[13] = strToFloat(recordStrArray[16]);
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        try {
            this.monitorValues[14] = strToFloat(recordStrArray[17]);
        } catch (ArrayIndexOutOfBoundsException e) {
        }

        try {
            this.monitorValues[15] = strToFloat(recordStrArray[18]);
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        try {
            this.monitorValues[16] = strToFloat(recordStrArray[19]);
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        try {
            this.monitorValues[17] = strToFloat(recordStrArray[20]);
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        try {
            this.monitorValues[18] = strToFloat(recordStrArray[21]);
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        try {
            this.monitorValues[19] = strToFloat(recordStrArray[22]);
        } catch (ArrayIndexOutOfBoundsException e) {
        }

        try {
            this.monitorValues[20] = strToFloat(recordStrArray[23]);
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        try {
            this.monitorValues[21] = strToFloat(recordStrArray[24]);
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        try {
            this.monitorValues[22] = strToFloat(recordStrArray[25]);
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        try {
            this.monitorValues[23] = strToFloat(recordStrArray[26]);
        } catch (ArrayIndexOutOfBoundsException e) {
        }
    }

    @Override
    public String toString() {
        String airQualityDataStr = String.format("%1$s, %2$s, %3$s, "
                + "%4$f, %5$f, %6$f, %7$f, %8$f, "
                + "%9$f, %10$f, %11$f, %12$f, %13$f, "
                + "%14$f, %15$f, %16$f, %17$f, %18$f, "
                + "%19$f, %20$f, %21$f, %22$f, %23$f, "
                + "%24$f, %25$f, %26$f, %27$f", getSiteName(), getMonitorDateStr(), getItemName(),
                monitorValues[0], monitorValues[1], monitorValues[2],
                monitorValues[3], monitorValues[4], monitorValues[5],
                monitorValues[6], monitorValues[7], monitorValues[8],
                monitorValues[9], monitorValues[10], monitorValues[11],
                monitorValues[12], monitorValues[13], monitorValues[14],
                monitorValues[15], monitorValues[16], monitorValues[17],
                monitorValues[18], monitorValues[19], monitorValues[20],
                monitorValues[21], monitorValues[22], monitorValues[23]);
        return airQualityDataStr;
    }

    public String getRecordStr() {
        String recordStr = String.format("%1$s,%2$s,%3$s,"
                + "%4$s,%5$s,%6$s,%7$s,%8$s,"
                + "%9$s,%10$s,%11$s,%12$s,%13$s,"
                + "%14$s,%15$s,%16$s,%17$s,%18$s,"
                + "%19$s,%20$s,%21$s,%22$s,%23$s,"
                + "%24$s,%25$s,%26$s,%27$s", getSiteName(), getMonitorDateStr(), getItemName(),
                getMonitorValueStr(monitorValues[0]), getMonitorValueStr(monitorValues[1]), getMonitorValueStr(monitorValues[2]),
                getMonitorValueStr(monitorValues[3]), getMonitorValueStr(monitorValues[4]), getMonitorValueStr(monitorValues[5]),
                getMonitorValueStr(monitorValues[6]), getMonitorValueStr(monitorValues[7]), getMonitorValueStr(monitorValues[8]),
                getMonitorValueStr(monitorValues[9]), getMonitorValueStr(monitorValues[10]), getMonitorValueStr(monitorValues[11]),
                getMonitorValueStr(monitorValues[12]), getMonitorValueStr(monitorValues[13]), getMonitorValueStr(monitorValues[14]),
                getMonitorValueStr(monitorValues[15]), getMonitorValueStr(monitorValues[16]), getMonitorValueStr(monitorValues[17]),
                getMonitorValueStr(monitorValues[18]), getMonitorValueStr(monitorValues[19]), getMonitorValueStr(monitorValues[20]),
                getMonitorValueStr(monitorValues[21]), getMonitorValueStr(monitorValues[22]), getMonitorValueStr(monitorValues[23]));
        return recordStr;
    }

    String getMonitorDateStr() {
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy/M/d"); //2016/1/15
        String timeStr = timeFormat.format(getMonitorDate());
        return timeStr;
    }
    
    String getMonitorDateStrWithDayOfWeek() {
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy/M/d EEE"); //2016/1/15
        String timeStr = timeFormat.format(getMonitorDate());
        return timeStr;
    }

    private float strToFloat(String str) {
        float value = NOT_SET;
        try {
            value = Float.valueOf(str.trim());
        } catch (NumberFormatException e) {
        };
        return value;
    }

    private String getMonitorValueStr(float monitorValue) {
        String monitorValueStr = "";
        if (monitorValue != NOT_SET) {
            monitorValueStr = Float.toString(monitorValue);
        }
        return monitorValueStr;
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
     * @return the monitorValue00
     */
    public float getMonitorValue(int index) throws ArrayIndexOutOfBoundsException {
        return monitorValues[index];
    }

    /**
     */
    public void setMonitorValue(float monitorValue, int index) {
        this.monitorValues[index] = monitorValue;
    }

    /**
     * @return the line
     */
    public int getLineNum() {
        return lineNum;
    }

    /**
     * @param line the line to set
     */
    public void setLineNum(int line) {
        this.lineNum = line;
    }

}
