package org.creek.openhab.androidclient.services.email;

import java.io.IOException;
import java.util.Set;

import org.creek.mailcontrol.model.message.TransformException;
import org.creek.openhab.androidclient.domain.Response;
import org.creek.openhab.androidclient.service.ServiceException;
import org.creek.openhab.androidclient.util.CryptoException;

import android.util.Log;

/**
 * 
 * @author Andrey Pereverzin
 */
public class EmailReceiver {
    private static final String TAG = EmailReceiver.class.getSimpleName();
    private final EmailSendingAndReceivingManager emailSendingAndReceivingManager;
    
    public EmailReceiver(EmailSendingAndReceivingManager emailSendingAndReceivingManager) throws IOException, CryptoException {
        this.emailSendingAndReceivingManager = emailSendingAndReceivingManager;
    }
    
    public Set<Response> receiveResponses() throws TransformException, ServiceException {
        Log.d(TAG, "receiveResponses()");
        Set<Response> receivedResponses = emailSendingAndReceivingManager.receiveResponses();
        Log.d(TAG, "--------------receiveResponses: " + receivedResponses.size());
                
        return receivedResponses;
    }
}
