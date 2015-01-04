package org.creek.openhab.androidclient.services.email;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.creek.mailcontrol.model.message.GenericMessage;
import org.creek.mailcontrol.model.message.GenericRequest;
import org.creek.openhab.androidclient.domain.Request;
import org.creek.openhab.androidclient.infrastructure.sqlite.SQLiteRepositoryManager;
import org.creek.openhab.androidclient.infrastructure.sqlite.SQLiteRequestRepository;
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
        List<Request> failedRequests = sendRequests();
        // TODO do something with unsent data
    }

    private List<Request> sendRequests() {
        Log.d(TAG, "sendRequests()");
        List<Request> unsentDataList = new ArrayList<Request>();
        SQLiteRequestRepository requestRepository;
        List<Request> unsentRequests;

        try {
            SQLiteRepositoryManager.getInstance().openDatabase();
            requestRepository = SQLiteRepositoryManager.getInstance().getRequestRepository();

            unsentRequests = requestRepository.getUnsentRequests();
        } finally {
            SQLiteRepositoryManager.getInstance().closeDatabase();
        }

        Log.d(TAG, "--------------sendRequests: " + unsentRequests.size());
        for (int i = 0; i < unsentRequests.size(); i++) {
            final Request request = unsentRequests.get(i);
            try {
                Log.d(TAG, "--------------sending request: " + request);
                emailSendingAndReceivingManager.sendMessage(request);

                try {
                    SQLiteRepositoryManager.getInstance().openDatabase();
                    requestRepository = SQLiteRepositoryManager.getInstance().getRequestRepository();

                    requestRepository.update(request);
                    
                    Log.d(TAG, "--------------request sent: " + request);
                } finally {
                    SQLiteRepositoryManager.getInstance().closeDatabase();
                }
            } catch (ServiceException ex) {
                unsentDataList.add(request);
            }
        }
        unsentRequests.removeAll(unsentDataList);
        return unsentDataList;
    }
}
