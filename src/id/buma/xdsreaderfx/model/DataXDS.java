/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.buma.xdsreaderfx.model;

import java.sql.Date;

/**
 *
 * @author Bayu Anandavi Muhardika
 * 
 */

public class DataXDS {
    
    public String idAnalisa;
    public java.sql.Timestamp tglAnalisa;
    public Double brix;
    public Double pol;
    
    public DataXDS(String idAnalisa, java.sql.Timestamp tglAnalisa, Double brix, Double pol){
        this.idAnalisa = idAnalisa;
        this.tglAnalisa = tglAnalisa;
        this.brix = brix;
        this.pol = pol;
    }

    public String getIdAnalisa() {
        return idAnalisa;
    }

    public void setIdAnalisa(String idAnalisa) {
        this.idAnalisa = idAnalisa;
    }

    public java.sql.Timestamp getTglAnalisa() {
        return tglAnalisa;
    }

    public void setTglAnalisa(java.sql.Timestamp tglAnalisa) {
        this.tglAnalisa = tglAnalisa;
    }

    public Double getBrix() {
        return brix;
    }

    public void setBrix(Double brix) {
        this.brix = brix;
    }

    public Double getPol() {
        return pol;
    }

    public void setPol(Double pol) {
        this.pol = pol;
    }
    
    
}
