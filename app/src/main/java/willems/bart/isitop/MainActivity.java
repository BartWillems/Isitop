package willems.bart.isitop;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
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
            Account a =  new Account();
            a.setUsername("admin");
            a.setPassword("43903b84d9ee3db3e2b6048728588f877d9ea07e430a506c0585ead964e4d0b157e301b319de8ff3d1ad7621deff1e2c3679febd97f586e8746d7f25e6b14ab7");
            a.setSalt("saltySaltOhManSoSalty");
            a.setAdmin(1);
            db.addAccount(a);
        }
    }

    public void login(View view)
    {
        EditText usernameText = (EditText) findViewById(R.id.login_username);
        String loginUsername = usernameText.getText().toString();

        EditText passwordText = (EditText) findViewById(R.id.login_password);
        String loginPassword = passwordText.getText().toString();

        //In case the user enters wrong info
        TextView username_label =  (TextView) findViewById(R.id.username_label);
        username_label.setTextColor(Color.RED);

        TextView password_label =  (TextView) findViewById(R.id.password_label);
        password_label.setTextColor(Color.RED);

        if(!loginUsername.equals(null) && !loginUsername.isEmpty())
        {
            if(!loginPassword.equals(null) && !loginPassword.isEmpty())
            {
                db = new MySQLiteOpenHelper(this);
                Account account = db.getAccountByUsername(loginUsername);
                if(account != null)
                {
                    username_label.setText("");
                    String result = getSHA512Password(
                            loginPassword,
                            account.getSalt()
                    );
                    if(result != null && result.equals(account.getPassword()))
                    {
                        // User is authenticated
                        Intent intent = new Intent(this, MenuActivity.class);

                        intent.putExtra(LOGIN_USERNAME, loginUsername);
                        startActivity(intent);
                    } else {
                        password_label.setText("ERROR: Wrong password");
                    }
                } else {
                    username_label.setText("ERROR: Unknown username");
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

