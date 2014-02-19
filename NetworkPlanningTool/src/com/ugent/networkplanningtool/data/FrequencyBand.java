package com.ugent.networkplanningtool.data;

import java.util.HashMap;
import java.util.Map;

import com.ugent.networkplanningtool.MainActivity;
import com.ugent.networkplanningtool.R;

public enum FrequencyBand {
	FREQBAND_2100(MainActivity.getInstance().getResources().getString(R.string.frequencyBand2100)),
	FREQBAND_2400(MainActivity.getInstance().getResources().getString(R.string.frequencyBand2400)),
	FREQBAND_2600(MainActivity.getInstance().getResources().getString(R.string.frequencyBand2600));
	
	private String text;
	
	private static Map<String, FrequencyBand> textToFreqBandMapping;
	
	private FrequencyBand(String text){
		this.text = text;
	}
	
	private static void initMapping() {
		textToFreqBandMapping = new HashMap<String, FrequencyBand>();
        for (FrequencyBand s : values()) {
        	textToFreqBandMapping.put(s.getText(), s);
        }
    }
	
	public static FrequencyBand getFrequencyBandByText(String text){
		if(textToFreqBandMapping == null){
			initMapping();
		}
		FrequencyBand fb = textToFreqBandMapping.get(text);
		return fb==null?FREQBAND_2400:fb;
	}
	
	public String getText(){
		return text;
	}
}
