package org.creek.openhab.androidclient.domain;

/**
 * 
 * @author Andrey Pereverzin
 */
public interface Sendable {
    long getId();
    void setId(long id);
    long getTimeCreated();
    void setTimeCreated(long timeCreated);
    long getTimeSent();
    void setTimeSent(long timeSent);
    long getTimeReceived();
    void setTimeReceived(long timeReceived);
    int getResultCode();
    void setResultCode(int resultCode);
    boolean isProcessed();
    void setProcessed(boolean processed);
    String getMessage();
    void setMessage(String message);
}
