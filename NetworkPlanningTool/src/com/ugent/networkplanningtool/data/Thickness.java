package com.ugent.networkplanningtool.data;

import java.util.HashMap;
import java.util.Map;

import com.ugent.networkplanningtool.MainActivity;
import com.ugent.networkplanningtool.R;

public enum Thickness {
	THICK(MainActivity.getInstance().getResources().getString(R.string.thickText),30),
	THIN(MainActivity.getInstance().getResources().getString(R.string.thinText),10);
	
	private String text;
	private int number;
	private static Map<String, Thickness> textToThicknessMapping;
	private static Map<Integer, Thickness> numberToThicknessMapping;
	
	private Thickness(String text, int number){
		this.text = text;
		this.number = number;
	}
	
	private static void initMapping1() {
		textToThicknessMapping = new HashMap<String, Thickness>();
        for (Thickness s : values()) {
        	textToThicknessMapping.put(s.getText(), s);
        }
    }
	
	public static Thickness getThicknessByText(String text){
		if(textToThicknessMapping == null){
			initMapping1();
		}
		return textToThicknessMapping.get(text);
	}
	
	private static void initMapping2() {
		numberToThicknessMapping = new HashMap<Integer, Thickness>();
        for (Thickness s : values()) {
        	numberToThicknessMapping.put(s.getNumber(), s);
        }
    }
	
	public static Thickness getThicknessByNumber(int number){
		if(numberToThicknessMapping == null){
			initMapping2();
		}
		return numberToThicknessMapping.get(number);
	}
	
	public String getText(){
		return text;
	}
	
	public int getNumber(){
		return number;
	}
}
