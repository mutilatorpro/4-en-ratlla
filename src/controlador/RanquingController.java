/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import DBAccess.Connect4DAOException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
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
    private TableColumn<Player, String> avatarColumn;
    @FXML
    private TableColumn<Player, String> userColumn;
    @FXML
    private TableColumn<Player, String> puntuacioColumn;
    @FXML
    private TableColumn<Player, String> posicioColumn;
    
    private Connect4 sistema;
    private List<Player> ranquing;
    private ObservableList<Player> dades;
    @FXML
    private TextField jugador;
    
    private Player jugador1 = null, jugador2 = null;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            sistema = Connect4.getSingletonConnect4();
            ranquing = sistema.getConnect4Ranking();
            dades = FXCollections.observableArrayList(ranquing);
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
            posicioColumn.setCellValueFactory(c -> {
                Player aux = c.getValue();  
                StringProperty posicioProperty = new SimpleStringProperty(String.valueOf(dades.indexOf(aux)));
                return posicioProperty;
            });
           /* avatarColumn.setCellValueFactory (c -> {
                Player aux = c.getValue();
                ImageView img = new ImageView();
                img.setImage(aux.getAvatar());
                return img.imageProperty();
            });*/
           avatarColumn.setCellValueFactory(new PropertyValueFactory<> ("avatar"));
           jugador.textProperty().addListener((observable, valorAntic, valorNou) -> {
                dades.clear();
                for (Player p : ranquing) {
                    String nom = p.getNickName().toLowerCase();
                    valorNou = valorNou.toLowerCase();
                    if (nom.startsWith(valorNou)) dades.add(p);
                }
            });
            
            
            
        } catch (Connect4DAOException ex) {
            Logger.getLogger(RanquingController.class.getName()).log(Level.SEVERE, null, ex);
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
    
}
class AvatarCelda extends TableCell<Player, String> {
    private ImageView vista = new ImageView();
    protected void updateItem (String item, boolean empty) {
        super.updateItem(item, empty);
        if (item == null || empty) setGraphic(null);
        else {
            Image img = new Image(getClass().getResourceAsStream(item),40,40,true,true);
            vista.setImage(img);
            setGraphic(vista);
        }
    }
}
