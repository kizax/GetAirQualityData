/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package getopendata;

import java.io.FileWriter;

/**
 *
 * @author kizax
 */
public class LogUtils {

    public static void log(FileWriter logFileWriter, String logStr) {
        
        System.out.println(logStr);
        
        WriteThread writerThread = new WriteThread(logFileWriter, logStr);
        writerThread.start();
    }

}
