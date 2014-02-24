package com.ugent.networkplanningtool.layout.dataobject;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.ugent.networkplanningtool.MainActivity;
import com.ugent.networkplanningtool.R;

public abstract class DataObjectView extends LinearLayout {

	public static enum ViewType{
		DRAW(MainActivity.getInstance().getResources().getString(R.string.drawDataObjectText)),
		EDIT(MainActivity.getInstance().getResources().getString(R.string.editDataObjectText)),
		INFO(MainActivity.getInstance().getResources().getString(R.string.infoDataObjectText));
		private String text;
		private static Map<String, ViewType> textToViewTypeMapping;
		private ViewType(String text){
			this.text = text;
		}
		private static void initMapping() {
			textToViewTypeMapping = new HashMap<String, ViewType>();
	        for (ViewType s : values()) {
	        	textToViewTypeMapping.put(s.getText(), s);
	        }
	    }
		public static ViewType getViewTypeByText(String text){
			if(textToViewTypeMapping == null){
				initMapping();
			}
			return textToViewTypeMapping.get(text);
		}
		public String getText(){
			return text;
		}
	}

	protected ViewType type;
	
	public DataObjectView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DataObjectView);
		type = ViewType.getViewTypeByText(a.getString(R.styleable.DataObjectView_draw));
		a.recycle();
	}

	public DataObjectView(Context context, AttributeSet attrs) {
		super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DataObjectView);
        type = ViewType.getViewTypeByText(a.getString(R.styleable.DataObjectView_draw));
        a.recycle();
	}

	public DataObjectView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public DataObjectView(Context context, ViewType type){
		this(context);
		this.type = type;
	}
	

}
