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
                " ( id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " username STRING UNIQUE,"
                + " password STRING, "
                + " salt STRING, "
                + " admin INTEGER )";
        db.execSQL(CREATE_USER_TABLE);

        // Create asset table
        String CREATE_ASSET_TABLE = "CREATE TABLE " + TBL_ASSETS_NAME +
                " ( id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "assetName STRING UNIQUE,  " +
                "amount INT)";
        db.execSQL(CREATE_ASSET_TABLE);

        // Add trigger table, not to be confused with tumblr
        String CREATE_MODFIICATION_TABLE =  "CREATE TABLE modifications(" +
                "tableName STRING NOT NULL PRIMARY KEY ON CONFLICT REPLACE," +
                "action TEXT NOT NULL," +
                "changedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
        db.execSQL(CREATE_MODFIICATION_TABLE);

        // ditto ^
        String CREATE_TRIGGER = "CREATE TRIGGER IF NOT EXISTS assetsOnUpdate AFTER UPDATE ON assets " +
                " BEGIN " +
                " INSERT INTO modifications (tableName, action) VALUES ('assets','UPDATE'); " +
                " END";
        db.execSQL(CREATE_TRIGGER);
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
        cv.put("password", a.getPassword());
        cv.put("salt", a.getSalt());
        cv.put("admin", a.getAdmin());

        long id = db.insert(TBL_ACCOUNTS_NAME, null, cv);
        db.close();
        return id;
    }

    public long changePassword(String username, String password, String salt)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put(TBL_ACCOUNTS_KEY_NAME, username);
        cv.put("password", password);
        cv.put("salt", salt);

        long id = db.update(TBL_ACCOUNTS_NAME, cv, "username='"+username+"'", null);

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
                a.setId(Integer.parseInt(cursor.getString(0)));
                a.setUsername(cursor.getString(1));
                accounts.add(a);
            } while (cursor.moveToNext());
        }

        return accounts;
    }

    public Account getAccountByUsername(String username)
    {
        String query = "SELECT username,  password, salt FROM " + TBL_ACCOUNTS_NAME + " WHERE username  =  '" + username + "' LIMIT 1";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        Account a = null;
        if(cursor.moveToFirst()) {
            do {
                a = new Account();
                //a.setId(Integer.parseInt(cursor.getString(0)));
                a.setUsername(cursor.getString(0));
                a.setPassword(cursor.getString(1));
                a.setSalt(cursor.getString(2));

            } while (cursor.moveToNext());
        }
        return a;
    }

    // Assets
    public long addAsset (Asset a)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("assetName", a.getAssetName());
        cv.put("amount", a.getAssetAmount());

        long id = db.insert(TBL_ASSETS_NAME, null, cv);
        db.close();
        return id;
    }

    public boolean removeAsset (int id)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        boolean result = db.delete(TBL_ASSETS_NAME, "id=" + id, null) > 0;
        db.close();
        return result;
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
                a.setAssetName(cursor.getString(1));
                a.setAssetAmount(Integer.parseInt(cursor.getString(2)));

                assets.add(a);
            } while (cursor.moveToNext());
        }

        return assets;
    }

    public long getLastAssetRecordTime()
    {
        // Return the last value from the modification table
        String query = "SELECT changedAt AS epoch FROM modifications DESC LIMIT 1";

        long time = -1;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()) {
            do {
                time = cursor.getLong(0);
            } while (cursor.moveToNext());
        }

        return time;
    }
}

