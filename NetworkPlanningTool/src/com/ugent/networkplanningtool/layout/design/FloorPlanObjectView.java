package com.ugent.networkplanningtool.layout.design;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.ugent.networkplanningtool.MainActivity;
import com.ugent.networkplanningtool.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Abstract view for general floor plan objects. To be extended by specific types of floor plan objects.
 */
public abstract class FloorPlanObjectView extends LinearLayout {

    /**
     * Different kinds of types for this to support different user interactions.
     */
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

    /**
     * The ViewType should always be set for each instance
     */
	protected ViewType type;

    /**
     * Default constructor setting the view type to "draw"
     * @param context the context of the parent
     * @param attrs the attribute set
     * @param defStyle the default style to apply
     */
	public FloorPlanObjectView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FloorPlanObjectView);
		type = ViewType.getViewTypeByText(a.getString(R.styleable.FloorPlanObjectView_draw));
		a.recycle();
	}

    /**
     * Default constructor  setting the view type to "draw"
     * @param context the context of the parent
     * @param attrs the attribute set
     */
	public FloorPlanObjectView(Context context, AttributeSet attrs) {
		super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FloorPlanObjectView);
        type = ViewType.getViewTypeByText(a.getString(R.styleable.FloorPlanObjectView_draw));
        a.recycle();
	}

    /**
     * Default constructor
     * @param context the context of the parent
     */
	public FloorPlanObjectView(Context context) {
		super(context);
	}

    /**
     * Default constructor
     * @param context context the context of the parent
     * @param type the view type to be used
     */
	public FloorPlanObjectView(Context context, ViewType type){
		this(context);
		this.type = type;
	}
	

}
