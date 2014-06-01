package com.ugent.networkplanningtool.data.enums;

import com.ugent.networkplanningtool.MainActivity;
import com.ugent.networkplanningtool.R;

import java.util.HashMap;
import java.util.Map;

/**
 * The type of an activity
 */
public enum ActivityType {
    HD_VIDEO(MainActivity.getInstance().getResources().getString(R.string.hdVideotext)),
    ACTION_GAMES(MainActivity.getInstance().getResources().getString(R.string.actionGamesText)),
    YOUTUBE(MainActivity.getInstance().getResources().getString(R.string.youtubeText)),
    SURFING(MainActivity.getInstance().getResources().getString(R.string.surfingText)),
    NO_COVERAGE(MainActivity.getInstance().getResources().getString(R.string.noCoverageText));

    private String text;
    private int textSize = 0;

    private static Map<String, ActivityType> textToActivityTypeMapping;

    private ActivityType(String text) {
        this.text = text;
    }

    private static void initMapping() {
        textToActivityTypeMapping = new HashMap<String, ActivityType>();
        for (ActivityType s : values()) {
            textToActivityTypeMapping.put(s.getText(), s);
        }
    }

    public static ActivityType getActivityTypeByText(String text) {
        if (textToActivityTypeMapping == null) {
            initMapping();
        }
        return textToActivityTypeMapping.get(text);
    }

    public String getText() {
        return text;
    }

    /**
     * @return the textSize
     */
    public int getTextSize() {
        return textSize;
    }

    /**
     * @param textSize the textSize to set
     */
    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    @Override
    public String toString() {
        return text;
    }
}
