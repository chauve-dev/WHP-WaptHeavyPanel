package client.modele;

import java.security.PublicKey;

public class data {

    private static data instance = null;
    private String identifiant = "";
    private String password = "";
    private String selectedSalle = "";
    private PublicKey serverPub;

    public static data getInstance(){
        if(instance == null){
            instance = new data();
        }
        return instance;
    }


    public String getIdentifiant() {
        return identifiant;
    }

    public void setIdentifiant(String identifiant) {
        this.identifiant = identifiant;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getSelectedSalle() {
        return selectedSalle;
    }

    public void setSelectedSalle(String selectedSalle) {
        this.selectedSalle = selectedSalle;
    }

    public PublicKey getServerPub() {
        return serverPub;
    }

    public void setServerPub(PublicKey serverPub) {
        this.serverPub = serverPub;
    }
}
