/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package getopendata;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author kizax
 */
public class DbUtils {

    public static String URL;

    public static String USERNAME;

    public static String PASSWORD;

    public static String DRIVER;

    private DbUtils() {
    }

    static {
        try {
            URL = new Scanner(new File("./dbsetting/url")).useDelimiter("\\Z").next();
            USERNAME = new Scanner(new File("./dbsetting/username")).useDelimiter("\\Z").next();
            PASSWORD = new Scanner(new File("./dbsetting/password")).useDelimiter("\\Z").next();
            DRIVER = "com.mysql.jdbc.Driver";
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DbUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static Connection getConnection() {
        Connection conn = null;
        try {
            conn =  DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Connection fail");
        }
        return conn;
    }

    public static void close(ResultSet rs, Statement stat, Connection conn) {
        try {
            if (rs != null) {
                rs.close();
            }
            if (stat != null) {
                stat.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
