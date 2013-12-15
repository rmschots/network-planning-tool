package com.ugent.networkplanningtool.layout.dataobject;

import com.ugent.networkplanningtool.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class WallView extends LinearLayout {
	
	private RadioGroup materialsRadioGroup;

	public WallView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.WallView);
		boolean draw = a.getBoolean(R.styleable.WallView_draw, false);
		a.recycle();
		
		LayoutInflater inflater = (LayoutInflater) context
		        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.design_view_doors, this, true);
		
		materialsRadioGroup = (RadioGroup) findViewById(R.id.doorsMaterialRadioGroup);
		
		Object tag = getTag();
		if(tag != null){
			if(tag.equals("doors")){
				initDoors();
			}else if(tag.equals("walls")){
				initWalls();
			}else if(tag.equals("windows")){
				initWindows();
			}
		}
		
		if(draw){
			Button stopDrawButton = new Button(context);
			stopDrawButton.setText(R.string.stopDrawingText);
			addView(stopDrawButton);
		}
		
	}
	
	private void initDoors(){
		/*RadioButton rb = new RadioButton(getContext());
		rb.setText("HOIZ");
		materialsRadioGroup.addView(rb);*/
	}
	
	private void initWalls(){
		
	}
	
	private void initWindows(){
		
	}
	

}
