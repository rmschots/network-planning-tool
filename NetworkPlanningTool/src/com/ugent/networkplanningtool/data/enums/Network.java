package com.ugent.networkplanningtool.data.enums;

import android.graphics.Color;

import com.ugent.networkplanningtool.MainActivity;
import com.ugent.networkplanningtool.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Represent networks
 */
public enum Network {
    NETWORK_A(MainActivity.getInstance().getResources().getString(R.string.networkAText), Color.rgb(115, 128, 190)),
    NETWORK_B(MainActivity.getInstance().getResources().getString(R.string.networkBText), Color.rgb(0, 128, 190)),
    NETWORK_C(MainActivity.getInstance().getResources().getString(R.string.networkCText), Color.rgb(115, 0, 190)),
    NETWORK_D(MainActivity.getInstance().getResources().getString(R.string.networkDText), Color.rgb(115, 128, 0)),
    NETWORK_E(MainActivity.getInstance().getResources().getString(R.string.networkEText), Color.rgb(255, 128, 190)),
    NETWORK_F(MainActivity.getInstance().getResources().getString(R.string.networkFText), Color.rgb(115, 255, 190)),
    NETWORK_G(MainActivity.getInstance().getResources().getString(R.string.networkGText), Color.rgb(115, 128, 255)),
    NETWORK_H(MainActivity.getInstance().getResources().getString(R.string.networkHText), Color.rgb(255, 128, 0));

    private String text;
    private int color;

    private static Map<String, Network> textToNetworkMapping;

    private Network(String text, int color) {
        this.text = text;
        this.color = color;
    }

    private static void initMapping() {
        textToNetworkMapping = new HashMap<String, Network>();
        for (Network s : values()) {
            textToNetworkMapping.put(s.getText(), s);
        }
    }

    public static Network getNetworkByText(String text) {
        if (textToNetworkMapping == null) {
            initMapping();
        }
        return textToNetworkMapping.get(text);
    }

    public String getText() {
        return text;
    }

    public int getColor() {
        return color;
    }

    @Override
    public String toString(){
        return text;
    }
}
