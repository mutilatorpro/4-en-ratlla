/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author inmad
 */
public class RegistrarController implements Initializable {

    @FXML
    private Label error;
    @FXML
    private TextField nom;
    @FXML
    private TextField contrasenya;
    @FXML
    private TextField correu;
    @FXML
    private DatePicker data;
    @FXML
    private Button registrar;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        registrar.disableProperty().bind(Bindings.or(Bindings.equal(nom.textProperty(), ""), Bindings.or(Bindings.equal(contrasenya.textProperty(),""), Bindings.or(Bindings.equal(correu.textProperty(),""), Bindings.isNull(data.valueProperty())))));
    }    

    @FXML
    private void okCambios(ActionEvent event) {
        
    }

    @FXML
    private void cancelCambios(ActionEvent event) {
        
    }
    
}
