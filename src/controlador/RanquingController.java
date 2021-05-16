/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import DBAccess.Connect4DAOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.Connect4;
import model.Player;

/**
 * FXML Controller class
 *
 * @author inmad
 */
public class RanquingController implements Initializable {

    @FXML
    private TableView<Player> taula;
    @FXML
    private TableColumn<Player, Image> avatarColumn;
    @FXML
    private TableColumn<Player, String> userColumn;
    @FXML
    private TableColumn<Player, String> puntuacioColumn;
    
    private Connect4 sistema;
    private List<Player> ranquing, ranquingAux;
    @FXML
    private TextField jugador;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            sistema = Connect4.getSingletonConnect4();
            ranquing = sistema.getConnect4Ranking();
            ranquingAux = sistema.getConnect4Ranking();
            ObservableList<Player> dades = FXCollections.observableArrayList(ranquingAux);
            taula.setItems(dades);
            userColumn.setCellValueFactory(c -> {
                Player aux = c.getValue();
                StringProperty userProperty = new SimpleStringProperty(aux.getNickName());
                return userProperty;
            });
            
            puntuacioColumn.setCellValueFactory (c -> {
                Player aux = c.getValue();
                StringProperty puntsProperty = new SimpleStringProperty(String.valueOf(aux.getPoints()));
                return puntsProperty;
            });
            //avatarColumn.setCellValueFactory(c -> new PropertyValueFactory<Player, Image>("avatar"));
            //avatarColumn.setCellFactory (c -> new AvatarCelda());
            jugador.textProperty().addListener((observable, oldVal, nouVal) -> {
               for (Player p: ranquing) {
                   ranquingAux = new ArrayList();
                   String userNormalitzat = p.getNickName().toLowerCase();
                   nouVal = nouVal.toLowerCase();
                   if (userNormalitzat.startsWith(nouVal)) {
                       ranquingAux.add(p);
                       System.out.println(p.getNickName());
                   }
               }
            });
            
        } catch (Connect4DAOException ex) {
            Logger.getLogger(RanquingController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }    
    
}
class AvatarCelda extends TableCell<Player, String> {
    private ImageView vista = new ImageView();
    protected void updateItem (String item, boolean empty) {
        super.updateItem(item, empty);
        if (item == null || empty) setGraphic(null);
        else {
            Image img = new Image(getClass().getResourceAsStream(item), 40, 40, true, true);
            vista.setImage(img);
            setGraphic(vista);
        }
    }
    
}
