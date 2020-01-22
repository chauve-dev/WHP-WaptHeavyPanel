package serveur;

import com.sun.org.apache.xpath.internal.operations.Bool;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;

public class JDBControleur {

    private static Connection conn;

    public Connection connect() throws SQLException {
        String url = "jdbc:postgresql://127.0.0.1/WHP";
        String user = "WHPUser";
        String password = "password";
        return DriverManager.getConnection(url, user, password);
    }

    public ArrayList<String> getAllUsers() throws SQLException {
        ArrayList<String> lesUtilisateurs = new ArrayList<String>();
        conn = this.connect();
        Statement stmt = conn.createStatement();
        String SQL = "select * from utilisateur";
        ResultSet rs = stmt.executeQuery(SQL);
        while (rs.next()) {
            lesUtilisateurs.add(new String(rs.getInt("uti_id")+" | "+rs.getString("uti_nom").toUpperCase()+" "+rs.getString("uti_prenom")+" "+rs.getString("uti_mail")+", id:"+rs.getString("uti_username")+", statut:"+rs.getInt("uti_statut")));
        }
        rs.close();
        return lesUtilisateurs;
    }

    public Boolean conUser(String username, String password) throws SQLException {
        conn = this.connect();
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
            conn = this.connect();
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
            conn = this.connect();
            Statement stmt = conn.createStatement();
            String SQL = "UPDATE utilisateur set "+args[1]+"='"+args[2]+"' WHERE uti_id="+args[0];
            System.out.println(SQL);
            if (args[0].equals("status")){
                SQL = SQL.replace("'", "");
            }
            stmt.executeUpdate(SQL);
            System.out.println("User updated.");
        }else{
            System.out.println("Error : Did you typed ';' in the information ?");
        }
    }

}
