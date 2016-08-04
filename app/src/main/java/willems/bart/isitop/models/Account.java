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
    private boolean admin;

    public int getId() { return id; }
    public String getUsername() { return username; }
    public boolean getAdmin() { return admin; }

    public void setId(int id) { this.id = id; }
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setSalt(String salt) { this.salt = salt; }
    public void setAdmin(boolean admin) { this.admin = admin; }
}
