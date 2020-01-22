package client.ihm;

import client.encryption;
import client.modele.data;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class salleControl implements Initializable {
    data donnees = data.getInstance();
    @FXML
    private ListView listePcs;
    private ObservableList<String> items = FXCollections.observableArrayList();

    public void selectPc(MouseEvent mouseEvent) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String[] listePc = new String[]{};
        try {
            donnees.getDos().writeUTF(encryption.encryptMessage("com:selSalle:"+donnees.getSelectedSalle(), donnees.getServerPub()));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            listePc = encryption.decryptMessage(donnees.getDis().readUTF(), donnees.getPrivateKey()).split(";");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        listePcs.setItems(items);


        for (String s : listePc){
            items.add(s);
        }
    }
}
