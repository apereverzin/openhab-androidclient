package org.creek.openhab.androidclient;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.creek.accessemail.connector.mail.MailPropertiesStorage;
import org.creek.accessemail.connector.mail.PredefinedMailProperties;
import org.creek.mailcontrol.model.data.ItemStateData;
import org.creek.mailcontrol.model.message.GenericRequest;
import org.creek.openhab.androidclient.activity.items.ItemsActivity;

import android.app.Application;
import android.util.Log;

/**
 * 
 * @author Andrey Pereverzin
 */
public class OpenHABClientApplication extends Application {
    private static final String EMAIL_ADDRESS = "andrey.pereverzin_sweethome@yahoo.com";
    private static final String EMAIL_PASSWORD = "SweetHome8";
    private static final String TAG = OpenHABClientApplication.class.getSimpleName();
    private static Properties mailProperties;
    private static boolean isEnabled = false;
    
    private static List<ItemStateData> itemStatesList = new ArrayList<ItemStateData>();
    private static List<GenericRequest> unsentRequests = new ArrayList<GenericRequest>();
    private static ItemsActivity itemsActivity = null;
    
    @Override
    public final void onCreate() {
        Log.d(TAG, "-=-=-=-=-=-=-=-=-=-=-onCreate");
        
        super.onCreate();
      
        buildMailProperties();
    }

    @Override
    public void onTerminate() {
        Log.d(TAG, "-=-=-=-=-=-=-=-=-=-=-onTerminate");
        
        super.onTerminate();
    }
    
    public static void registerItemsActivity(ItemsActivity _itemsActivity) {
        Log.d(TAG, "registerItemsActivity");
        itemsActivity = _itemsActivity;
    }
    
    public static void unregisterItemsActivity() {
        Log.d(TAG, "unregisterItemsActivity");
        itemsActivity = null;
    }
    
    public static ItemsActivity getItemsActivity() {
        return itemsActivity;
    }

    public static String getSenderEmailAddress() {
        return EMAIL_ADDRESS;
    }
    
    public static String getOpenHABServerEmailAddress() {
        return EMAIL_ADDRESS;
    }
    
    public static Properties getMailProperties() {
        return mailProperties;
    }

    private void buildMailProperties() {
//        mailProperties = PredefinedMailProperties.getPredefinedProperties(getOpenHABServerEmailAddress());
//        mailProperties.put(MailPropertiesStorage.MAIL_USERNAME_PROPERTY, getOpenHABServerEmailAddress());
//        mailProperties.put(MailPropertiesStorage.MAIL_PASSWORD_PROPERTY, EMAIL_PASSWORD);

        mailProperties = new Properties();
        mailProperties.put("mail.username", getOpenHABServerEmailAddress());
        mailProperties.put("mail.password", EMAIL_PASSWORD);
        mailProperties.put("mail.smtp.host", "smtp.mail.yahoo.com");
        mailProperties.put("mail.smtp.port", "587");
        mailProperties.put("mail.smtp.auth", "true");
        mailProperties.put("mail.smtp.starttls.enable", "true");
        mailProperties.put("mail.smtp.socketFactory.port", "995");
        mailProperties.put("mail.pop3.host", "pop.mail.yahoo.com");
        mailProperties.put("mail.pop3.port", "995");
        mailProperties.put("mail.pop3.socketFactory.port", "995");
        mailProperties.put("mail.pop3.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

        Log.i(TAG, mailProperties.getProperty(MailPropertiesStorage.MAIL_USERNAME_PROPERTY));
        Log.i(TAG, mailProperties.getProperty(MailPropertiesStorage.MAIL_PASSWORD_PROPERTY));

        ByteArrayOutputStream baos = new ByteArrayOutputStream(4096);
        PrintStream ps = new PrintStream(baos);
        mailProperties.list(ps);
        Log.i(TAG, "=====================");
        Log.i(TAG, new String(baos.toByteArray()));
        Log.i(TAG, "=====================");
    }

    public static List<ItemStateData> getItemStates() {
        synchronized (itemStatesList) {
            List<ItemStateData> newItemStates = new ArrayList<ItemStateData>();
            for (int i = 0; i < itemStatesList.size(); i++) {
                newItemStates.add(itemStatesList.get(i));
            }
            return newItemStates;
        }
    }

    public static void setItemStates(List<ItemStateData> itemStates) {
        synchronized (itemStates) {
            OpenHABClientApplication.itemStatesList = itemStates;
        }
    }

    public static void addRequest(GenericRequest request) {
        synchronized (unsentRequests) {
            Log.d(TAG, "addRequest " + request.toJSON().toJSONString());

            unsentRequests.add(request);
        }
    }
    
    public static List<GenericRequest> getUnsentRequests() {
        synchronized (unsentRequests) {
            List<GenericRequest> newUnsentRequests = unsentRequests;
            unsentRequests = new ArrayList<GenericRequest>();
            
            Log.d(TAG, "getUnsentRequests " + newUnsentRequests.size());

            return newUnsentRequests;
        }
    }

    public static boolean isEnabled() {
        return isEnabled;
    }

    public static void enable() {
        isEnabled = true;
    }

    public static void disable() {
        isEnabled = false;
    }
}
