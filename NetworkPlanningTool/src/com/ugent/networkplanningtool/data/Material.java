package com.ugent.networkplanningtool.data;

import java.util.HashMap;
import java.util.Map;

import com.ugent.networkplanningtool.MainActivity;
import com.ugent.networkplanningtool.R;

import android.graphics.Color;
import android.util.Log;

public enum Material {
	BRICK(MainActivity.getContext().getResources().getString(R.string.brickText),Color.RED),
	LAYERED_DRYWALL(MainActivity.getContext().getResources().getString(R.string.layeredDrywallText),Color.rgb(205,133,63)),
	CONCRETE(MainActivity.getContext().getResources().getString(R.string.concreteText),Color.DKGRAY),
	WOOD(MainActivity.getContext().getResources().getString(R.string.woodText),Color.rgb(255,165,0)),
	GLASS(MainActivity.getContext().getResources().getString(R.string.glassText),Color.rgb(132,112,255)),
	METAL(MainActivity.getContext().getResources().getString(R.string.metalText),Color.rgb(106,90,205)),
	ZERO_DB(MainActivity.getContext().getResources().getString(R.string.zeroDbText),Color.LTGRAY);

	private String text;
	private int color;
	
	private static Map<String, Material> textToMaterialMapping;
	
	private Material(String text, int color){
		Log.d("TEST",text);
		this.text = text;
		this.color = color;
		
	}
	
	private static void initMapping() {
		textToMaterialMapping = new HashMap<String, Material>();
        for (Material s : values()) {
        	textToMaterialMapping.put(s.getText(), s);
        }
    }
	
	public static Material getMaterialByText(String text){
		if(textToMaterialMapping == null){
			initMapping();
		}
		return textToMaterialMapping.get(text);
	}
	
	public int getColor(){
		return color;
	}
	
	public String getText(){
		return text;
	}
}
