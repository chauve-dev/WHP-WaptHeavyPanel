package client.modele;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.security.PrivateKey;
import java.security.PublicKey;

public class data {

    private static data instance = null;
    private String identifiant = "";
    private String password = "";
    private String selectedSalle = "";
    private PrivateKey privateKey;
    private DataInputStream dis;
    private DataOutputStream dos;
    private PublicKey serverPub;
    private Socket s;

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

    public DataInputStream getDis() {
        return dis;
    }

    public void setDis(DataInputStream dis) {
        this.dis = dis;
    }

    public DataOutputStream getDos() {
        return dos;
    }

    public void setDos(DataOutputStream dos) {
        this.dos = dos;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
    }

    public Socket getS() {
        return s;
    }

    public void setS(Socket s) {
        this.s = s;
    }
}
