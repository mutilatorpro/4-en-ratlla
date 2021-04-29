/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
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
    @FXML
    private Button iniciar;
    @FXML
    private Button tancar2;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ranquing.setDisable(true);
        estadistiques.setDisable(true);
        pvp.disableProperty().bind(Bindings.lessThan(numJugadors, 2));
        iniciar.disableProperty().bind(Bindings.equal(numJugadors,2));
        tancar2.disableProperty().bind(Bindings.lessThan(numJugadors,2));
    }    

    public void inicialitzarJugador(Player player) {
        this.jugador1 = player;
        numJugadors.set(1);
        benvinguda.setText("Hola " + this.jugador1.getNickName() + "!");
    }
    public void inicialitzarJugadors(Player j1, Player j2) {
        jugador1 = j1;
        jugador2 = j2;
        numJugadors.set(2);
        benvinguda.setText("Hola " + this.jugador1.getNickName() + " i " + this.jugador2.getNickName() + "!");
    }
    @FXML
    private void tancarSessio(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(null);
        alert.setTitle("Confirmació eixida");
        alert.setContentText("Estàs segur que vols tancar la sessió de l'usuari " + this.jugador1.getNickName() + "?");
        Optional<ButtonType> action = alert.showAndWait();
        if (action.get() == ButtonType.OK) {
            jugador1 = null;
            numJugadors.set(numJugadors.get() - 1);
            if (jugador2 != null) {
                jugador1 = jugador2;
                jugador2 = null;
                benvinguda.setText("Hola " + this.jugador1.getNickName());
            }
            else {
                Parent root = FXMLLoader.load(getClass().getResource("/vista/Principal.fxml"));
                Scene scene = new Scene(root);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.toFront();
                stage.show();
            }
        }
    }

    @FXML
    private void jugarJugador(ActionEvent event) throws IOException {
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("/vista/Joc4.fxml"));
        Parent root = cargador.load();
        Joc4Controller controlador = cargador.getController();
        controlador.inicialitzarJugadors(jugador1,jugador2);
        controlador.noMaquina();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.toFront();
        stage.show();
    }

    @FXML
    private void jugarMaquina(ActionEvent event) throws IOException {
        if (jugador2 == null) {
            FXMLLoader cargador = new FXMLLoader(getClass().getResource("/vista/Joc4.fxml"));
            Parent root = cargador.load();
            Joc4Controller controlador = cargador.getController();
            controlador.inicialitzarJugador1(jugador1);
            controlador.maquina();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.toFront();
            stage.show();
        } else {
            FXMLLoader cargador = new FXMLLoader(getClass().getResource("/vista/TriarJugador.fxml"));
            Parent root = cargador.load();
            TriarJugadorController controlador = cargador.getController();
            controlador.inicialitzarJugadors(jugador1, jugador2);
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.toFront();
            stage.show();
        }
    }

    @FXML
    private void tancarSessio2(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(null);
        alert.setTitle("Confirmació tancar sessió");
        alert.setContentText("Estàs segur que vols tancar la sessió de l'usuari " + this.jugador2.getNickName() + "?");
        Optional<ButtonType> action = alert.showAndWait();
        if (action.get() == ButtonType.OK) {
            numJugadors.set(numJugadors.get() - 1);
            jugador2 = null;
            benvinguda.setText("Hola " + this.jugador1.getNickName());
        }
    }

    @FXML
    private void iniciarSessio(ActionEvent event) throws IOException {
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("/vista/Autenticar.fxml"));
        Parent root = cargador.load();
        AutenticarController controlador = cargador.getController();
        controlador.inicialitzarJugador(jugador1);
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.toFront();
        stage.show();
    }
    
}
