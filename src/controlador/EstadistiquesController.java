/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
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
import javafx.util.converter.LocalDateStringConverter;

/**
 * FXML Controller class
 *
 * @author inmad
 */
public class EstadistiquesController implements Initializable {

    @FXML
    private ToggleGroup vsQui;
    @FXML
    private DatePicker dataInici;
    @FXML
    private DatePicker dataFi;
    @FXML
    private TextField nomUsuari;
    @FXML
    private Button botoMostrar;
    @FXML
    private Label error;
    @FXML
    private ChoiceBox<String> PartUsuEleccio;

    
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
        //PartUsuEleccio = new ChoiceBox();
        //PartUsuEleccio.getItems().add("Jugades");
        //PartUsuEleccio.getItems().add("Guanyades");
        //PartUsuEleccio.getItems().add("Perdudes");
        
        //FXCollections.observableArrayList("Jugades", "Guanyades", "Perdudes");
        //final ChoiceBox<String> choiceBox = new ChoiceBox(options);
        //PartUsuEleccio = new ChoiceBox(FXCollections.observableArrayList("Jugades", "Guanyades", "Perdudes"));
        
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
                    //quan sapiga usar la ChoiceBox
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
        aux.carregarDades(this);
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

    @FXML
    private void mostraOpc(MouseEvent event) {
        PartUsuEleccio = new ChoiceBox(FXCollections.observableArrayList("Jugades", "Guanyades", "Perdudes"));
        
    }
}
