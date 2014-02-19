package com.ugent.networkplanningtool.data;

import java.util.HashMap;
import java.util.Map;

import com.ugent.networkplanningtool.MainActivity;
import com.ugent.networkplanningtool.R;

public enum RadioModel {
	DLINK(MainActivity.getInstance().getResources().getString(R.string.dlinkText)),
	CUSTOM(MainActivity.getInstance().getResources().getString(R.string.customText)),
	CC2420(MainActivity.getInstance().getResources().getString(R.string.cc2420Text)),
	JN516x(MainActivity.getInstance().getResources().getString(R.string.jn516xtext)),
	HUAWEI(MainActivity.getInstance().getResources().getString(R.string.huaweiText));

	private String text;
	
	private static Map<String, RadioModel> textToRadioModelMapping;
	
	private RadioModel(String text){
		this.text = text;
	}
	
	private static void initMapping() {
		textToRadioModelMapping = new HashMap<String, RadioModel>();
        for (RadioModel s : values()) {
        	textToRadioModelMapping.put(s.getText().toLowerCase(), s);
        }
    }
	
	public static RadioModel getRadioModelByText(String text){
		if(textToRadioModelMapping == null){
			initMapping();
		}
		return textToRadioModelMapping.get(text.toLowerCase());
	}
	
	public String getText(){
		return text;
	}
}
