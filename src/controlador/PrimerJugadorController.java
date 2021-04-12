/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import model.Connect4;
import model.Player;

/**
 * FXML Controller class
 *
 * @author inmad
 */
public class PrimerJugadorController implements Initializable {
    
    private Player jugador1 = null;
    private Player jugador2 = null;
    private SimpleIntegerProperty numJugadors = new SimpleIntegerProperty(0);
    @FXML
    private Label benvinguda;
    @FXML
    private Button obscur;
    @FXML
    private Button pvp;
    @FXML
    private Button maquina;
    @FXML
    private Button estadistiques;
    @FXML
    private Button ranquing;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ranquing.setDisable(true);
        estadistiques.setDisable(true);
        pvp.disableProperty().bind(Bindings.lessThan(numJugadors, 2));
    }    

    public void inicialitzarJugador(Player player) {
        this.jugador1 = player;
        numJugadors.set(numJugadors.get() + 1);
        benvinguda.setText("Hola " + this.jugador1.getNickName() + "!");
    }
    public void inicialitzarJugadors(Player j1, Player j2) {
        jugador1 = j1;
        jugador2 = j2;
    }
    @FXML
    private void tancarSessio(ActionEvent event) throws IOException {
        jugador1 = null;
        numJugadors.set(numJugadors.get() - 1);
        Parent root = FXMLLoader.load(getClass().getResource("/vista/Principal.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.toFront();
        stage.show();
    }

    @FXML
    private void jugarJugador(ActionEvent event) {
    }

    @FXML
    private void jugarMaquina(ActionEvent event) throws IOException {
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("/vista/Joc4.fxml"));
        Parent root = cargador.load();
        Joc4Controller controlador = cargador.getController();
        controlador.inicialitzarJugador1(jugador1);
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.toFront();
        stage.show();
    }

    @FXML
    private void tancarSessio2(ActionEvent event) {
    }
    
}
