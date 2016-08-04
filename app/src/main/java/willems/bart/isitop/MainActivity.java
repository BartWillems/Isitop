package willems.bart.isitop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;


public class MainActivity extends AppCompatActivity {
    public final static String LOGIN_USERNAME = "com.example.Isitop.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void login(View view)
    {
        EditText editText = (EditText) findViewById(R.id.login_username);
        String message = editText.getText().toString();

        if(!message.equals(null) && !message.isEmpty()) {
            Intent intent = new Intent(this, LoginActivity.class);

            intent.putExtra(LOGIN_USERNAME, message);
            startActivity(intent);
        }
    }
}

