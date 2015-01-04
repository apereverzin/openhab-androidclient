package org.creek.openhab.androidclient.infrastructure.sqlite;

import static org.creek.openhab.androidclient.infrastructure.sqlite.SQLiteUtils.closeCursor;

import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import org.creek.openhab.androidclient.domain.Response;
import org.creek.openhab.androidclient.util.Util;

/**
 * 
 * @author Andrey Pereverzin
 */
public final class SQLiteResponseRepository extends AbstractRequestResponseRepository<Response> {
    private static final String TAG = SQLiteResponseRepository.class.getSimpleName();

    static final String REQUEST_ID_FIELD_NAME = "request_id";
    
    private final String[] fieldNames = new String[] {
            REQUEST_ID_FIELD_NAME
        };

    private final String[] fieldTypes = new String[] {
            "int not null"
        };
       
    @Override
    protected int getNumberOfFields() {
        return super.getNumberOfFields() + fieldNames.length;
    }
    
    @Override
    protected String[] getFieldNames() {
        return  Util.concatArrays(super.getFieldNames(), fieldNames);
    }
    
    @Override
    protected String[] getFieldTypes() {
        return Util.concatArrays(super.getFieldTypes(), fieldTypes);
    }

    public final List<Response> getAllResponses() {
        Log.d(TAG, "getAllResponses()");

        Cursor contactDataCursor = null;
        try {
            contactDataCursor = createCursor(null, null, null);
            List<Response> entities = createEntityListFromCursor(contactDataCursor);
            return entities;
        } finally {
            closeCursor(contactDataCursor);
        }
    }

    public final List<Response> getUnsentResponses() {
        Log.d(TAG, "getUnsentContactResponses()");
        List<Response> entities = getUnsent();
        return entities;
    }
    
    @Override
    protected final Response createEntityInstance() {
        return new Response();
    }
    
    @Override
    protected final ContentValues getContentValues(Response contactResponse) {
        Log.d(TAG, "getContentValues()");
        ContentValues values = super.getContentValues(contactResponse);
        values.put(REQUEST_ID_FIELD_NAME, -1);
        return values;
    }
    
    @Override
    protected final Response createEntity(Cursor cursor) {
        Response response = super.createEntity(cursor);
        int numberOfFields = super.getNumberOfFields();
        response.setRequestId(cursor.getInt(numberOfFields++));
        Log.d(TAG, "createEntity(): " + response);
        return response;
    }
    
    @Override
    protected final String getTableName() {
        return RESPONSE_TABLE;
    }
}
