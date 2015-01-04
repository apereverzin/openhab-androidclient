package org.creek.openhab.androidclient.activity.items;

import java.util.List;

import org.creek.mailcontrol.model.data.ItemStateData;
import org.creek.mailcontrol.model.message.ItemsStateResponseMessage;
import org.creek.mailcontrol.model.util.JSONTransformer;
import org.creek.openhab.androidclient.R;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import static android.view.Menu.FIRST;
import static org.creek.openhab.androidclient.util.ActivityUtil.setActivityTitle;
import static org.creek.openhab.androidclient.util.ActivityUtil.showException;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;

/**
 * List of contacts.
 * 
 * @author Andrey Pereverzin
 */
public final class ItemsActivity extends ListActivity {
    private static final String TAG = ItemsActivity.class.getSimpleName();
    
    // Options menu
    private static final int REFRESH_ITEM_STATES_MENU_ITEM = FIRST;

    // Context menu
    private static final int EDIT_CONTACT_DETAILS_MENU_ITEM = FIRST;

    static final String CONTACT_SELECTED = "CONTACT_SELECTED";
    
    private List<ItemStateData> itemStates;
    
    private ListView lv;
    
    private ItemStateArrayAdapter itemStatesListAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate()");
        super.onCreate(savedInstanceState);

        setActivityTitle(this, R.string.item_states);

        setContentView(R.layout.item_states_list);

        lv = (ListView) findViewById(android.R.id.list);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                
            }
        });
        
        ItemsStateResponseMessage msg = buildMessage();
        itemStates = msg.getItemStates();
        
        itemStatesListAdapter = new ItemStateArrayAdapter(this, itemStates);
        setListAdapter(itemStatesListAdapter);

        registerForContextMenu(getListView());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i(TAG, "onCreateOptionsMenu()");
        try {
            menu.add(0, REFRESH_ITEM_STATES_MENU_ITEM, 0, R.string.menu_refresh_item_states);
        } catch (Exception ex) {
            showException(ItemsActivity.this, ex);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case REFRESH_ITEM_STATES_MENU_ITEM:
            //
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        Log.d(TAG, "onCreateContextMenu()");
        super.onCreateContextMenu(menu, v, menuInfo);
        final ItemStateData itemStateData = itemStates.get((int) ((AdapterContextMenuInfo) menuInfo).id);
        menu.add(0, EDIT_CONTACT_DETAILS_MENU_ITEM, 0, R.string.menu_view_item_state);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        final ItemStateData itemStateData = itemStates.get((int) info.id);
        switch (item.getItemId()) {
        case EDIT_CONTACT_DETAILS_MENU_ITEM:
            //
        default:
            return super.onContextItemSelected(item);
        }
    }

    private boolean startNewActivity(Class<? extends Activity> clazz) {
        try {
            Intent intent = new Intent(this, clazz);
            startActivity(intent);
        } catch (Exception ex) {
            showException(ItemsActivity.this, ex);
        }
        return true;
    }
    
    private ItemsStateResponseMessage buildMessage() {
        String s = "{\"itemStates\":[{\"acceptedCommands\":[\"ON_OFF\",\"INCREASE_DECREASE\",\"PERCENT\",\"HSB\"],\"state\":{\"brightness\":0,\"saturation\":0,\"hue\":0,\"type\":\"HSB\"},\"itemId\":\"Light1\",\"timeSent\":\"1419892542018\"},{\"acceptedCommands\":[],\"state\":{\"value\":\"2014-12-29T22:34:51\",\"type\":\"DATE_TIME\"},\"itemId\":\"Date\",\"timeSent\":\"1419892542019\"}],\"requestId\":{\"timestamp\":0,\"senderEmail\":\"andrey.pereverzin_sweethome@yahoo.com\"},\"messageId\":{\"timestamp\":1419892542019,\"senderEmail\":\"andrey.pereverzin_sweethome@yahoo.com\"},\"messageType\":\"115\",\"productVersion\":\"1.0\"}";
        JSONParser parser = new JSONParser();
        JSONTransformer transformer = new JSONTransformer();
        try {
            parser.parse(s, transformer);
        } catch (ParseException ex) {
            //
        }

        // {"requestId":{"timestamp":1420380013436,"senderEmail":"aa@bb.cc"},"productVersion":"1.0","messageType":"113","timeSent":0,"itemState":{"itemId":"LIGHT","timeSent":"0","acceptedCommands":["INCREASE_DECREASE","ON_OFF"],"state":{"type":"DECIMAL","value":"12"}},"messageId":{"timestamp":1420380013436,"senderEmail":"dd@ee.ff"}}
        JSONObject res = (JSONObject) transformer.getResult();
        ItemsStateResponseMessage messageRes = new ItemsStateResponseMessage(res);
        return messageRes;
    }
}
