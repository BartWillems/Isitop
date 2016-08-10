package willems.bart.isitop;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.List;
import java.util.concurrent.ThreadFactory;

import willems.bart.isitop.models.Asset;
import willems.bart.isitop.sqlite.MySQLiteOpenHelper;

public class MenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public String username = "";
    public final static String LOGIN_USERNAME = "willems.bart.isitop.MenuActivity";
    private MySQLiteOpenHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Done drawing, now do stuff!
        Intent intent = getIntent();

        String addBeerResult = intent.getStringExtra(AddBeer.ADD_BEER);
        if(addBeerResult != null){
            TextView orderResult = (TextView) findViewById(R.id.orderResultLabel);
            orderResult.setText(addBeerResult);
            addBeerResult = null;
        }

        username = intent.getStringExtra(MainActivity.LOGIN_USERNAME);
        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
        View hView = navigationView.getHeaderView(0);
        TextView usernameText = (TextView) hView.findViewById(R.id.username);
        usernameText.setText(username);

        // Display beers
        db = new MySQLiteOpenHelper(this);
        List<Asset> assets = db.getAssets();
        TableLayout ll = (TableLayout) findViewById(R.id.orderTable);

        TextView beer;
        Button beerBtn;
        int count = 0;
        for(Asset a : assets){
            TableRow row = new TableRow(this);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
            row.setLayoutParams(lp);


            beerBtn = new Button(this);
            beerBtn.setId(a.getId());
            beerBtn.setText(R.string.beerBoughtBtn);

            beer = new TextView(this);
            beer.setTextColor(Color.BLACK);
            beer.setText(a.getAssetName() + " " + a.getAssetAmount() + "L");
            beer.setId(a.getId());
            beer.setGravity(Gravity.LEFT);

            beerBtn.setOnClickListener(new Button.OnClickListener(){
                public void onClick(View v){
                    // delete beer
                    db.removeAsset(v.getId());
                    finish();
                    startActivity(getIntent());
                }
            });

            row.addView(beer,(new TableRow.LayoutParams(0, ActionBar.LayoutParams.WRAP_CONTENT, 0.8f)));
            row.addView(beerBtn,(new TableRow.LayoutParams(0, ActionBar.LayoutParams.WRAP_CONTENT, 0.8f)));
            //row.addView(beerBtn);
            ll.addView(row,count);
            count++;
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            setResult(0);
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent = null;

        if (id == R.id.nav_addBeer) {
            intent = new Intent(this, AddBeer.class);
            startActivityForResult(intent, 0);
        } else if (id == R.id.nav_accountSettings) {
            intent = new Intent(this, AccountSettings.class);
            intent.putExtra(LOGIN_USERNAME, username);
            startActivityForResult(intent, 0);
        } else if (id == R.id.nav_addAccount) {

        } else if (id == R.id.nav_logOut) {
            logOut();
            setResult(1);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void logOut() {
        SharedPreferences launchPref = getSharedPreferences("isLoggedIn", 0);
        SharedPreferences.Editor editor = launchPref.edit();
        editor.putBoolean("isLoggedIn",false);
        editor.putString("username","");
        editor.commit();
    }
}
