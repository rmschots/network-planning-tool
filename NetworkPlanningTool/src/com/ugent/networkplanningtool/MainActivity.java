package com.ugent.networkplanningtool;

import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ViewFlipper;
import android.widget.ZoomControls;
import ar.com.daidalos.afiledialog.FileChooserDialog;
import ar.com.daidalos.afiledialog.FileChooserDialog.OnFileSelectedListener;

import com.ugent.networkplanningtool.data.AccessPoint;
import com.ugent.networkplanningtool.data.ActivityType;
import com.ugent.networkplanningtool.data.ConnectionPoint;
import com.ugent.networkplanningtool.data.ConnectionPointType;
import com.ugent.networkplanningtool.data.DataActivity;
import com.ugent.networkplanningtool.data.DataObject;
import com.ugent.networkplanningtool.data.Material;
import com.ugent.networkplanningtool.data.Network;
import com.ugent.networkplanningtool.data.RadioModel;
import com.ugent.networkplanningtool.data.RadioType;
import com.ugent.networkplanningtool.data.Thickness;
import com.ugent.networkplanningtool.data.Wall;
import com.ugent.networkplanningtool.data.WallType;
import com.ugent.networkplanningtool.layout.DrawingView;
import com.ugent.networkplanningtool.layout.MyScrollBar;
import com.ugent.networkplanningtool.model.DrawingModel;
import com.ugent.networkplanningtool.model.FloorPlanModel;

public class MainActivity extends Activity implements Observer,OnTouchListener{
	
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
	
	private RadioGroup wallMaterialRadioGroup;
	private RadioGroup doorMaterialRadioGroup;
	private RadioGroup windowMaterialRadioGroup;
	private RadioGroup wallThicknessRadioGroup;
	private RadioGroup doorThicknessRadioGroup;
	private RadioGroup windowThicknessRadioGroup;
	
	private RadioGroup activityTypeRadioGroup;
	
	private RadioGroup connectionTypeRadioGroup;
	
	private ZoomControls zoomControls;
	private ImageButton undoButton;
	private ImageButton redoButton;
	
