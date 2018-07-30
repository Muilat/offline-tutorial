package com.muilat.android.offlinetutorial.sync;

import android.app.Service;
        import android.content.Intent;
        import android.os.IBinder;
        import android.util.Log;

/**
 * Created by delaroy on 10/18/17.
 */

public class OfflineTutorialSyncService extends Service {
    private static final Object sSyncAdapterLock = new Object();
    private static OfflineTutorialSyncAdapter eFragranceSyncAdapter = null;

    @Override
    public void onCreate() {
        Log.d("MovieSyncService", "onCreate - MovieSyncService");
        synchronized (sSyncAdapterLock) {
            if (eFragranceSyncAdapter == null) {
                eFragranceSyncAdapter = new OfflineTutorialSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return eFragranceSyncAdapter.getSyncAdapterBinder();
    }
}
