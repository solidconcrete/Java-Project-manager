package entities.User;
public class User {
    private String login;
    private String password;
    private String token;
    
        public User(String login, String password)
    {
        this.login = login;
        this.password = password;
        
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "User{" + "login=" + login + ", password=" + password + '}';
    }
        
        
}