package org.creek.openhab.androidclient.stateprocessor;

import org.creek.mailcontrol.model.data.CommandTransformable;

/**
 * 
 * @author Andrey Pereverzin
 */
public class CommandTransformableMenuItem {
    private final int stringResourceId;
    private final CommandTransformable command;

    public CommandTransformableMenuItem(int stringResourceId, CommandTransformable command) {
        this.stringResourceId = stringResourceId;
        this.command = command;
    }

    public int getStringResourceId() {
        return stringResourceId;
    }

    public CommandTransformable getCommand() {
        return command;
    }

}
