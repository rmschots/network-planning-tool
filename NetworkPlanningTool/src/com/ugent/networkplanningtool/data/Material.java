package com.ugent.networkplanningtool.data;

import java.util.HashMap;
import java.util.Map;

import com.ugent.networkplanningtool.MainActivity;
import com.ugent.networkplanningtool.R;

import android.graphics.Color;

public enum Material {
	BRICK(MainActivity.getContext().getResources().getString(R.string.brickText),Color.rgb(221,0,0)),
	LAYERED_DRYWALL(MainActivity.getContext().getResources().getString(R.string.layeredDrywallText),Color.rgb(176,129,64)),
	CONCRETE(MainActivity.getContext().getResources().getString(R.string.concreteText),Color.rgb(136,136,136)),
	WOOD(MainActivity.getContext().getResources().getString(R.string.woodText),Color.rgb(252,206,140)),
	GLASS(MainActivity.getContext().getResources().getString(R.string.glassText),Color.rgb(149,165,236)),
	METAL(MainActivity.getContext().getResources().getString(R.string.metalText),Color.rgb(102,102,153)),
	ZERO_DB(MainActivity.getContext().getResources().getString(R.string.zeroDbText),Color.rgb(221,221,221));

	private String text;
	private int color;
	
	private static Map<String, Material> textToMaterialMapping;
	
	private Material(String text, int color){
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
