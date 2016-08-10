package willems.bart.isitop;

import android.app.*;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import willems.bart.isitop.models.Account;
import willems.bart.isitop.sqlite.MySQLiteOpenHelper;


public class MainActivity extends AppCompatActivity {
    public final static String LOGIN_USERNAME = "willems.bart.isitop.MainActivity";
    private MySQLiteOpenHelper  db;
    static NotificationCompat.Builder notification;
    static android.app.NotificationManager nm;
    private static final int uniqueID = 1337699001;


    public void startBeerService(android.app.NotificationManager nm){
        notification = new NotificationCompat.Builder(this);
        notification.setAutoCancel(false); // Show notification only once
        Intent serviceIntent = new Intent(this, AssetIntentService.class);
        startService(serviceIntent);
    }

    public void stopBeerService(android.app.NotificationManager notification){
       Intent serviceIntent = new Intent(this, AssetIntentService.class);
        stopService(serviceIntent);
        notification.cancel(uniqueID);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new MySQLiteOpenHelper(this);


        nm = (android.app.NotificationManager) getSystemService(NOTIFICATION_SERVICE);


        if(isLoggedIn()){
            startBeerService(nm);
            Intent intent = new Intent(this, MenuActivity.class);
            intent.putExtra(LOGIN_USERNAME, whoIsLoggedIn());
            startActivity(intent);
        }

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
                    String result = Functions.getSHA512Password(
                            loginPassword,
                            account.getSalt()
                    );
                    if(result != null && result.equals(account.getPassword()))
                    {
                        logIn(loginUsername);
                        startBeerService(nm);

                        // User is authenticated
                        Intent intent = new Intent(this, MenuActivity.class);

                        intent.putExtra(LOGIN_USERNAME, loginUsername);
                        startActivityForResult(intent, 0);
                    } else {
                        password_label.setText(getResources().getString(R.string.wrongPasswordLabel));
                    }
                } else {
                    username_label.setText(getResources().getString(R.string.missingUserLabel));
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == 0) {
            finish();
        } else {
            stopBeerService(nm);
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

    public void logIn(String username){
        SharedPreferences launchPref = getSharedPreferences("isLoggedIn", 0);
        SharedPreferences.Editor editor = launchPref.edit();
        editor.putString("username",username);
        editor.putBoolean("isLoggedIn",true);
        editor.commit();
    }

    public boolean isLoggedIn() {
        boolean isLoggedIn = false;
        SharedPreferences launchPref = getSharedPreferences("isLoggedIn", 0);
        SharedPreferences.Editor editor = launchPref.edit();
        if(launchPref.getBoolean("isLoggedIn",false)){
            editor.putBoolean("isLoggedIn",true);
            isLoggedIn = true;
        } else {
            editor.putBoolean("isLoggedIn",false);
            isLoggedIn = false;
        }
        editor.commit();
        return isLoggedIn;
    }

    public String whoIsLoggedIn(){
        SharedPreferences launchPref = getSharedPreferences("isLoggedIn", 0);
        return launchPref.getString("username", "unknown user");
    }

}

