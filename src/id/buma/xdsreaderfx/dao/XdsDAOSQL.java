/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.buma.xdsreaderfx.dao;

import id.buma.xdsreaderfx.controller.ReaderController;
import id.buma.xdsreaderfx.database.DB;
import id.buma.xdsreaderfx.model.DataXDS;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Bayu Anandavi Muhardika
 * 
 */

public class XdsDAOSQL implements XdsDAO{
    
    private final ReaderController rc = new ReaderController();

    @Override
    public Boolean insertXds(List<DataXDS> lxds) {
        String sql = "update TBL_CORELAB set BRIX=?, " //1
                + "POL=?, " //2
                + "HK=?, " //3
                + "TGL_XDS=?, " //4
                + "RAFAKSI=? where " //5
                + "ID_ANALISA=?"; //6
        try (java.sql.Connection conn = DB.getConn()){
            conn.setAutoCommit(false);
            PreparedStatement ps = conn.prepareStatement(sql);
            for (DataXDS dxds : lxds){
                ps.setDouble(1, dxds.getBrix());
                ps.setDouble(2, dxds.getPol());
                ps.setDouble(3, dxds.getPurity());
                ps.setTimestamp(4, dxds.getTglAnalisa());
                ps.setInt(5, dxds.getRafaksi());
                ps.setString(6, dxds.getIdAnalisa());
                ps.addBatch();
            }
            ps.executeBatch();
            conn.commit();
            conn.setAutoCommit(true);
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(XdsDAOSQL.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex.toString());
        }
        return false;
    }

    @Override
    public int getNilaiRafaksi(Double brix, int HK) {
        int rafaksi = 0;
        try (java.sql.Connection conn = DB.getConn()){
            int purity_high = 0;
            int purity_low = 0;
            int rafaksi_high = 0;
            int rafaksi_low = 0;
            String sql = "select max(purity) as purity, min(rafaksi) as rafaksi from tbl_rafaksi where brix_low <= ? and brix_high >= ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setDouble(1, brix);
            ps.setDouble(2, brix);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                purity_high = rs.getInt("purity");
                rafaksi_high = rs.getInt("rafaksi");
            }
            sql = "select min(purity) as purity, max(rafaksi) as rafaksi from tbl_rafaksi where brix_low <= ? and brix_high >= ?";
            ps = conn.prepareStatement(sql);
            ps.setDouble(1, brix); 
            ps.setDouble(2, brix);
            rs = ps.executeQuery();
            while (rs.next()){
                purity_low = rs.getInt("purity");
                rafaksi_low = rs.getInt("rafaksi");
            }
            if (brix > 13.49){
                if (HK < purity_low){
                    //ambil data rafaksi terendah pada range tersebut
                    rafaksi = rafaksi_low;
                } else {
                    if ((HK <= purity_high) && (HK >= purity_low)){
                        sql = "select purity, rafaksi from tbl_rafaksi where brix_low <= ? and brix_high >= ? and purity = ?";
                        ps = conn.prepareStatement(sql);
                        ps.setDouble(1, brix);
                        ps.setDouble(2, brix);
                        ps.setInt(3, HK);
                        rs = ps.executeQuery();
                        while (rs.next()){
                            rafaksi = rs.getInt("rafaksi");
                        }
                    } else {
                        if (HK > purity_high){
                            rafaksi = 0;
                        }
                    }
                }
            } else {
                if (HK < 84){
                    rafaksi = 20;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(XdsDAOSQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rafaksi;
    }

    @Override
    public int maxPurity(Double brix) {
        String sql = "select max(purity) as max_purity from tbl_rafaksi where brix_low <= ? and brix_high >= ?";
        int maxPurity = 0;
        try (Connection conn = DB.getConn()){
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setDouble(1, brix);
            ps.setDouble(2, brix);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                maxPurity = rs.getInt("max_purity");
            }
            if (brix > 17.00) maxPurity = 75;
            if (brix < 13.50) maxPurity = 82;
            //System.out.println(maxPurity);
        } catch (SQLException ex) {
            Logger.getLogger(XdsDAOSQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return maxPurity;
    }
    
}
