 package org.creek.openhab.androidclient.services.receivers;

import org.creek.openhab.androidclient.services.email.EmailSendingAndReceivingService;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * 
 * @author Andrey Pereverzin
 */
public class BootReceiver extends android.content.BroadcastReceiver {
    private static final String TAG = BootReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context ctx, Intent intent) {
        Log.d(TAG, "-=-=-=-=-=-=-=-=-=-=-onReceive");
        
        startService(ctx, EmailSendingAndReceivingService.class);
    }
    
    private void startService(Context context, @SuppressWarnings("rawtypes") Class clazz) {
        Intent serviceIntent = new Intent(clazz.getName());
        context.startService(serviceIntent);
    }
}
