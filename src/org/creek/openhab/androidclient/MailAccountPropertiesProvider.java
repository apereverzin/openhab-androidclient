package org.creek.openhab.androidclient;

import java.io.IOException;
import java.util.Properties;

import org.creek.accessemail.connector.mail.MailPropertiesStorage;
import org.creek.openhab.androidclient.util.CryptoException;
import org.creek.openhab.androidclient.util.CryptoUtil;

import android.util.Log;

import static org.creek.accessemail.connector.mail.MailPropertiesStorage.MAIL_PASSWORD_PROPERTY;

/**
 * 
 * @author Andrey Pereverzin
 */
public class MailAccountPropertiesProvider {
    private static final String TAG = MailAccountPropertiesProvider.class.getSimpleName();
    private static final String OPENHAB_CLIENT_PROPERTIES_FILE_PATH = "/Android/openhabclient.properties";
    private static final String PASSWORD_ENCRYPTION_SEED = "tobeornottobe";
    private Properties mailProperties = null;
    private final FileProvider fileProvider = new FileProvider();
    
    private static final MailAccountPropertiesProvider instance = new MailAccountPropertiesProvider();

    private MailAccountPropertiesProvider() {
        //
    }
    
    public static final MailAccountPropertiesProvider getInstance() {
        return instance;
    }
    
    public void persistMailProperties(Properties propsToPersist) throws IOException, CryptoException {
        String password = propsToPersist.getProperty(MAIL_PASSWORD_PROPERTY);
        String cryptPassword = CryptoUtil.encrypt(PASSWORD_ENCRYPTION_SEED, password);
        propsToPersist.setProperty(MAIL_PASSWORD_PROPERTY, cryptPassword);
        
        fileProvider.persistPropertiesToFile(OPENHAB_CLIENT_PROPERTIES_FILE_PATH, propsToPersist);

        propsToPersist.setProperty(MAIL_PASSWORD_PROPERTY, password);
        mailProperties = propsToPersist;
    }

    public Properties getMailProperties() throws IOException, CryptoException {
        Log.i(TAG, "getMailProperties");
        if (mailProperties == null) {
            // TODO
//            mailProperties = fileProvider.retrievePropertiesFromFile(OPENHAB_CLIENT_PROPERTIES_FILE_PATH);
//            if (mailProperties != null) {
//                decryptPassword(mailProperties);
//            } else {
//                return new Properties();
//            }
            mailProperties = OpenHABClientApplication.getMailProperties();
        }
        Log.i(TAG, mailProperties.getProperty(MailPropertiesStorage.MAIL_USERNAME_PROPERTY));
        return mailProperties;
    }
    
    private void decryptPassword(Properties props) throws CryptoException {
        String cryptPassword = props.getProperty(MAIL_PASSWORD_PROPERTY);
        String password = CryptoUtil.decrypt(PASSWORD_ENCRYPTION_SEED, cryptPassword);
        props.setProperty(MAIL_PASSWORD_PROPERTY, password);
    }
}
