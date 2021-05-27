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
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.converter.LocalDateStringConverter;
import model.Connect4;
import model.Player;
/**
 * FXML Controller class
 *
 * @author inmad
 */
public class EditarController implements Initializable {

    @FXML
    private TextField nom;
    @FXML
    private PasswordField contrasenya;
    @FXML
    private TextField correu;
    @FXML
    private DatePicker data;
    @FXML
    private Label error;
    @FXML
    private ImageView imatge;
    private Player jugador1 = null, jugador2 = null, mostrar = null;
    @FXML
    private Button modificar;
    private File imatgeAvatar = null;
    private Image img;
    private Connect4 sistema;
    private HBox contenidorImatge;
    @FXML
    private Label nomArxiu;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Parent root = modificar.getParent();
        if (Dades.getDades().isModeObs())  root.getStylesheets().addAll("resources/obscFulla.css");
        else root.getStylesheets().addAll("resources/blancFulla.css");
        
        modificar.disableProperty().bind(Bindings.or(Bindings.equal(nom.textProperty(), ""), Bindings.or(Bindings.equal(contrasenya.textProperty(),""), Bindings.or(Bindings.equal(correu.textProperty(),""), Bindings.isNull(data.valueProperty())))));
        data.setEditable(false); //per evitar que es puga introduir la data "a mà"
        data.setDayCellFactory(c -> new DateCell() {
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                setDisable(item.isAfter(LocalDate.now()));
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

    public void inicialitzarJugador(Player j) {
        jugador1 = j;
        mostrar = j;
        imatge.setImage(j.getAvatar());
        mostrarInformacio();
    }
    
    public void inicialitzarJugadors(Player j1, Player j2, Player aMostrar) {
        jugador1 = j1;
        jugador2 = j2;
        mostrar = aMostrar;
        mostrarInformacio();
    }
    private void mostrarInformacio() {
        imatge.setImage(mostrar.getAvatar());
        nom.setText(mostrar.getNickName());
        contrasenya.setText(mostrar.getPassword());
        correu.setText(mostrar.getEmail());
        data.setValue(mostrar.getBirthdate());
    }
    private void tancarFinestra(ActionEvent event) throws IOException {
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("/vista/PrimerJugador.fxml"));
        Parent root = cargador.load();
        PrimerJugadorController controlador = cargador.getController();
        if (jugador2 != null) controlador.inicialitzarJugadors(jugador1,jugador2);
        else controlador.inicialitzarJugador(jugador1);
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.toFront();
        stage.show();
    }
    @FXML
    private void okCambios(ActionEvent event) throws Connect4DAOException, IOException {
        String contra = contrasenya.getText();
        String mail = correu.getText();
        LocalDate naixement = data.getValue();
        if (!Player.checkEmail(mail)) {
            error.setText("El correu introduït no té el format vàlid");
            correu.getStyleClass().add("error");
        }
        else if (!Player.checkPassword(contra)) {
            error.setText("La contrasenya no té el format vàlid");
            correu.getStyleClass().clear();
            contrasenya.getStyleClass().add("error");
        }
        else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText(null);
            alert.setTitle("Confirmació de modificació");
            alert.setContentText("Estàs segur que vols modificar les dades?");
            Optional<ButtonType> action = alert.showAndWait();
            if (action.get() == ButtonType.OK) {
                correu.getStyleClass().clear();
                contrasenya.getStyleClass().clear();
                if (imatgeAvatar == null) {
                mostrar.setBirthdate(naixement);
                mostrar.setEmail(mail);
                mostrar.setPassword(contra);
                tancarFinestra(event);
                }
                else {
                    mostrar.setAvatar((Image) img);
                    mostrar.setBirthdate(naixement);
                    mostrar.setEmail(mail);
                    mostrar.setPassword(contra);
                    tancarFinestra(event);
                }
            }
        }
    }

    @FXML
    private void cancelCambios(ActionEvent event) throws IOException {
        String contra = contrasenya.getText();
        String mail = correu.getText();
        LocalDate naixement = data.getValue();
        if (!contra.equals(mostrar.getPassword()) || !mail.equals(mostrar.getEmail()) || !naixement.equals(mostrar.getBirthdate()) || img == null || !img.equals(mostrar.getAvatar())) {
            //revisar la condició
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText(null);
            alert.setTitle("Confirmació eixida");
            alert.setContentText("Estàs segur que vols eixir d'esta finestra? Es perdran tots el canvis ");
            Optional<ButtonType> action = alert.showAndWait();
            if (action.get() == ButtonType.OK) {
                tancarFinestra(event);
            }
        } //així si no ha fet cap canvi no es demana confirmació 
        else tancarFinestra(event);
    }

    @FXML
    private void obrirArxiu(MouseEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Tria una imatge per al teu avatar");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        File aux = fileChooser.showOpenDialog(stage);
        if (aux != null) {
            imatgeAvatar = aux;
            img = new Image(imatgeAvatar.toURI().toString());
            imatge.setImage(img);
        }
    }

    @FXML
    private void ressaltarImatge(MouseEvent event) {
        imatge.setCursor(Cursor.HAND);
        imatge.getStyleClass().add("imatge");
    }
    
    @FXML
    private void iniciImatge(MouseEvent event) {
        imatge.setCursor(Cursor.DEFAULT);
        imatge.getStyleClass().clear();
        
    }
    
    private void tancarFinestraKey(KeyEvent event) throws IOException {
       FXMLLoader cargador = new FXMLLoader(getClass().getResource("/vista/PrimerJugador.fxml"));
       Parent root = cargador.load();
       PrimerJugadorController controlador = cargador.getController();
       if (jugador2 != null) controlador.inicialitzarJugadors(jugador1,jugador2);
       else controlador.inicialitzarJugador(jugador1);
       Scene scene = new Scene(root);
       Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
       stage.setScene(scene);
       stage.toFront();
       stage.show();
    }
    
    private void okCambios(KeyEvent event) throws Connect4DAOException, IOException {
        String contra = contrasenya.getText();
        String mail = correu.getText();
        LocalDate naixement = data.getValue();
        if (!Player.checkEmail(mail)) error.setText("El correu introduït no té el format vàlid");
        else if (!Player.checkPassword(contra)) error.setText("La contrasenya no té el format vàlid");
        else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText(null);
            alert.setTitle("Confirmació de modificació");
            alert.setContentText("Estàs segur que vols modificar les dades?");
            Optional<ButtonType> action = alert.showAndWait();
            if (action.get() == ButtonType.OK) {
                if (imatgeAvatar == null) {
                mostrar.setBirthdate(naixement);
                mostrar.setEmail(mail);
                mostrar.setPassword(contra);
                tancarFinestraKey(event);
                }
                else {
                    mostrar.setAvatar((Image) img);
                    mostrar.setBirthdate(naixement);
                    mostrar.setEmail(mail);
                    mostrar.setPassword(contra);
                    tancarFinestraKey(event);
                }
            }
        }
    }

    
    @FXML
    private void enter(KeyEvent event) throws Connect4DAOException, IOException {
        KeyCode tecla = event.getCode();
        if (tecla == KeyCode.ENTER) {
            okCambios(event);
        }
    }    
}
