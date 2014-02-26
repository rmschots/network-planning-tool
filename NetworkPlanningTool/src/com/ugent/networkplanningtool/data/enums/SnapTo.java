package com.ugent.networkplanningtool.data.enums;

import com.ugent.networkplanningtool.MainActivity;
import com.ugent.networkplanningtool.R;

import java.util.HashMap;
import java.util.Map;

public enum SnapTo {
    GRID(MainActivity.getInstance().getResources().getString(R.string.snapToGridText)),
    WALLS(MainActivity.getInstance().getResources().getString(R.string.snapToWallsText));
    private String text;

    private static Map<String, SnapTo> textToSnapToMapping;

    private SnapTo(String text) {
        this.text = text;
    }

    private static void initMapping() {
        textToSnapToMapping = new HashMap<String, SnapTo>();
        for (SnapTo s : values()) {
            textToSnapToMapping.put(s.getText(), s);
        }
    }

    public static SnapTo getSnapToByText(String text) {
        if (textToSnapToMapping == null) {
            initMapping();
        }
        return textToSnapToMapping.get(text);
    }

    public String getText() {
        return text;
    }
}
