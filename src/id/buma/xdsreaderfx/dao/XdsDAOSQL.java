/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.buma.xdsreaderfx.dao;

import id.buma.xdsreaderfx.controller.ReaderController;
import id.buma.xdsreaderfx.database.DB;
import id.buma.xdsreaderfx.model.DataXDS;
import java.sql.PreparedStatement;
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
        String sql = "update tbl_corelab set brix=?, pol=?, tgl_xds=? where id_analisa=?";
        try (java.sql.Connection conn = DB.getConn()){
            conn.setAutoCommit(false);
            PreparedStatement ps = conn.prepareStatement(sql);
            for (DataXDS dxds : lxds){
                ps.setDouble(1, dxds.getBrix());
                ps.setDouble(2, dxds.getPol());
                ps.setTimestamp(3, dxds.getTglAnalisa());
                ps.setString(4, dxds.getIdAnalisa());
                ps.addBatch();
            }
            ps.executeBatch();
            conn.commit();
            conn.setAutoCommit(true);
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(XdsDAOSQL.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex.toString());
            //rc.writeLog(LocalDateTime.now() + " : " + ex.toString());
        }
        return false;
    }
    
}
