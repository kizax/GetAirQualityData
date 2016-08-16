/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package getopendata;

/**
 *
 * @author kizax
 */
import java.io.FileWriter;
import java.io.IOException;

public class WriteThread extends Thread {

    private final String record;
    private final FileWriter fileWriter;

    public WriteThread(FileWriter fileWriter, String record) {
        this.fileWriter = fileWriter;
        this.record = record;
    }

    @Override
    public void run() {
        super.run();
        try {

            synchronized (fileWriter) {
                fileWriter.write(record + System.getProperty("line.separator"));
                fileWriter.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
