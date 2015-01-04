package org.creek.openhab.androidclient.services.email;

import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.creek.openhab.androidclient.domain.Response;

import static org.creek.openhab.androidclient.util.ActivityUtil.showException;

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
            Log.i(TAG, "===================EmailSendingAndReceivingService doing work");
            try {
                EmailSendingAndReceivingManager emailSendingAndReceivingManager = new EmailSendingAndReceivingManager();
                
                Log.d(TAG, "===================EmailSendingAndReceivingService sending");
                EmailSender emailSender = new EmailSender(emailSendingAndReceivingManager);
                emailSender.sendRequestsAndResponses();
                
                Log.d(TAG, "===================EmailSendingAndReceivingService receiving");
                EmailReceiver emailReceiver = new EmailReceiver(emailSendingAndReceivingManager);
                Set<Response> responses = emailReceiver.receiveResponses();
                if (responses.size() > 0) {
                    //
                }
            } catch(Throwable ex) {
                showException(EmailSendingAndReceivingService.this, ex);
            }
        }
    };
}
