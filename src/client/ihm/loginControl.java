package client.ihm;

import client.modele.data;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import client.encryption;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Map;

public class loginControl {
    final static int ServerPort = 1234;
    private Boolean isInit = false;
    @FXML
    private TextField login;
    @FXML
    private PasswordField password;

    private Socket s;
    private InetAddress ip;
    private DataInputStream dis;
    private DataOutputStream dos;


    private static Map<String, Object> keys;

    static {
        try {
            keys = encryption.getRSAKeys();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static PrivateKey privateKey = (PrivateKey) keys.get("private");
    private static PublicKey publicKey = (PublicKey) keys.get("public");


    private data donnees = data.getInstance();
    public loginControl() throws Exception {
    }

    public void sendMessage(String msg) throws IOException {
        dos.writeUTF(msg);
    }

    public void sendEncryptedMessage(String msg, PublicKey PK) throws Exception {
        dos.writeUTF(encryption.encryptMessage(msg, PK));
    }

    public void init() throws Exception {
        try {
            this.ip = InetAddress.getByName("localhost");

            // establish the connection
            this.s = new Socket(ip, ServerPort);
            donnees.setS(this.s);
            // obtaining input and out streams
            this.dis = new DataInputStream(s.getInputStream());
            this.dos = new DataOutputStream(s.getOutputStream());
            this.isInit = true;
            sendMessage("com:myPK:"+encryption.publicKeyToString(publicKey));
            donnees.setServerPub(encryption.stringToPublicKey(dis.readUTF()));

        }catch (IOException e){
            Alert socketError = new Alert(Alert.AlertType.ERROR);
            socketError.setTitle("Erreur de connexion");
            socketError.setContentText("Connexion au serveur échouée contacter \nl'administrateur système");
            socketError.showAndWait();
        }
    }

    public void connect(ActionEvent actionEvent) throws Exception {
        init();
        if(password.getLength()>0 && login.getLength()>0) {
            if (isInit) {
                donnees.setPrivateKey(privateKey);
                try {
                    String commmand = "com:connexion:" + login.getText() + ";" + password.getText();
                    sendEncryptedMessage(commmand, donnees.getServerPub());
                    String received = dis.readUTF();
                    if (received.startsWith("canLogin")) {
                        donnees.setDos(dos);
                        donnees.setDis(dis);
                        donnees.setIdentifiant(login.getText());
                        donnees.setPassword(password.getText());
                        //ouverture de l'accueil
                        Stage main = new Stage();
                        Parent root = FXMLLoader.load(getClass().getResource("accueil.fxml"));
                        main.setTitle("WHP - Accueil");
                        main.setScene(new Scene(root));
                        main.setResizable(false);
                        main.show();

                        Stage stage =  (Stage)login.getScene().getWindow();
                        stage.close();
                    } else {
                        Alert connError = new Alert(Alert.AlertType.ERROR);
                        connError.setTitle("Erreur");
                        connError.setContentText("Impossible mot de passe \nou identifiant\nincorrect");
                        connError.showAndWait();
                    }
                    System.out.println(received);

                } catch (IOException e) {
                    Alert socketError = new Alert(Alert.AlertType.ERROR);
                    socketError.setTitle("Erreur");
                    socketError.setContentText("Impossible de communiquer \navec le serveur");
                    socketError.showAndWait();
                }
                //s.close();
            }
        }else{
            Alert inputError = new Alert(Alert.AlertType.ERROR);
            inputError.setTitle("Erreur");
            inputError.setContentText("L'identifiant et le mot de passe \nne peuvent être vide.");
            inputError.showAndWait();
        }
    }
}
