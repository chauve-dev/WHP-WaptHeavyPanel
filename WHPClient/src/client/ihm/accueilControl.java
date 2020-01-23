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
import client.encryption;
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
        String[] listeSalle = new String[]{};
        try {
            donnees.getDos().writeUTF(encryption.encryptMessage("com:getSalles:"+donnees.getIdentifiant(), donnees.getServerPub()));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            listeSalle = encryption.decryptMessage(donnees.getDis().readUTF(), donnees.getPrivateKey()).split(";");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        listSalles.setItems(items);
        for (String s : listeSalle){
            items.add(s);
        }
    }



    public void selectItem(MouseEvent mouseEvent) throws IOException {
        if(mouseEvent.getClickCount()>=2) {
            String salle = listSalles.getSelectionModel().getSelectedItems().toString();
            salle = salle.substring(1, salle.length()-1);
            String[] laSalle = salle.split("\n");
            donnees.setSelectedSalle(laSalle[0]);
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
