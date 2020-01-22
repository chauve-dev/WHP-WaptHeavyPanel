package serveur;

import javax.xml.bind.DatatypeConverter;
import java.sql.SQLException;
import java.security.*;
public class controleur {
    private static JDBControleur bdd = new JDBControleur();
    public Boolean canConnect(String login, String password) throws SQLException, NoSuchAlgorithmException {
        Boolean canLogin=false;
        byte[] bytePassword = password.getBytes();
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(bytePassword);
        byte[] digest = md.digest();
        password = DatatypeConverter.printHexBinary(digest).toLowerCase();
        if(bdd.conUser(login, password)) {
            canLogin=true;
        }
        return canLogin;
    }
}