	private DrawingModel drawingModel;
	private FloorPlanModel floorPlanModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_main);
        
        designView = (DrawingView) findViewById(R.id.drawingView);
        drawingModel = new DrawingModel(designView.getWidth(), designView.getHeight());
        floorPlanModel = FloorPlanModel.getInstance();
        
        hScrollBar = (MyScrollBar) findViewById(R.id.myScrollBar1);
        vScrollBar = (MyScrollBar) findViewById(R.id.myScrollBar2);
        locationText = (TextView)findViewById(R.id.locationText);
        coordinatesText = (TextView)findViewById(R.id.coordinatesTextView);
        
        mainFlip = (ViewFlipper)findViewById(R.id.mainFlipper);
        designFlip = (ViewFlipper)findViewById(R.id.designFlipper);
        parametersFlip = (ViewFlipper)findViewById(R.id.parametersFlipper);
        toolsFlip = (ViewFlipper)findViewById(R.id.toolsFlipper);
        resultsFlip = (ViewFlipper)findViewById(R.id.resultsFlipper);
        
        wallMaterialRadioGroup = (RadioGroup) findViewById(R.id.wallsMaterialRadioGroup);
        doorMaterialRadioGroup = (RadioGroup) findViewById(R.id.doorsMaterialRadioGroup);
        windowMaterialRadioGroup = (RadioGroup) findViewById(R.id.windowsMaterialRadioGroup);
        wallThicknessRadioGroup = (RadioGroup) findViewById(R.id.wallsThicknessRadioGroup);
        doorThicknessRadioGroup = (RadioGroup) findViewById(R.id.doorsThicknessRadioGroup);
        windowThicknessRadioGroup = (RadioGroup) findViewById(R.id.windowsThicknessRadioGroup);
        
        activityTypeRadioGroup = (RadioGroup) findViewById(R.id.activityTypeRadioGroup);
        
        connectionTypeRadioGroup = (RadioGroup) findViewById(R.id.connectionTypeRadioGroup);
        
        mainActive = findViewById(R.id.designButton);
        designActive = findViewById(R.id.wallsButton);
        parametersActive = findViewById(R.id.recieversButton);
        toolsActive = findViewById(R.id.predictCoverageButton);
        resultsActive = findViewById(R.id.renderDataButton);
        
        zoomControls = (ZoomControls) findViewById(R.id.zoomControls1);
        
        undoButton = (ImageButton) findViewById(R.id.undoButton);
        redoButton = (ImageButton) findViewById(R.id.redoButton);
        
        onMainFlipClick(mainActive);
        onDesignFlipClick(designActive);
        onParametersFlipClick(parametersActive);
        onToolsFlipClick(toolsActive);
        onResultsFlipClick(resultsActive);
        
        
        zoomControls.setOnZoomInClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				drawingModel.zoomIn();
			}
		});
        zoomControls.setOnZoomOutClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				drawingModel.zoomOut();
			}
		});
        
        undoButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				floorPlanModel.undo();
				Log.d("DEBUG","undo");
			}
		});
        
        redoButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				floorPlanModel.redo();
				Log.d("DEBUG","redo");
			}
		});
        
        
        
        designView.setModel(drawingModel);
        hScrollBar.setModel(drawingModel);
        vScrollBar.setModel(drawingModel);
        drawingModel.addObserver(this);
        floorPlanModel.addObserver(this);
        
        designView.setOnTouchListener(this);
        
        OnCheckedChangeListener materialListener = new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId){
				RadioButton rb = (RadioButton) findViewById(checkedId);
				DataObject obj = drawingModel.getTouchDataObject();
				if(obj instanceof Wall){
					Wall w = (Wall) obj;
					w.setMaterial(Material.getMaterialByText(rb.getText().toString()));
				}else{
					// should not happen
				}
			}
		};
		wallMaterialRadioGroup.setOnCheckedChangeListener(materialListener);
		doorMaterialRadioGroup.setOnCheckedChangeListener(materialListener);
		windowMaterialRadioGroup.setOnCheckedChangeListener(materialListener);
		OnCheckedChangeListener thicknessListener = new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId){
				RadioButton rb = (RadioButton) findViewById(checkedId);
				DataObject obj = drawingModel.getTouchDataObject();
				if(obj instanceof Wall){
					Wall w = (Wall) obj;
					w.setThickness(Thickness.getThicknessByText(rb.getText().toString()));
				}else{
					// should not happen
				}
			}
		};
		wallThicknessRadioGroup.setOnCheckedChangeListener(thicknessListener);
		doorThicknessRadioGroup.setOnCheckedChangeListener(thicknessListener);
		windowThicknessRadioGroup.setOnCheckedChangeListener(thicknessListener);
		OnCheckedChangeListener activityTypeListener = new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId){
				RadioButton rb = (RadioButton) findViewById(checkedId);
				DataObject obj = drawingModel.getTouchDataObject();
				if(obj instanceof DataActivity){
					DataActivity da = (DataActivity) obj;
					da.setType(ActivityType.getActivityTypeByText(rb.getText().toString()));
				}else{
					// should not happen
				}
			}
		};
		activityTypeRadioGroup.setOnCheckedChangeListener(activityTypeListener);
		OnCheckedChangeListener connectionTypeListener = new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId){
				RadioButton rb = (RadioButton) findViewById(checkedId);
				DataObject obj = drawingModel.getTouchDataObject();
				if(obj instanceof ConnectionPoint){
					ConnectionPoint cp = (ConnectionPoint) obj;
					cp.setType(ConnectionPointType.getConnectionPointTypeByText(rb.getText().toString()));
				}else{
					// should not happen
				}
			}
		};
		connectionTypeRadioGroup.setOnCheckedChangeListener(connectionTypeListener);
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
			setDrawWall(WallType.WALL,wallMaterialRadioGroup,wallThicknessRadioGroup);
		}else if(tag.equals("doors")){
			setDrawWall(WallType.DOOR,doorMaterialRadioGroup,doorThicknessRadioGroup);
		}else if(tag.equals("windows")){
			setDrawWall(WallType.WINDOW,windowMaterialRadioGroup,windowThicknessRadioGroup);
		}else if(tag.equals("accesspoints")){
			drawingModel.setTouchDataObject(new AccessPoint("", 175, RadioType.WIFI, RadioModel.DLINK, 10, 10, 10, 10, Network.NETWORK_A));
		}else if(tag.equals("activities")){
			drawingModel.setTouchDataObject(new DataActivity(ActivityType.HD_VIDEO));
		}else if(tag.equals("connections")){
			drawingModel.setTouchDataObject(new ConnectionPoint(ConnectionPointType.POWER));
		}else{
			Log.e("DEBUG","LOLWUTUTRYNTODRAW?");
		}
	}
	
	private void setDrawAccessPoints(){
		// TODO
	}
	
	private void setDrawWall(WallType wallType, RadioGroup materialRadioGroup, RadioGroup thicknessRadiogroup){
		RadioButton rb = (RadioButton) findViewById(materialRadioGroup.getCheckedRadioButtonId());
		Material material = Material.getMaterialByText(rb.getText().toString());
		rb = (RadioButton) findViewById(thicknessRadiogroup.getCheckedRadioButtonId());
		Thickness thickness = Thickness.getThicknessByText(rb.getText().toString());
		if(drawingModel.getTouchDataObject() instanceof Wall){
			Wall wall = (Wall) drawingModel.getTouchDataObject();
			wall.setMaterial(material);
		}else{
			drawingModel.setTouchDataObject(new Wall(wallType, thickness, material));
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
		String newCoords = Math.round(drawingModel.getOffsetX()*100)/100+":"+Math.round(drawingModel.getOffsetY()*100)/100;
		if(newCoords.equals(coordinatesText.getText())){
			coordinatesText.setText(newCoords);
		}
		
		//Log.d("DEBUG",""+model.isZoomInMaxed());
		zoomControls.setIsZoomInEnabled(!drawingModel.isZoomInMaxed());
		zoomControls.setIsZoomOutEnabled(!drawingModel.isZoomOutMaxed());
		
		Log.d("DEBUG","UPDATE");
		undoButton.setEnabled(floorPlanModel.canUndo());
		redoButton.setEnabled(floorPlanModel.canRedo());
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
		FileChooserDialog dialog = new FileChooserDialog(this,Environment.getExternalStorageDirectory().getAbsolutePath());
		dialog.addListener(new OnFileSelectedListener() {
			
			@Override
			public void onFileSelected(Dialog source, File folder, String name) {
			}
			
			@Override
			public void onFileSelected(Dialog source, File file) {
				Log.d("DEBUG",file.getAbsolutePath());
				source.dismiss();
				try {
					FloorPlanModel.loadFloorPlan(file);
				} catch (Exception e) {
					Log.d("DEBUG","Error loading file: "+e);
					e.printStackTrace();
				}
			}
		});
		// dialog.setFilter(".*jpg|.*png|.*gif|.*JPG|.*PNG|.*GIF");
		dialog.show();
		
	}
	
	public void handleSaveClick(View v){
		final Dialog d = new Dialog(this);
		d.setTitle(R.string.savePlanTitle);
		d.setContentView(R.layout.save_floorplan);
		Button saveButton = (Button)d.findViewById(R.id.saveButton);
		saveButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				d.dismiss();
				String fileName = ((EditText)d.findViewById(R.id.fileNameEditText)).getText().toString();
				if(((CheckBox)d.findViewById(R.id.saveInDefaultFolderCheckBox)).isChecked()){
					Log.d("DEBUG","3: "+Environment.getExternalStorageDirectory().getAbsolutePath());
					File f = new File(Environment.getExternalStorageDirectory(),fileName);
					if(!f.exists()){
						try {
							f.createNewFile();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							Log.e("DEBUG","cannot create new file");
						}
					}
					try {
						floorPlanModel.saveFloorPlan(f);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						String StackTrace = "";
						for(StackTraceElement s : e.getStackTrace()){
							StackTrace+=" "+s.toString();
						}
						Log.d("DEBUG","error saving to "+f.getAbsolutePath()+" "+StackTrace);
						e.printStackTrace();
					}
				}else{
					FileChooserDialog dialog = new FileChooserDialog(MainActivity.this);
					dialog.addListener(new OnFileSelectedListener() {
						
						@Override
						public void onFileSelected(Dialog source, File folder, String name) {
							Log.d("DEBUG","1111");
						}
						
						@Override
						public void onFileSelected(Dialog source, File file) {
							Log.d("DEBUG","2222");
						}
					});
					dialog.setFolderMode(true);
					dialog.setCanCreateFiles(true);
					dialog.show();
				}
				
			}
		});
		d.show();
	}
	
	public void handleNewFileClick(View v){
		floorPlanModel.reset();
	}
	
	public static Context getContext(){
        return mContext;
    }
	
	public void handleStopDrawing(View view){
		drawingModel.setTouchDataObject(drawingModel.getTouchDataObject());
	}
}
