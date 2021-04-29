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
import model.Connect4;
import model.Player;

/**
 * FXML Controller class
 *
 * @author inmad
 */
public class RecordarController implements Initializable {
    private Connect4 sistema;
    private Player jugador1 = null;
    @FXML
    private TextField nombreTextF;
    @FXML
    private Label error;
    @FXML
    private Button botoOK;
    @FXML
    private TextField correuTextF;

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
        botoOK.disableProperty().bind(Bindings.or(Bindings.equal(nombreTextF.textProperty(),""),Bindings.equal(correuTextF.textProperty(),"")));
        
    }    
    
    
    @FXML
    private void okCambios(ActionEvent event) throws IOException {
        Player jugador = sistema.getPlayer(nombreTextF.getText());
        if (jugador == null) { 
            error.setText("El nom d'usuari inserit no existeix."); 
            //Interessant mirar si se poden posar els textFields amb el borde roig
            correuTextF.setStyle("-fx-border-color: red");
            nombreTextF.setStyle("-fx-border-color: red");
        }
        if (jugador.getEmail().equals(correuTextF.getText())) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setTitle("Generador de codi de seguretat");
            Random generador = new Random();
            int num = (generador.nextInt(10) + generador.nextInt(10) * 10 + generador.nextInt(10) * 100 + generador.nextInt(10) * 1000);
            alert.setContentText("Aquest és el codi de recuperació del teu compte " + num);
            alert.showAndWait();
            FXMLLoader cargador = new FXMLLoader(getClass().getResource("/vista/Codi.fxml"));
            Parent root = cargador.load();
            CodiController controlador = cargador.getController();
            controlador.passarInfo(num, jugador.getNickName(), jugador.getPassword());
            if (jugador1 != null) controlador.inicialitzarJugador(jugador1);
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.toFront();
            stage.show();
        } else { 
            error.setText("El nom d'usuari no es correspón amb el correu introduït.");
        }
    }
    @FXML
    private void cancelCambios(ActionEvent event) throws IOException {
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("/vista/Autenticar.fxml"));
        Parent root = cargador.load();
        AutenticarController controlador = cargador.getController();
        if (jugador1 != null) controlador.inicialitzarJugador(jugador1);
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.toFront();
        stage.show();
    }
    public void inicialitzarJugador(Player j1) {
        jugador1 = j1;
    }

    @FXML
    private void enter(KeyEvent event) throws IOException {
        KeyCode tecla = event.getCode();
        if (tecla == KeyCode.ENTER) {
            if (!nombreTextF.getText().equals("") && !correuTextF.getText().equals("")) {
                Player jugador = sistema.getPlayer(nombreTextF.getText());
                if (jugador == null) { 
                    error.setText("El nom d'usuari inserit no existeix\nIntenta-ho de nou."); 
                    //Interessant mirar si se poden posar els textFields amb el borde roig
                    correuTextF.setStyle("-fx-border-color: red");
                    nombreTextF.setStyle("-fx-border-color: red");
                }
                if (jugador.getEmail().equals(correuTextF.getText())) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText(null);
                    alert.setTitle("Generador de codi de seguretat");
                    Random generador = new Random();
                    int num = (generador.nextInt(10) + generador.nextInt(10) * 10 + generador.nextInt(10) * 100 + generador.nextInt(10) * 1000);
                    alert.setContentText("Aquest és el codi de recuperació del teu compte " + num);
                    alert.showAndWait();
                    FXMLLoader cargador = new FXMLLoader(getClass().getResource("/vista/Codi.fxml"));
                    Parent root = cargador.load();
                    CodiController controlador = cargador.getController();
                    controlador.passarInfo(num, jugador.getNickName(), jugador.getPassword());
                    if (jugador1 != null) controlador.inicialitzarJugador(jugador1);
                    Scene scene = new Scene(root);
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.setScene(scene);
                    stage.toFront();
                    stage.show();
                } else { 
                    error.setText("El nom d'usuari no es correspón amb el correu introduït.");
                }
            }
        }
    }
}
