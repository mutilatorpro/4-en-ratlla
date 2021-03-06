/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import DBAccess.Connect4DAOException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
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
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.util.converter.LocalDateStringConverter;
import model.Connect4;
import model.Player;

/**
 * FXML Controller class
 *
 * @author inmad
 */
public class RegistrarController implements Initializable {

    @FXML
    private Label error;
    @FXML
    private TextField nom;
    @FXML
    private TextField contrasenya;
    @FXML
    private TextField correu;
    @FXML
    private DatePicker data;
    @FXML
    private Button registrar;
    @FXML
    private GridPane miGrid;
    private File imatgeAvatar = null;
    private Image img;
    private Connect4 sistema;
    @FXML
    private Label nomArxiu;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Parent root = registrar.getParent();
        while (root.getParent() != null) root = root.getParent();
        if (Dades.getDades().isModeObs())  { 
            root.getStylesheets().remove("resources/blancFulla.css");
            root.getStylesheets().add("resources/obscFulla.css");
        }
        else {
            root.getStylesheets().remove("resources/obscFulla.css"); 
            root.getStylesheets().add("resources/blancFulla.css");
        } 
        registrar.disableProperty().bind(Bindings.or(Bindings.equal(nom.textProperty(), ""), Bindings.or(Bindings.equal(contrasenya.textProperty(),""), Bindings.or(Bindings.equal(correu.textProperty(),""), Bindings.isNull(data.valueProperty())))));
        data.setEditable(false); //per evitar que es puga introduir la data "a m??"
        data.setDayCellFactory(c -> new DateCell() {
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                setDisable(item.isAfter(LocalDate.now().minusYears(12))); //almenys tinga 18 anys
            }
        });
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy");
        data.setConverter(new LocalDateStringConverter(formatter, null));
        data.showWeekNumbersProperty().set(false);
        try {
            sistema = Connect4.getSingletonConnect4();
            sistema.getConnect4DAO().toTextFile("base_de_dades.txt");
        } catch (Connect4DAOException ex) {
            Logger.getLogger(RegistrarController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(RegistrarController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    

    @FXML
    private void okCambios(ActionEvent event) throws Connect4DAOException, IOException {
        String usuari = nom.getText();
        String contra = contrasenya.getText();
        String mail = correu.getText();
        LocalDate naixement = data.getValue();
        if (sistema.exitsNickName(usuari)) error.setText("El nom d'usuari introdu??t ja existeix.");
        else if (!Player.checkNickName(usuari)) error.setText("El nom d'usuari introdu??t no t?? el format v??lid");
        else if (!Player.checkEmail(mail)) error.setText("El correu introdu??t no t?? el format v??lid");
        else if (!Player.checkPassword(contra)) error.setText("La contrasenya no t?? el format v??lid");
        else {
            if (imatgeAvatar == null) {
                sistema.registerPlayer(usuari, mail, contra, naixement, 0);
                cancelCambios(event);
            }
            else {
                img = new Image(imatgeAvatar.toURI().toString());
                sistema.registerPlayer(usuari, mail, contra, (Image) img, naixement, 0);
                cancelCambios(event);
            }
        }
    }

    @FXML
    private void cancelCambios(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/vista/Principal.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.toFront();
        stage.show();
    }

    @FXML
    private void obrirArxiu(ActionEvent event) {
        imatgeAvatar = null;
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Tria una imatge per al teu avatar");
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        imatgeAvatar = fileChooser.showOpenDialog(stage);
        if (imatgeAvatar != null) nomArxiu.setText(imatgeAvatar.getName());
    }
    
}
