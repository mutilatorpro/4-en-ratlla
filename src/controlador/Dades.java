/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

/**
 *
 * @author Violeta
 */
public class Dades {
    
    private static Dades meuesDades = null;
    
    private boolean modeObs = false;

    /**
     * Get the value of modeObs
     *
     * @return the value of modeObs
     */
    public boolean isModeObs() {
        return modeObs;
    }

    /**
     * Set the value of modeObs
     *
     * @param modeObs new value of modeObs
     */
    public void setModeObs(boolean modeObs) {
        this.modeObs = modeObs;
    }

    private Dades() {}
   
    public static Dades getDades() {
        if (meuesDades == null) meuesDades = new Dades();
        return meuesDades;
    }
}
