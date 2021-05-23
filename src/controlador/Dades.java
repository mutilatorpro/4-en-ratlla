/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.time.LocalDate;

/**
 *
 * @author Violeta
 */
public class Dades {
    
    private static Dades meuesDades = null;
    
    private boolean modeObs = false;
    

    private LocalDate dataI = null, dataF = null;    
    private String nomUsuari = "";

    public LocalDate getDataI() {
        return dataI;
    }

    public void setDataI(LocalDate dataI) {
        this.dataI = dataI;
    }

    public LocalDate getDataF() {
        return dataF;
    }

    public void setDataF(LocalDate dataF) {
        this.dataF = dataF;
    }
    
    public boolean isModeObs() {
        return modeObs;
    }

    public void setModeObs(boolean modeObs) {
        this.modeObs = modeObs;
    }

    public String getNomUsuari() {
        return nomUsuari;
    }

    public void setNomUsuari(String nomUsuari) {
        this.nomUsuari = nomUsuari;
    }
    
    private Dades() {}
   
    public static Dades getDades() {
        if (meuesDades == null) meuesDades = new Dades();
        return meuesDades;
    }
}
