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
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.converter.LocalDateStringConverter;
import model.Connect4;
import model.Player;

/**
 * FXML Controller class
 *
 * @author inmad
 */
public class EstadistiquesController implements Initializable {

    @FXML
    private ToggleGroup vsQui;
    @FXML
    private  DatePicker dataInici;
    @FXML
    private  DatePicker dataFi;
    @FXML
    private TextField nomUsuari;
    @FXML
    private Button botoMostrar;
    @FXML
    private Label error;
    @FXML
    private ChoiceBox<String> PartUsuEleccio;
    private Connect4 sistema;
    private Player jugador1 = null, jugador2 = null;

    
    private DateTimeFormatter formatter;
    private DateTimeFormatter formatter2;
    private LocalDate dataI = null, dataF = null;
    
  
    
    @FXML
    private RadioButton vsSist;
    @FXML
    private RadioButton vsUsu;
    @FXML
    private VBox centreBox;
    @FXML
    private BorderPane borderPane;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarDates();
        try {
            sistema = Connect4.getSingletonConnect4();
            
            botoMostrar.disableProperty().bind(Bindings.or(
                    Bindings.or(Bindings.isNull(dataInici.valueProperty()),Bindings.isNull(dataFi.valueProperty())),
                    Bindings.and(vsUsu.pressedProperty(),Bindings.equal(nomUsuari.textProperty(), "")))); 
            //ObservableBoolanValue cond1 = Bindings.or(Bindings.isNull(dataInici.valueProperty()),Bindings.isNull(dataFi.valueProperty()));
            //BooleanBinding cond2 = Bindings.and(vsUsu.pressedProperty(),Bindings.equal(nomUsuari.textProperty(), ""));
            if (vsUsu.isPressed() && nomUsuari.equals("")) { botoMostrar.disableProperty(); }
            else {
                botoMostrar.disableProperty().bind(Bindings.equal(nomUsuari.textProperty(), ""));
            }
            dataInici.valueProperty().addListener((observable, valorAntic, valorNou) -> { dataI = valorNou; });
            dataFi.valueProperty().addListener((observable, valorAntic, valorNou) -> { dataF = valorNou; });
        } catch (Connect4DAOException ex) {
            Logger.getLogger(RanquingController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        PartUsuEleccio.getItems().add("Partides jugades");
        PartUsuEleccio.getItems().add("Partides guanyades");
        PartUsuEleccio.getItems().add("Partides perdudes");
    }    

    @FXML
    private void mostrarRondes(ActionEvent event) {
        if (dataI != null && dataF != null) {
            if (dataI.isAfter(dataF)) error.setText("La data d'inici ha de ser prèvia a la de fi.");
            else if (vsSist.isPressed() || vsUsu.isPressed()){ //carregarDades();
                if (vsSist.isPressed()) { 
                    setCenterScene("PartidesSistema.fxml");
                }
                else {
                    if (PartUsuEleccio.getValue().equals("Partides jugades")) { setCenterScene("PartidesJugador.fxml"); }
                    else if (PartUsuEleccio.getValue().equals("Partides guanyades")) { setCenterScene("PartidesJugadorGuanyades.fxml");}
                    else if (PartUsuEleccio.getValue().equals("Partides perdudes")) { setCenterScene("PartidesJugadorPerdudes.fxml");}
                }
                
            }
        }
    }
    
    private FXMLLoader setCenterScene(String FXML_relative_path){
        
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource(FXML_relative_path));
        
        try{
            Parent root = loader.load();
            borderPane.setCenter(root);
        } catch(IOException e){System.out.println(e.toString());}
    
        EstadistiquesSelector aux = (EstadistiquesSelector)loader.getController();
        //centreBox = aux;
        actualitzDades();
        aux.carregarDades();
        return loader;

    }
    
    private void configurarDates() {
        dataInici.setEditable(false); //per evitar que es puga introduir la data "a mà"
        dataInici.setDayCellFactory(c -> new DateCell() {
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                setDisable(item.isAfter(LocalDate.now()));
            }
        });
        formatter = DateTimeFormatter.ofPattern("dd/MM/yy");
        formatter2 = DateTimeFormatter.ofPattern("HH:mm:ss");
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
    }

    private void actualitzDades() {
        Dades.getDades().setDataI(dataI);
        Dades.getDades().setDataF(dataF);
        Dades.getDades().setNomUsuari(nomUsuari.getText()); 
    }

    @FXML
    private void enrere(ActionEvent event) throws IOException, IOException {
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
    public void inicialitzarJugador (Player j1) { jugador1 = j1; }
    public void inicialitzarJugadors (Player j1, Player j2) {
        jugador1 = j1;
        jugador2 = j2;
    }
}
