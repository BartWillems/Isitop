package willems.bart.isitop;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import willems.bart.isitop.models.Account;
import willems.bart.isitop.sqlite.MySQLiteOpenHelper;

public class AccountSettings extends AppCompatActivity {
    private MySQLiteOpenHelper db;
    private SQLiteDatabase sqLiteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);
        db = new MySQLiteOpenHelper(this);
    }

    public void changePassword(View view){
        Intent intent = getIntent();
        String username = intent.getStringExtra(MenuActivity.LOGIN_USERNAME);

        EditText currentPasswordText = (EditText) findViewById(R.id.currentPassword);
        String currentPassword = currentPasswordText.getText().toString();

        EditText newPasswordText = (EditText) findViewById(R.id.newPassword);
        String newPassword = newPasswordText.getText().toString();

        EditText passwordRepeatText = (EditText) findViewById(R.id.repeatPassword);
        String passwordRepeat = passwordRepeatText.getText().toString();

        // Check if the user entered the right password
        Account account = db.getAccountByUsername(username);
        if(account != null){
            String oldPasswordSalt = Functions.getSHA512Password(
                    currentPassword,
                    account.getSalt()
            );
            if(account.getPassword().equals(oldPasswordSalt)){
                // Check if the new passwords match
                if(newPassword.equals(passwordRepeat)){
                    String salt = Functions.generateSalt();
                    String passwordSalt = Functions.getSHA512Password(newPassword, salt);
                    long result  = db.changePassword(username, passwordSalt, salt);

                    Toast.makeText(this, getResources().getString(R.string.passwordChangedSuccess),
                            Toast.LENGTH_LONG).show();
                    // Return to the menu
                    setResult(RESULT_OK, null);
                    finish();
                } else {
                    //passwords don't match
                    Toast.makeText(this, getResources().getString(R.string.passwordChangedMismatch),
                            Toast.LENGTH_LONG).show();
                    newPasswordText.setTextColor(Color.RED);
                    passwordRepeatText.setTextColor(Color.RED);
                }
            } else {
                // wrong password
                Toast.makeText(this, getResources().getString(R.string.passwordChangedWrong),
                        Toast.LENGTH_LONG).show();
                currentPasswordText.setTextColor(Color.RED);
            }
        } else {
            Toast.makeText(this, getResources().getString(R.string.passwordChangedFubar),
                    Toast.LENGTH_SHORT).show();
        }
    }
}
