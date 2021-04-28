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
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 * FXML Controller class
 *
 * @author inmad
 */
public class TriarJugadorController implements Initializable {

    @FXML
    private Label titol;
    @FXML
    private Label nomJugador1;
    @FXML
    private ImageView imgJugador1;
    @FXML
    private Label nomJugador2;
    @FXML
    private ImageView imgJugador2;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void enrere(ActionEvent event) {
    }

    @FXML
    private void jugarJugador1(MouseEvent event) {
    }

    @FXML
    private void jugarJugador2(MouseEvent event) {
    }
    
}
