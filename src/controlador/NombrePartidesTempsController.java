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
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import model.Connect4;
import model.Player;
import model.Round;

/**
 * FXML Controller class
 *
 * @author inmad
 */
public class NombrePartidesTempsController implements Initializable {

    @FXML
    private DatePicker daraInici;
    @FXML
    private DatePicker dataFi;
    @FXML
    private LineChart<LocalDate, Number> chart;
    @FXML
    private NumberAxis yAxis;
    @FXML
    private CategoryAxis xAxis;
    private Set<LocalDate> dates;
    private Connect4 sistema;
    private  TreeMap<LocalDate,List<Round>> partidesPerDia;
    
    private ObservableList<XYChart.Data<LocalDate,Number>> llistaDates = FXCollections.observableArrayList();
    
    private Player jugador1 = null, jugador2 = null;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            sistema = Connect4.getSingletonConnect4();
            partidesPerDia = sistema.getRoundsPerDay();
            dates = partidesPerDia.keySet();
            for (LocalDate data: dates) {
                llistaDates.add(new XYChart.Data<LocalDate, Number> (data, partidesPerDia.get(data).size()));
            }
            xAxis.setLabel("Data");
            yAxis.setLabel("Nombre de partides");
        } catch (Connect4DAOException ex) {
            Logger.getLogger(NombrePartidesTempsController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }    
    public void inicialitzarJugador (Player j1) { jugador1 = j1; }
    public void inicialitzarJugadors (Player j1, Player j2) { 
        jugador1 = j1;
        jugador2 = j2;
    }
    @FXML
    private void enrere(ActionEvent event) throws IOException {
        if (jugador1 != null) {
            FXMLLoader cargador = new FXMLLoader(getClass().getResource("/vista/PrimerJugador.fxml"));
            Parent root = cargador.load();
            PrimerJugadorController controlador = cargador.getController();
            if (jugador2 != null) {
                controlador.inicialitzarJugadors(jugador1, jugador2);
            } else {
                controlador.inicialitzarJugador(jugador1);
            }
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
    
}
