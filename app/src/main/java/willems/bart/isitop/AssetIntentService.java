package willems.bart.isitop;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import willems.bart.isitop.sqlite.MySQLiteOpenHelper;

/**
 * Created by bart on 5/08/16.
 */
public class AssetIntentService extends Service {

    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;
    private static final String TAG = "willems.bart.isitop";
    private MySQLiteOpenHelper db;
    private SQLiteDatabase sqLiteDatabase;


    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper){
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            try {
                Thread.sleep(500);
                long timestamp = db.getLastAssetRecordTime();
                boolean success = false;
                if(timestamp >= 0)
                    success = true;
                else
                    success = false;
                willems.bart.isitop.NotificationManager n = new willems.bart.isitop.NotificationManager();
                n.sendBeerNotification(success, MenuActivity.notification, MenuActivity.nm);
            } catch(InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            stopSelf(msg.arg1);
        }
    }



    protected void onHandleIntent(Intent intent) {
        // Warn users when we run out of beer!
        Log.i(TAG, "The service is started");
    }

    @Override
    public void onCreate() {
        db = new MySQLiteOpenHelper(this);
        HandlerThread thread = new HandlerThread("THREAD_PRIORITY_BACKGROUND");
        thread.start();

        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();

        Message msg = mServiceHandler.obtainMessage();
        msg.arg1 = startId;
        mServiceHandler.sendMessage(msg);

        // If we get killed, after returning from here, restart
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
    }
}
