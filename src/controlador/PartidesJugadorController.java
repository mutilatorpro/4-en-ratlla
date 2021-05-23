
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
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
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
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.converter.LocalDateStringConverter;
import javax.swing.ImageIcon;
import model.Connect4;
import model.Player;
import model.Round;

/**
 * FXML Controller class
 *
 * @author inmad
 */
public abstract class PartidesJugadorController implements Initializable, EstadistiquesSelector {

    @FXML
    private TableView<Round> taula;
    
    private Connect4 sistema;
    private ObservableList<Round> dadesRondes;
    
    private Player jugador1 = null, jugador2 = null;
    @FXML
    private DatePicker dataInici;
    @FXML
    private DatePicker dataFi;
    @FXML
    private TableColumn<Round, String> diaColumn;
    @FXML
    private TableColumn<Round, String> horaColumn;
    @FXML
    private TableColumn<Round, String> guanyadorColumn;
    @FXML
    private TableColumn<Round, String> perdedorColumn;
    private DateTimeFormatter formatter;    
    private DateTimeFormatter formatter2;
    private LocalDate dataI = null, dataF = null;
    @FXML
    private Button botoMostrar;
    @FXML
    private Label error;
    @FXML
    private TextField nomUsuari;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            configurarDates();
            sistema = Connect4.getSingletonConnect4();
            dadesRondes = FXCollections.observableArrayList();
            taula.setItems(dadesRondes);
            diaColumn.setCellValueFactory(c -> {
                Round aux = c.getValue();
                StringProperty userProperty = new SimpleStringProperty(aux.getLocalDate().format(formatter));
                return userProperty;
            });
            
            horaColumn.setCellValueFactory (c -> {
                Round aux = c.getValue();
                StringProperty puntsProperty = new SimpleStringProperty(aux.getTimeStamp().format(formatter2));
                
                return puntsProperty;
            });
            guanyadorColumn.setCellValueFactory(c -> {
                Round aux = c.getValue();  
                StringProperty posicioProperty = new SimpleStringProperty(aux.getWinner().getNickName());
                return posicioProperty;
            });
            perdedorColumn.setCellValueFactory(c -> {
                Round aux = c.getValue();  
                StringProperty posicioProperty = new SimpleStringProperty(aux.getLoser().getNickName());
                return posicioProperty;
            });
            botoMostrar.disableProperty().bind(Bindings.or(Bindings.isNull(dataInici.valueProperty()),Bindings.or(Bindings.isNull(dataFi.valueProperty()),Bindings.equal(nomUsuari.textProperty(), ""))));
            dataInici.valueProperty().addListener((observable, valorAntic, valorNou) -> { dataI = valorNou; });
            dataFi.valueProperty().addListener((observable, valorAntic, valorNou) -> { dataF = valorNou; });
            taula.setItems(dadesRondes);
        } catch (Connect4DAOException ex) {
            Logger.getLogger(RanquingController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
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
    
    private void carregarDades() {
        dadesRondes.clear();
        Player auxiliar = sistema.getPlayer(nomUsuari.getText());
        if (auxiliar == null) error.setText("Aquest jugador no existeix en el nostre sistema.");
        else {
            List<Round> aux = sistema.getRoundsPlayer(auxiliar);
            for (Round r: aux) {
                if (r.getLocalDate().isAfter(dataI.minusDays(1)) && r.getLocalDate().isBefore(dataF.plusDays(1))) {
                    dadesRondes.add(r);
                }
            }
        }
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
    private void mostrarRondes(ActionEvent event) {
        if (dataI != null && dataF != null && !nomUsuari.getText().equals("")) {
            if (dataI.isAfter(dataF)) error.setText("La data d'inici ha de ser prèvia a la de fi.");
            else carregarDades();
        }
    }
    
}