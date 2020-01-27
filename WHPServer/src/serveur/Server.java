package serveur;// Java implementation of serveur.Server side
// It contains two classes : serveur.Server and serveur.ClientHandler
// Save file as serveur.Server.java


import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
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
        System.out.println("\033[31mBeware when stopping the server, you must type 'close' in the console or the socket will stay alive, and you really don't want that to happen.");
        System.out.println("\033[31mYou can type 'help' to get all the commands.");
        loader loader = new loader();
        Thread load = new Thread(loader);
        data donnees = data.getInstance();
        donnees.setUser(args[1]);
        donnees.setPassword(args[2]);
        load.start();
        keys = encryption.getRSAKeys();
        loader.setShowProgress(false);
        PrivateKey privateKey = (PrivateKey) keys.get("private");
        PublicKey publicKey = (PublicKey) keys.get("public");
        serverCommander sc = new serverCommander();
        Thread ServC = new Thread(sc);
        ServC.start();
        // server is listening on port 1234
        ServerSocket ss;
        try {
            ss = new ServerSocket(Integer.parseInt(args[0]));
        }catch (NumberFormatException e){
            ss = new ServerSocket(1234);
        }
        Socket s;
        // running  infinite loop for getting
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

class serverCommander implements Runnable{
    Socket s;
    public serverCommander(){
    }
    @Override
    public void run() {
        String input;
        while(true) {
            Scanner userInput = new Scanner(System.in);
            input = userInput.nextLine();

            JDBControleur Bdd = null;
            switch (input){
                case("getAllUsers"):
                    Bdd = new JDBControleur();
                    try {
                        for (String s : Bdd.getAllUsers()) {
                            System.out.println("\033[0;33m"+s+"\033[0m");
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;

                case("getAllRooms"):
                    Bdd = new JDBControleur();
                    try {
                        for (String s : Bdd.getAllRooms()) {
                            System.out.println("\033[0;33m"+s+"\033[0m");
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;

                case("addUser"):
                    Bdd = new JDBControleur();
                    String cUser ="";
                    Scanner scUser = new Scanner(System.in);
                    System.out.println("Last name : ");
                    cUser = cUser+scUser.nextLine()+";";
                    System.out.println("Name : ");
                    cUser = cUser+scUser.nextLine()+";";
                    System.out.println("Mail : ");
                    cUser = cUser+scUser.nextLine()+";";
                    System.out.println("Login : ");
                    cUser = cUser+scUser.nextLine()+";";
                    System.out.println("password : ");
                    cUser = cUser+scUser.nextLine()+";";
                    System.out.println("Status : ");
                    cUser = cUser+scUser.nextLine();
                    try {
                        Bdd.addUser(cUser);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }
                    break;
                case ("updateUser"):
                    Bdd = new JDBControleur();
                    String uUser ="";
                    Scanner scUUser = new Scanner(System.in);
                    System.out.println("Id of the user you want to update : ");
                    uUser = uUser + scUUser.nextLine()+";";
                    System.out.println("What do you want to update\n" +
                            "0 | Nothing\n" +
                            "1 | Last name\n" +
                            "2 | Name\n" +
                            "3 | Mail\n" +
                            "4 | Login\n" +
                            "5 | Password\n" +
                            "6 | Status");
                    String toUpdateU = "0";
                    toUpdateU = scUUser.nextLine();
                    switch (toUpdateU){
                        case("1"):
                            uUser = uUser+"uti_nom;";
                            break;
                        case("2"):
                            uUser = uUser+"uti_prenom;";
                            break;
                        case("3"):
                            uUser = uUser+"uti_mail;";
                            break;
                        case("4"):
                            uUser = uUser+"uti_username;";
                            break;
                        case("5"):
                            uUser = uUser+"uti_password;";
                            break;
                        case("6"):
                            uUser = uUser+"uti_statut;";
                            break;

                    }
                    if(!(toUpdateU.equals("0"))) {
                        System.out.println("With the value : ");
                        uUser = uUser + scUUser.nextLine();
                        try {
                            Bdd.updateUser(uUser);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case("addRoom"):
                    Bdd = new JDBControleur();
                    String cRoom ="";
                    Scanner scRoom = new Scanner(System.in);
                    System.out.println("Name of the room : ");
                    cRoom = cRoom+scRoom.nextLine()+";";
                    System.out.println("Description of the room : ");
                    cRoom = cRoom+scRoom.nextLine();
                    try {
                        Bdd.addSalle(cRoom);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;

                case ("updateRoom"):
                    Bdd = new JDBControleur();
                    String uRoom ="";
                    Scanner scURoom = new Scanner(System.in);
                    System.out.println("Id of the room you want to update : ");
                    uRoom = uRoom + scURoom.nextLine()+";";
                    System.out.println("What do you want to update\n" +
                            "0 | Nothing\n" +
                            "1 | Name\n" +
                            "2 | Description");
                    String toUpdater = "0";
                    toUpdater = scURoom.nextLine();
                    switch (toUpdater){
                        case("1"):
                            uRoom = uRoom+"sal_nom;";
                            break;
                        case("2"):
                            uRoom = uRoom+"sal_desc;";
                            break;


                    }
                    if(!(toUpdater.equals("0"))) {
                        System.out.println("With the value : ");
                        uRoom = uRoom + scURoom.nextLine();
                        try {
                            Bdd.updateRoom(uRoom);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        }
                    }
                    break;

                case("listAccess"):
                    Bdd = new JDBControleur();
                    try {
                        for (String s : Bdd.listAccess()) {
                            System.out.println("\033[0;33m"+s+"\033[0m");
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;

                case("addAccess"):
                    Bdd = new JDBControleur();
                    String cAcces ="";
                    Scanner scAcces = new Scanner(System.in);
                    System.out.println("Id of the user : ");
                    cAcces = cAcces+scAcces.nextLine()+";";
                    System.out.println("Id of the room : ");
                    cAcces = cAcces+scAcces.nextLine();
                    try {
                        Bdd.addUserToRoom(cAcces);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;

                case("removeAccess"):
                    Bdd = new JDBControleur();
                    String rAcces ="";
                    Scanner scrAcces = new Scanner(System.in);
                    System.out.println("Id of the user : ");
                    rAcces = rAcces+scrAcces.nextLine()+";";
                    System.out.println("Id of the room : ");
                    rAcces = rAcces+scrAcces.nextLine();
                    try {
                        Bdd.deleteUserFromRoom(rAcces);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;


                case("help"):
                    System.out.println("\033[0;33mServer commands : ");
                    System.out.println("\033[0;36mhelp  \033[0m| Show this help page.");
                    System.out.println("\033[0;36mgetAllUsers  \033[0m| Show all users referenced in the database.");
                    System.out.println("\033[0;36maddUser \033[0m| Add a user.");
                    System.out.println("\033[0;36mupdateUser \033[0m| Update the value of a user.");
                    System.out.println("\033[0;36mlistAccess \033[0m| List all user->room access.");
                    System.out.println("\033[0;36maddAccess \033[0m| Add a user->room access.");
                    System.out.println("\033[0;36mremoveAccess \033[0m| Remove a user->room access.");
                    System.out.println("\033[0;36mclose \033[0m| Close the program and all component properly.");
                    break;
                case("close"):
                    System.exit(0);
                    break;
            }
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
            // closing resources
            this.dis.close();
            this.dos.close();
            s.close();

        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
