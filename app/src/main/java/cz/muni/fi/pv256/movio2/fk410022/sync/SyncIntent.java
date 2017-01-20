package cz.muni.fi.pv256.movio2.fk410022.sync;

import android.content.Context;

public class SyncIntent {
    private Context context;

    /**
     * Helper class to have the sync adapter sync immediately
     *
     * @param context The context used to access the account service
     */
    public SyncIntent(Context context) {
        this.context = context.getApplicationContext();
    }

    public void sync() {
        SyncAdapter.syncImmediately(context);
    }
}
