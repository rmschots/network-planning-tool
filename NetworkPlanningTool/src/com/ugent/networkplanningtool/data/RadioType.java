package com.ugent.networkplanningtool.data;

import java.util.HashMap;
import java.util.Map;

import com.ugent.networkplanningtool.MainActivity;
import com.ugent.networkplanningtool.R;

public enum RadioType {
	WIFI(MainActivity.getContext().getResources().getString(R.string.wifiText)),
	SENSOR(MainActivity.getContext().getResources().getString(R.string.sensorText)),
	LTE_FEMTOCELL(MainActivity.getContext().getResources().getString(R.string.lteFemtocellText)),
	UMTS_FEMTOCELL(MainActivity.getContext().getResources().getString(R.string.umtsFemtocellText));

	private String text;
	
	private static Map<String, RadioType> textToRadioTypeMapping;
	
	private RadioType(String text){
		this.text = text;
	}
	
	private static void initMapping() {
		textToRadioTypeMapping = new HashMap<String, RadioType>();
        for (RadioType s : values()) {
        	textToRadioTypeMapping.put(s.getText(), s);
        }
    }
	
	public static RadioType getRadioTypeByText(String text){
		if(textToRadioTypeMapping == null){
			initMapping();
		}
		return textToRadioTypeMapping.get(text);
	}
	
	public String getText(){
		return text;
	}
}
