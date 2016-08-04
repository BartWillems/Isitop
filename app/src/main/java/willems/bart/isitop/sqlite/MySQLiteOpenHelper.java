package willems.bart.isitop.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


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
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
