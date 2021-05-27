/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import DBAccess.Connect4DAOException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import model.Connect4;
import model.Player;

/**
 * FXML Controller class
 *
 * @author inmad
 */
public class PrincipalController implements Initializable {
    private boolean modeObsc = false;
    @FXML
    private Button botAutenticar;
    @FXML
    private Button botRegistrar;
    @FXML
    private Button botRanking;
    @FXML
    private Button botEstadistiques;
    @FXML
    private Button obscur;
    @FXML
    private ImageView imgview;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Parent root = botAutenticar.getParent();
        while (root.getParent() != null) root = root.getParent();
        if (Dades.getDades().isModeObs())  { 
            root.getStylesheets().remove("resources/blancFulla.css");
            root.getStylesheets().add("resources/obscFulla.css");
        }
        else {
            root.getStylesheets().remove("resources/obscFulla.css"); 
            root.getStylesheets().add("resources/blancFulla.css");
        } 
    }    

    @FXML
    private void finestraAutenticar(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/vista/Autenticar.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.toFront();
        stage.show();
    }

    @FXML
    private void finestraRegistrar(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/vista/Registrar.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.toFront();
        stage.show();
    }

    @FXML
    private void finestraRanquing(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/vista/Ranquing.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.toFront();
        stage.show();
    }

    @FXML
    private void finestraEstadistiques(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/vista/Estadistiques.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.toFront();
        stage.show();
    }

//    @FXML
//    private void nombrePartides(ActionEvent event) throws IOException {
//        Parent root = FXMLLoader.load(getClass().getResource("/vista/NombrePartidesTemps.fxml"));
//        Scene scene = new Scene(root);
//        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//        stage.setScene(scene);
//        stage.toFront();
//        stage.show();
//    }
//
//    @FXML
//    private void partidesGuanyades(ActionEvent event) throws IOException {
//        Parent root = FXMLLoader.load(getClass().getResource("/vista/NombrePartidesGuanyadesPerdudes.fxml"));
//        Scene scene = new Scene(root);
//        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//        stage.setScene(scene);
//        stage.toFront();
//        stage.show();
//    }
//
//    @FXML
//    private void rondesSistema(ActionEvent event) throws IOException {
//        Parent root = FXMLLoader.load(getClass().getResource("/vista/PartidesSistema.fxml"));
//        Scene scene = new Scene(root);
//        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//        stage.setScene(scene);
//        stage.toFront();
//        stage.show();
//    }
//
//    @FXML
//    private void rondesJugador(ActionEvent event) throws IOException {
//        Parent root = FXMLLoader.load(getClass().getResource("/vista/PartidesJugador.fxml"));
//        Scene scene = new Scene(root);
//        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//        stage.setScene(scene);
//        stage.toFront();
//        stage.show();
//    }
//
//    @FXML
//    private void rondesJugadorGuanyades(ActionEvent event) throws IOException {
//        Parent root = FXMLLoader.load(getClass().getResource("/vista/PartidesJugadorGuanyades.fxml"));
//        Scene scene = new Scene(root);
//        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//        stage.setScene(scene);
//        stage.toFront();
//        stage.show();
//    }
//
//    @FXML
//    private void rondesJugadorPerdudes(ActionEvent event) throws IOException {
//        Parent root = FXMLLoader.load(getClass().getResource("/vista/PartidesJugadorPerdudes.fxml"));
//        Scene scene = new Scene(root);
//        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//        stage.setScene(scene);
//        stage.toFront();
//        stage.show();
//    }

    @FXML
    private void obscBut(ActionEvent event) {
        modeObsc = !modeObsc;
        Dades.getDades().setModeObs(modeObsc);
        Parent root = obscur.getParent(); // EN AQUESTA PANTALLA NO VULL CANVIAR AL MODE OBSCUR, VULL QUE ES QUEDE EN BLAU
        while (root.getParent() != null) root = root.getParent();
        
        if (Dades.getDades().isModeObs())  { 
            root.getStylesheets().remove("resources/blancFulla.css");
            root.getStylesheets().add("resources/obscFulla.css");
        }
        else {
            root.getStylesheets().remove("resources/obscFulla.css"); 
            root.getStylesheets().add("resources/blancFulla.css");
            }
    }
}
