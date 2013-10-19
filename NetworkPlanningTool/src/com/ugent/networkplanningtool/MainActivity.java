package com.ugent.networkplanningtool;

import java.util.Observable;
import java.util.Observer;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.ViewFlipper;

import com.ugent.networkplanningtool.layout.DrawingView;
import com.ugent.networkplanningtool.layout.MyScrollBar;
import com.ugent.networkplanningtool.model.FloorModel;

public class MainActivity extends Activity implements Observer,OnTouchListener{
	
	private DrawingView designView;
	private TextView locationText;
	private TextView coordinatesText;
	private MyScrollBar hScrollBar;
	private MyScrollBar vScrollBar;
	
	private View designActive;
	private View mainActive;
	private View parametersActive;
	
	
	private ViewFlipper mainFlip;
	private ViewFlipper designFlip;
	private ViewFlipper parametersFlip;
	
	private FloorModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        designView = (DrawingView) findViewById(R.id.drawingView);
        hScrollBar = (MyScrollBar) findViewById(R.id.myScrollBar1);
        vScrollBar = (MyScrollBar) findViewById(R.id.myScrollBar2);
        locationText = (TextView)findViewById(R.id.locationText);
        coordinatesText = (TextView)findViewById(R.id.coordinatesTextView);
        
        mainFlip = (ViewFlipper)findViewById(R.id.mainFlipper);
        designFlip = (ViewFlipper)findViewById(R.id.designFlipper);
        parametersFlip = (ViewFlipper)findViewById(R.id.parametersFlipper);
        
        mainActive = findViewById(R.id.designButton);
        onMainFlipClick(mainActive);
        
        designActive = findViewById(R.id.wallsButton);
        onDesignFlipClick(designActive);
        
        parametersActive = findViewById(R.id.recieversButton);
        onParametersFlipClick(parametersActive);
        
        model = new FloorModel(designView.getWidth(), designView.getHeight());
        
        designView.setModel(model);
        hScrollBar.setModel(model);
        vScrollBar.setModel(model);
        model.addObserver(this);
        
        designView.setOnTouchListener(this);
        
    }

	public void onMainFlipClick(View v) {
		mainActive.setEnabled(true);
		v.setEnabled(false);
		mainActive = v;
		Object o = v.getTag();
		for(int i = 0; i < mainFlip.getChildCount(); i ++){
			if(mainFlip.getChildAt(i).getTag().equals(o)){
				mainFlip.setDisplayedChild(i);
				return;
			}
		}
	}
	
	public void onDesignFlipClick(View v) {
		designActive.setEnabled(true);
		v.setEnabled(false);
		designActive = v;
		Object tag = v.getTag();
		for(int i = 0; i < designFlip.getChildCount(); i ++){
			if(designFlip.getChildAt(i).getTag().equals(tag)){
				designFlip.setDisplayedChild(i);
				return;
			}
		}
	}
	
	public void onParametersFlipClick(View v) {
		parametersActive.setEnabled(true);
		v.setEnabled(false);
		parametersActive = v;
		Object o = v.getTag();
		for(int i = 0; i < parametersFlip.getChildCount(); i ++){
			if(parametersFlip.getChildAt(i).getTag().equals(o)){
				parametersFlip.setDisplayedChild(i);
				return;
			}
		}
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		coordinatesText.setText(Math.round(model.getOffsetX()*100)/100+":"+Math.round(model.getOffsetY()*100)/100);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if(v == designView){
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
	        case MotionEvent.ACTION_MOVE:
	        case MotionEvent.ACTION_UP:
	        case MotionEvent.ACTION_CANCEL:
	        	locationText.setText((int)event.getX(0)+":"+(int)event.getY(0));
	            break;
	        default: break;
			}
		}
		return false;
	}
}
