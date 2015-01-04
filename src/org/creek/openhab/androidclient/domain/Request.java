package org.creek.openhab.androidclient.domain;

/**
 * 
 * @author Andrey Pereverzin
 */
public class Request extends AbstractSendable implements Sendable {
    private long responseId;

    public long getResponseId() {
        return responseId;
    }

    public void setResponseId(long responseId) {
        this.responseId = responseId;
    }

    @Override
    public String toString() {
        return getClass().getName() + " [" + super.toString() + ", responseId=" + responseId + "]";
    }
}
