package org.creek.openhab.androidclient.domain;

/**
 * 
 * @author Andrey Pereverzin
 */
public class Response extends AbstractSendable implements Sendable {
    private long requestId;

    public long getRequestId() {
        return requestId;
    }

    public void setRequestId(long requestId) {
        this.requestId = requestId;
    }

    @Override
    public String toString() {
        return getClass().getName() + " [" + super.toString() + ", requestId=" + requestId + "]";
    }
}
