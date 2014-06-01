package com.ugent.networkplanningtool.data.enums;

import android.graphics.Color;

import com.ugent.networkplanningtool.MainActivity;
import com.ugent.networkplanningtool.R;

import java.util.HashMap;
import java.util.Map;

/**
 * The connection type of a ConnectionPoint (data or power)
 */
public enum ConnectionPointType {
    DATA(MainActivity.getInstance().getResources().getString(R.string.dataConnectionPointText), Color.rgb(114, 15, 24)),
    POWER(MainActivity.getInstance().getResources().getString(R.string.powerConnectionPointText), Color.rgb(116, 172, 36));

    private String text;
    private int color;

    private static Map<String, ConnectionPointType> textToConnectionPointTypeMapping;

    private ConnectionPointType(String text, int color) {
        this.text = text;
        this.color = color;
    }

    private static void initMapping() {
        textToConnectionPointTypeMapping = new HashMap<String, ConnectionPointType>();
        for (ConnectionPointType s : values()) {
            textToConnectionPointTypeMapping.put(s.getText(), s);
        }
    }

    public static ConnectionPointType getConnectionPointTypeByText(String text) {
        if (textToConnectionPointTypeMapping == null) {
            initMapping();
        }
        return textToConnectionPointTypeMapping.get(text);
    }

    public String getText() {
        return text;
    }

    public int getColor() {
        return color;
    }
}
