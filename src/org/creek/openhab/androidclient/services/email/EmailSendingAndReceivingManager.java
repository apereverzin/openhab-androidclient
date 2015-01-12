package org.creek.openhab.androidclient.services.email;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import java.util.Properties;

import android.util.Log;

import org.creek.accessemail.connector.mail.ConnectorException;
import org.creek.accessemail.connector.mail.MailConnector;
import org.creek.mailcontrol.model.message.GenericRequest;
import org.creek.mailcontrol.model.message.GenericResponse;
import org.creek.mailcontrol.model.message.TransformException;
import org.creek.openhab.androidclient.MailAccountPropertiesProvider;
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
        MailConnector connector = new MailConnector(mailProps);
        try {
            connector.checkSMTPConnection();
        } catch (ConnectorException ex) {
            Log.e(TAG, "Cannot establish SMTP connection", ex);
        }
        try {
            connector.checkPOP3Connection();
        } catch (ConnectorException ex) {
            Log.e(TAG, "Cannot establish POP3 connection", ex);
        }
        messagesService = new MessagesService(connector);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream(4096);
        PrintStream ps = new PrintStream(baos);
        mailProps.list(ps);
        Log.i(TAG, "=====================");
        Log.i(TAG, new String(baos.toByteArray()));
        Log.i(TAG, "=====================");
    }

    public void sendMessage(GenericRequest request) throws ServiceException {
        Log.d(TAG, "sendRequest()");
        request.setTimeSent(System.currentTimeMillis());
        String message = request.toJSON().toJSONString();
        Log.d(TAG, "--------------sendMessage: " + message);
        messagesService.sendRequest(message);
        Log.d(TAG, "-------------sendMessage - message sent: " + message);
    }
    
    public List<GenericResponse> receiveResponses() throws ServiceException, TransformException {
        Log.d(TAG, "receiveMessages()");
        List<GenericResponse> messages = messagesService.receiveResponses();
        Log.d(TAG, "--------------receiveMessages: " + messages.size());
        return messages;
    }
}
