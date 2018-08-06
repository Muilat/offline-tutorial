package com.muilat.android.offlinetutorial.sync;

    import android.content.BroadcastReceiver;
        import android.content.Context;
        import android.content.Intent;


public class OfflineTutorialBroadcastReceiver extends BroadcastReceiver {
    static int noOfTimes = 0;

    // Method gets called when Broad Case is issued from MainActivity for every 10 seconds
    @Override
    public void onReceive(final Context context, Intent intent) {
        Intent intentToSyncImmediately = new Intent(context, OfflineTutorialSyncIntentService.class);
        context.startService(intentToSyncImmediately);
    }
}
