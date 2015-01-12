package org.creek.openhab.androidclient.service;

import static org.creek.openhab.androidclient.OpenHABClientApplication.getOpenHABServerEmailAddress;
import static org.creek.openhab.androidclient.OpenHABClientApplication.getSenderEmailAddress;

import java.util.ArrayList;
import java.util.List;

import org.creek.accessemail.connector.mail.ConnectorException;
import org.creek.accessemail.connector.mail.MailConnector;
import org.creek.mailcontrol.model.message.GenericResponse;
import org.creek.mailcontrol.model.message.GenericResponseTransformer;
import org.creek.mailcontrol.model.message.TransformException;

import android.util.Log;

/**
 * 
 * @author Andrey Pereverzin
 */
public class MessagesService {
    private static final String TAG = MessagesService.class.getSimpleName();
    private MailConnector mailConnector;
    private final GenericResponseTransformer responseTransformer;
    
    private static final String REQUEST_SUBJECT = "OpenHABRequest";
    private static final String RESPONSE_SUBJECT = "OpenHABResponse";

    public MessagesService(MailConnector mailConnector) {
        this.mailConnector = mailConnector;
        this.responseTransformer = new GenericResponseTransformer();
    }

    public void sendRequest(String request) throws ServiceException {
        try {
            Log.d(TAG, "sendRequest: " + request);
            Log.d(TAG, "from: " + getSenderEmailAddress());
            Log.d(TAG, "to: " + getOpenHABServerEmailAddress());
            mailConnector.sendMessage(REQUEST_SUBJECT, getSenderEmailAddress(), request, getOpenHABServerEmailAddress());
            Log.d(TAG, "sendRequest - request sent: " + request);
        } catch(ConnectorException ex) {
            Log.e(TAG, ex.getMessage(), ex);
            throw new ServiceException(ex);
        }
    }
    
    public List<GenericResponse> receiveResponses() throws TransformException, ServiceException {
        try {
            List<GenericResponse> responses = new ArrayList<GenericResponse>();
            List<Object> messages = mailConnector.receiveMessages(RESPONSE_SUBJECT);
            
            for (int i = 0; i < messages.size(); i++) {
                Object msg = messages.get(i);
                if (msg instanceof String) {
                    GenericResponse contactLocationData = (GenericResponse)responseTransformer.transform((String)msg);
                    responses.add(contactLocationData);
                }
            }
            
            return responses;
        } catch(ConnectorException ex) {
            throw new ServiceException(ex);
        }
    }
}
