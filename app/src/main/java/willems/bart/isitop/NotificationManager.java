package willems.bart.isitop;

import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by bart on 5/08/16.
 */
public class NotificationManager extends AppCompatActivity{
    NotificationCompat.Builder notification;
    private static final int uniqueID = 1337699001;

    protected void sendBeerNotification(boolean success, NotificationCompat.Builder notification, android.app.NotificationManager nm){
        notification.setSmallIcon(R.mipmap.ic_launcher);
        notification.setWhen(System.currentTimeMillis());

        if(success){
            notification.setTicker("Uh oh");
            notification.setContentTitle("Something went wrong...");
            notification.setContentText("Please call your local open source developer");
        } else {
            notification.setTicker("BEER");
            notification.setContentTitle("NO MORE BEER");
            notification.setContentText("It seems like we ran out of beer :o Go to the store!");
        }
        synchronized (nm){
            nm.notify(uniqueID, notification.build());
        }

    }
}
