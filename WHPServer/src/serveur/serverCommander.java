package serveur;

import org.json.JSONException;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Scanner;

class serverCommander implements Runnable{
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

                case("initBdd"):
                    String[] lesPc = new String[]{};
                    Bdd = new JDBControleur();
                    try {
                        Bdd.initPc();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    try {
                        lesPc = httpRequest.getHosts().split(";");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    for (String s : lesPc){
                        try {
                            Bdd.addPc(s);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("Ok !");
                    break;

                case "updateBdd":
                    String[] lesPcs = new String[]{};
                    Bdd = new JDBControleur();
                    try {
                        lesPcs = httpRequest.getHosts().split(";");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    for (String s : lesPcs){
                        try {
                            if(!(Bdd.doesComputerExist(s))) {
                                Bdd.addPc(s);
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("Ok !");
                    break;


                case("help"):
                    System.out.println("\033[0;33mServer commands : ");
                    System.out.println("\033[0;36mhelp  \033[0m| Show this help page.");
                    System.out.println("\033[0;36minitBdd \033[0m| Init the database with computer of WAPT (warning it reset the room param to default.");
                    System.out.println("\033[0;36mupdateBdd \033[0m| update all the computer without deleting the previous one.");
                    System.out.println("\033[0;36mgetAllUsers  \033[0m| Show all users referenced in the database.");
                    System.out.println("\033[0;36maddUser \033[0m| Add a user.");
                    System.out.println("\033[0;36mupdateUser \033[0m| Update the value of a user.");
                    System.out.println("\033[0;36mgetAllRooms  \033[0m| Show all rooms referenced in the database.");
                    System.out.println("\033[0;36maddRoom \033[0m| Add a room.");
                    System.out.println("\033[0;36mupdateRoom \033[0m| Update the value of a room.");
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
