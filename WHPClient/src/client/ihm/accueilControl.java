package client.ihm;

import client.modele.data;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import client.encryption;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class accueilControl implements Initializable {
    public VBox checkboxes;
    public String selected=null;
    public Label salleselectionnee;
    @FXML
    private ListView listSalles;
    private ObservableList<String> items = FXCollections.observableArrayList();
    data donnees = data.getInstance();


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        String[] listeSalle = new String[]{};
        String[] listePaquet = new String[]{};
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


        try {
            donnees.getDos().writeUTF(encryption.encryptMessage("com:getPaquets", donnees.getServerPub()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            listePaquet = encryption.decryptMessage(donnees.getDis().readUTF(), donnees.getPrivateKey()).split(";");
        } catch (Exception e) {
            e.printStackTrace();
        }

        listSalles.setItems(items);
        for (String s : listeSalle){
            items.add(s);
        }


        for (String s : listePaquet){
            checkboxes.getChildren().add(new CheckBox(s));
            checkboxes.getChildren().add(new Separator());
        }
    }



    public void selectItem(MouseEvent mouseEvent) throws IOException {
        if(!listSalles.getSelectionModel().getSelectedItems().toString().equals("")) {
            if (mouseEvent.getClickCount() == 1) {
                String salle = listSalles.getSelectionModel().getSelectedItems().toString();
                salle = salle.substring(1, salle.length() - 1);
                String[] laSalle = salle.split("\n");
                this.selected = laSalle[0];
                salleselectionnee.setText(this.selected);
            }
            if (mouseEvent.getClickCount() >= 2) {
                String salle = listSalles.getSelectionModel().getSelectedItems().toString();
                salle = salle.substring(1, salle.length() - 1);
                String[] laSalle = salle.split("\n");
                donnees.setSelectedSalle(laSalle[0]);
                Stage main = new Stage();
                Parent root = FXMLLoader.load(getClass().getResource("salle.fxml"));
                main.setTitle("WHP - " + salle);
                main.initModality(Modality.WINDOW_MODAL);
                main.initOwner(listSalles.getScene().getWindow());
                main.setScene(new Scene(root));
                main.setResizable(false);
                main.show();
            }
        }
    }

    public void lanceInstall(MouseEvent mouseEvent) {
        if(!(this.selected == null)) {
            Boolean ite = false;
            for (Node i : checkboxes.getChildren()) {
                if (i.getTypeSelector().equals("CheckBox")) {
                    if(((CheckBox)i).isSelected()) {
                        ite = true;
                        System.out.println(((CheckBox) i).getText());
                        ((CheckBox) i).setSelected(false);
                    }
                }
            }
            if (ite) {
                Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
                confirmation.setTitle("Ok !");
                confirmation.setContentText("La requête d'installation\na été envoyé, les paquets\nseront installé sous peu.");
                confirmation.showAndWait();
            }else{
                Alert confirmation = new Alert(Alert.AlertType.WARNING);
                confirmation.setTitle("Attention !");
                confirmation.setContentText("Veuillez sélectionner\nau moins un paquet.");
                confirmation.showAndWait();
            }
        }else{
            Alert selectionWarning = new Alert(Alert.AlertType.WARNING);
            selectionWarning.setTitle("Attention !");
            selectionWarning.setContentText("Il n'est pas possible d'installer\ndes paquets temps que la salle n'est\npas sélectionnée.");
            selectionWarning.showAndWait();
        }
    }
}
