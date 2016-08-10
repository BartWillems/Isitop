package willems.bart.isitop;

import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by bart on 5/08/16.
 */
public class NotificationManager extends AppCompatActivity{
    private static final int uniqueID = 1337699001;

    public void sendBeerNotification(NotificationCompat.Builder notification, android.app.NotificationManager nm){
        notification.setSmallIcon(R.mipmap.ic_launcher);
        notification.setWhen(System.currentTimeMillis());
        synchronized (nm){
            notification.setOngoing(true); // can not swipe until you buy the bear
            nm.notify(uniqueID, notification.build());
        }

    }

    protected void removeBeerNotification(android.app.NotificationManager nm){
        String ns = Context.NOTIFICATION_SERVICE;
        nm.cancel(uniqueID);
    }
}
