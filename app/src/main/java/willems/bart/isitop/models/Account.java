package willems.bart.isitop.models;

/**
 * Created by bart on 4/08/16.
 */
public class Account {

    public Account()
    {

    }

    private int id;
    private String username;
    private String password;
    private String salt;
    private int admin;

    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getSalt() { return salt; }
    public int getAdmin() { return admin; }

    public void setId(int id) { this.id = id; }
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setSalt(String salt) { this.salt = salt; }
    public void setAdmin(int admin) { this.admin = admin; }

    @Override
    public String toString() {
        return username;
    }
}
