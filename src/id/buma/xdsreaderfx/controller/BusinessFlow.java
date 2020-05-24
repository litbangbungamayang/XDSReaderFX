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
    
    public int cekRafaksi(String idAnalisa, Double brix, Double pol, Double purity){
        XdsDAOSQL xdsdao = new XdsDAOSQL();
        int rafaksi = 0;
        DecimalFormat dfBulat = new DecimalFormat("#");
        dfBulat.setRoundingMode(RoundingMode.HALF_UP);
        int HKrounded = Integer.valueOf(dfBulat.format(purity));
        rafaksi = xdsdao.getNilaiRafaksi(brix,HKrounded);
        // cek Brix
        System.out.println("id = " + idAnalisa +"; brix = " + brix + "; pol = " + pol +"; HK = " + purity +"; HK(pemb) = " + 
                HKrounded + "; rafaksi = " + rafaksi);
        return rafaksi;
    }
    
}
