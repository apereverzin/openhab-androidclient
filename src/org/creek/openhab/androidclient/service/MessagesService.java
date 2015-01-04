package org.creek.openhab.androidclient.service;

import static org.creek.openhab.androidclient.OpenHABClientApplication.getOpenHABServerEmailAddress;
import static org.creek.openhab.androidclient.OpenHABClientApplication.getSenderEmailAddress;

import java.util.HashSet;
import java.util.Set;

import org.creek.accessemail.connector.mail.ConnectorException;
import org.creek.accessemail.connector.mail.MailConnector;
import org.creek.mailcontrol.model.message.GenericResponseTransformer;
import org.creek.mailcontrol.model.message.TransformException;
import org.creek.openhab.androidclient.OpenHABClientApplication;
import org.creek.openhab.androidclient.domain.Response;


/**
 * 
 * @author Andrey Pereverzin
 */
public class MessagesService {
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
            mailConnector.sendMessage(REQUEST_SUBJECT, getSenderEmailAddress(), request, getOpenHABServerEmailAddress());
        } catch(ConnectorException ex) {
            throw new ServiceException(ex);
        }
    }
    
    public Set<Response> receiveResponses() throws TransformException, ServiceException {
        try {
            Set<Response> responses = new HashSet<Response>();
            Set<Object> messages = mailConnector.receiveMessages(RESPONSE_SUBJECT);
            
            for (Object msg: messages) {
                if (msg instanceof String) {
                    Response contactLocationData = (Response)responseTransformer.transform((String)msg);
                    responses.add(contactLocationData);
                }
            }
            
            return responses;
        } catch(ConnectorException ex) {
            throw new ServiceException(ex);
        }
    }
}
