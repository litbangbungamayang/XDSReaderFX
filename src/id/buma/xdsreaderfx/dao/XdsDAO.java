/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.buma.xdsreaderfx.dao;

import id.buma.xdsreaderfx.model.DataXDS;
import java.util.List;

/**
 *
 * @author Bayu Anandavi Muhardika
 * 
 */

public interface XdsDAO {

    public Boolean insertXds(List<DataXDS> lxds);
    
}
