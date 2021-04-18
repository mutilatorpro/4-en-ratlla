/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import DBAccess.Connect4DAOException;
import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Connect4;
import model.Player;

/**
 * FXML Controller class
 *
 * @author inmad
 */
public class Joc4Controller implements Initializable {
    private Connect4 sistema;
    private boolean maquina = true;
    private int numJugades = 0;
    private boolean esJugador1 = true;
    private String missatgeError = "Tria una altra columna, esta ja està plena";
    private int[][] matriu = new int[7][8];
    private Player jugador1 = null;
    private Player jugador2 = null;
    private int punts;
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
        try {
            sistema = Connect4.getSingletonConnect4();
        } catch (Connect4DAOException ex) {
            System.out.println("Error en la càrrega del sistema");
        }
    }    

    @FXML
    private void moure(ActionEvent event) {
        if (esJugador1) { //Torn del 1r jugador
            Button triat = (Button) event.getSource();
            Integer fila = miGrid.getRowIndex(triat);
            Integer columna = miGrid.getColumnIndex(triat);
            if (fila == null) fila = 0;
            if (columna == null) columna = 0;
            if (matriu[0][columna] != 0) { error.setText(missatgeError); }
            else {
                fila = matriu.length - 1;
                while (fila >= 0 && matriu[fila][columna] != 0) { fila--; }
                matriu[fila][columna] = 1;
                Button aux = (Button) getNode(fila, columna);
                aux.setText("O");
                aux.setStyle("-fx-color: Red");
                if (!maquina && jugador2 != null) {
                    text_jugador.setText("Torn de " + jugador2.getNickName());
                    text_jugador.setStyle("-fx-color: Blue");
                }
            }
        } else { 
            if (maquina) { //Torn de la màquina
                Random aleatori = new Random();
                int cAleatoria = aleatori.nextInt(7);
                while (matriu[0][cAleatoria] != 0) { cAleatoria = aleatori.nextInt(7); }
                
            } else { //Torn del 2on jugador
                Button triat = (Button) event.getSource();
                Integer fila = miGrid.getRowIndex(triat);
                Integer columna = miGrid.getColumnIndex(triat);
                if (fila == null) fila = 0;
                if (columna == null) columna = 0;
                if (matriu[0][columna] != 0) { error.setText(missatgeError); }
                else {
                    fila = matriu.length - 1;
                    while (fila >= 0 && matriu[fila][columna] != 0) { fila--; }
                    matriu[fila][columna] = 2;
                    Button aux = (Button) getNode(fila, columna);
                    aux.setText("O");
                    aux.setStyle("-fx-color: Blue");
                    if (!maquina && jugador2 != null) {
                        text_jugador.setText("Torn de " + jugador1.getNickName());
                        text_jugador.setStyle("-fx-color: Red");
                    }
                }
            }
        }
        esJugador1 = !esJugador1;
        numJugades++;
        if (numJugades > 6) comprovarVictoria();
    }
    private Node getNode(int fila, int col) {
        for (Node node : miGrid.getChildren()) {
            Integer f = GridPane.getRowIndex(node);
            Integer c = GridPane.getColumnIndex(node);
            if (f == null) f = 0;
            if (c == null) c = 0;
            if (c == col && f == fila) {
                return node;
            }
        }
        return null;
    }
    private void comprovarVictoria() {
        
    }
    public void inicialitzarJugador1(Player j1) {
        this.jugador1 = j1;
        if (jugador1 != null && !maquina) text_jugador.setText("Torn de " + jugador1.getNickName());
        punts = sistema.getPointsAlone();
    }
    public void inicialitzarJugadors(Player j1, Player j2) {
        this.jugador1 = j1;
        this.jugador2 = j2;
        if (jugador1 != null) text_jugador.setText("Torn de " + jugador1.getNickName());
        punts = sistema.getPointsRound();
    }

    @FXML
    private void enrere(ActionEvent event) throws IOException {
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("/vista/PrimerJugador.fxml"));
        Parent root = cargador.load();
        PrimerJugadorController controlador = cargador.getController();
        controlador.inicialitzarJugadors(jugador1,jugador2);
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.toFront();
        stage.show();
    }
}
