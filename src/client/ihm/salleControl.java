package client.ihm;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class salleControl implements Initializable {
    @FXML
    private ListView listePc;
    private ObservableList<String> items = FXCollections.observableArrayList();

    public void selectPc(MouseEvent mouseEvent) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listePc.setItems(items);
        for(int i=0; i<20; i++){
            items.add("PC "+i);
        }
    }
}
