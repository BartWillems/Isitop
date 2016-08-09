package willems.bart.isitop;

import android.app.Notification;
import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by bart on 5/08/16.
 */
public class NotificationManager extends AppCompatActivity{
    NotificationCompat.Builder notification;
    private static final int uniqueID = 1337699001;

    protected void sendBeerNotification(NotificationCompat.Builder notification, android.app.NotificationManager nm){
        notification.setSmallIcon(R.mipmap.ic_launcher);
        notification.setWhen(System.currentTimeMillis());

        notification.setTicker("BEER");
        notification.setContentTitle("NO MORE BEER");
        notification.setContentText("It seems like we ran out of beer :o Go to the store!");

        synchronized (nm){
            notification.setOngoing(true); // can not swipe until you buy the bear
            nm.notify(uniqueID, notification.build());
        }

    }

    protected void removeBeerNotification(NotificationCompat.Builder notification, android.app.NotificationManager nm){
        String ns = Context.NOTIFICATION_SERVICE;
        nm.cancel(uniqueID);
    }
}
