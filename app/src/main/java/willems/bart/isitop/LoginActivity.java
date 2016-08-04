package willems.bart.isitop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.LOGIN_USERNAME);
        TextView welcome_label =  (TextView) findViewById(R.id.welcome_label_id);
        welcome_label.setText(welcome_label.getText() + " " + message);
    }
}
