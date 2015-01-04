package org.creek.openhab.androidclient.infrastructure.sqlite;

import java.io.File;

import org.creek.openhab.androidclient.OpenHABClientApplication;

import static android.database.sqlite.SQLiteDatabase.CREATE_IF_NECESSARY;
import static android.database.sqlite.SQLiteDatabase.OPEN_READWRITE;
import static org.creek.openhab.androidclient.infrastructure.sqlite.AbstractSQLiteRepository.ID_FIELD_NAME;
import static org.creek.openhab.androidclient.infrastructure.sqlite.AbstractSQLiteRepository.REQUEST_TABLE;
import static org.creek.openhab.androidclient.infrastructure.sqlite.AbstractSQLiteRepository.RESPONSE_TABLE;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

/**
 * 
 * @author Andrey Pereverzin
 * 
 */
public class SQLiteRepositoryManager {
    private static final String TAG = SQLiteRepositoryManager.class.getSimpleName();
    
    private static final SQLiteRepositoryManager instance = new SQLiteRepositoryManager();
    private SQLiteDatabase openHABClientDb;
    
    private static final String DATABASE_PATH = "/data/data/org.creek.openhab.androidclient/databases/";
    private static final String DATABASE_NAME = "openhabclient.db";
    private static final String DROP_TABLE = "drop table if exists %s";
    
    private SQLiteRequestRepository requestRepository;
    private SQLiteResponseRepository responseRepository;

    private SQLiteRepositoryManager() {
        //
    }

    public static SQLiteRepositoryManager getInstance() {
        return instance;
    }

    public void openDatabase() {
        Log.d(TAG, "++++++++++++openDatabase");
        openHABClientDb = OpenHABClientApplication.getDatabase();
        Log.d(TAG, "++++++++++++isOpen: " + openHABClientDb.isOpen());
    }

    public void closeDatabase() {
        Log.d(TAG, "++++++++++++closeDatabase");
    }

    public SQLiteDatabase getDatabase() {
        return openHABClientDb;
    }
    
    public void beginTransaction() {
        openHABClientDb.beginTransaction();
    }

    public void endTransaction() {
        openHABClientDb.endTransaction();
    }

    public SQLiteRequestRepository getRequestRepository() {
        if (requestRepository == null) {
            requestRepository = new SQLiteRequestRepository();
        }

        requestRepository.setDatabase(openHABClientDb);
        return requestRepository;
    }

    public SQLiteResponseRepository getResponseRepository() {
        if (responseRepository == null) {
            responseRepository = new SQLiteResponseRepository();
        }
        
        responseRepository.setDatabase(openHABClientDb);
        return responseRepository;
    }

    public SQLiteDatabase createDatabaseIfDoesNotExist() {
        Log.d(TAG, "createDatabaseIfDoesNotExist " + DATABASE_PATH);

        File dbPath = new File(DATABASE_PATH);
        dbPath.mkdirs();

        openHABClientDb = SQLiteDatabase.openDatabase(DATABASE_PATH + DATABASE_NAME, null, OPEN_READWRITE + CREATE_IF_NECESSARY);

        if (!doDatabaseTablesExist()) {
            requestRepository = new SQLiteRequestRepository();
            responseRepository = new SQLiteResponseRepository();

            dropTable(REQUEST_TABLE);
            dropTable(RESPONSE_TABLE);
            openHABClientDb.execSQL(((SQLiteRequestRepository) requestRepository).getCreateTableCommand());
            openHABClientDb.execSQL(((SQLiteResponseRepository) responseRepository).getCreateTableCommand());
            requestRepository.setDatabase(openHABClientDb);
            responseRepository.setDatabase(openHABClientDb);
            Log.d(TAG, "===================Database created");
        }
        
        //closeDatabase();
        return openHABClientDb;
    }

    private boolean doDatabaseTablesExist() {
        try {
            testTable(REQUEST_TABLE);
            testTable(RESPONSE_TABLE);
            Log.d(TAG, "db exists");
            return true;
        } catch (SQLiteException ex) {
            Log.d(TAG, "db does not exist");
            return false;
        }
    }
    
    private void dropTable(String tableName) {
        openHABClientDb.execSQL(String.format(DROP_TABLE, tableName));
    }
    
    private void testTable(String tableName) {
        Cursor cursor = null;
        try {
            cursor = openHABClientDb.query(tableName, new String[] { ID_FIELD_NAME }, null, null, null, null, null);
        } finally {
            SQLiteUtils.closeCursor(cursor);
        }
    }
}
