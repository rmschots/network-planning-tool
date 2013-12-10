package com.ugent.networkplanningtool.data;

import java.util.HashMap;
import java.util.Map;

import android.graphics.Color;

import com.ugent.networkplanningtool.MainActivity;
import com.ugent.networkplanningtool.R;

public enum Network {
	NETWORK_A(MainActivity.getContext().getResources().getString(R.string.networkAText),Color.rgb(115, 128, 190)),
	NETWORK_B(MainActivity.getContext().getResources().getString(R.string.networkBText),Color.rgb(0, 128, 190)),
	NETWORK_C(MainActivity.getContext().getResources().getString(R.string.networkCText),Color.rgb(115, 0, 190)),
	NETWORK_D(MainActivity.getContext().getResources().getString(R.string.networkDText),Color.rgb(115, 128, 0)),
	NETWORK_E(MainActivity.getContext().getResources().getString(R.string.networkEText),Color.rgb(255, 128, 190)),
	NETWORK_F(MainActivity.getContext().getResources().getString(R.string.networkFText),Color.rgb(115, 255, 190)),
	NETWORK_G(MainActivity.getContext().getResources().getString(R.string.networkGText),Color.rgb(115, 128, 255)),
	NETWORK_H(MainActivity.getContext().getResources().getString(R.string.networkHText),Color.rgb(255, 128, 0));

	private String text;
	private int color;
	
	private static Map<String, Network> textToNetworkMapping;
	
	private Network(String text, int color){
		this.text = text;
		this.color = color;
	}
	
	private static void initMapping() {
		textToNetworkMapping = new HashMap<String, Network>();
        for (Network s : values()) {
        	textToNetworkMapping.put(s.getText(), s);
        }
    }
	
	public static Network getNetworkByText(String text){
		if(textToNetworkMapping == null){
			initMapping();
		}
		return textToNetworkMapping.get(text);
	}
	
	public String getText(){
		return text;
	}
	
	public int getColor(){
		return color;
	}
}
