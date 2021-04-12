/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import model.Player;

/**
 * FXML Controller class
 *
 * @author inmad
 */
public class Joc4Controller implements Initializable {
    private boolean maquina = true;
    private int numJugades = 0;
    private boolean esJugador1 = true;
    private String missatgeError = "Tria una altra columna, esta ja està plena";
    private int[][] matriu = new int[7][8];
    private Player jugador1 = null;
    private Player jugador2 = null;
    //color Jugador1 = Negre
    //color Jugador2 = Blau
    @FXML
    private Text text_jugador;
    @FXML
    private Text error;
    @FXML
    private GridPane miGrid;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //TODO
    }    

    @FXML
    private void moure(ActionEvent event) {
        if (esJugador1) { //Torn del 1r jugador
            Button triat = (Button) event.getSource();
            Integer fila = miGrid.getRowIndex(triat);
            Integer columna = miGrid.getColumnIndex(triat);
            if (fila == null) fila = 0;
            if (columna == null) columna = 0;
            if (matriu[fila][columna] != 0) { error.setText(missatgeError); }
            else {
                matriu[fila][columna] = 1;
            }
        } else { 
            if (maquina) { //Torn de la màquina
                
            } else { //Torn del 2on jugador
                
            }
        }
        esJugador1 = !esJugador1;
        numJugades++;
        comprovarVictoria();
    }
    private void comprovarVictoria() {
        
    }
    public void inicialitzarJugador1(Player j1) {
        this.jugador1 = j1;
        if (jugador1 != null) text_jugador.setText("Torn de " + jugador1.getNickName());
    }
    public void inicialitzarJugadors(Player j1, Player j2) {
        this.jugador1 = j1;
        this.jugador2 = j2;
        if (jugador1 != null) text_jugador.setText("Torn de " + jugador1.getNickName());
    }
}
