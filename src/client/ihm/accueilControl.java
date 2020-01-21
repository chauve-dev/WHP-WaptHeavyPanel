package client.ihm;

import client.modele.data;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class accueilControl implements Initializable {
    @FXML
    private ListView listSalles;
    private ObservableList<String> items = FXCollections.observableArrayList();
    data donnees = data.getInstance();


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        listSalles.setItems(items);
        for(int i=0; i<200; i++){
            items.add("Salle "+i);
        }
    }



    public void selectItem(MouseEvent mouseEvent) throws IOException {
        if(mouseEvent.getClickCount()>=2) {
            String salle = listSalles.getSelectionModel().getSelectedItems().toString();
            salle = salle.substring(1, salle.length()-1);
            donnees.setSelectedSalle(salle);
            Stage main = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("salle.fxml"));
            main.setTitle("WHP - "+salle);
            main.initModality(Modality.WINDOW_MODAL);
            main.initOwner(listSalles.getScene().getWindow());
            main.setScene(new Scene(root));
            main.setResizable(false);
            main.show();
        }
    }
}
