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
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Player;

/**
 * FXML Controller class
 *
 * @author inmad
 */
public class TriarJugadorController implements Initializable {
    private Player jugador1, jugador2;
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
        
    }    

    @FXML
    private void enrere(ActionEvent event) throws IOException {
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("/vista/PrimerJugador.fxml"));
        Parent root = cargador.load();
        PrimerJugadorController controlador = cargador.getController();
        controlador.inicialitzarJugadors(jugador1, jugador2);
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.toFront();
        stage.show();
    }

    @FXML
    private void jugarJugador1(MouseEvent event) {
    }

    @FXML
    private void jugarJugador2(MouseEvent event) {
    }
    public void inicialitzarJugadors(Player j1, Player j2) {
        jugador1 = j1;
        jugador2 = j2;
        nomJugador1.setText(jugador1.getNickName());
        nomJugador2.setText(jugador2.getNickName());
        imgJugador1.setImage(jugador1.getAvatar());
        imgJugador2.setImage(jugador2.getAvatar());
    }
}
