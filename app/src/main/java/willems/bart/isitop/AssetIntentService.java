package willems.bart.isitop;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

/**
 * Created by bart on 5/08/16.
 */
public class AssetIntentService extends IntentService{

    private static final String TAG = "willems.bart.isitop";

    public AssetIntentService() {
        super("AssetIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // Warn users when we run out of beer!
        Log.i(TAG, "The service is started");
    }
}
