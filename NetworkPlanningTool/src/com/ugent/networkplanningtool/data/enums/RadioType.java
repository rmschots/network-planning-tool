package com.ugent.networkplanningtool.data.enums;

import com.ugent.networkplanningtool.MainActivity;
import com.ugent.networkplanningtool.R;

import java.util.HashMap;
import java.util.Map;

public enum RadioType {
    WIFI(MainActivity.getInstance().getResources().getString(R.string.wifiText), new FrequencyBand[]{FrequencyBand.FREQBAND_2400}),
    SENSOR(MainActivity.getInstance().getResources().getString(R.string.sensorText), new FrequencyBand[]{FrequencyBand.FREQBAND_2400}),
    LTE_FEMTOCELL(MainActivity.getInstance().getResources().getString(R.string.lteFemtocellText), new FrequencyBand[]{FrequencyBand.FREQBAND_2600}),
    UMTS_FEMTOCELL(MainActivity.getInstance().getResources().getString(R.string.umtsFemtocellText), new FrequencyBand[]{FrequencyBand.FREQBAND_2100});

    private String text;
    private FrequencyBand[] fbs;

    private static Map<String, RadioType> textToRadioTypeMapping;

    private RadioType(String text, FrequencyBand[] fbs) {
        this.text = text;
        this.fbs = fbs;
    }
	
	private static void initMapping() {
		textToRadioTypeMapping = new HashMap<String, RadioType>();
        for (RadioType s : values()) {
        	textToRadioTypeMapping.put(s.getText().toLowerCase(), s);
        }
    }
	
	public static RadioType getRadioTypeByText(String text){
		if(textToRadioTypeMapping == null){
			initMapping();
		}
		return textToRadioTypeMapping.get(text.toLowerCase());
	}
	
	public String getText(){
		return text;
	}

    public FrequencyBand[] getFrequencyBands() {
        return fbs;
    }

    @Override
    public String toString() {
        return text;
    }
}
