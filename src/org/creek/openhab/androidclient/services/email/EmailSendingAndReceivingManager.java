package org.creek.openhab.androidclient.services.email;

import java.io.IOException;
import java.util.Properties;
import java.util.Set;

import android.util.Log;

import org.creek.accessemail.connector.mail.MailConnector;
import org.creek.mailcontrol.model.message.TransformException;
import org.creek.openhab.androidclient.MailAccountPropertiesProvider;
import org.creek.openhab.androidclient.domain.Request;
import org.creek.openhab.androidclient.domain.Response;
import org.creek.openhab.androidclient.service.MessagesService;
import org.creek.openhab.androidclient.service.ServiceException;
import org.creek.openhab.androidclient.util.CryptoException;

/**
 * 
 * @author Andrey Pereverzin
 */
public class EmailSendingAndReceivingManager {
    private static final String TAG = EmailSendingAndReceivingManager.class.getSimpleName();
    
    private final MessagesService messagesService;
    private final Properties mailProps;
    
    public EmailSendingAndReceivingManager() throws CryptoException, IOException {
        mailProps = MailAccountPropertiesProvider.getInstance().getMailProperties();
        messagesService = new MessagesService(new MailConnector(mailProps));
    }

    public void sendMessage(Request request) throws ServiceException {
        Log.d(TAG, "sendRequest()");
        request.setTimeSent(System.currentTimeMillis());
        Log.d(TAG, "--------------sendMessage: " + request.getMessage());
        messagesService.sendRequest(request.getMessage());
        Log.d(TAG, "--------------sendMessage: " + request.getMessage());
    }
    
    public Set<Response> receiveResponses() throws ServiceException, TransformException {
        Log.d(TAG, "receiveMessages()");
        Set<Response> messages = messagesService.receiveResponses();
        Log.d(TAG, "--------------receiveMessages: " + messages.size());
        return messages;
    }
}
