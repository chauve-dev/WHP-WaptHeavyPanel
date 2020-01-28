package serveur;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;

// serveur.ClientHandler class
class ClientHandler implements Runnable
{
    controleur leControleur = new controleur();
    private String name;
    final DataInputStream dis;
    final DataOutputStream dos;
    Socket s;
    PrivateKey prK;
    PublicKey puK;
    private JDBControleur Bdd;

    PublicKey puKClient;
    boolean isloggedin;

    // constructor
    public ClientHandler(Socket s, String name,
                         DataInputStream dis, DataOutputStream dos, PrivateKey prK, PublicKey puK) {
        this.dis = dis;
        this.dos = dos;
        this.name = name;
        this.s = s;
        this.isloggedin=true;
        this.prK = prK;
        this.puK = puK;
    }

    public void sendMessage(String msg) throws IOException {
        dos.writeUTF(msg);
    }

    public void sendEncryptedMessage(String msg, PublicKey PK) throws Exception {
        dos.writeUTF(encryption.encryptMessage(msg, PK));
    }

    @Override
    public void run() {

        String receivedTr;
        String[] received;
        while (true)
        {
            try
            {
                // receive the string
                receivedTr = dis.readUTF();
                if (receivedTr.startsWith("com")){
                    received = receivedTr.split(":");
                switch (received[0]) {
                        case "com":
                            switch (received[1]) {
                                case "myPK":
                                    this.puKClient = encryption.stringToPublicKey(received[2]);
                                    sendMessage(encryption.publicKeyToString(puK));
                            }
                            break;
                        default:
                            //doThing
                    }
            }else{
                    received = encryption.decryptMessage(receivedTr, prK).split(":");
                    System.out.println(this.name+" asked for "+received[0]+":"+received[1]);
                    switch (received[0]) {
                        case "com":
                            switch (received[1]) {
                                case "connexion":
                                    String[] command = received[2].split(";");
                                    String login = command[0];
                                    String password = command[1];
                                    if (leControleur.canConnect(login, password)) {
                                        this.name = "client_"+login+s.getInetAddress();
                                        sendMessage("canLogin:"+encryption.publicKeyToString(puK));
                                        System.out.println(this.name+ " is logged in");
                                    } else {
                                        sendMessage("cannotLogin");
                                        System.out.println(this.name + " tried to connect");
                                    }
                                    break;
                                case "getSalles":
                                    Bdd = new JDBControleur();
                                    ArrayList<String> lesSalles = new ArrayList<String>();
                                    String toSendS ="";
                                    for (String s : Bdd.getAllRoomsUsable(received[2])){
                                        String[] s1 = s.split(";");
                                        toSendS = toSendS+s1[1]+"\n\t"+s1[2]+";";
                                    }
                                    if (toSendS.length()>0) {
                                        toSendS = toSendS.substring(0, toSendS.length() - 1);
                                    }
                                    sendEncryptedMessage(toSendS, puKClient);
                                    break;
                                case "selSalle":
                                    Bdd = new JDBControleur();
                                    ArrayList<String> lesPc = new ArrayList<String>();
                                    String toSendP ="";
                                    for (String s : Bdd.getAllPcUsable(received[2])){
                                        toSendP = toSendP+s+";";
                                    }
                                    if (toSendP.length()>0) {
                                        toSendP = toSendP.substring(0, toSendP.length() - 1);
                                    }
                                    sendEncryptedMessage(toSendP, puKClient);
                                    break;
                                default:
                                    System.out.println(this.name+" asked for "+received[0]+":"+received[1]+" but this command doesn't exist");
                                    break;
                            }
                            break;
                        default:
                            System.out.println(this.name+" asked for "+received[0]+" but this service doesn't exist");
                            break;
                    }
                }
            } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e) {
                this.isloggedin=false;
                try {
                    this.s.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                break;
            } catch (BadPaddingException e) {
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        try
        {
            // closing client.resources
            this.dis.close();
            this.dos.close();
            s.close();

        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
