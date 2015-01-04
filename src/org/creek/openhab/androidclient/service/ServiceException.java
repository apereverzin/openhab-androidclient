package org.creek.openhab.androidclient.service;

/**
 * 
 * @author Andrey Pereverzin
 */
@SuppressWarnings("serial")
public class ServiceException extends Exception {
    public ServiceException(Throwable ex) {
        super(ex);
    }
}
