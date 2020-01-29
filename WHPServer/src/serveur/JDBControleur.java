package serveur;


import com.sun.org.apache.xpath.internal.operations.Bool;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;

public class JDBControleur {

    private static Connection conn;

    public Connection connect(String db) throws SQLException {
        data donnees = data.getInstance();
        String url = "jdbc:postgresql://10.122.52.253/"+db;
        String user = donnees.getUser();
        String password = donnees.getPassword();
        return DriverManager.getConnection(url, user, password);
    }


    public void deletePackage(String uuid, String pak) throws SQLException {
        conn = this.connect("wapt");
        Statement stmt = conn.createStatement();
        String SQL = "DELETE FROM hostgroups where host_id='"+uuid+"' AND group_name='"+pak+"'";
        stmt.executeUpdate(SQL);
    }

    public void addPackage(String uuid, String pak) throws SQLException {
        conn = this.connect("wapt");
        Statement stmt = conn.createStatement();
        String SQL = "insert into hostgroups (host_id, group_name) values('"+uuid+"', '"+pak+"')";
        stmt.executeUpdate(SQL);
    }


    public void initPc() throws SQLException {
        conn = this.connect("whp");
        Statement stmt = conn.createStatement();
        String SQL = "DELETE FROM pc";
        stmt.executeUpdate(SQL);
    }

    public void addPc(String nom) throws SQLException {
        conn = this.connect("whp");
        Statement stmt = conn.createStatement();
        String SQL = "INSERT into pc (pc_nom, sal_id) values ("+"'"+nom+"',1)";
        stmt.executeUpdate(SQL);
    }

    public Boolean doesComputerExist(String nom) throws SQLException {
        conn = this.connect("whp");
        Statement stmt = conn.createStatement();
        String SQL = "select * from pc where pc_nom='"+nom+"'";
        ResultSet rs = stmt.executeQuery(SQL);
        if(rs.next()){
            return true;
        }
        return false;
    }

    public ArrayList<String> getAllUsersUsable() throws SQLException {
        ArrayList<String> lesUtilisateurs = new ArrayList<String>();
        conn = this.connect("whp");
        Statement stmt = conn.createStatement();
        String SQL = "select * from utilisateur";
        ResultSet rs = stmt.executeQuery(SQL);
        while (rs.next()) {
            lesUtilisateurs.add(new String(rs.getInt("uti_id") + ";" + rs.getString("uti_nom") + ";" + rs.getString("uti_prenom") + ";" + rs.getString("uti_mail") + ";" + rs.getString("uti_username") + ";" + rs.getInt("uti_statut")));
        }
        rs.close();
        return lesUtilisateurs;
    }

    public ArrayList<String> getAllRoomsUsable(String login) throws SQLException {
        ArrayList<String> lesSalles = new ArrayList<String>();
        conn = this.connect("whp");
        Statement stmt = conn.createStatement();
        String SQL = "select * from salle inner join acces on acces.sal_id = salle.sal_id inner join utilisateur on acces.uti_id = utilisateur.uti_id where uti_username='"+login+"';";
        ResultSet rs = stmt.executeQuery(SQL);
        while (rs.next()) {
            lesSalles.add(new String(rs.getInt("sal_id")+";"+rs.getString("sal_nom")+";"+rs.getString("sal_desc")));
        }
        rs.close();
        return lesSalles;
    }

    public ArrayList<String> getAllPcUsable(String salle) throws SQLException {
        ArrayList<String> lesPc = new ArrayList<String>();
        conn = this.connect("whp");
        Statement stmt = conn.createStatement();
        String SQL = "select pc_nom from pc inner join salle on pc.sal_id = salle.sal_id where salle.sal_nom='"+salle+"';";
        ResultSet rs = stmt.executeQuery(SQL);
        while (rs.next()) {
            lesPc.add(rs.getString("pc_nom"));
        }
        rs.close();
        return lesPc;
    }

    public void addUserToRoom(String com) throws SQLException {
        String[] args = com.split(";");
        if (args.length == 2){
            conn = this.connect("whp");
            Statement stmt = conn.createStatement();
            String SQL = "INSERT INTO acces VALUES  ("+args[0]+", "+args[1]+")";
            stmt.executeUpdate(SQL);
            System.out.println("access created.");
        }else{
            System.out.println("Error : Did you typed ';' in the information ?");
        }
    }

    public void deleteUserFromRoom(String com) throws SQLException {
        String[] args = com.split(";");
        if (args.length == 2){
            conn = this.connect("whp");
            Statement stmt = conn.createStatement();
            String SQL = "DELETE FROM acces where uti_id="+args[0]+" AND sal_id="+args[1];
            stmt.executeUpdate(SQL);
            System.out.println("access removed.");
        }else{
            System.out.println("Error : Did you typed ';' in the information ?");
        }
    }

