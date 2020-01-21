package serveur;

public class controleur {

    public Boolean canConnect(String login, String password){
        Boolean canLogin=false;
        if(login.equals("admin") && password.equals("admin")) {
            canLogin=true;
        }
        return canLogin;
    }
}
