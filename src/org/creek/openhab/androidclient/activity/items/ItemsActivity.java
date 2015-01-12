package org.creek.openhab.androidclient.activity.items;

import java.util.List;
import java.util.Map;

import org.creek.mailcontrol.model.data.CommandTransformable;
import org.creek.mailcontrol.model.data.ItemCommandData;
import org.creek.mailcontrol.model.data.ItemStateData;
import org.creek.mailcontrol.model.message.ItemCommandRequestMessage;
import org.creek.mailcontrol.model.message.ItemsStateRequestMessage;
import org.creek.mailcontrol.model.message.ItemsStateResponseMessage;
import org.creek.mailcontrol.model.util.JSONTransformer;
import org.creek.openhab.androidclient.OpenHABClientApplication;
import org.creek.openhab.androidclient.R;
import org.creek.openhab.androidclient.stateprocessor.CommandTransformableMenuItem;
import org.creek.openhab.androidclient.stateprocessor.ItemStateProcessor;
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
    private static final int ENABLE_MENU_ITEM = FIRST + 1;
    private static final int DISABLE_MENU_ITEM = FIRST + 2;

    // Context menu
    private static final int VIEW_ITEM_STATE_MENU_ITEM = FIRST;

    private List<ItemStateData> itemStates;
    
    private ListView lv;
    
    private ItemStatesArrayAdapter itemStatesListAdapter;
    private ItemStateProcessor itemStateProcessor;
    private Map<Integer, CommandTransformableMenuItem> contextMenuItems;
    
    private MenuItem enableMenuItem;
    private MenuItem disableMenuItem;

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
        
//        ItemsStateResponseMessage msg = buildMessage();
//        itemStates = msg.getItemStates();
        itemStates = OpenHABClientApplication.getItemStates();
        
        OpenHABClientApplication.registerItemsActivity(this);
        
        updateListAdapter();
        
        itemStateProcessor = new ItemStateProcessor();

        registerForContextMenu(getListView());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i(TAG, "onCreateOptionsMenu()");
        try {
            menu.add(0, REFRESH_ITEM_STATES_MENU_ITEM, 0, R.string.menu_refresh_item_states);
            menu.add(0, DISABLE_MENU_ITEM, 0, R.string.menu_disable);
            menu.add(0, ENABLE_MENU_ITEM, 0, R.string.menu_enable);
            enableMenuItem = menu.findItem(ENABLE_MENU_ITEM);
            disableMenuItem = menu.findItem(DISABLE_MENU_ITEM);
            if (OpenHABClientApplication.isEnabled()) {
                setEnabled();
            } else {
                setDisabled();
            }
        } catch (Exception ex) {
            showException(ItemsActivity.this, ex);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case REFRESH_ITEM_STATES_MENU_ITEM:
            itemStates = OpenHABClientApplication.getItemStates();
            ItemsStateRequestMessage request = new ItemsStateRequestMessage(OpenHABClientApplication.getSenderEmailAddress());
            OpenHABClientApplication.addRequest(request);
            return true;
        case ENABLE_MENU_ITEM:
            OpenHABClientApplication.enable();
            setEnabled();
            return true;
        case DISABLE_MENU_ITEM:
            OpenHABClientApplication.disable();
            setDisabled();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        Log.d(TAG, "onCreateContextMenu()");
        super.onCreateContextMenu(menu, v, menuInfo);
        final ItemStateData itemStateData = itemStates.get((int) ((AdapterContextMenuInfo) menuInfo).id);

        menu.add(0, VIEW_ITEM_STATE_MENU_ITEM, 0, R.string.menu_view_item_state);
        
        contextMenuItems = itemStateProcessor.buildMenuItems(FIRST + 1, itemStateData);
        
        for (int ind: contextMenuItems.keySet()) {
            menu.add(0, ind, 0, contextMenuItems.get(ind).getStringResourceId());
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        final ItemStateData itemStateData = itemStates.get((int) info.id);
        int itemId = item.getItemId();
        if (item.getItemId() == VIEW_ITEM_STATE_MENU_ITEM) {
            return true;
        } else if (contextMenuItems.containsKey(itemId)) {
            CommandTransformable command = contextMenuItems.get(itemId).getCommand();
            ItemCommandData itemCommand = new ItemCommandData(System.currentTimeMillis(), itemStateData.getItemId(), command);
            ItemCommandRequestMessage message = new ItemCommandRequestMessage(itemCommand, OpenHABClientApplication.getSenderEmailAddress());
            OpenHABClientApplication.addRequest(message);
            return true;
        } 
        
        return super.onContextItemSelected(item);
    }
    
    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy()");
        OpenHABClientApplication.unregisterItemsActivity();
        
        super.onDestroy();
    }
    
    public void refreshListAdapter() {
        Log.d(TAG, "refreshListAdapter()");
        updateListAdapter();
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

    //{"itemStates":[{"acceptedCommands":["ON_OFF","INCREASE_DECREASE","PERCENT","HSB"],"state":{"brightness":0,"saturation":0,"hue":0,"type":"HSB"},"itemId":"Light1","timeSent":"1419892542018"},{"acceptedCommands":[],"state":{"value":"2014-12-29T22:34:51","type":"DATE_TIME"},"itemId":"Date","timeSent":"1419892542019"}],"requestId":{"timestamp":0,"senderEmail":"andrey.pereverzin_sweethome@yahoo.com"},"messageId":{"timestamp":1419892542019,"senderEmail":"andrey.pereverzin_sweethome@yahoo.com"},"messageType":"115","productVersion":"1.0"}
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

    private void setEnabled() {
        enableMenuItem.setVisible(false);
        disableMenuItem.setVisible(true);
    }

    private void setDisabled() {
        enableMenuItem.setVisible(true);
        disableMenuItem.setVisible(false);
    }

    private void updateListAdapter() {
        runOnUiThread(new Runnable() {
            public void run() {
                itemStatesListAdapter = new ItemStatesArrayAdapter(ItemsActivity.this, itemStates);
                setListAdapter(itemStatesListAdapter);
                lv.invalidateViews();
            }
        });
    }
}
