package org.creek.openhab.androidclient.stateprocessor;

import static org.creek.mailcontrol.model.types.OnOffDataType.OFF;
import static org.creek.mailcontrol.model.types.OnOffDataType.ON;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.creek.mailcontrol.model.data.DataType;
import org.creek.mailcontrol.model.data.HSBData;
import org.creek.mailcontrol.model.data.ItemStateData;
import org.creek.mailcontrol.model.data.OnOffData;
import org.creek.mailcontrol.model.types.HSBDataType;
import org.creek.openhab.androidclient.R;

/**
 * 
 * @author Andrey Pereverzin
 */
public class ItemStateProcessor {
    private static final int MIN_HUE_VALUE = 0;
    private static final int MAX_HUE_VALUE = 360;
    private static final int HUE_STEP = 30;
    private static final int MIN_SATURATION_VALUE = 0;
    private static final int MAX_SATURATION_VALUE = 100;
    private static final int SATURATION_STEP = 10;
    private static final int MIN_BRIGHTNESS_VALUE = 0;
    private static final int MAX_BRIGHTNESS_VALUE = 100;
    private static final int BRIGHTNESS_STEP = 10;

    public Map<Integer, CommandTransformableMenuItem> buildMenuItems(int firstMenuIndex, ItemStateData itemStateData) {
        int menuIndex = firstMenuIndex;
        Map<Integer, CommandTransformableMenuItem> menuItems = new LinkedHashMap<Integer, CommandTransformableMenuItem>();

        List<DataType> acceptedCommands = itemStateData.getAcceptedCommands();
        if (itemStateData.getState().getStateType() == DataType.HSB) {
            HSBDataType hsbData = ((HSBData) itemStateData.getState()).getData();
            if (acceptedCommands.contains(DataType.ON_OFF)) {
                if (isHsbItemOff(hsbData)) {
                    menuItems.put(menuIndex++, new CommandTransformableMenuItem(R.string.menu_on_item_state, new OnOffData(ON)));
                } else {
                    menuItems.put(menuIndex++, new CommandTransformableMenuItem(R.string.menu_off_item_state, new OnOffData(OFF)));
                    buildHsbMenuItems(menuIndex, menuItems, acceptedCommands, hsbData);
                }
            } else {
                if (acceptedCommands.contains(DataType.HSB)) {

                }
            }
        }

        return menuItems;
    }

    private void buildHsbMenuItems(int menuIndex, Map<Integer, CommandTransformableMenuItem> menuItems, List<DataType> acceptedCommands, HSBDataType hsbData) {
        if (acceptedCommands.contains(DataType.HSB)) {
            if (hsbData.getHue() < MAX_HUE_VALUE) {
                HSBDataType newHsbData = new HSBDataType(hsbData.getHue() + HUE_STEP, hsbData.getSaturation(), hsbData.getBrightness());
                menuItems.put(menuIndex++, new CommandTransformableMenuItem(R.string.menu_increase_hue_item_state, new HSBData(newHsbData)));
            }
            if (hsbData.getHue() > MAX_HUE_VALUE) {
                HSBDataType newHsbData = new HSBDataType(hsbData.getHue() - HUE_STEP, hsbData.getSaturation(), hsbData.getBrightness());
                menuItems.put(menuIndex++, new CommandTransformableMenuItem(R.string.menu_decrease_hue_item_state, new HSBData(newHsbData)));
            }
            if (hsbData.getSaturation() < MAX_SATURATION_VALUE) {
                HSBDataType newHsbData = new HSBDataType(hsbData.getHue(), hsbData.getSaturation() + SATURATION_STEP, hsbData.getBrightness());
                menuItems.put(menuIndex++, new CommandTransformableMenuItem(R.string.menu_increase_saturation_item_state, new HSBData(newHsbData)));
            }
            if (hsbData.getSaturation() > MAX_SATURATION_VALUE) {
                HSBDataType newHsbData = new HSBDataType(hsbData.getHue(), hsbData.getSaturation() - SATURATION_STEP, hsbData.getBrightness());
                menuItems.put(menuIndex++, new CommandTransformableMenuItem(R.string.menu_decrease_saturation_item_state, new HSBData(newHsbData)));
            }
            if (hsbData.getBrightness() < MAX_BRIGHTNESS_VALUE) {
                HSBDataType newHsbData = new HSBDataType(hsbData.getHue(), hsbData.getSaturation(), hsbData.getBrightness() + BRIGHTNESS_STEP);
                menuItems.put(menuIndex++, new CommandTransformableMenuItem(R.string.menu_increase_brightness_item_state, new HSBData(newHsbData)));
            }
            if (hsbData.getBrightness() > MAX_BRIGHTNESS_VALUE) {
                HSBDataType newHsbData = new HSBDataType(hsbData.getHue(), hsbData.getSaturation(), hsbData.getBrightness() - BRIGHTNESS_STEP);
                menuItems.put(menuIndex++, new CommandTransformableMenuItem(R.string.menu_decrease_brightness_item_state, new HSBData(newHsbData)));
            }
        }
    }

    private boolean isHsbItemOff(HSBDataType hsbData) {
        return hsbData.getHue() == MIN_HUE_VALUE || hsbData.getSaturation() == MIN_SATURATION_VALUE || hsbData.getBrightness() == MIN_BRIGHTNESS_VALUE;
    }
}
