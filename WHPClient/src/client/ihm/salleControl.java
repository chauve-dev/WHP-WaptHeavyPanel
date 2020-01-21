package client.ihm;

import client.encryption;
import client.modele.data;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class salleControl implements Initializable {
    data donnees = data.getInstance();
    @FXML
    private ListView listePc;
    private ObservableList<String> items = FXCollections.observableArrayList();

    public void selectPc(MouseEvent mouseEvent) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            donnees.getDos().writeUTF(encryption.encryptMessage("com:selSalle:"+donnees.getSelectedSalle(), donnees.getServerPub()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        listePc.setItems(items);
        for(int i=0; i<20; i++){
            items.add("PC "+i);
        }
    }
}
