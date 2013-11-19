package com.ugent.networkplanningtool.data;

import java.util.HashMap;
import java.util.Map;

import com.ugent.networkplanningtool.MainActivity;
import com.ugent.networkplanningtool.R;

public enum RadioModel {
	DLINK(MainActivity.getContext().getResources().getString(R.string.dlinkText)),
	CUSTOM(MainActivity.getContext().getResources().getString(R.string.customText)),
	CC2420(MainActivity.getContext().getResources().getString(R.string.cc2420Text)),
	JN516x(MainActivity.getContext().getResources().getString(R.string.jn516xtext)),
	HUAWEI(MainActivity.getContext().getResources().getString(R.string.huaweiText));

	private String text;
	
	private static Map<String, RadioModel> textToRadioModelMapping;
	
	private RadioModel(String text){
		this.text = text;
	}
	
	private static void initMapping() {
		textToRadioModelMapping = new HashMap<String, RadioModel>();
        for (RadioModel s : values()) {
        	textToRadioModelMapping.put(s.getText(), s);
        }
    }
	
	public static RadioModel getRadioModelByText(String text){
		if(textToRadioModelMapping == null){
			initMapping();
		}
		return textToRadioModelMapping.get(text);
	}
	
	public String getText(){
		return text;
	}
}
