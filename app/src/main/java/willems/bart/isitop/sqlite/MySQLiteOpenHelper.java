package willems.bart.isitop.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;

import willems.bart.isitop.models.Account;
import willems.bart.isitop.models.Asset;


/**
 * Created by bart on 4/08/16.
 */
public class MySQLiteOpenHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 7;
    // Database Name
    private static final String DATABASE_NAME = "OpAppDB";
    private static final String TBL_ACCOUNTS_KEY_NAME = "username";
    private static final String TBL_ACCOUNTS_NAME = "accounts";
    private static final String TBL_ASSETS_KEY_NAME = "asset_name";
    private static final String TBL_ASSETS_NAME = "assets";

    public MySQLiteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create user table
        String CREATE_USER_TABLE = "CREATE TABLE " + TBL_ACCOUNTS_NAME +
                " ( "
                + " username STRING PRIMARY KEY,"
                + " password STRING, "
                + " salt STRING, "
                + " admin INTEGER )";
        db.execSQL(CREATE_USER_TABLE);

        // Insert default value
        String INSERT_ADMIN_ACCOUNT = "INSERT INTO " + TBL_ACCOUNTS_NAME + " VALUES ('admin','43903b84d9ee3db3e2b6048728588f877d9ea07e430a506c0585ead964e4d0b157e301b319de8ff3d1ad7621deff1e2c3679febd97f586e8746d7f25e6b14ab7','saltySaltOhManSoSalty','1');";
        db.execSQL(INSERT_ADMIN_ACCOUNT);

        // Create asset table
        String CREATE_ASSET_TABLE = "CREATE TABLE " + TBL_ASSETS_NAME + " ( id INTEGER PRIMARY KEY AUTOINCREMENT, " + TBL_ASSETS_KEY_NAME +  "STRING ,  amount INT)";
        db.execSQL(CREATE_ASSET_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS" + TBL_ACCOUNTS_NAME);
        db.execSQL("DROP TABLE IF EXISTS" + TBL_ASSETS_NAME);

        this.onCreate(db);
    }

    public long addAccount(Account a)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(TBL_ACCOUNTS_KEY_NAME, a.getUsername());

        long id = db.insert(TBL_ACCOUNTS_NAME, null, cv);
        db.close();
        return id;
    }

    public List<Account> getAccounts()
    {
        List<Account> accounts = new LinkedList<Account>();

        String query = "SELECT username, FROM " + TBL_ACCOUNTS_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        Account a = null;
        if(cursor.moveToFirst()) {
            do {
                a = new Account();
                a.setUsername(cursor.getString(0));
                accounts.add(a);
            } while (cursor.moveToNext());
        }

        return accounts;
    }

    public Account getAccountByUsername(String username)
    {
        String query = "SELECT username,  password, salt FROM " + TBL_ACCOUNTS_NAME + " WHERE username=" + username + " LIMIT 1";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        Account a = null;
        if(cursor.moveToFirst()) {
            do {
                a = new Account();
                a.setUsername(cursor.getString(0));
                a.setPassword(cursor.getString(1));
                a.setSalt(cursor.getString(2));
                Log.d("Got account? ",a.toString()); //No account??

            } while (cursor.moveToNext());
        }
        return a;
    }

    // Assets
    public long addAsset (Asset a)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(TBL_ASSETS_KEY_NAME, a.getAssetName());

        long id = db.insert(TBL_ASSETS_NAME, null, cv);
        db.close();
        return id;
    }

    public List<Asset> getAssets()
    {
        List<Asset> assets = new LinkedList<Asset>();

        String query = "SELECT * FROM " + TBL_ASSETS_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        Asset a = null;
        if(cursor.moveToFirst()) {
            do {
                a = new Asset();
                a.setId(Integer.parseInt(cursor.getString(0)));

                assets.add(a);
            } while (cursor.moveToNext());
        }

        return assets;
    }
}

