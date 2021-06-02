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
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
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
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.util.converter.LocalDateStringConverter;
import model.Connect4;
import model.Player;
import model.Round;

/**
 * FXML Controller class
 *
 * @author inmad
 */
public class NombrePartidesTempsController implements Initializable {

    //private DatePicker dataInici;
    //private DatePicker dataFi;
    @FXML
    private LineChart<String, Number> chart;
    @FXML
    private NumberAxis yAxis;
    @FXML
    private CategoryAxis xAxis;
    private Set<LocalDate> dates;
    private Connect4 sistema;
    private  TreeMap<LocalDate,Integer> partidesPerDia;
    
    private ObservableList<XYChart.Data<String,Number>> llistaDates = FXCollections.observableArrayList();
    private LocalDate dataI, dataF;
    private String nomUsuari = "";
    private Player jugador1 = null, jugador2 = null;
    private DateTimeFormatter formatter;
    //private Label error;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Parent root = chart.getParent();
        while (root.getParent() != null) root = root.getParent();
        if (Dades.getDades().isModeObs())  { 
            root.getStylesheets().remove("resources/blancFulla.css");
            root.getStylesheets().add("resources/obscFulla.css");
        }
        else {
            root.getStylesheets().remove("resources/obscFulla.css"); 
            root.getStylesheets().add("resources/blancFulla.css");
        } 
        inicialitzarDades();
//        dataInici.setEditable(false); //per evitar que es puga introduir la data "a mà"
//        dataInici.setDayCellFactory(c -> new DateCell() {
//            public void updateItem(LocalDate item, boolean empty) {
//                super.updateItem(item, empty);
//                setDisable(item.isAfter(LocalDate.now()));
//            }
//        });
        formatter = DateTimeFormatter.ofPattern("dd/MM/yy");
//        dataInici.setConverter(new LocalDateStringConverter(formatter, null));
//        dataInici.showWeekNumbersProperty().set(false);
//        dataFi.setEditable(false); //per evitar que es puga introduir la data "a mà"
//        dataFi.setDayCellFactory(c -> new DateCell() {
//            public void updateItem(LocalDate item, boolean empty) {
//                super.updateItem(item, empty);
//                setDisable(item.isAfter(LocalDate.now()));
//            }
//        });
//        dataFi.setConverter(new LocalDateStringConverter(formatter, null));
//        dataFi.showWeekNumbersProperty().set(false);
        //chart.disableProperty().bind(Bindings.or(Bindings.isNull(dataInici.valueProperty()), Bindings.isNull(dataFi.valueProperty())));
        try {
            sistema = Connect4.getSingletonConnect4();
            partidesPerDia = sistema.getRoundCountsPerDay();
            dates = partidesPerDia.keySet();
//            dataFi.valueProperty().addListener((observable, valorAntic, valorNou) -> { 
//                dataF = valorNou;
//                if (dataI != null) {
//                    if (dataI.isBefore(dataF) || dataF.isAfter(dataI)) error.setText("La data d'inici ha de ser prèvia a la de fi.");
//                    else {
//                        reubica();
//                    }
//                }
//                
//            });
//            dataInici.valueProperty().addListener((observable, valorAntic, valorNou) -> {
//                dataI = valorNou; 
//                if (dataF != null) {
//                    if (dataI.isAfter(dataF) || dataF.isBefore(dataI)) error.setText("La data d'inici ha de ser prèvia a la de fi.");
//                    else {
//                        reubica();
//                    }
//                }
//            });
            chart.setTitle("Partides jugades per dia");
            chart.getData().add(new XYChart.Series<> ("Partides", llistaDates));
            xAxis.setLabel("Data");
            xAxis.setTickLabelFill(Paint.valueOf("white"));
            yAxis.setTickLabelFill(Paint.valueOf("white"));
            yAxis.setLabel("Nombre de partides");
            inicialitzarDades();
            reubica();
        } catch (Connect4DAOException ex) {
            Logger.getLogger(NombrePartidesTempsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    private void reubica () {
        //error.setText("");
        llistaDates.clear();
        for (LocalDate data : dates) {
            if (!(data.isBefore(dataI) || data.isAfter(dataF))) {
                llistaDates.add(new XYChart.Data<String, Number>(data.format(formatter), partidesPerDia.get(data)));
            }
        }
    }
    public void inicialitzarDades() {
        dataI = Dades.getDades().getDataI();
        dataF = Dades.getDades().getDataF();
        nomUsuari = Dades.getDades().getNomUsuari();
    }
    public void inicialitzarJugador (Player j1) { jugador1 = j1; }
    public void inicialitzarJugadors (Player j1, Player j2) { 
        jugador1 = j1;
        jugador2 = j2;
    }
    
    
}
