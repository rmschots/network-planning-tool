package com.ugent.networkplanningtool;

import java.io.File;
import java.util.Observable;
import java.util.Observer;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ViewFlipper;
import android.widget.ZoomControls;
import ar.com.daidalos.afiledialog.FileChooserDialog;
import ar.com.daidalos.afiledialog.FileChooserDialog.OnFileSelectedListener;

import com.ugent.networkplanningtool.data.AccessPoint;
import com.ugent.networkplanningtool.data.DataObject;
import com.ugent.networkplanningtool.data.Material;
import com.ugent.networkplanningtool.data.Network;
import com.ugent.networkplanningtool.data.RadioModel;
import com.ugent.networkplanningtool.data.RadioType;
import com.ugent.networkplanningtool.data.Wall;
import com.ugent.networkplanningtool.data.WallType;
import com.ugent.networkplanningtool.layout.DrawingView;
import com.ugent.networkplanningtool.layout.MyScrollBar;
import com.ugent.networkplanningtool.model.DrawingModel;
import com.ugent.networkplanningtool.model.FloorPlanModel;

public class MainActivity extends Activity implements Observer,OnTouchListener,OnFileSelectedListener{
	
	private static Context mContext;
	
	private DrawingView designView;
	private TextView locationText;
	private TextView coordinatesText;
	private MyScrollBar hScrollBar;
	private MyScrollBar vScrollBar;
	
	private View designActive;
	private View mainActive;
	private View parametersActive;
	private View toolsActive;
	private View resultsActive;
	
	private ViewFlipper mainFlip;
	private ViewFlipper designFlip;
	private ViewFlipper parametersFlip;
	private ViewFlipper toolsFlip;
	private ViewFlipper resultsFlip;
	
	private ZoomControls zoomControls;
	
	private DrawingModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_main);
        
        designView = (DrawingView) findViewById(R.id.drawingView);
        model = new DrawingModel(designView.getWidth(), designView.getHeight());
        
        hScrollBar = (MyScrollBar) findViewById(R.id.myScrollBar1);
        vScrollBar = (MyScrollBar) findViewById(R.id.myScrollBar2);
        locationText = (TextView)findViewById(R.id.locationText);
        coordinatesText = (TextView)findViewById(R.id.coordinatesTextView);
        
        mainFlip = (ViewFlipper)findViewById(R.id.mainFlipper);
        designFlip = (ViewFlipper)findViewById(R.id.designFlipper);
        parametersFlip = (ViewFlipper)findViewById(R.id.parametersFlipper);
        toolsFlip = (ViewFlipper)findViewById(R.id.toolsFlipper);
        resultsFlip = (ViewFlipper)findViewById(R.id.resultsFlipper);
        
        mainActive = findViewById(R.id.designButton);
        designActive = findViewById(R.id.wallsButton);
        parametersActive = findViewById(R.id.recieversButton);
        toolsActive = findViewById(R.id.predictCoverageButton);
        resultsActive = findViewById(R.id.renderDataButton);
        onMainFlipClick(mainActive);
        onDesignFlipClick(designActive);
        onParametersFlipClick(parametersActive);
        onToolsFlipClick(toolsActive);
        onResultsFlipClick(resultsActive);
        
        zoomControls = (ZoomControls) findViewById(R.id.zoomControls1);
        zoomControls.setOnZoomInClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				model.zoomIn();
			}
		});
        zoomControls.setOnZoomOutClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				model.zoomOut();
			}
		});
        
        
        
        designView.setModel(model);
        hScrollBar.setModel(model);
        vScrollBar.setModel(model);
        model.addObserver(this);
        
        designView.setOnTouchListener(this);
        
        OnCheckedChangeListener materialListener = new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId){
				DataObject obj = model.getDrawItem();
				if(obj instanceof Wall){
					Wall w = (Wall) obj;
					RadioButton rb = (RadioButton) group.getChildAt(checkedId);
					w.setMaterial(Material.getMaterialByText(rb.getText().toString()));
					model.setDrawItem(w);
				}else{
					// should not happen
				}
			}
		};
		RadioGroup rg = (RadioGroup) findViewById(R.id.wallsMaterialRadioGroup);
		rg.setOnCheckedChangeListener(materialListener);
    }


	public void onMainFlipClick(View v) {
		mainActive.setEnabled(true);
		mainActive = v;
		onFlipClick(v, mainFlip);
	}
	
	public void onDesignFlipClick(View v) {
		designActive.setEnabled(true);
		designActive = v;
		View flippedView = onFlipClick(v, designFlip);
		Object tag = flippedView.getTag();
		if(tag.equals("walls")){
			model.setDrawItem(new Wall(-1, -1, WallType.WALL, 10, Material.BRICK));
		}else if(tag.equals("doors")){
			model.setDrawItem(new Wall(-1, -1, WallType.DOOR, 10, Material.METAL));
		}else if(tag.equals("windows")){
			model.setDrawItem(new Wall(-1, -1, WallType.WINDOW, 10, Material.GLASS));
		}else if(tag.equals("accesspoints")){
			model.setDrawItem(new AccessPoint(-1, -1, "", 175, RadioType.WIFI, RadioModel.DLINK, 10, 10, 10, 10, Network.NETWORK_A));
		}
	}
	
	public void onParametersFlipClick(View v) {
		parametersActive.setEnabled(true);
		parametersActive = v;
		onFlipClick(v, parametersFlip);
	}
	
	public void onToolsFlipClick(View v) {
		toolsActive.setEnabled(true);
		toolsActive = v;
		onFlipClick(v, toolsFlip);
	}
	
	public void onResultsFlipClick(View v) {
		resultsActive.setEnabled(true);
		resultsActive = v;
		onFlipClick(v, resultsFlip);
	}
	
	private View onFlipClick(View v, ViewFlipper vf){
		v.setEnabled(false);
		Object o = v.getTag();
		for(int i = 0; i < vf.getChildCount(); i ++){
			if(vf.getChildAt(i).getTag().equals(o)){
				vf.setDisplayedChild(i);
				return vf.getChildAt(i);
			}
		}
		return null;
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		String newCoords = Math.round(model.getOffsetX()*100)/100+":"+Math.round(model.getOffsetY()*100)/100;
		if(newCoords.equals(coordinatesText.getText())){
			coordinatesText.setText(newCoords);
		}
		
		//Log.d("DEBUG",""+model.isZoomInMaxed());
		zoomControls.setIsZoomInEnabled(!model.isZoomInMaxed());
		zoomControls.setIsZoomOutEnabled(!model.isZoomOutMaxed());
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
	
	public void handleOpenFileClick(View v){
		FileChooserDialog dialog = new FileChooserDialog(this);
		dialog.addListener(this);
		// dialog.setFilter(".*jpg|.*png|.*gif|.*JPG|.*PNG|.*GIF");
		dialog.show();
		
	}
	
	public void handleNewFileClick(View v){
		FloorPlanModel.getInstance().reset();
	}


	@Override
	public void onFileSelected(Dialog source, File file) {
		source.dismiss();
		try {
			FloorPlanModel.loadFloorPlan(file);
		} catch (Exception e) {
			Log.d("DEBUG","Error loading file: "+e);
			e.printStackTrace();
		}
	}


	@Override
	public void onFileSelected(Dialog source, File folder, String name) {
		// TODO Auto-generated method stub
		
	}
	
	public static Context getContext(){
        return mContext;
    }
	
	public void handleStopDrawing(View view){
		model.setDrawItem(model.getDrawItem());
	}
}
