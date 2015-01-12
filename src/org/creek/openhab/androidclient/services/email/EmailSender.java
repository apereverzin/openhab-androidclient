package org.creek.openhab.androidclient.services.email;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.creek.mailcontrol.model.message.GenericRequest;
import org.creek.openhab.androidclient.OpenHABClientApplication;
import org.creek.openhab.androidclient.service.ServiceException;
import org.creek.openhab.androidclient.util.CryptoException;

import android.util.Log;

/**
 * 
 * @author Andrey Pereverzin
 */
public class EmailSender {
    private static final String TAG = EmailSender.class.getSimpleName();
    private final EmailSendingAndReceivingManager emailSendingAndReceivingManager;

    public EmailSender(EmailSendingAndReceivingManager emailSendingAndReceivingManager) throws IOException, CryptoException {
        this.emailSendingAndReceivingManager = emailSendingAndReceivingManager;
    }

    public void sendRequestsAndResponses() {
        Log.d(TAG, "sendRequestsAndResponses()");
        List<GenericRequest> failedRequests = sendRequests();
        // TODO do something with unsent data
    }

    private List<GenericRequest> sendRequests() {
        Log.d(TAG, "sendRequests()");
        List<GenericRequest> unsentDataList = new ArrayList<GenericRequest>();
        List<GenericRequest> unsentRequests;

        unsentRequests = OpenHABClientApplication.getUnsentRequests();

        Log.d(TAG, "--------------sendRequests: " + unsentRequests.size());
        for (int i = 0; i < unsentRequests.size(); i++) {
            final GenericRequest request = unsentRequests.get(i);
            try {
                Log.d(TAG, "--------------sending request: " + request);
                emailSendingAndReceivingManager.sendMessage(request);
            } catch (ServiceException ex) {
                unsentDataList.add(request);
            }
        }
        unsentRequests.removeAll(unsentDataList);
        return unsentDataList;
    }
}
