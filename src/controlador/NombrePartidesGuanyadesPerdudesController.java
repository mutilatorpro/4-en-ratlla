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
import java.time.format.FormatStyle;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
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
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.util.converter.LocalDateStringConverter;
import jdk.nashorn.internal.objects.NativeDate;
import model.Connect4;
import model.DayRank;
import model.Player;
import model.Round;

/**
 * FXML Controller class
 *
 * @author inmad
 */
public class NombrePartidesGuanyadesPerdudesController implements Initializable {

    private Player jugador1 = null, jugador2 = null;
    private DateTimeFormatter formatter;
    private LocalDate dataI, dataF;
    private Connect4 sistema;
    private Player usuariDades;
    private String nomUsuari = "";
    
    @FXML
    private Label error;
    @FXML
    private StackedBarChart<String, Number> chart1;
    @FXML
    private BarChart<String, Number> chart2;
    
    private ObservableList<XYChart.Data<String,Number>> dataVictories = FXCollections.observableArrayList();
    private ObservableList<XYChart.Data<String,Number>> dataDerrotes = FXCollections.observableArrayList();    
    private ObservableList<XYChart.Data<String,Number>> dataJugadorsDistints = FXCollections.observableArrayList();

    private TreeMap <LocalDate,DayRank> partidesPerDia;
    private XYChart.Series seriesVictories;
    private XYChart.Series seriesDerrotes;    
    private XYChart.Series seriesJugadorsDistints;
    @FXML
    private NumberAxis yAxis;
    @FXML
    private CategoryAxis xAxis;
    @FXML
    private NumberAxis yAxis2;
    @FXML
    private CategoryAxis xAxis2;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Parent root = chart1.getParent();
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
        formatter = DateTimeFormatter.ofPattern("dd/MM/yy");
        
        //chart1.disableProperty().bind(Bindings.or(Bindings.isNull(dataInici.valueProperty()), Bindings.or(Bindings.isNull(dataFi.valueProperty()), Bindings.equal(nomUsuari.textProperty(), ""))));
        chart2.disableProperty().bind(chart1.disableProperty());
        //boto.disableProperty().bind(chart1.disableProperty());
        try {
            sistema = Connect4.getSingletonConnect4();
        } catch (Connect4DAOException ex) {
            Logger.getLogger(NombrePartidesTempsController.class.getName()).log(Level.SEVERE, null, ex);
        }
//        dataInici.valueProperty().addListener((observable, valorAntic, valorNou) -> {
//                dataI = valorNou;
//        });
//        dataFi.valueProperty().addListener((observable, valorAntic, valorNou) -> {
//                dataF = valorNou;
//        });
        chart1.setTitle("Partides perdudes/guanyades");
        chart2.setTitle("Nombre d'oponents distints");
        seriesVictories = new XYChart.Series(dataVictories);
        seriesVictories.setName("Victòries");
        seriesDerrotes = new XYChart.Series(dataDerrotes);
        seriesDerrotes.setName("Derrotes");
        seriesJugadorsDistints = new XYChart.Series(dataJugadorsDistints);
        yAxis.setAutoRanging(false);
        yAxis.setTickUnit(1);
        yAxis.setMinorTickCount(0);
        yAxis2.setAutoRanging(false);
        yAxis2.setTickUnit(1);
        yAxis2.setMinorTickCount(0);
        xAxis.setAutoRanging(true);
        xAxis2.setAutoRanging(true);
        chart1.setAnimated(false);        
        chart2.setAnimated(false);
        xAxis.setTickLabelFill(Paint.valueOf("white"));      
        xAxis2.setTickLabelFill(Paint.valueOf("white"));
        carregarGrafiques();
    }    
    
    public void inicialitzarJugador (Player j1) { jugador1 = j1; }
    public void inicialitzarJugadors (Player j1, Player j2) { 
        jugador1 = j1;
        jugador2 = j2;
    }
    
    private void carregarGrafiques() {
        if (nomUsuari.equals("")) error.setText("Cal que introduïsques un nom d'usuari.");
        else if (dataI == null )error.setText("Cal que introduïsques una data d'inici.");
        else if (dataF == null )error.setText("Cal que introduïsques una data de fi.");
        else if (dataF.isBefore(dataI)) error.setText("La data d'inici cal que siga prèvia a la final.");
        else {
            //carregar les dades de l'observable list
            error.setText("");
          //  dataVictories.clear();
           // dataDerrotes.clear();
            usuariDades = sistema.getPlayer(nomUsuari);
            if (usuariDades == null) error.setText("Jugador no registrat en el nostre sistema.");
            else {
                carregarDades();
            }
        }
    }
    public void inicialitzarDades() {
        dataI = Dades.getDades().getDataI();
        dataF = Dades.getDades().getDataF();
        nomUsuari = Dades.getDades().getNomUsuari();
    }
    private void carregarDades() {
        partidesPerDia = sistema.getDayRanksPlayer(usuariDades);
        Set<LocalDate> claus = partidesPerDia.keySet();
        /*seriesVictories.getData().clear();
        seriesDerrotes.getData().clear();
        seriesJugadorsDistints.getData().clear();*/
        chart1.getData().clear();
        chart2.getData().clear();
        seriesVictories = new XYChart.Series();
        seriesVictories.setName("Victòries");
        seriesDerrotes = new XYChart.Series();
        seriesDerrotes.setName("Derrotes");
        seriesJugadorsDistints = new XYChart.Series();
        int max = 0;
        for (LocalDate d : claus) {
            if (d.isAfter(dataI.minusDays(1)) && d.isBefore(dataF.plusDays(1))) {
                int guanyades = partidesPerDia.get(d).getWinnedGames();
                int perdudes = partidesPerDia.get(d).getLostGames();
                seriesVictories.getData().add(new XYChart.Data<String, Number>(d.format(formatter), guanyades));
                seriesDerrotes.getData().add(new XYChart.Data<String, Number>(d.format(formatter), perdudes));
                seriesJugadorsDistints.getData().add(new XYChart.Data<String, Number>(d.format(formatter), partidesPerDia.get(d).getOponents()));
                if (guanyades + perdudes > max) max = guanyades + perdudes;
            }
        }
        
        xAxis = new CategoryAxis();
        yAxis.setUpperBound(max);
        yAxis2.setUpperBound(max);
        chart1.getData().addAll(seriesVictories, seriesDerrotes);
        chart2.getData().add(seriesJugadorsDistints);
    }
    
}
