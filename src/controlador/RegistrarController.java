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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author inmad
 */
public class RegistrarController implements Initializable {

    @FXML
    private TextField nombreTextF;
    @FXML
    private Label error;
    @FXML
    private TextField apellidoTextF;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void okCambios(ActionEvent event) {
    }

    @FXML
    private void cancelCambios(ActionEvent event) {
    }
    
}
