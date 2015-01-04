package org.creek.openhab.androidclient.infrastructure.sqlite;

import java.util.List;

import org.creek.openhab.androidclient.domain.Request;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import static org.creek.openhab.androidclient.infrastructure.sqlite.Comparison.EQUALS;
import static org.creek.openhab.androidclient.infrastructure.sqlite.ComparisonClause.CREATION_TIME_KNOWN;
import static org.creek.openhab.androidclient.infrastructure.sqlite.ComparisonClause.RECEIVED_TIME_UNKNOWN;
import static org.creek.openhab.androidclient.infrastructure.sqlite.ComparisonClause.NOT_PROCESSED;
import static org.creek.openhab.androidclient.infrastructure.sqlite.SQLiteUtils.closeCursor;
import static org.creek.openhab.androidclient.util.Util.concatArrays;

/**
 * 
 * @author Andrey Pereverzin
 */
public final class SQLiteRequestRepository extends AbstractRequestResponseRepository<Request> {
    private static final String TAG = SQLiteRequestRepository.class.getSimpleName();

    private final String[] fieldNames = new String[] {
        };

    private final String[] fieldTypes = new String[] {
        };
    
    @Override
    protected ContentValues getContentValues(Request contactRequest) {
        Log.d(TAG, "getContentValues()");
        ContentValues values = super.getContentValues(contactRequest);
        return values;
    }

    @Override
    protected Request createEntity(Cursor cursor) {
        Request request = super.createEntity(cursor);
        Log.d(TAG, "createEntity(): " + request);
        return request;
    }
    
    @Override
    protected int getNumberOfFields() {
        return super.getNumberOfFields() + fieldNames.length;
    }

    @Override
    protected String[] getFieldNames() {
        return concatArrays(super.getFieldNames(), fieldNames);
    }

    @Override
    protected String[] getFieldTypes() {
        return concatArrays(super.getFieldTypes(), fieldTypes);
    }

    public final List<Request> getAllRequests() {
        Log.d(TAG, "getAllRequests()");

        Cursor contactRequestCursor = null;
        try {
            contactRequestCursor = createCursor(null, null, null);
            return createEntityListFromCursor(contactRequestCursor);
        } finally {
            closeCursor(contactRequestCursor);
        }
    }

    public final List<Request> getUnsentRequests() {
        Log.d(TAG, "getUnsentRequests()");
        return getUnsent();
    }
    
    public final List<Request> getAllUnrespondedRequests() {
        Log.d(TAG, "getAllUnrespondedRequests()");
        return null;
    }

    public void updateProcessedRequests(String contactEmail) {
        Log.d(TAG, "updateProcessedRequests: " + contactEmail);
        Log.d(TAG, "--------------updateProcessedOutgoingContactRequests: " + contactEmail);
        ComparisonClause emailComparisonClause = new ComparisonClause(EMAIL_FIELD_NAME, EQUALS, contactEmail);
        String criteria = createWhereAndCriteria(
                new ComparisonClause[]{emailComparisonClause, CREATION_TIME_KNOWN, RECEIVED_TIME_UNKNOWN, NOT_PROCESSED});
        ContentValues valuesToSet = getValuesToSet();
        whereAreYouDb.update(getTableName(), valuesToSet, criteria, null);
    }

    @Override
    protected final Request createEntityInstance() {
        return new Request();
    }
    
    @Override
    protected final String getTableName() {
        return REQUEST_TABLE;
    }

    private ContentValues getValuesToSet() {
        ContentValues valuesToSet = new ContentValues();
        valuesToSet.put(PROCESSED_FIELD_NAME, INT_TRUE);
        return valuesToSet;
    }
}
