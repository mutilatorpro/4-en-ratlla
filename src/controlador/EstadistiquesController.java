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
import javafx.scene.control.Alert;
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

    private Connect4 sistema;
    private Player jugador1 = null, jugador2 = null;
    private DateTimeFormatter formatter, formatter2;   
    private LocalDate dataI = null, dataF = null;
    
    private final String p1 = "Partides jugades";
    private final String p2 = "Partides guanyades en forma de llista"; // o ranking
    private final String p3 = "Partides perdudes en forma de llista";
    private final String p4 = "Partides guanyades y perdudes en forma de gràfica";
//    private final String p5 = "Partides perdudes en forma de gràfica";
    
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
            
//            botoMostrar.disableProperty().bind(Bindings.or(
//                    Bindings.or(Bindings.isNull(dataInici.valueProperty()),Bindings.isNull(dataFi.valueProperty())),
//                    Bindings.and(vsUsu.selectedProperty(),Bindings.equal(nomUsuari.textProperty(), ""))));  // REVISARRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR
            botoMostrar.disableProperty().bind(Bindings.or(
                                                Bindings.and(
                                                vsUsu.selectedProperty(), 
                                                Bindings.or(Bindings.equal(nomUsuari.textProperty(), ""), 
                                                        Bindings.or(Bindings.isNull(PartUsuEleccio.valueProperty()),
                                                                Bindings.or(Bindings.isNull(dataInici.valueProperty()),
                                                                            Bindings.isNull(dataFi.valueProperty()))))),
                                                Bindings.and(
                                                        vsSist.selectedProperty(),
                                                        Bindings.or(Bindings.isNull(dataInici.valueProperty()),
                                                                            Bindings.isNull(dataFi.valueProperty())))));
                                            
            
            
            dataInici.valueProperty().addListener((observable, valorAntic, valorNou) -> { dataI = valorNou; });
            dataFi.valueProperty().addListener((observable, valorAntic, valorNou) -> { dataF = valorNou; });
        } catch (Connect4DAOException ex) {
            Logger.getLogger(RanquingController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        PartUsuEleccio.getItems().add(p1);
        PartUsuEleccio.getItems().add(p2);
        PartUsuEleccio.getItems().add(p3);
        PartUsuEleccio.getItems().add(p4);
    }    

    @FXML
    private void mostrarRondes(ActionEvent event) {
        actualitzDades();
        if (dataI != null && dataF != null) {
            if (dataI.isAfter(dataF)) error.setText("La data d'inici ha de ser prèvia a la de fi.");
            else if (vsUsu.isSelected()){ 
                String eleccio = PartUsuEleccio.getValue();
                switch (eleccio) {
                    case p1: 
                        setCenterScene("/vista/PartidesJugador.fxml");
                        break;
                    case p2: 
                        setCenterScene("/vista/PartidesJugadorGuanyades.fxml");
                        break;
                    case p3: 
                        setCenterScene("/vista/PartidesJugadorPerdudes.fxml");
                        break;
                    case p4: 
                        setCenterScene("/vista/NombrePartidesGuanyadesPerdudes.fxml");
                        break;
                    
                    default: 
                        setCenterScene("/vista/PartidesSistema.fxml");
                }
            } else setCenterScene("/vista/PartidesSistema.fxml");
        }
    }
    
    private FXMLLoader setCenterScene(String FXML_relative_path){
        
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource(FXML_relative_path));
        
        try{
            Parent root = loader.load();
            borderPane.setCenter(root);
        } catch(IOException e){System.out.println(e.toString());}
    
        //EstadistiquesSelector aux = (EstadistiquesSelector)loader.getController();
        //centreBox = aux;
        //actualitzDades();
        //aux.inicialitzarDades();
        return loader;

    }
//    
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

    

    @FXML
    private void ajuda(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(null);
        alert.setTitle("Ajuda a la navegació");
        alert.setHeight(600);
        alert.setWidth(900);
        alert.setContentText("Aquesta finestra et permet cercar informació sobre estadístiques, ja siga sobre partides realitzades en el sistema o per a buscar informació sobre un usuari en concret.\n" 
                + "Per a cercar informació concreta sobre un jugador has d'escriure el seu nom d'usuari i elegir un dels tipus d'estadístiques.\n" );
        alert.showAndWait();
    }
}
