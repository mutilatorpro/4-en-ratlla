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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import model.Player;

/**
 * FXML Controller class
 *
 * @author Violeta
 */
public class CodiController implements Initializable {

    private int codiUsu = 0;
    private String nomUsu = "";
    private String contraUsu = "";
    private Player jugador1 = null;
    @FXML
    private TextField codiUsuari;
    @FXML
    private Label error;
    @FXML
    private Button botoOK;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Parent root = botoOK.getParent();
        while (root.getParent() != null) root = root.getParent();
        if (Dades.getDades().isModeObs())  { 
            root.getStylesheets().remove("resources/blancFulla.css");
            root.getStylesheets().add("resources/obscFulla.css");
        }
        else {
            root.getStylesheets().remove("resources/obscFulla.css"); 
            root.getStylesheets().add("resources/blancFulla.css");
        } 
        botoOK.disableProperty().bind(Bindings.equal(codiUsuari.textProperty(),""));
    }    

    @FXML
    private void okCambios(ActionEvent event) throws IOException {
        if (Integer.parseInt(codiUsuari.getText()) == (this.codiUsu)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setTitle("Recordador de contrasenya");
            alert.setContentText("Aquest ??s la contrasenya del teu compte " + contraUsu + " del teu compte amb " + nomUsu + " com a nom d'usuari");
            alert.showAndWait();
             FXMLLoader cargador = new FXMLLoader(getClass().getResource("/vista/Autenticar.fxml"));
            Parent root = cargador.load();
            AutenticarController controlador = cargador.getController();
            if (jugador1 != null) controlador.inicialitzarJugador(jugador1);
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.toFront();
            stage.show();

        } else {
            error.setText("El codi inserit no ??s v??lid.");
            codiUsuari.setStyle("-fx-border-color: red");
        }
    }

    @FXML
    private void cancelCambios(ActionEvent event) throws IOException {
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("/vista/Recordar.fxml"));
        Parent root = cargador.load();
        RecordarController controlador = cargador.getController();
        if (jugador1 != null) controlador.inicialitzarJugador(jugador1);
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.toFront();
        stage.show();

    }
    
    public void passarInfo(int num, String nom, String contra) {
         this.codiUsu = num;
         this.nomUsu = nom;
         this.contraUsu = contra;
    }
    public void inicialitzarJugador(Player j1) {
        jugador1 = j1;
    }

    @FXML
    private void enter(KeyEvent event) throws IOException {
        KeyCode tecla = event.getCode();
        if (tecla == KeyCode.ENTER) {
            if (!codiUsuari.getText().equals("")) {
                if (Integer.parseInt(codiUsuari.getText()) == (this.codiUsu)) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText(null);
                    alert.setTitle("Recordador de contrasenya");
                    alert.setContentText("Aquest ??s la contrasenya del teu compte " + contraUsu + " del teu compte amb " + nomUsu + " com a nom d'usuari");
                    alert.showAndWait();
                     FXMLLoader cargador = new FXMLLoader(getClass().getResource("/vista/Autenticar.fxml"));
                    Parent root = cargador.load();
                    AutenticarController controlador = cargador.getController();
                    if (jugador1 != null) controlador.inicialitzarJugador(jugador1);
                    Scene scene = new Scene(root);
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.setScene(scene);
                    stage.toFront();
                    stage.show();

                } else {
                    error.setText("El codi inserit no ??s v??lid.");
                    codiUsuari.setStyle("-fx-border-color: red");
                }
            }
        }
    }
}
