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
    @FXML
    private DatePicker dataInici;
    @FXML
    private DatePicker dataFi;
    @FXML
    private Label error;
    @FXML
    private TextField usuari;
    @FXML
    private StackedBarChart<String, Number> chart1;
    @FXML
    private BarChart<String, Number> chart2;
    @FXML
    private Button boto;
    private ObservableList<XYChart.Data<String,Number>> dataVictories = FXCollections.observableArrayList();
    private ObservableList<XYChart.Data<String,Number>> dataDerrotes = FXCollections.observableArrayList();    
    private ObservableList<XYChart.Data<String,Number>> dataJugadorsDistints = FXCollections.observableArrayList();

    private TreeMap <LocalDate,DayRank> partidesPerDia;
    private XYChart.Series seriesVictories;
    private XYChart.Series seriesDerrotes;    
    private XYChart.Series seriesJugadorsDistints;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dataInici.setEditable(false); //per evitar que es puga introduir la data "a mà"
        dataInici.setDayCellFactory(c -> new DateCell() {
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                setDisable(item.isAfter(LocalDate.now()));
            }
        });
        formatter = DateTimeFormatter.ofPattern("dd/MM/yy");
        dataInici.setConverter(new LocalDateStringConverter(formatter, null));
        dataInici.showWeekNumbersProperty().set(false);
        dataFi.setEditable(false); //per evitar que es puga introduir la data "a mà"
        dataFi.setDayCellFactory(c -> new DateCell() {
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                setDisable(item.isAfter(LocalDate.now()));
            }
        });
        dataFi.setConverter(new LocalDateStringConverter(formatter, null));
        dataFi.showWeekNumbersProperty().set(false);
        chart1.disableProperty().bind(Bindings.or(Bindings.isNull(dataInici.valueProperty()), Bindings.or(Bindings.isNull(dataFi.valueProperty()), Bindings.equal(usuari.textProperty(), ""))));
        chart2.disableProperty().bind(chart1.disableProperty());
        boto.disableProperty().bind(chart1.disableProperty());
        try {
            sistema = Connect4.getSingletonConnect4();
        } catch (Connect4DAOException ex) {
            Logger.getLogger(NombrePartidesTempsController.class.getName()).log(Level.SEVERE, null, ex);
        }
        dataInici.valueProperty().addListener((observable, valorAntic, valorNou) -> {
                dataI = valorNou;
        });
        dataFi.valueProperty().addListener((observable, valorAntic, valorNou) -> {
                dataF = valorNou;
        });
        chart1.setTitle("Partides perdudes/guanyades");
        chart2.setTitle("Nombre d'oponents distints");
        seriesVictories = new XYChart.Series(dataVictories);
        seriesVictories.setName("Victòries");
        seriesDerrotes = new XYChart.Series(dataDerrotes);
        seriesDerrotes.setName("Derrotes");
        seriesJugadorsDistints = new XYChart.Series(dataJugadorsDistints);
    }    
    
    public void inicialitzarJugador (Player j1) { jugador1 = j1; }
    public void inicialitzarJugadors (Player j1, Player j2) { 
        jugador1 = j1;
        jugador2 = j2;
    }
    
    @FXML
    private void carregarGrafiques() {
        if (usuari.getText() == "") error.setText("Cal que introduïsques un nom d'usuari.");
        else if (dataI == null )error.setText("Cal que introduïsques una data d'inici.");
        else if (dataF == null )error.setText("Cal que introduïsques una data de fi.");
        else if (dataF.isBefore(dataI)) error.setText("La data d'inici cal que siga prèvia a la final.");
        else {
            //carregar les dades de l'observable list
            error.setText("");
            dataVictories.clear();
            dataDerrotes.clear();
            usuariDades = sistema.getPlayer(usuari.getText());
            if (usuariDades == null) error.setText("Jugador no registrat en el nostre sistema.");
            else {
                carregarDades();
            }
        }

    }
    
    private void carregarDades() {
        partidesPerDia = sistema.getDayRanksPlayer(usuariDades);
        Set<LocalDate> claus = partidesPerDia.keySet();
        seriesVictories.getData().clear();
        seriesDerrotes.getData().clear();
        seriesJugadorsDistints.getData().clear();
        chart1.getData().clear();
        chart2.getData().clear();
        for (LocalDate d : claus) {
            if (d.isAfter(dataI.minusDays(1)) && d.isBefore(dataF.plusDays(1))) {
                System.out.println("Partides guanyades el dia " + d.format(formatter) + " : " + partidesPerDia.get(d).getWinnedGames());
                System.out.println("Partides perdudes el dia " + d.format(formatter) + " : " + partidesPerDia.get(d).getLostGames());
                //dataVictories.add(new XYChart.Data<String, Number>(d.format(formatter), partidesPerDia.get(d).getWinnedGames()));
                //dataDerrotes.add(new XYChart.Data<String, Number>(d.format(formatter), partidesPerDia.get(d).getLostGames()));
                //dataJugadorsDistints.add(new XYChart.Data<String, Number>(d.format(formatter), partidesPerDia.get(d).getOponents()));
                seriesVictories.getData().add(new XYChart.Data<String, Number>(d.format(formatter), partidesPerDia.get(d).getWinnedGames()));
                seriesDerrotes.getData().add(new XYChart.Data<String, Number>(d.format(formatter), partidesPerDia.get(d).getLostGames()));
                seriesJugadorsDistints.getData().add(new XYChart.Data<String, Number>(d.format(formatter), partidesPerDia.get(d).getOponents()));
            }
        }
        chart1.getData().addAll(seriesVictories, seriesDerrotes);
        chart2.getData().add(seriesJugadorsDistints);
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
