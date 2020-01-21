package serveur;// Java implementation of serveur.Server side
// It contains two classes : serveur.Server and serveur.ClientHandler
// Save file as serveur.Server.java


import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.util.*;
import java.net.*;

// serveur.Server class
public class Server
{
    static int i = 0;
    // Vector to store active clients
    static Vector<ClientHandler> ar = new Vector<>();
    private static Map<String, Object> keys;
    private static PrivateKey privateKey;
    private static PublicKey publicKey;

    public Server() throws NoSuchPaddingException, NoSuchAlgorithmException {
    }

    public static void main(String[] args) throws Exception
    {
        System.out.println("Starting KeyGen");
        loader loader = new loader();
        Thread load = new Thread(loader);
        load.start();
        keys = encryption.getRSAKeys();
        loader.setShowProgress(false);
        PrivateKey privateKey = (PrivateKey) keys.get("private");
        PublicKey publicKey = (PublicKey) keys.get("public");
        // server is listening on port 1234
        ServerSocket ss = new ServerSocket(1234);

        Socket s;
        // running infinite loop for getting
        // client request
        while (true)
        {
            // Accept the incoming request
            s = ss.accept();
            // obtain input and output streams
            DataInputStream dis = new DataInputStream(s.getInputStream());
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());
            // Create a new handler object for handling this request.
            ClientHandler mtch = new ClientHandler(s,"client_" + i + "_" + s.getInetAddress(), dis, dos, privateKey, publicKey);
            // Create a new Thread with this object.
            Thread t = new Thread(mtch);
            // add this client to active clients list
            ar.add(mtch);

            // start the thread.
            t.start();
            i++;
        }
    }
}

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
                    System.out.println(this.name+" asked "+encryption.decryptMessage(receivedTr, prK));
                    received = encryption.decryptMessage(receivedTr, prK).split(":");
                    switch (received[0]) {
                        case "com":
                            switch (received[1]) {
                                case "connexion":
                                    String[] command = received[2].split(";");
                                    String login = command[0];
                                    String password = command[1];
                                    if (leControleur.canConnect(login, password)) {
                                        sendMessage("canLogin:"+encryption.publicKeyToString(puK));
                                        System.out.println(this.name + " is logged in");
                                    } else {
                                        sendMessage("cannotLogin");
                                        System.out.println(this.name + " tried to connect");
                                    }
                                    break;
                                case "getSalles":
                                    String[] listeSalle = {"Salle 1", "Salle 2" ,"Salle 3", "Salle 4"};
                                    String toSend ="";
                                    for (String s : listeSalle){
                                        toSend = toSend+s+";";
                                    }
                                    toSend = toSend.substring(0, toSend.length()-1);
                                    sendEncryptedMessage(toSend, puKClient);
                            }
                            break;
                        default:
                            //doThing
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
            // closing resources
            this.dis.close();
            this.dos.close();

        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
