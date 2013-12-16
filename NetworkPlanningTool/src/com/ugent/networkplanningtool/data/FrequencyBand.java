package com.ugent.networkplanningtool.data;

import java.util.HashMap;
import java.util.Map;

import com.ugent.networkplanningtool.MainActivity;
import com.ugent.networkplanningtool.R;

public enum FrequencyBand {
	FREQBAND_1(MainActivity.getInstance().getResources().getString(R.string.frequencyBand1)),
	FREQBAND_2(MainActivity.getInstance().getResources().getString(R.string.frequencyBand2)),
	FREQBAND_3(MainActivity.getInstance().getResources().getString(R.string.frequencyBand3)),
	FREQBAND_4(MainActivity.getInstance().getResources().getString(R.string.frequencyBand4)),
	FREQBAND_5(MainActivity.getInstance().getResources().getString(R.string.frequencyBand5)),
	FREQBAND_6(MainActivity.getInstance().getResources().getString(R.string.frequencyBand6)),
	FREQBAND_7(MainActivity.getInstance().getResources().getString(R.string.frequencyBand7)),
	FREQBAND_8(MainActivity.getInstance().getResources().getString(R.string.frequencyBand8)),
	FREQBAND_9(MainActivity.getInstance().getResources().getString(R.string.frequencyBand9)),
	FREQBAND_10(MainActivity.getInstance().getResources().getString(R.string.frequencyBand10)),
	FREQBAND_11(MainActivity.getInstance().getResources().getString(R.string.frequencyBand11)),;
	
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
		return textToFreqBandMapping.get(text);
	}
	
	public String getText(){
		return text;
	}
}
