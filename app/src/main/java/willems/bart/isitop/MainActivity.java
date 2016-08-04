package willems.bart.isitop;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import willems.bart.isitop.models.Account;
import willems.bart.isitop.sqlite.MySQLiteOpenHelper;


public class MainActivity extends AppCompatActivity {
    public final static String LOGIN_USERNAME = "com.example.Isitop.MESSAGE";
    private MySQLiteOpenHelper  db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(isFirstLaunch())
        {

        }
    }

    public void login(View view)
    {
        EditText usernameText = (EditText) findViewById(R.id.login_username);
        String usernameMessage = usernameText.getText().toString();

        EditText passwordText = (EditText) findViewById(R.id.login_password);
        String passwordMessage = passwordText.getText().toString();

        if(!usernameMessage.equals(null) && !usernameMessage.isEmpty())
        {
            if(!passwordMessage.equals(null) && !passwordMessage.isEmpty())
            {
                db = new MySQLiteOpenHelper(this);
                Account account = db.getAccountByUsername(usernameMessage);
                if(account != null)
                {
                    //String passwordHash = sha512();
                    MessageDigest md = null;
                    String salt = account.getSalt();
                    try {
                        md = MessageDigest.getInstance("SHA-512");
                        // if account.getPassword() = sha512(password,account.getSalt())
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }
                    // User is authenticated
                    Intent intent = new Intent(this, LoginActivity.class);

                    intent.putExtra(LOGIN_USERNAME, usernameMessage);
                    startActivity(intent);
                }

            }
        }
    }

    private boolean isFirstLaunch() {
        // Restore preferences
        SharedPreferences settings = getSharedPreferences("FIRST_LAUNCH", 0);
        boolean isFirstLaunch = settings.getBoolean("isFirstLaunch", true);
        Log.i( "isFirstLaunch", "sharedPreferences ");
        return isFirstLaunch;
    }
}

