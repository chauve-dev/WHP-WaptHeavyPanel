package serveur;


public class data {

    private static data instance = null;
    private String user;
    private String password;
    public static data getInstance(){
        if(instance == null){
            instance = new data();
        }
        return instance;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
