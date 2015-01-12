package org.creek.openhab.androidclient.activity.items;

import org.creek.mailcontrol.model.data.DataType;
import org.creek.mailcontrol.model.data.DateTimeData;
import org.creek.mailcontrol.model.data.HSBData;
import org.creek.mailcontrol.model.data.StateTransformable;
import org.creek.mailcontrol.model.types.DateTimeDataType;
import org.creek.mailcontrol.model.types.HSBDataType;
import org.creek.openhab.androidclient.R;

import android.content.Context;

/**
 * 
 * @author Andrey Pereverzin
 */
public class ItemStateConvertor {
    private static final int MIN_HUE_VALUE = 0;
    private static final int MIN_SATURATION_VALUE = 0;
    private static final int MIN_BRIGHTNESS_VALUE = 0;

    public String buildStateDescription(Context ctx, StateTransformable itemState) {
        if (itemState.getStateType() == DataType.HSB) {
            HSBDataType hsbData = ((HSBData) itemState).getData();
            if (isHsbItemOff(hsbData)) {
                return ctx.getString(R.string.item_state_description_off);
            } else {
                return ctx.getString(R.string.item_state_description_hue) + " " + hsbData.getHue() + ", " + ctx.getString(R.string.item_state_description_saturation) + " " + hsbData.getSaturation() + ", " + ctx.getString(R.string.item_state_description_brightness) + " " + hsbData.getBrightness();
            }
        } else if (itemState.getStateType() == DataType.DATE_TIME) {
            DateTimeDataType dtData = ((DateTimeData) itemState).getData();
            return dtData.toString();
        } else {
            return itemState.toString();
        }
    }

    private boolean isHsbItemOff(HSBDataType hsbData) {
        return hsbData.getHue() == MIN_HUE_VALUE || hsbData.getSaturation() == MIN_SATURATION_VALUE || hsbData.getBrightness() == MIN_BRIGHTNESS_VALUE;
    }
}
