package org.creek.openhab.androidclient.services.email;

import java.io.IOException;
import java.util.List;

import org.creek.mailcontrol.model.message.GenericResponse;
import org.creek.mailcontrol.model.message.TransformException;
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
    
    public List<GenericResponse> receiveResponses() throws TransformException, ServiceException {
        Log.d(TAG, "receiveResponses()");
        List<GenericResponse> receivedResponses = emailSendingAndReceivingManager.receiveResponses();
        Log.d(TAG, "--------------receiveResponses: " + receivedResponses.size());
                
        return receivedResponses;
    }
}
