package org.creek.openhab.androidclient.activity.items;

import static org.creek.openhab.androidclient.util.Util.isStringNotEmpty;

import java.util.List;

import org.creek.mailcontrol.model.data.ItemStateData;
import org.creek.openhab.androidclient.R;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 
 * @author Andrey Pereverzin
 */
public class ItemStateArrayAdapter extends ArrayAdapter<ItemStateData> {
    private static final String TAG = ItemStateArrayAdapter.class.getSimpleName();

    private final Context ctx;
    private final List<ItemStateData> values;

    public ItemStateArrayAdapter(Context ctx, List<ItemStateData> values) {
        super(ctx, R.layout.item_state_row, values);
        this.ctx = ctx;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.item_state_row, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.item_id);

        ItemStateData itemState = values.get(position);
        
        textView.setText(itemState.getItemId());

        return rowView;
    }
}
