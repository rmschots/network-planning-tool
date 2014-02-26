package com.ugent.networkplanningtool.data.enums;

import com.ugent.networkplanningtool.MainActivity;
import com.ugent.networkplanningtool.R;

import java.util.HashMap;
import java.util.Map;

public enum WallType {
    WALL(MainActivity.getInstance().getResources().getString(R.string.wallText).toLowerCase()),
    DOOR(MainActivity.getInstance().getResources().getString(R.string.doorText).toLowerCase()),
    WINDOW(MainActivity.getInstance().getResources().getString(R.string.windowText).toLowerCase());

    private String text;

    private static Map<String, WallType> textToWallMapping;

    private WallType(String text) {
        this.text = text;
    }

    private static void initMapping() {
        textToWallMapping = new HashMap<String, WallType>();
        for (WallType s : values()) {
            textToWallMapping.put(s.getText(), s);
        }
    }

    public static WallType getWallTypeByText(String text) {
        if (textToWallMapping == null) {
            initMapping();
        }
        return textToWallMapping.get(text);
    }

    public String getText() {
        return text;
    }
}
