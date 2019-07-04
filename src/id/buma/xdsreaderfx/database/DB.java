/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.buma.xdsreaderfx.database;

import id.buma.xdsreaderfx.controller.ReaderController;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Bayu Anandavi Muhardika
 * 
 * 
 */

public class DB {
   private static Connection conn;
   private static final ReaderController rc = new ReaderController();
   
    public static Connection getConn(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String server_lokal = "jdbc:mysql://localhost:3306/db_cs?user=root&password=adminBUMA789&useSSL=false";
            String server_kandir = "jdbc:mysql://192.168.208.98:3306/db_litbang?user=admintr&password=ptpn7@jaya&useSSL=false";
            String server_buma = "jdbc:mysql://192.168.39.150:3306/simpg?user=simpg&password=tiptpn7&useSSL=false";
            String server_cloud = "jdbc:mysql://128.199.189.176:3306/bungamay_litbang?user=bungamay_ods&password=bumaPTPN7&useSSL=false&noAccessToProcedureBodies=true";
            conn = DriverManager.getConnection(server_buma);
        } catch (ClassNotFoundException | SQLException ex) {
            //errMsg.showErrorAlert("Database tidak terkoneksi! " + ex.toString());
            rc.writeLog(LocalDateTime.now() + " : " + ex.toString());
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return conn;
    }
    
    public static boolean isConnect(){
        try {
            return getConn() != null;
        } catch (Exception ex) {
            //errMsg.showErrorAlert(ex.getMessage());
        }
        return false;
    }
}
