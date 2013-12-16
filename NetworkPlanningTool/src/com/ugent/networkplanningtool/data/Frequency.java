package com.ugent.networkplanningtool.data;

import java.util.HashMap;
import java.util.Map;

import com.ugent.networkplanningtool.MainActivity;
import com.ugent.networkplanningtool.R;

public enum Frequency {
	FREQ_2100(MainActivity.getInstance().getResources().getString(R.string.frequency2100)),
	FREQ_2400(MainActivity.getInstance().getResources().getString(R.string.frequency2400)),
	FREQ_2600(MainActivity.getInstance().getResources().getString(R.string.frequency2600));
	
	private String text;
	
	private static Map<String, Frequency> textToFreqMapping;
	
	private Frequency(String text){
		this.text = text;
	}
	
	private static void initMapping() {
		textToFreqMapping = new HashMap<String, Frequency>();
        for (Frequency s : values()) {
        	textToFreqMapping.put(s.getText(), s);
        }
    }
	
	public static Frequency getFrequencyByText(String text){
		if(textToFreqMapping == null){
			initMapping();
		}
		return textToFreqMapping.get(text);
	}
	
	public String getText(){
		return text;
	}
}
