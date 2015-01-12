package org.creek.openhab.androidclient.services.email;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.creek.mailcontrol.model.message.GenericResponse;
import org.creek.mailcontrol.model.message.ItemStateResponseMessage;
import org.creek.mailcontrol.model.message.ItemsStateResponseMessage;
import org.creek.openhab.androidclient.OpenHABClientApplication;
import org.creek.openhab.androidclient.activity.items.ItemsActivity;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * 
 * @author Andrey Pereverzin
 */
public class EmailSendingAndReceivingService extends Service {
    private static final String TAG = EmailSendingAndReceivingService.class.getSimpleName();

    private Timer timer;
    protected ContentResolver contentResolver;

    private static final int MILLISECONDS_IN_MINUTE = 60 * 1000;
    // TODO make configurable
    private int timeoutInMinutes = 1;
    private int timeoutMs = timeoutInMinutes * MILLISECONDS_IN_MINUTE;

    private static final String TIMER_NAME = "OpenHABClientEmailReceivingTimer";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "-=-=-=-=-=-=-=-=-=-=-onCreate");
        contentResolver = getContentResolver();

        timer = new Timer(TIMER_NAME);
        timer.schedule(emailSendingAndReceivingTask, 1000L, timeoutMs);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
        timer = null;
    }

    private TimerTask emailSendingAndReceivingTask = new TimerTask() {
        @Override
        public void run() {
            Log.i(TAG, "===================EmailSendingAndReceivingService doing work: " + OpenHABClientApplication.isEnabled());
            if (OpenHABClientApplication.isEnabled()) {
                try {
                    EmailSendingAndReceivingManager emailSendingAndReceivingManager = new EmailSendingAndReceivingManager();

                    Log.d(TAG, "===================EmailSendingAndReceivingService sending");
                    EmailSender emailSender = new EmailSender(emailSendingAndReceivingManager);
                    emailSender.sendRequestsAndResponses();

                    Log.d(TAG, "===================EmailSendingAndReceivingService receiving");
                    EmailReceiver emailReceiver = new EmailReceiver(emailSendingAndReceivingManager);
                    List<GenericResponse> responses = emailReceiver.receiveResponses();
                    if (responses.size() > 0) {
                        for (int i = 0; i < responses.size(); i++) {
                            GenericResponse response = responses.get(i);
                            if (response instanceof ItemsStateResponseMessage) {
                                ItemsStateResponseMessage message = (ItemsStateResponseMessage) response;
                                OpenHABClientApplication.setItemStates(message.getItemStates());
                                ItemsActivity itemsActivity = OpenHABClientApplication.getItemsActivity();
                                Log.d(TAG, "listAdapter is to be refreshed");
                                if (itemsActivity != null) {
                                    Log.d(TAG, "refreshing listAdapter");
                                    itemsActivity.refreshListAdapter();
                                }
                            } else if (response instanceof ItemStateResponseMessage) {
                                ItemStateResponseMessage message = (ItemStateResponseMessage) response;
                            }
                        }
                    }
                } catch (Throwable ex) {
                    Log.e(TAG, ex.getMessage(), ex);
                    // showException(EmailSendingAndReceivingService.this, ex);
                }
            }
        }
    };
}