    public ArrayList<String> listAccess() throws SQLException {
        ArrayList<String> lesAcces = new ArrayList<String>();
        conn = this.connect("whp");
        Statement stmt = conn.createStatement();
        String SQL = "select uti_username, sal_nom from acces\n" +
                "inner join salle on acces.sal_id = salle.sal_id\n" +
                "inner join utilisateur on acces.uti_id = utilisateur.uti_id";
        ResultSet rs = stmt.executeQuery(SQL);
        while (rs.next()) {
            lesAcces.add(rs.getString("uti_username")+" -> "+rs.getString("sal_nom"));
        }
        rs.close();
        return lesAcces;
    }

    public ArrayList<String> getAllUsers() throws SQLException {
        ArrayList<String> lesUtilisateurs = new ArrayList<String>();
        conn = this.connect("whp");
        Statement stmt = conn.createStatement();
        String SQL = "select * from utilisateur";
        ResultSet rs = stmt.executeQuery(SQL);
        while (rs.next()) {
            lesUtilisateurs.add(new String(rs.getInt("uti_id")+" | "+rs.getString("uti_nom").toUpperCase()+" "+rs.getString("uti_prenom")+" "+rs.getString("uti_mail")+", id:"+rs.getString("uti_username")+", statut:"+rs.getInt("uti_statut")));
        }
        rs.close();
        return lesUtilisateurs;
    }

    public ArrayList<String> getAllRooms() throws SQLException {
        ArrayList<String> lesSalles = new ArrayList<String>();
        conn = this.connect("whp");
        Statement stmt = conn.createStatement();
        String SQL = "select * from salle";
        ResultSet rs = stmt.executeQuery(SQL);
        while (rs.next()) {
            lesSalles.add(new String(rs.getInt("sal_id")+" | Name: "+rs.getString("sal_nom")+"\nDescription: "+rs.getString("sal_desc")+"\n-"));
        }
        rs.close();
        return lesSalles;
    }

    public Boolean conUser(String username, String password) throws SQLException {
        conn = this.connect("whp");
        Statement stmt = conn.createStatement();
        String SQL = "select * from utilisateur where uti_username='" +username+ "' and uti_password='" +password+"'";
        ResultSet rs = stmt.executeQuery(SQL);
        return rs.isBeforeFirst();
    }

    public void addUser(String com) throws SQLException, NoSuchAlgorithmException {
        String[] args = com.split(";");
        if (args.length == 6){
            byte[] bytePassword = args[4].getBytes();
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(bytePassword);
            byte[] digest = md.digest();
            String password = DatatypeConverter.printHexBinary(digest).toLowerCase();
            conn = this.connect("whp");
            Statement stmt = conn.createStatement();
            String SQL = "INSERT INTO utilisateur (UTI_NOM, UTI_PRENOM, UTI_MAIL, UTI_USERNAME, UTI_PASSWORD, UTI_STATUT) VALUES  ('"+args[0]+"', '"+args[1]+"', '"+args[2]+"', '"+args[3]+"', '"+password+"', "+args[5]+")";
            stmt.executeUpdate(SQL);
            System.out.println("User created.");
        }else{
            System.out.println("Error : Did you typed ';' in the information ?");
        }
    }

    public void updateUser(String com) throws SQLException, NoSuchAlgorithmException {
        String[] args = com.split(";");
        if (args.length == 3){
            if (args[1].equals("password")) {
                byte[] bytePassword = args[2].getBytes();
                MessageDigest md = MessageDigest.getInstance("MD5");
                md.update(bytePassword);
                byte[] digest = md.digest();
                args[2] = DatatypeConverter.printHexBinary(digest).toLowerCase();
            }
            conn = this.connect("whp");
            Statement stmt = conn.createStatement();
            String SQL = "UPDATE utilisateur set "+args[1]+"='"+args[2]+"' WHERE uti_id="+args[0];
            if (args[0].equals("status")){
                SQL = SQL.replace("'", "");
            }
            stmt.executeUpdate(SQL);
            System.out.println("User updated.");
        }else{
            System.out.println("Error : Did you typed ';' in the information ?");
        }
    }

    public void addSalle(String com) throws SQLException {
        String[] args = com.split(";");
        if (args.length == 2){
            conn = this.connect("whp");
            Statement stmt = conn.createStatement();
            String SQL = "INSERT INTO salle (sal_nom, sal_desc) VALUES  ('"+args[0]+"', '"+args[1]+"');";
            stmt.executeUpdate(SQL);
            System.out.println("Room created.");
        }else{
            System.out.println("Error : Did you typed ';' in the information ?");
        }
    }

    public void updateRoom(String com) throws SQLException, NoSuchAlgorithmException {
        String[] args = com.split(";");
        if (args.length == 3){
            conn = this.connect("whp");
            Statement stmt = conn.createStatement();
            String SQL = "UPDATE salle set "+args[1]+"='"+args[2]+"' WHERE sal_id="+args[0];
            if (args[0].equals("status")){
                SQL = SQL.replace("'", "");
            }
            stmt.executeUpdate(SQL);
            System.out.println("Room updated.");
        }else{
            System.out.println("Error : Did you typed ';' in the information ?");
        }
    }

}
