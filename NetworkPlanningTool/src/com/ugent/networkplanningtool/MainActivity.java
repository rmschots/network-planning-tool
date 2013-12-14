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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
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
	
	private RadioGroup wallSnapToRadioGroup;
	private RadioGroup doorSnapToRadioGroup;
	private RadioGroup windowSnapToRadioGroup;
	
	
	private RadioGroup activityTypeRadioGroup;
	
	private RadioGroup connectionTypeRadioGroup;
	
	private Spinner networkSignalTypeSpinner;
	private Spinner networkMhzSpinner;
	private Spinner networkChannelSpinner;
	private Spinner networkModelSpinner;
	private EditText antennaGainEditText;
	private EditText transmitPowerEditText;
	private Spinner networkIDSpinner;
	private EditText elevationEditText;
	
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
        
        wallSnapToRadioGroup = (RadioGroup) findViewById(R.id.wallsSnapToRadioGroup);
        doorSnapToRadioGroup = (RadioGroup) findViewById(R.id.doorsSnapToRadioGroup);
        windowSnapToRadioGroup = (RadioGroup) findViewById(R.id.windowsSnapToRadioGroup);
        
        activityTypeRadioGroup = (RadioGroup) findViewById(R.id.activityTypeRadioGroup);
        
        connectionTypeRadioGroup = (RadioGroup) findViewById(R.id.connectionTypeRadioGroup);
        
        networkSignalTypeSpinner = (Spinner) findViewById(R.id.networkSignalTypeSpinner);
        networkMhzSpinner = (Spinner) findViewById(R.id.networkMHzSpinner);
        networkChannelSpinner = (Spinner) findViewById(R.id.networkChannelSpinner);
        networkModelSpinner = (Spinner) findViewById(R.id.networkModelSpinner);
        transmitPowerEditText = (EditText) findViewById(R.id.transmitPowerEditText);
        antennaGainEditText = (EditText) findViewById(R.id.antennaGainEditText);
        elevationEditText = (EditText) findViewById(R.id.elevationEditText);
        networkIDSpinner = (Spinner) findViewById(R.id.networkIDSpinner);
        
        
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
        
        OnCheckedChangeListener drawCheckedChangedListener = new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId){
				adjustDraw();
			}
		};
		OnItemSelectedListener drawChangedItemListener = new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View arg1, int pos, long arg3) {
				adjustDraw();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		};
		OnCheckedChangeListener snapToListener = new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId){
				RadioButton rb = (RadioButton) findViewById(checkedId);
				drawingModel.setSnapToGrid(rb.getText().equals(getResources().getString(R.string.snapToGridText)));
			}
		};
		wallMaterialRadioGroup.setOnCheckedChangeListener(drawCheckedChangedListener);
		doorMaterialRadioGroup.setOnCheckedChangeListener(drawCheckedChangedListener);
		windowMaterialRadioGroup.setOnCheckedChangeListener(drawCheckedChangedListener);
		wallThicknessRadioGroup.setOnCheckedChangeListener(drawCheckedChangedListener);
		doorThicknessRadioGroup.setOnCheckedChangeListener(drawCheckedChangedListener);
		windowThicknessRadioGroup.setOnCheckedChangeListener(drawCheckedChangedListener);
		wallSnapToRadioGroup.setOnCheckedChangeListener(snapToListener);
		doorSnapToRadioGroup.setOnCheckedChangeListener(snapToListener);
		windowSnapToRadioGroup.setOnCheckedChangeListener(snapToListener);	
		activityTypeRadioGroup.setOnCheckedChangeListener(drawCheckedChangedListener);
		connectionTypeRadioGroup.setOnCheckedChangeListener(drawCheckedChangedListener);
		networkSignalTypeSpinner.setOnItemSelectedListener(drawChangedItemListener);
		networkMhzSpinner.setOnItemSelectedListener(drawChangedItemListener);
        networkChannelSpinner.setOnItemSelectedListener(drawChangedItemListener);
        networkModelSpinner.setOnItemSelectedListener(drawChangedItemListener);
        networkIDSpinner.setOnItemSelectedListener(drawChangedItemListener);
    }


	public void onMainFlipClick(View v) {
		mainActive.setEnabled(true);
		mainActive = v;
		onFlipClick(v, mainFlip);
	}
	
	private void adjustDraw(){
		DataObject dObj = drawingModel.getTouchDataObject();
		
		if(dObj instanceof Wall){
			switch(((Wall)dObj).getWallType()){
			case DOOR:
				Log.d("DEBUG","adjust door");
				setDrawWall(WallType.DOOR,doorMaterialRadioGroup,doorThicknessRadioGroup);
				break;
			case WALL:
				setDrawWall(WallType.WALL,wallMaterialRadioGroup,wallThicknessRadioGroup);
				break;
			case WINDOW:
				setDrawWall(WallType.WINDOW,windowMaterialRadioGroup,windowThicknessRadioGroup);
				break;
			}
		}else if(dObj instanceof AccessPoint){
			setDrawAccessPoints(networkSignalTypeSpinner, networkMhzSpinner, networkChannelSpinner, networkModelSpinner, transmitPowerEditText, antennaGainEditText, elevationEditText, networkIDSpinner);
		}else if(dObj instanceof DataActivity){
			setDrawActivities(activityTypeRadioGroup);
		}else if(dObj instanceof ConnectionPoint){
			setDrawConnections(connectionTypeRadioGroup);
		}else{
			Log.e("DEBUG","LOLWUTUTRYNTODRAW?");
		}
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
			setDrawAccessPoints(networkSignalTypeSpinner, networkMhzSpinner, networkChannelSpinner, networkModelSpinner, transmitPowerEditText, antennaGainEditText, elevationEditText, networkIDSpinner);
		}else if(tag.equals("activities")){
			setDrawActivities(activityTypeRadioGroup);
		}else if(tag.equals("connections")){
			setDrawConnections(connectionTypeRadioGroup);
		}else if(tag.equals("eraser")){
			drawingModel.setRemoveSelectionMode();
		}else if(tag.equals("edit")){
			drawingModel.setEditSelectionMode();
		}else if(tag.equals("info")){
			drawingModel.setInfoSelectionMode();
		}else{
			Log.e("DEBUG","LOLWUTUTRYNTODO?");
		}
	}
	
	private void setDrawWall(WallType wallType, RadioGroup materialRadioGroup, RadioGroup thicknessRadiogroup){
		RadioButton rb = (RadioButton) findViewById(materialRadioGroup.getCheckedRadioButtonId());
		Material material = Material.getMaterialByText(rb.getText().toString());
		rb = (RadioButton) findViewById(thicknessRadiogroup.getCheckedRadioButtonId());
		Thickness thickness = Thickness.getThicknessByText(rb.getText().toString());
		if(drawingModel.getTouchDataObject() instanceof Wall){
			Wall wall = (Wall) drawingModel.getTouchDataObject();
			wall.setWallType(wallType);
			wall.setMaterial(material);
			wall.setThickness(thickness);
		}else{
			drawingModel.setPlaceMode();
			drawingModel.setTouchDataObject(new Wall(wallType, thickness, material));
		}
	}
	
	private void setDrawAccessPoints(Spinner radioTypeSpinner, Spinner mhzSpinner, Spinner channelSpinner, Spinner modelSpinner, EditText transmitPowerEditText, EditText antennaGainEditText, EditText elevationEditText, Spinner networkIDSpinner){
		RadioType rt = RadioType.getRadioTypeByText(radioTypeSpinner.getSelectedItem().toString());
		int mhz = Integer.parseInt(mhzSpinner.getSelectedItem().toString());
		int channel = Integer.parseInt(channelSpinner.getSelectedItem().toString());
		RadioModel rm = RadioModel.getRadioModelByText(modelSpinner.getSelectedItem().toString());
		int transmitPower = Integer.parseInt(transmitPowerEditText.getText().toString());
		int antennaGain = Integer.parseInt(antennaGainEditText.getText().toString());
		int elevation = Integer.parseInt(elevationEditText.getText().toString());
		Network nw = Network.getNetworkByText(networkIDSpinner.getSelectedItem().toString());
		if(drawingModel.getTouchDataObject() instanceof AccessPoint){
			AccessPoint ap = (AccessPoint) drawingModel.getTouchDataObject();
			ap.setType(rt);
			ap.setFrequency(mhz);
			ap.setFrequencyband(channel);
			ap.setModel(rm);
			ap.setPower(transmitPower);
			ap.setGain(antennaGain);
			ap.setHeight(elevation);
			ap.setNetwork(nw);
		}else{
			drawingModel.setPlaceMode();
			drawingModel.setTouchDataObject(new AccessPoint("", elevation, rt, rm, mhz, channel, antennaGain, transmitPower, nw));
		}
		
	}
	
	private void setDrawActivities(RadioGroup activityTypeRadioGroup){
		RadioButton rb = (RadioButton) findViewById(activityTypeRadioGroup.getCheckedRadioButtonId());
		ActivityType aType = ActivityType.getActivityTypeByText(rb.getText().toString());
		if(drawingModel.getTouchDataObject() instanceof DataActivity){
			DataActivity da = (DataActivity) drawingModel.getTouchDataObject();
			da.setType(aType);
		}else{
			drawingModel.setPlaceMode();
			drawingModel.setTouchDataObject(new DataActivity(aType));
		}
	}
	
	private void setDrawConnections(RadioGroup connectionTypeRadioGroup){
		RadioButton rb = (RadioButton) findViewById(connectionTypeRadioGroup.getCheckedRadioButtonId());
		ConnectionPointType cType = ConnectionPointType.getConnectionPointTypeByText(rb.getText().toString());
		if(drawingModel.getTouchDataObject() instanceof ConnectionPoint){
			ConnectionPoint cp = (ConnectionPoint) drawingModel.getTouchDataObject();
			cp.setType(cType);
		}else{
			drawingModel.setPlaceMode();
			drawingModel.setTouchDataObject(new ConnectionPoint(cType));
		}
	}
	
	public void onParametersFlipClick(View v) {
		parametersActive.setEnabled(true);
		parametersActive = v;
		onFlipClick(v, parametersFlip);
		drawingModel.setIdle();
	}
	
	public void onToolsFlipClick(View v) {
		toolsActive.setEnabled(true);
		toolsActive = v;
		onFlipClick(v, toolsFlip);
		drawingModel.setIdle();
	}
	
	public void onResultsFlipClick(View v) {
		resultsActive.setEnabled(true);
		resultsActive = v;
		onFlipClick(v, resultsFlip);
		drawingModel.setIdle();
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
		Point touchLocation = drawingModel.getTouchLocation();
    	if(touchLocation != null){
    		coordinatesText.setText(touchLocation.x+":"+touchLocation.y);
    	}else{
    		coordinatesText.setText(":");
    	}
		
		//Log.d("DEBUG",""+model.isZoomInMaxed());
		zoomControls.setIsZoomInEnabled(!drawingModel.isZoomInMaxed());
		zoomControls.setIsZoomOutEnabled(!drawingModel.isZoomOutMaxed());
		
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
					floorPlanModel.loadFloorPlan(file);
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
