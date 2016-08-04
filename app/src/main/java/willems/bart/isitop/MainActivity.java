package willems.bart.isitop;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import willems.bart.isitop.models.Account;
import willems.bart.isitop.sqlite.MySQLiteOpenHelper;


public class MainActivity extends AppCompatActivity {
    public final static String LOGIN_USERNAME = "com.example.Isitop.MESSAGE";
    private MySQLiteOpenHelper  db;
    private SQLiteDatabase sqLiteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new MySQLiteOpenHelper(this);
        if(isFirstLaunch())
        {
            //db.onCreate(sqLiteDatabase);
        }
    }

    public void login(View view)
    {
        EditText usernameText = (EditText) findViewById(R.id.login_username);
        String loginUsername = usernameText.getText().toString();

        EditText passwordText = (EditText) findViewById(R.id.login_password);
        String loginPassword = passwordText.getText().toString();

        if(!loginUsername.equals(null) && !loginUsername.isEmpty())
        {
            if(!loginPassword.equals(null) && !loginPassword.isEmpty())
            {
                db = new MySQLiteOpenHelper(this);
                Account account = db.getAccountByUsername(loginUsername); //crashes the coed
                if(account != null)
                {
                    String result = getSHA512Password(
                            loginPassword,
                            account.getSalt()
                    );
                    if(result != null && result.equals(account.getPassword()))
                    {
                        // User is authenticated
                        Intent intent = new Intent(this, LoginActivity.class);

                        intent.putExtra(LOGIN_USERNAME, loginUsername);
                        startActivity(intent);
                    }
                }
            }
        }
    }

    private boolean isFirstLaunch() {

        boolean isFirstLaunch = false;

        SharedPreferences launchPref = getSharedPreferences("hasLaunched", 0);

        if(!launchPref.getBoolean("hasLaunched",false))
        {
            SharedPreferences.Editor editor = launchPref.edit();
            editor.putBoolean("hasLaunched",true);
            editor.commit();
            isFirstLaunch = true;
        }
        return isFirstLaunch;
    }

    // Returns either SHA512 or null
    public String getSHA512Password(String passwordToHash, String salt){
        String passwordResult  = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(salt.getBytes());
            byte[] bytes = md.digest(passwordToHash.getBytes());
            StringBuilder sb = new StringBuilder();
            for(int i = 0; i < bytes.length; i++) {
               sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            passwordResult = sb.toString();
        } catch(NoSuchAlgorithmException e) {
             e.printStackTrace();
        }
        return passwordResult;
    }
}

