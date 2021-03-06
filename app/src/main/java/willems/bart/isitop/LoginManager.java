package willems.bart.isitop;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by bart on 6/08/16.
 */
public class LoginManager extends AppCompatActivity{

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

    public void logOut() {
        SharedPreferences launchPref = getSharedPreferences("isLoggedIn", 0);
        SharedPreferences.Editor editor = launchPref.edit();
        editor.putBoolean("isLoggedIn",false);
        editor.putString("username","");
        editor.commit();
    }
}
