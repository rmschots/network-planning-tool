package com.ugent.networkplanningtool.data;

import java.util.HashMap;
import java.util.Map;

import com.ugent.networkplanningtool.MainActivity;
import com.ugent.networkplanningtool.R;

public enum FrequencyBand {
	FREQBAND_1(MainActivity.getInstance().getResources().getString(R.string.frequencyBand1),2412),
	FREQBAND_2(MainActivity.getInstance().getResources().getString(R.string.frequencyBand2),2417),
	FREQBAND_3(MainActivity.getInstance().getResources().getString(R.string.frequencyBand3),2422),
	FREQBAND_4(MainActivity.getInstance().getResources().getString(R.string.frequencyBand4),2427),
	FREQBAND_5(MainActivity.getInstance().getResources().getString(R.string.frequencyBand5),2432),
	FREQBAND_6(MainActivity.getInstance().getResources().getString(R.string.frequencyBand6),2437),
	FREQBAND_7(MainActivity.getInstance().getResources().getString(R.string.frequencyBand7),2442),
	FREQBAND_8(MainActivity.getInstance().getResources().getString(R.string.frequencyBand8),2447),
	FREQBAND_9(MainActivity.getInstance().getResources().getString(R.string.frequencyBand9),2452),
	FREQBAND_10(MainActivity.getInstance().getResources().getString(R.string.frequencyBand10),2457),
	FREQBAND_11(MainActivity.getInstance().getResources().getString(R.string.frequencyBand11),2462);
	
	private String text;
	private int number;
	
	private static Map<String, FrequencyBand> textToFreqBandMapping;
	private static Map<Integer, FrequencyBand> numberToFreqBandMapping;
	
	private FrequencyBand(String text, int number){
		this.text = text;
		this.number = number;
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
	
	private static void initMapping2() {
		numberToFreqBandMapping = new HashMap<Integer, FrequencyBand>();
        for (FrequencyBand s : values()) {
        	numberToFreqBandMapping.put(s.getNumber(), s);
        }
    }
	
	public static FrequencyBand getFreqBandByNumber(int number){
		if(numberToFreqBandMapping == null){
			initMapping2();
		}
		return numberToFreqBandMapping.get(number);
	}
	
	public String getText(){
		return text;
	}
	
	public int getNumber(){
		return number;
	}
}
