
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
public class PartidesJugadorController implements Initializable {

    private Connect4 sistema;
    private ObservableList<Round> dadesRondes;
    private Player jugador1 = null, jugador2 = null;
    private DateTimeFormatter formatter, formatter2;   
    private LocalDate dataI = null, dataF = null;
    private String nomUsuari = "";
    
    @FXML
    private TableView<Round> taula;
    @FXML
    private TableColumn<Round, String> diaColumn;
    @FXML
    private TableColumn<Round, String> horaColumn;
    @FXML
    private TableColumn<Round, String> guanyadorColumn;
    @FXML
    private TableColumn<Round, String> perdedorColumn;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Parent root = taula.getParent();
        while (root.getParent() != null) root = root.getParent();
        if (Dades.getDades().isModeObs())  { 
            root.getStylesheets().remove("resources/blancFulla.css");
            root.getStylesheets().add("resources/obscFulla.css");
        }
        else {
            root.getStylesheets().remove("resources/obscFulla.css"); 
            root.getStylesheets().add("resources/blancFulla.css");
        } 
        formatter = DateTimeFormatter.ofPattern("dd/MM/yy");
        formatter2 = DateTimeFormatter.ofPattern("HH:mm:ss");
        try {
            inicialitzarDades();
            sistema = Connect4.getSingletonConnect4();
            dadesRondes = FXCollections.observableArrayList();
            taula.setItems(dadesRondes);            
            carregarDades();
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
            
            taula.setItems(dadesRondes);
        } catch (Connect4DAOException ex) {
            Logger.getLogger(RanquingController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }    

    
    private void carregarDades() {
        dadesRondes.clear();
        Player auxiliar = sistema.getPlayer(nomUsuari);
        if (auxiliar != null) {
            List<Round> aux = sistema.getRoundsPlayer(auxiliar);
            for (Round r: aux) {
                if (r.getLocalDate().isAfter(dataI.minusDays(1)) && r.getLocalDate().isBefore(dataF.plusDays(1))) {
                    dadesRondes.add(r);
                }
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

//    private void mostrarRondes(ActionEvent event) {
//        if (dataI != null && dataF != null && !nomUsuari.equals("")) {
//            if (dataI.isAfter(dataF)) error.setText("La data d'inici ha de ser pr??via a la de fi.");
//            else carregarDades();
//        }
//    }
    
}