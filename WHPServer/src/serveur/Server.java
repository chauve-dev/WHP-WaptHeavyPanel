package serveur;

import java.io.*;
import java.security.*;
import java.util.*;
import java.net.*;

// serveur.Server class
public class Server
{
    static int i = 0;
    // Vector to store active clients
    static Vector<ClientHandler> ar = new Vector<>();
    private static Map<String, Object> keys;

    public Server()  {
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

