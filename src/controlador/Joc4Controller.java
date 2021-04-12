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
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
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
    private void moure(ActionEvent event) throws InterruptedException, IOException {
        if (numJugades < 56) { //només hi ha 56 caselles, s'ha de comprovar que encara quede alguna lliure
            if (maquina) {
                Button triat = (Button) event.getSource();
                Integer columna = miGrid.getColumnIndex(triat);
                if (columna == null) columna = 0;
                if (matriu[0][columna] != 0) { error.setText(missatgeError); }
                else {
                    int fila = matriu.length - 1;
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
                numJugades++;
                if (numJugades > 6) comprovarVictoria(event);
                //Thread.sleep(500);
                if (numJugades < 56) {
                    Task<Void> sleeper = new Task<Void>() {
                        @Override
                        protected Void call() throws Exception {
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                            }
                            return null;
                        }
                    };
                    sleeper.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                        @Override
                        public void handle(WorkerStateEvent evento) {
                            Random aleatori = new Random();
                            int cMaquina = aleatori.nextInt(8);
                            while (matriu[0][cMaquina] != 0) cMaquina = aleatori.nextInt(8);
                            int fMaquina = matriu.length - 1;
                            while (fMaquina >= 0 && matriu[fMaquina][cMaquina] != 0) fMaquina--;
                            matriu[fMaquina][cMaquina] = 2;
                            Button auxMaquina = (Button) getNode(fMaquina, cMaquina);
                            auxMaquina.setText("O");
                            auxMaquina.setStyle("-fx-color: Blue");
                            numJugades++;
                            if (numJugades > 6) { 
                                try {comprovarVictoria(event); }
                                catch (IOException ex) {    }
                            }
                        }
                    });
                    new Thread(sleeper).start(); 
                } else { taulerPle(event); }
            } else {
                if (esJugador1) { //Torn Jugador 1 contra Jugador 2
                    Button triat = (Button) event.getSource();
                    Integer columna = miGrid.getColumnIndex(triat);
                    if (columna == null) columna = 0;
                    if (matriu[0][columna] != 0) error.setText(missatgeError);
                    else {
                        int fila = matriu.length - 1;
                        while (fila >= 0 && matriu[fila][columna] != 0) {
                            fila--;
                        }
                        matriu[fila][columna] = 1;
                        Button aux = (Button) getNode(fila, columna);
                        aux.setText("O");
                        aux.setStyle("-fx-color: Red");
                        if (jugador2 != null) {
                            text_jugador.setText("Torn de " + jugador2.getNickName());
                            text_jugador.setStyle("-fx-color: Blue");
                        }
                    }
                } else { //Torn Jugador 2 contra Jugador 1
                    Button triat = (Button) event.getSource();
                    Integer columna = miGrid.getColumnIndex(triat);
                    if (columna == null) columna = 0;
                    if (matriu[0][columna] != 0) { error.setText(missatgeError); }
                    else {
                        int fila = matriu.length - 1;
                        while (fila >= 0 && matriu[fila][columna] != 0) { fila--; }
                        matriu[fila][columna] = 2;
                        Button aux = (Button) getNode(fila, columna);
                        aux.setText("O");
                        aux.setStyle("-fx-color: Blue");
                        if (jugador1 != null) {
                            text_jugador.setText("Torn de " + jugador1.getNickName());
                            text_jugador.setStyle("-fx-color: Red");
                        }
                    }
                }
                esJugador1 = !esJugador1;
                numJugades++;
                if (numJugades > 6) comprovarVictoria(event);
            }
        } else { taulerPle(event); } 
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
    private void taulerPle(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setTitle("Empat!!");
        alert.setContentText("Ja no hi queden més caselles lliures.\nHeu arribat a un empat!\nTornant a la pàgina principal...");
        alert.showAndWait();
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("/vista/PrimerJugador.fxml"));
        Parent root = cargador.load();
        PrimerJugadorController controlador = cargador.getController();
        if (jugador2 != null) {
            controlador.inicialitzarJugadors(jugador1, jugador2);
        } else {
            controlador.inicialitzarJugador(jugador1);
        }
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.toFront();
        stage.show();
    }
    private void comprovarVictoria(ActionEvent event) throws IOException {
        boolean victoria = false;
        
        if (victoria) {
            FXMLLoader cargador = new FXMLLoader(getClass().getResource("/vista/PrimerJugador.fxml"));
            Parent root = cargador.load();
            PrimerJugadorController controlador = cargador.getController();
            if (jugador2 != null) controlador.inicialitzarJugadors(jugador1,jugador2);
            else controlador.inicialitzarJugador(jugador1);
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.toFront();
            stage.show();
        }
        
    }
    public void inicialitzarJugador1(Player j1) {
        this.jugador1 = j1;
        if (jugador1 != null){
            if (!maquina) text_jugador.setText("Torn de " + jugador1.getNickName());
            else text_jugador.setText("Jugant contra la màquina!");
        }
        punts = sistema.getPointsAlone();
    }
    public void inicialitzarJugadors(Player j1, Player j2) {
        this.jugador1 = j1;
        this.jugador2 = j2;
        if (jugador1 != null) text_jugador.setText("Torn de " + jugador1.getNickName());
        punts = sistema.getPointsRound();
        maquina = false;
    }

    @FXML
    private void enrere(ActionEvent event) throws IOException {
        //Ací falta un Alert de CONFIRMATION per comprovar que no ha clicat sense voler
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("/vista/PrimerJugador.fxml"));
        Parent root = cargador.load();
        PrimerJugadorController controlador = cargador.getController();
        if (jugador2 != null) controlador.inicialitzarJugadors(jugador1,jugador2);
        else controlador.inicialitzarJugador(jugador1);
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.toFront();
        stage.show();
    }
}
