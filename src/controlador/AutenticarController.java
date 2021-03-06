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
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import static javafx.scene.input.KeyCode.UP;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
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
        try {
            sistema = Connect4.getSingletonConnect4();
        } catch (Connect4DAOException ex) {
            System.out.println("Error en la c??rrega del sistema");
        }
        botoOK.disableProperty().bind(Bindings.or(Bindings.equal(nombreTextF.textProperty(),""),Bindings.equal(contrasenyaTextF.textProperty(),"")));
    }    

    @FXML
    private void okCambios(ActionEvent event) throws IOException {
        Player jugador = sistema.loginPlayer(nombreTextF.getText(), contrasenyaTextF.getText());
        if (jugador == null) { 
            error.setText("El nom d'usuari i la contrasenya no coincideixen. \nTinc compte amb les maj??scules i min??scules i torna a intentar-ho.");
            contrasenyaTextF.setStyle("-fx-border-color: red");
            nombreTextF.setStyle("-fx-border-color: red");
        } 
        else if(jugador.equals(jugador1)) { error.setText("Eixe jugador ja ha iniciat sessi?? en el sistema."); }
        else {
            FXMLLoader cargador = new FXMLLoader(getClass().getResource("/vista/PrimerJugador.fxml"));
            Parent root = cargador.load();
            PrimerJugadorController controlador = cargador.getController();
            if (jugador1 != null) {
                controlador.inicialitzarJugadors(jugador1, jugador); //El 1r jugador ja havia iniciat sessi?? i l'hem de mantindre i el 2n ??s el nou
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

    @FXML
    private void mouseOFF(MouseEvent event) {
        botoRecordar.setStyle("-fx-background-color:  #999999; -fx-background-radius: 15; -fx-text-fill:  #ffff;");
    }

    @FXML
    private void mouseON(MouseEvent event) {
        botoRecordar.setStyle("-fx-background-color:  #0e53c3; -fx-background-radius: 15; -fx-text-fill:  #ffff;");
    }

    @FXML
    private void enter(KeyEvent event) throws IOException {
        KeyCode tecla = event.getCode();
        if (tecla == KeyCode.ENTER) {
            if (!contrasenyaTextF.getText().equals("") && !nombreTextF.getText().equals("")) {
                Player jugador = sistema.loginPlayer(nombreTextF.getText(), contrasenyaTextF.getText());
                if (jugador == null) { 
                    error.setText("El nom d'usuari i la contrasenya no coincideixen\nIntenta-ho de nou."); 
                    contrasenyaTextF.setStyle("-fx-border-color: red");
                    nombreTextF.setStyle("-fx-border-color: red");
                    //Interessant mirar si se poden posar els textFields amb el borde roig
                } 
                else if(jugador.equals(jugador1)) { error.setText("Eixe jugador ja ha iniciat sessi?? en el sistema."); }
                else {
                    FXMLLoader cargador = new FXMLLoader(getClass().getResource("/vista/PrimerJugador.fxml"));
                    Parent root = cargador.load();
                    PrimerJugadorController controlador = cargador.getController();
                    if (jugador1 != null) {
                        controlador.inicialitzarJugadors(jugador1, jugador); //El 1r jugador ja havia iniciat sessi?? i l'hem de mantindre i el 2n ??s el nou
                    }
                    else controlador.inicialitzarJugador(jugador);
                    Scene scene = new Scene(root);
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.setScene(scene);
                    stage.toFront();
                    stage.show();
                }
            }
        }
    }
}
