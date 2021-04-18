/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Violeta
 */
public class CodiController implements Initializable {

    private int codiUsu = 0;
    private String nomUsu = "";
    private String contraUsu = "";
    
    @FXML
    private TextField codiUsuari;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void okCambios(ActionEvent event) throws IOException {
        
        if (Integer.parseInt(codiUsuari.getText()) == (this.codiUsu)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setTitle("Recordador de contrasenya");
            alert.setContentText("Aquest és la contrasenya del teu compte " + contraUsu + " del teu compte amb " + nomUsu + " com a nom d'usuari");
            alert.showAndWait();
            
            Parent root = FXMLLoader.load(getClass().getResource("/vista/Autenticar.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.toFront();
            stage.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setTitle("Recordador de contrasenya");
            alert.setContentText("El codi inserit no coincideix amb el proporcionat pel sistema\n Torna a intentar-ho");
            alert.showAndWait();
        }
    }

    @FXML
    private void cancelCambios(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/vista/Recordar.fxml"));
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
}
