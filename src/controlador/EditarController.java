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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
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
    private GridPane miGrid;
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
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
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
    @FXML
    private void okCambios(ActionEvent event) throws Connect4DAOException, IOException {
        String contra = contrasenya.getText();
        String mail = correu.getText();
        LocalDate naixement = data.getValue();
        if (!Player.checkEmail(mail)) error.setText("El correu introduït no té el format vàlid");
        else if (!Player.checkPassword(contra)) error.setText("La contrasenya no té el format vàlid");
        else {
            if (imatgeAvatar == null) {
                mostrar.setBirthdate(naixement);
                mostrar.setEmail(mail);
                mostrar.setPassword(contra);
                cancelCambios(event);
            }
            else {
                mostrar.setAvatar((Image) img);
                mostrar.setBirthdate(naixement);
                mostrar.setEmail(mail);
                mostrar.setPassword(contra);
                cancelCambios(event);
            }
        }
    }

    @FXML
    private void cancelCambios(ActionEvent event) throws IOException {
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
    private void obrirArxiu(MouseEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Tria la imatge pel teu avatar");
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
        //Completar perquè aparega un llapis o algo, mirar com fer transicions
    }
    
}
