/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.buma.xdsreaderfx.controller;

import id.buma.xdsreaderfx.dao.XdsDAOSQL;
import id.buma.xdsreaderfx.model.Rafaksi;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 *
 * @author Bayu Anandavi Muhardika
 * 
 */

public class BusinessFlow {
    
    public int cekRafaksi(Double brix, Double pol){
        XdsDAOSQL xdsdao = new XdsDAOSQL();
        int rafaksi = 0;
        if (brix < 17.50){
            Double HK = (pol/brix)*100;
            DecimalFormat dfBulat = new DecimalFormat("#");
            DecimalFormat dfDuaAngka = new DecimalFormat("#.##");
            dfBulat.setRoundingMode(RoundingMode.HALF_UP);
            dfDuaAngka.setRoundingMode(RoundingMode.HALF_UP);
            //Double HKrounded = Double.valueOf(dfBulat.format(HK));
            int HKrounded = Integer.valueOf(dfBulat.format(HK));
            Double brixRounded = Double.valueOf(dfDuaAngka.format(brix));
            rafaksi = xdsdao.getRangeRafaksi(brix,HKrounded);
            // cek Brix
            System.out.println("brix = " + brixRounded + "; HK = " + dfDuaAngka.format(HK) +"; HK(pemb) = " + HKrounded + "; rafaksi = " + rafaksi);
        }
        return rafaksi;
    }
    
}
