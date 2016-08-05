package willems.bart.isitop;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import willems.bart.isitop.models.Asset;
import willems.bart.isitop.sqlite.MySQLiteOpenHelper;

public class AddBeer extends AppCompatActivity {
    public final static String ADD_BEER = "willems.bart.isitop.addBeer";
    private MySQLiteOpenHelper db;
    private SQLiteDatabase sqLiteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_beer);

        db = new MySQLiteOpenHelper(this);
    }

    public void addOrder(View view) {
        Asset asset = new Asset();

        EditText beerNameTxt = (EditText) findViewById(R.id.beerName);
        String beerName = beerNameTxt.getText().toString();

        EditText beerAmountTxt = (EditText) findViewById(R.id.beerAmount);

        int beerAmount = Integer.parseInt(beerAmountTxt.getText().toString());

        asset.setAssetName(beerName);
        asset.setAssetAmount(beerAmount);

        long result = db.addAsset(asset);
        Intent intent = new Intent(this, MenuActivity.class);
        if(result >= 0){
            intent.putExtra(ADD_BEER, "Beer added successfully!");
        } else {
            intent.putExtra(ADD_BEER, "There was a problem adding your beer... You had one too many?");
        }

        startActivity(intent);
    }
}
