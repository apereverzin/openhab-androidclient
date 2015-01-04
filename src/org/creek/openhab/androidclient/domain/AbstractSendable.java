package org.creek.openhab.androidclient.domain;

/**
 * 
 * @author Andrey Pereverzin
 */
public abstract class AbstractSendable implements Sendable {
    protected long id;
    private long timeCreated;
    private long timeSent;
    private long timeReceived;
    private int resultCode;
    private boolean processed;
    private String message;
 
    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    @Override
    public long getTimeCreated() {
        return timeCreated;
    }

    @Override
    public void setTimeCreated(long timeCreated) {
        this.timeCreated = timeCreated;
    }

    @Override
    public long getTimeSent() {
        return timeSent;
    }

    @Override
    public void setTimeSent(long timeSent) {
        this.timeSent = timeSent;
    }

    @Override
    public long getTimeReceived() {
        return timeReceived;
    }

    @Override
    public void setTimeReceived(long timeReceived) {
        this.timeReceived = timeReceived;
    }

    @Override
    public int getResultCode() {
        return resultCode;
    }

    @Override
    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    @Override
    public boolean isProcessed() {
        return processed;
    }

    @Override
    public void setProcessed(boolean processed) {
        this.processed = processed;
    }
    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return super.toString() + ", timeCreated=" + timeCreated + ", timeSent=" + timeSent + ", timeReceived=" + timeReceived + ", resultCode=" + resultCode + ", processed=" + processed;
    }
}
