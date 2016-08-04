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
public class TimestampUtils {

    public static String getTimestampStr() {
        SimpleDateFormat timestampFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //2016-01-15 00:00:00
        String timestampStr = timestampFormat.format(new Date());

        return timestampStr;
    }
    
    public static String dateToStr(Date date) {
        SimpleDateFormat timestampFormat = new SimpleDateFormat("yyyy/M/d"); //2016/1/15
        String timestampStr = timestampFormat.format(date);

        return timestampStr;
    }
}
