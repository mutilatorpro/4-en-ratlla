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
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.TranslateTransition;
import javafx.beans.binding.Bindings;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import static javafx.scene.transform.Transform.translate;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Connect4;
import model.Player;

/**
 * FXML Controller class
 *
 * @author inmad
 */
public class Joc4Controller implements Initializable {
    private Connect4 sistema;
    private boolean maquina;
    private int numJugades = 0;
    private boolean esJugador1 = true;
    private String missatgeError = "Tria una altra columna, esta ja està plena";
    private int[][] matriu = new int[7][8];
    private Player jugador1 = null;
    private Player jugador2 = null;
    private int punts = 0;
    private LocalDateTime data;
    private Retraso retraso;
    private ActionEvent eventMaquina;
    @FXML
    private Text text_jugador;
    @FXML
    private Text error;
    @FXML
    private GridPane miGrid;
    @FXML
    private Button casella;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            sistema = Connect4.getSingletonConnect4();
            LocalDate hui = LocalDate.now();
            LocalTime ara = LocalTime.now();
            data = LocalDateTime.of(hui, ara);
            retraso = new Retraso();
            retraso.setOnSucceeded((a) -> {
                try {
                    jugarMaquina(eventMaquina);
                } catch (IOException ex) {
                    Logger.getLogger(Joc4Controller.class.getName()).log(Level.SEVERE, null, ex);
                } catch (Connect4DAOException ex) {
                    Logger.getLogger(Joc4Controller.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            } catch (Connect4DAOException ex) {
                Logger.getLogger(Joc4Controller.class.getName()).log(Level.SEVERE, null, ex);
            }
    }    
    
    @FXML
    private void moure(ActionEvent event) throws InterruptedException, IOException, Connect4DAOException {
        if (numJugades < 56) { //només hi ha 56 caselles, s'ha de comprovar que encara quede alguna lliure
            if (maquina && esJugador1) {
                Button triat = (Button) event.getSource();
                Integer columna = miGrid.getColumnIndex(triat);
                if (columna == null) columna = 0;
                if (matriu[0][columna] != 0) { error.setText(missatgeError); }
                else {
                    int fila = matriu.length - 1;
                    while (fila >= 0 && matriu[fila][columna] != 0) { fila--; }
                    matriu[fila][columna] = 1;
                    Circle cercle = new Circle(0,0,0);
                    cercle.radiusProperty().bind(Bindings.divide(casella.widthProperty(),5));
                    cercle.centerXProperty().bind(Bindings.add(casella.widthProperty(),20));
                    cercle.centerYProperty().bind(Bindings.divide(casella.heightProperty(),2));
                    cercle.setFill(Color.RED);
                    miGrid.add(cercle,columna,0);
                    double currentYPostion = cercle.translateYProperty().getValue();
                    TranslateTransition translate = new TranslateTransition(Duration.millis(300), cercle);
                    translate.setToY(currentYPostion + fila * casella.getHeight());
                    translate.play();
                    numJugades++;
                    boolean guanyat = false;
                    esJugador1 = !esJugador1;
                    if (numJugades > 6)  guanyat = comprovarVictoria(event);
                    if (numJugades < 56) {
                        if (!guanyat) {
                            eventMaquina = event;
                            retraso.restart();
                        }
                    }
                    else taulerPle(event);
                }
            } else if (!maquina){
                if (esJugador1) { //Torn Jugador 1 contra Jugador 2
                    Button triat = (Button) event.getSource();
                    Integer columna = miGrid.getColumnIndex(triat);
                    if (columna == null) columna = 0;
                    if (matriu[0][columna] != 0) error.setText(missatgeError);
                    else {
                        int fila = matriu.length - 1;
                        while (fila >= 0 && matriu[fila][columna] != 0) {
                            fila--;
                        }
                        matriu[fila][columna] = 1;
                        Circle cercle = new Circle(0,0,0);
                        cercle.radiusProperty().bind(Bindings.divide(casella.widthProperty(),5));
                        cercle.centerXProperty().bind(Bindings.divide(casella.widthProperty(),2));
                        cercle.centerYProperty().bind(Bindings.divide(casella.heightProperty(),2));
                        cercle.setFill(Color.RED);
                        miGrid.add(cercle,columna,0);
                        double currentYPostion2 = cercle.translateYProperty().getValue();
                        TranslateTransition translate = new TranslateTransition(Duration.millis(300), cercle);
                        translate.setToY(currentYPostion2 + fila * casella.getHeight());
                        translate.play();
                        //cercle.translateYProperty().setValue(currentYPostion2+fila * casella.getHeight());
                        if (jugador2 != null) {
                            text_jugador.setText("Torn de " + jugador2.getNickName());
                            text_jugador.setFill(Color.YELLOW);
                        }
                    }
                } else { //Torn Jugador 2 contra Jugador 1
                    Button triat = (Button) event.getSource();
                    Integer columna = miGrid.getColumnIndex(triat);
                    if (columna == null) columna = 0;
                    if (matriu[0][columna] != 0) { error.setText(missatgeError); }
                    else {
                        int fila = matriu.length - 1;
                        while (fila >= 0 && matriu[fila][columna] != 0) { fila--; }
                        matriu[fila][columna] = 2;
                        Circle cercle2 = new Circle(0,0,0);
                        cercle2.radiusProperty().bind(Bindings.divide(casella.widthProperty(),5));
                        cercle2.centerXProperty().bind(Bindings.divide(casella.widthProperty(),2));
                        cercle2.centerYProperty().bind(Bindings.divide(casella.heightProperty(),2));
                        cercle2.setFill(Color.YELLOW);
                        miGrid.add(cercle2,columna,0);
                        double currentYPostion2 = cercle2.translateYProperty().getValue();
                        TranslateTransition translate = new TranslateTransition(Duration.millis(300), cercle2);
                        translate.setToY(currentYPostion2 + fila * casella.getHeight());
                        translate.play();
                        if (jugador1 != null) {
                            text_jugador.setText("Torn de " + jugador1.getNickName());
                            text_jugador.setFill(Color.RED);
                        }
                    }
                }
                esJugador1 = !esJugador1;
                numJugades++;
                if (numJugades > 6) {
                    comprovarVictoria(event);
                    if (numJugades == 56) taulerPle(event); //ho posem ací perquè així si he fet l'últim moviment ja està, no he d'esperar a posar una fitxa perquè em diga que hem empatat
                }
            } else { 
                error.setText("No és el teu torn, espera't!");
            }
        } else { taulerPle(event); } 
    }
    private void jugarMaquina(ActionEvent event) throws IOException, Connect4DAOException {
        Random aleatori = new Random();
        int cMaquina = aleatori.nextInt(8);
        while (matriu[0][cMaquina] != 0) {
            cMaquina = aleatori.nextInt(8);
        }
        int fMaquina = matriu.length - 1;
        while (fMaquina >= 0 && matriu[fMaquina][cMaquina] != 0) {
            fMaquina--;
        }
        matriu[fMaquina][cMaquina] = 2;
        /*Button auxMaquina = (Button) getNode(fMaquina, cMaquina);
                        auxMaquina.setText("O");
                        auxMaquina.setStyle("-fx-color: Blue");*/
        Circle cercle2 = new Circle(0, 0, 0);
        cercle2.radiusProperty().bind(Bindings.divide(casella.widthProperty(), 5));
        cercle2.centerXProperty().bind(Bindings.divide(casella.widthProperty(), 2));
        cercle2.centerYProperty().bind(Bindings.divide(casella.heightProperty(), 2));
        cercle2.setFill(Color.YELLOW); //#f3da3c
        miGrid.add(cercle2, cMaquina, 0);
        double currentYPostion2 = cercle2.translateYProperty().getValue();
        TranslateTransition translate2 = new TranslateTransition(Duration.millis(300), cercle2);
        translate2.setToY(currentYPostion2 + fMaquina * casella.getHeight());
        translate2.play();
        //cercle2.translateYProperty().setValue(currentYPostion2+fMaquina * casella.getHeight());
        numJugades++;
        if (numJugades > 6)
            comprovarVictoria(event);
    }
    private Node getNode(int fila, int col) {
        for (Node node : miGrid.getChildren()) {
            Integer f = GridPane.getRowIndex(node);
            Integer c = GridPane.getColumnIndex(node);
            if (f == null) f = 0;
            if (c == null) c = 0;
            if (c == col && f == fila) {
                return node;
            }
        }
        return null;
    }
    private void taulerPle(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setTitle("Empat!!");
        alert.setContentText("Ja no hi queden més caselles lliures.\nHeu arribat a un empat!\nTornant a la pàgina principal...");
        alert.showAndWait();
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
    }
    private boolean comprovarVictoria(ActionEvent event) throws IOException, Connect4DAOException {
        int guanyador = 0; //se quedarà a 0 mentre no s'hagen connectat 4
        for (int i = 0; i < 7 && guanyador == 0; i++) { //recorrem les files
            for (int j = 0; j < 5 && guanyador == 0; j++) { //recorrem UNA fila concreta
                if (matriu[i][j] == matriu[i][j + 1] && matriu[i][j + 1] == matriu[i][j + 2] && matriu[i][j + 2] == matriu[i][j + 3]) {
                    guanyador = matriu[i][j]; //se posarà a 1 si són els de Jugador1 qui ha connectat 4 o 2 si ha sigut el 2
                }
            }
        }
        for (int i = 0; i < 8 && guanyador == 0; i++) {
            for (int j = 0; j < 4 && guanyador == 0; j++) {
                if (matriu[j][i] == matriu[j + 1][i] && matriu[j + 1][i] == matriu[j + 2][i] && matriu[j + 2][i] == matriu[j + 3][i]) {
                    guanyador = matriu[j][i];
                }
            }
        }
        for (int i = 0; i < 5 && guanyador == 0; i++) {
            for (int j = 0; j < 4 && guanyador == 0; j++) {
                if (matriu[j][i] == matriu[j + 1][i + 1] && matriu[j + 1][i + 1] == matriu[j + 2][i + 2] && matriu[j + 2][i + 2] == matriu[j + 3][i + 3]) {
                    guanyador = matriu[j][i];
                }
            }
        }
        for (int i = 3; i < 8 && guanyador == 0; i++) {
            for (int j = 0; j < 4 && guanyador == 0; j++) {
                if (matriu[j][i] == matriu[j + 1][i - 1] && matriu[j + 1][i - 1] == matriu[j + 2][i - 2] && matriu[j + 2][i - 2] == matriu[j + 3][i - 3]) {
                    guanyador = matriu[j][i];
                }
            }
        }
        if (guanyador != 0) { //s'ha canviat guanyador perquè s'han trobat 4 connectades
            if (guanyador == 1 && jugador1 != null) { //Alert de victòria al 1r jugador
               jugador1.plusPoints(punts);
               if (!maquina) sistema.regiterRound(data, jugador1, jugador2);
            }
            if (guanyador == 2 && jugador2 != null) { //si el jugador és un altre que no el principal i el jugador2 existeix(no és la màquina)
                jugador2.plusPoints(punts);
                if (!maquina) sistema.regiterRound(data, jugador2, jugador1);
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            if (guanyador != 0) alert.setTitle("Guanyador!!"); //ha guanyat un dels jugadors
            else alert.setTitle("Derrota"); //ha guanyat la màquina
            if (guanyador == 1) alert.setContentText("Enhorabona!\nHa guanyat el jugador " + jugador1.getNickName() + "!!\nS'han sumat a la teua puntuació: " + punts + " punts");
            else {
                if (!maquina) alert.setContentText("Enhorabona!\nHa guanyat el jugador " + jugador2.getNickName() + "!!\nS'han sumat a la teua puntuació: " + punts + " punts");
                else alert.setContentText("Ha guanyat la màquina!\nHo sentim molt, torna-ho a intentar.");
            }
            alert.showAndWait();
            FXMLLoader cargador = new FXMLLoader(getClass().getResource("/vista/PrimerJugador.fxml"));
            Parent root = cargador.load();
            PrimerJugadorController controlador = cargador.getController();
            if (jugador2 != null) controlador.inicialitzarJugadors(jugador1, jugador2);
            else controlador.inicialitzarJugador(jugador1);
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.toFront();
            stage.show();
        }
        return guanyador != 0;
    }
    public void inicialitzarJugador1(Player j1) {
        this.jugador1 = j1;
    }
    public void inicialitzarJugadors(Player j1, Player j2) {
        this.jugador1 = j1;
        this.jugador2 = j2;
    }

    @FXML
    private void enrere(ActionEvent event) throws IOException {
        //Ací falta un Alert de CONFIRMATION per comprovar que no ha clicat sense voler
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(null);
        alert.setTitle("Confirmació eixida");
        alert.setContentText("Estàs segur que vols abandonar la partida?");
        Optional<ButtonType> action = alert.showAndWait();
        if (action.isPresent() && action.get() == ButtonType.OK) {
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
    }
    public void maquina() {
        maquina = true;
        text_jugador.setText("Jugant contra la màquina!");
        punts = sistema.getPointsAlone();
    }
    public void noMaquina() {
        maquina = false;
        punts = sistema.getPointsRound();
        if (jugador1 != null) {
            text_jugador.setText("Torn de " + jugador1.getNickName());
            text_jugador.setFill(Color.RED);
        }
    }
    class Retraso extends Service<Void> {
        private long delayMilis = 600;
        public long getRetaso() { return delayMilis; }
        public void setRetraso(long retraso) { this.delayMilis = retraso; }
        protected Task<Void> createTask() {
            return new Task<Void>() {
                protected Void call() throws Exception {
                    Thread.sleep(delayMilis);
                    esJugador1 = !esJugador1;
                    return null;
                }
            };
        }
    }
}
