package com.ugent.networkplanningtool;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ViewFlipper;
import android.widget.ZoomControls;
import ar.com.daidalos.afiledialog.FileChooserDialog;
import ar.com.daidalos.afiledialog.FileChooserDialog.OnFileSelectedListener;

import com.ugent.networkplanningtool.layout.DrawingView;
import com.ugent.networkplanningtool.layout.ImportImage;
import com.ugent.networkplanningtool.layout.MyScrollBar;
import com.ugent.networkplanningtool.layout.dataobject.AccessPointView;
import com.ugent.networkplanningtool.layout.dataobject.ConnectionPointView;
import com.ugent.networkplanningtool.layout.dataobject.DataActivityView;
import com.ugent.networkplanningtool.layout.dataobject.WallView;
import com.ugent.networkplanningtool.model.DrawingModel;
import com.ugent.networkplanningtool.model.FloorPlanModel;

public class MainActivity extends Activity implements Observer,OnTouchListener{
	
	private static MainActivity mContext;
	
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
	
	private WallView wallView;
	private WallView doorView;
	private WallView windowView;
	private AccessPointView accessPointView;
	private DataActivityView dataActivityView;
	private ConnectionPointView connectionPointView;
	
	private Button eraseAccessPointsButton;
	private Button eraseDataActivitiesButton;
	private Button eraseConnectionPointsButton;
	
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
        
        wallView = (WallView) findViewById(R.id.wallViewWall);
        doorView = (WallView) findViewById(R.id.wallViewDoor);
        windowView = (WallView) findViewById(R.id.wallViewWindow);
        accessPointView = (AccessPointView) findViewById(R.id.accessPointView);
        dataActivityView = (DataActivityView) findViewById(R.id.dataActivityView);
        connectionPointView = (ConnectionPointView) findViewById(R.id.connectionPointView);
        
        eraseAccessPointsButton = (Button) findViewById(R.id.eraseAccesspointsButton);
        eraseDataActivitiesButton = (Button) findViewById(R.id.eraseActivitiesButton);
        eraseConnectionPointsButton = (Button) findViewById(R.id.eraseConnectionPointsButton);
        
        
        mainActive = findViewById(R.id.designButton);
        designActive = findViewById(R.id.wallsButton);
        parametersActive = findViewById(R.id.recieversButton);
        toolsActive = findViewById(R.id.predictCoverageButton);
        resultsActive = findViewById(R.id.renderDataButton);
        
        zoomControls = (ZoomControls) findViewById(R.id.zoomControls1);
        
        undoButton = (ImageButton) findViewById(R.id.undoButton);
        redoButton = (ImageButton) findViewById(R.id.redoButton);
        
        wallView.setDrawingModel(drawingModel);
        doorView.setDrawingModel(drawingModel);
        windowView.setDrawingModel(drawingModel);
        accessPointView.setDrawingModel(drawingModel);
        dataActivityView.setDrawingModel(drawingModel);
        connectionPointView.setDrawingModel(drawingModel);
        
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
        
        eraseAccessPointsButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				floorPlanModel.deleteAllAccessPoints();
			}
		});
        eraseDataActivitiesButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				floorPlanModel.deleteAllDataActivities();
			}
		});
        eraseConnectionPointsButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				floorPlanModel.deleteAllConnectionPoints();
			}
		});
        
        designView.setModel(drawingModel);
        hScrollBar.setModel(drawingModel);
        vScrollBar.setModel(drawingModel);
        drawingModel.addObserver(this);
        floorPlanModel.addObserver(this);
        
        designView.setOnTouchListener(this);
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
		Log.d("DEBUG","FLIP: "+tag);
		if(tag.equals(getResources().getString(R.string.wallText))){
			wallView.updateDrawingModel();
		}else if(tag.equals(getResources().getString(R.string.doorText))){
			doorView.updateDrawingModel();
		}else if(tag.equals(getResources().getString(R.string.windowText))){
			windowView.updateDrawingModel();
		}else if(tag.equals(getResources().getString(R.string.accessPointText))){
			accessPointView.updateDrawingModel();
		}else if(tag.equals(getResources().getString(R.string.dataActivityText))){
			dataActivityView.updateDrawingModel();
		}else if(tag.equals(getResources().getString(R.string.connectionPointText))){
			connectionPointView.updateDrawingModel();
		}else if(tag.equals(getResources().getString(R.string.eraserText))){
			drawingModel.setRemoveSelectionMode();
		}else if(tag.equals(getResources().getString(R.string.editText))){
			drawingModel.setEditSelectionMode();
		}else if(tag.equals(getResources().getString(R.string.infoText))){
			drawingModel.setInfoSelectionMode();
		}else{
			Log.e("DEBUG","LOLWUTUTRYNTODO?");
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
	
	public View getEditView(String tag){
		for(int i = 0; i < designFlip.getChildCount(); i ++){
			if(designFlip.getChildAt(i).getTag().equals(tag)){
				return designFlip.getChildAt(i);
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
					if(!fileName.toLowerCase().endsWith(".xml")){
						fileName+=".xml";
					}
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
					// TODO
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
	
	public static MainActivity getInstance(){
        return mContext;
    }
	
	public void handleStopDrawing(View view){
		drawingModel.setTouchDataObject(drawingModel.getTouchDataObject());
	}
	
	public void handleScreenshot(View v){
			FileChooserDialog dialog = new FileChooserDialog(MainActivity.this);
			dialog.addListener(new OnFileSelectedListener() {
				
				@Override
				public void onFileSelected(Dialog source, File folder, String name) {
					try{
						saveScreenshot(folder);
						source.dismiss();
					} catch (FileNotFoundException e) {
					Log.d("DEBUG","failed saving screenshot: "+e);
					}
				}
				
				@Override
				public void onFileSelected(Dialog source, File file) {
					try{
						saveScreenshot(file);
						source.dismiss();
					} catch (FileNotFoundException e) {
					Log.d("DEBUG","failed saving screenshot: "+e);
					}
				}
			});
			dialog.setFolderMode(true);
			dialog.setShowOnlySelectable(true);
			dialog.setCanCreateFiles(true);
			dialog.show();
			
		
	}
	
	public void saveScreenshot(File file) throws FileNotFoundException{
		Bitmap b = designView.getDrawingCache();
		b.compress(CompressFormat.PNG, 100, new FileOutputStream(file+"/test.png"));
	}
	
	public void handleImportImage(View v){
		
		final ImportImage iiDialog = new ImportImage(this);
		iiDialog.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		iiDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		iiDialog.show();
		iiDialog.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				if(iiDialog.isCompleted()){
					Log.d("DEBUG","IN ORDE ENZO");
				}
			}
		});
		
		/*final Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("file/*");
		startActivity(intent);
		/*FileChooserDialog dialog = new FileChooserDialog(this,Environment.getExternalStorageDirectory().getAbsolutePath());
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
		dialog.show();*/
	}
}
