package client.ihm;

import client.encryption;
import client.modele.data;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class salleControl implements Initializable {
    public Label pcselectionnee;
    public Label labelP;
    public ImageView pstate;
    public ImageView lstate;
    public VBox checkboxes;
    private String selected = null;
    data donnees = data.getInstance();
    @FXML
    private ListView listePcs;
    private ObservableList<String> items = FXCollections.observableArrayList();

    public void selectPc(MouseEvent mouseEvent) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String[] listePc = new String[]{};
        String[] listePaquet = new String[]{};
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

        listePcs.setItems(items);
        for (String s : listePc){
            items.add(s);
        }

        for (String s : listePaquet){
            checkboxes.getChildren().add(new CheckBox(s));
            checkboxes.getChildren().add(new Separator());
        }

        pstate.setImage(new Image(String.valueOf(getClass().getClassLoader().getResource("invalid.png"))));
        lstate.setImage(new Image(String.valueOf(getClass().getClassLoader().getResource("valid.png"))));
        labelP.setText(donnees.getSelectedSalle());
    }

    public void selectItem(MouseEvent mouseEvent) {
        String pc = listePcs.getSelectionModel().getSelectedItems().toString();
        pc = pc.substring(1, pc.length()-1);
        this.selected = pc;
        pcselectionnee.setText(this.selected);
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
            selectionWarning.setContentText("Il n'est pas possible d'installer\ndes paquets temps que l'ordinateur n'est\npas sélectionnée.");
            selectionWarning.showAndWait();
        }
    }
}
