/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import DBAccess.Connect4DAOException;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
public class AutenticarController implements Initializable {
    
    private Connect4 sistema;
    private Player jugador1 = null;
    @FXML
    private TextField nombreTextF;
    @FXML
    private Button botoOK;
    @FXML
    private TextField contrasenyaTextF;
    @FXML
    private Label error;
    @FXML
    private Button botoRecordar;
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
        botoOK.disableProperty().bind(Bindings.or(Bindings.equal(nombreTextF.textProperty(),""),Bindings.equal(contrasenyaTextF.textProperty(),"")));
    }    

    @FXML
    private void okCambios(ActionEvent event) throws IOException {
        Player jugador = sistema.loginPlayer(nombreTextF.getText(), contrasenyaTextF.getText());
        if (jugador == null) { 
            error.setText("El nom d'usuari i la contrasenya no coincideixen\nIntenta-ho de nou."); 
            //Interessant mirar si se poden posar els textFields amb el borde roig
        } 
        else if(jugador.equals(jugador1)) { error.setText("Eixe jugador ja ha iniciat sessió en el sistema."); }
        else {
            FXMLLoader cargador = new FXMLLoader(getClass().getResource("/vista/PrimerJugador.fxml"));
            Parent root = cargador.load();
            PrimerJugadorController controlador = cargador.getController();
            if (jugador1 != null) {
                controlador.inicialitzarJugadors(jugador1, jugador); //El 1r jugador ja havia iniciat sessió i l'hem de mantindre i el 2n és el nou
            }
            else controlador.inicialitzarJugador(jugador);
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.toFront();
            stage.show();
        }
    }

    @FXML
    private void cancelCambios(ActionEvent event) throws IOException {
        if (jugador1 != null) {
            FXMLLoader cargador = new FXMLLoader(getClass().getResource("/vista/PrimerJugador.fxml"));
            Parent root = cargador.load();
            PrimerJugadorController controlador = cargador.getController();
            controlador.inicialitzarJugador(jugador1);
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.toFront();
            stage.show();
        } else {
            Parent root = FXMLLoader.load(getClass().getResource("/vista/Principal.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.toFront();
            stage.show();
        }
    }

    @FXML
    private void finestraRecordar(ActionEvent event) throws IOException {
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
    public void inicialitzarJugador(Player j1) {
        jugador1 = j1;
    }
}
