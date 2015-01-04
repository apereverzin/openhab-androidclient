package org.creek.openhab.androidclient;

import org.creek.openhab.androidclient.infrastructure.sqlite.SQLiteRepositoryManager;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * 
 * @author Andrey Pereverzin
 */
public class OpenHABClientApplication extends Application {
    private static final String TAG = OpenHABClientApplication.class.getSimpleName();
    private static SQLiteDatabase openHabClientDatabase;
    
    @Override
    public final void onCreate() {
        Log.d(TAG, "-=-=-=-=-=-=-=-=-=-=-onCreate");
        
        super.onCreate();
      
        openHabClientDatabase = SQLiteRepositoryManager.getInstance().createDatabaseIfDoesNotExist();
    }

    @Override
    public void onTerminate() {
        Log.d(TAG, "-=-=-=-=-=-=-=-=-=-=-onTerminate");
        
        if (openHabClientDatabase != null && openHabClientDatabase.isOpen()) {
            openHabClientDatabase.close();
        }

        super.onTerminate();
    }

    public static void setDatabase(SQLiteDatabase _whereAreYouDatabase) {
        Log.d(TAG, "-=-=-=-=-=-=-=-=-=-=-setDatabase: " + _whereAreYouDatabase.isOpen());
        openHabClientDatabase = _whereAreYouDatabase;
    }
    
    public static SQLiteDatabase getDatabase() {
        return openHabClientDatabase;
    }
    
    public static String getSenderEmailAddress() {
        return "andrey.pereverzin_sweethome@yahoo.com";
    }
    
    public static String getOpenHABServerEmailAddress() {
        return "andrey.pereverzin_sweethome@yahoo.com";
    }
}
