package org.creek.openhab.androidclient.activity.items;

import java.util.List;

import org.creek.mailcontrol.model.data.ItemStateData;
import org.creek.openhab.androidclient.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * 
 * @author Andrey Pereverzin
 */
public class ItemStatesArrayAdapter extends ArrayAdapter<ItemStateData> {
    private final Context ctx;
    private final List<ItemStateData> values;
    private final ItemStateConvertor itemStateConvertor;

    public ItemStatesArrayAdapter(Context ctx, List<ItemStateData> values) {
        super(ctx, R.layout.item_state_row, values);
        this.ctx = ctx;
        this.values = values;
        itemStateConvertor = new ItemStateConvertor();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.item_state_row, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.item_id);
        TextView stateView = (TextView) rowView.findViewById(R.id.item_state);

        ItemStateData itemState = values.get(position);
        
        textView.setText(itemState.getItemId());
        stateView.setText(itemStateConvertor.buildStateDescription(ctx, itemState.getState()));

        return rowView;
    }
}
