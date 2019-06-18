/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.buma.xdsreaderfx.model;

/**
 *
 * @author Bayu Anandavi Muhardika
 * 
 */

public class Rafaksi {
    
    public int purity_low;
    public int purity_high;
    
    public Rafaksi(int purity_high, int purity_low){
        this.purity_high = purity_high;
        this.purity_low = purity_low;
    }

    public int getPurity_low() {
        return purity_low;
    }

    public void setPurity_low(int purity_low) {
        this.purity_low = purity_low;
    }

    public int getPurity_high() {
        return purity_high;
    }

    public void setPurity_high(int purity_high) {
        this.purity_high = purity_high;
    }
    
}
