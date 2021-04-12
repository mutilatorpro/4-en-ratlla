/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import DBAccess.Connect4DAOException;
import java.io.IOException;
import java.util.Random;
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
        Player jugador = sistema.getPlayer(nombreTextF);
        if (jugador == null) { 
            error.setText("El nom d'usuari inserit no existeix\nIntenta-ho de nou."); 
            //Interessant mirar si se poden posar els textFields amb el borde roig
        }
        
        if (jugador.getEmail().equals(correuTextF)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setTitle("Generador de codi de seguretat");
            int num = (int) (Math.random() + Math.random() * 10 + Math.random() * 100 + Math.random() * 1000);
            alert.setContentText("Aquest és el codi de recuperació del teu compte " + num);
            alert.showAndWait();
            
            
        }
        
        } else {
            FXMLLoader cargador = new FXMLLoader(getClass().getResource("/vista/PrimerJugador.fxml"));
            Parent root = cargador.load();
            PrimerJugadorController controlador = cargador.getController();
            controlador.inicialitzarJugador(jugador);
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.toFront();
            stage.show();
        }
    }
    @FXML
    private void cancelCambios(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/vista/Autenticar.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.toFront();
        stage.show();
    }
    
}
