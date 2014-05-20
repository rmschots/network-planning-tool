package com.ugent.networkplanningtool;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import android.widget.ZoomControls;

import com.ugent.networkplanningtool.data.ApMeasurement;
import com.ugent.networkplanningtool.data.FloorPlan;
import com.ugent.networkplanningtool.data.RealAccessPoint;
import com.ugent.networkplanningtool.data.ServiceData.CSVResult;
import com.ugent.networkplanningtool.data.ServiceData.DeusRequest;
import com.ugent.networkplanningtool.data.ServiceData.DeusResult;
import com.ugent.networkplanningtool.data.XMLTransformable;
import com.ugent.networkplanningtool.data.enums.results.ExportRawDataType;
import com.ugent.networkplanningtool.io.ASyncIOTaskManager;
import com.ugent.networkplanningtool.io.OnAsyncTaskCompleteListener;
import com.ugent.networkplanningtool.io.img.SaveImageParams;
import com.ugent.networkplanningtool.io.img.SaveImageTask;
import com.ugent.networkplanningtool.io.ksoap2.services.EstimateSARTask;
import com.ugent.networkplanningtool.io.ksoap2.services.ExposureReductionTask;
import com.ugent.networkplanningtool.io.ksoap2.services.NetworkReductionTask;
import com.ugent.networkplanningtool.io.ksoap2.services.OptimalPlacementTask;
import com.ugent.networkplanningtool.io.ksoap2.services.PredictCoverageTask;
import com.ugent.networkplanningtool.io.plaintext.SavePlainTextParams;
import com.ugent.networkplanningtool.io.plaintext.SavePlainTextTask;
import com.ugent.networkplanningtool.io.wifi.WifiDetectTask;
import com.ugent.networkplanningtool.io.xml.LoadFloorPlanTask;
import com.ugent.networkplanningtool.io.xml.LoadMeasurementsTask;
import com.ugent.networkplanningtool.io.xml.SaveXMLParams;
import com.ugent.networkplanningtool.io.xml.SaveXMLTask;
import com.ugent.networkplanningtool.io.xml.XMLIO;
import com.ugent.networkplanningtool.layout.DrawingView;
import com.ugent.networkplanningtool.layout.ImportImage;
import com.ugent.networkplanningtool.layout.components.MyScrollBar;
import com.ugent.networkplanningtool.layout.design.AccessPointView;
import com.ugent.networkplanningtool.layout.design.ConnectionPointView;
import com.ugent.networkplanningtool.layout.design.DataActivityView;
import com.ugent.networkplanningtool.layout.design.WallView;
import com.ugent.networkplanningtool.layout.measure.ApLinkingView;
import com.ugent.networkplanningtool.layout.parameters.AlgorithmsView;
import com.ugent.networkplanningtool.layout.parameters.GeneratedAPsView;
import com.ugent.networkplanningtool.layout.parameters.MarginsView;
import com.ugent.networkplanningtool.layout.parameters.ReceiversView;
import com.ugent.networkplanningtool.layout.results.ExportRawDataView;
import com.ugent.networkplanningtool.layout.results.MeasureView;
import com.ugent.networkplanningtool.layout.results.RenderDataView;
import com.ugent.networkplanningtool.layout.results.VisualOptionsView;
import com.ugent.networkplanningtool.layout.tools.EstimateSARView;
import com.ugent.networkplanningtool.layout.tools.ExposureReductionView;
import com.ugent.networkplanningtool.layout.tools.NetworkReductionView;
import com.ugent.networkplanningtool.layout.tools.OptimalPlacementView;
import com.ugent.networkplanningtool.model.DrawingModel;
import com.ugent.networkplanningtool.model.FloorPlanModel;
import com.ugent.networkplanningtool.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import java.util.Set;

import ar.com.daidalos.afiledialog.FileChooserDialog;
import ar.com.daidalos.afiledialog.FileChooserDialog.OnFileSelectedListener;

public class MainActivity extends Activity implements Observer,OnTouchListener{

    private static final String TAG = MainActivity.class.getName();
	
	private static MainActivity mContext;
	
	private DrawingView drawingView;
	private TextView locationText;
	private TextView coordinatesText;

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

    private AlgorithmsView algorithmsView;
    private GeneratedAPsView generatedAPsView;
    private MarginsView marginsView;
    private ReceiversView recieversView;

    private OptimalPlacementView optimalPlacementeView;
    private EstimateSARView estimateSARView;
    private NetworkReductionView networkReductionView;
    private ExposureReductionView exposureReductionView;

    private RenderDataView renderDataView;
    private VisualOptionsView visualOptionsView;
    private ExportRawDataView exportRawDataView;
    private MeasureView measureView;

    private ZoomControls zoomControls;
	private ImageButton undoButton;
	private ImageButton redoButton;

    private Button resultsButton;

    private DrawingModel drawingModel;
	private FloorPlanModel floorPlanModel;
	
	private ASyncIOTaskManager taskManager;
	private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_main);
        
        drawingView = (DrawingView) findViewById(R.id.drawingView);
        drawingModel = new DrawingModel(drawingView.getWidth(), drawingView.getHeight());
        floorPlanModel = FloorPlanModel.INSTANCE;

        MyScrollBar hScrollBar = (MyScrollBar) findViewById(R.id.myScrollBar1);
        MyScrollBar vScrollBar = (MyScrollBar) findViewById(R.id.myScrollBar2);
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

        algorithmsView = (AlgorithmsView) findViewById(R.id.algorithmsView);
        generatedAPsView = (GeneratedAPsView) findViewById(R.id.generatedAPsView);
        marginsView = (MarginsView) findViewById(R.id.marginsView);
        recieversView = (ReceiversView) findViewById(R.id.receiversView);

        optimalPlacementeView = (OptimalPlacementView) findViewById(R.id.optimalPlacementeView);
        estimateSARView = (EstimateSARView) findViewById(R.id.estimateSarView);
        networkReductionView = (NetworkReductionView) findViewById(R.id.networkReductionView);
        exposureReductionView = (ExposureReductionView) findViewById(R.id.reduceExposureView);

        renderDataView = (RenderDataView) findViewById(R.id.renderDataView);
        visualOptionsView = (VisualOptionsView) findViewById(R.id.visualOptionsView);
        exportRawDataView = (ExportRawDataView) findViewById(R.id.exportRawDataView);
        measureView = (MeasureView) findViewById(R.id.measureMeasureView);

        Button eraseAccessPointsButton = (Button) findViewById(R.id.eraseAccesspointsButton);
        Button eraseDataActivitiesButton = (Button) findViewById(R.id.eraseActivitiesButton);
        Button eraseConnectionPointsButton = (Button) findViewById(R.id.eraseConnectionPointsButton);
        
        
        mainActive = findViewById(R.id.designButton);
        designActive = findViewById(R.id.wallsButton);
        parametersActive = findViewById(R.id.recieversButton);
        toolsActive = findViewById(R.id.predictCoverageButton);
        resultsActive = findViewById(R.id.renderDataButton);
        
        zoomControls = (ZoomControls) findViewById(R.id.zoomControls1);
        
        undoButton = (ImageButton) findViewById(R.id.undoButton);
        redoButton = (ImageButton) findViewById(R.id.redoButton);

        resultsButton = (Button) findViewById(R.id.resultsButton);

        wallView.setDrawingModel(drawingModel);
        doorView.setDrawingModel(drawingModel);
        windowView.setDrawingModel(drawingModel);
        accessPointView.setDrawingModel(drawingModel);
        dataActivityView.setDrawingModel(drawingModel);
        connectionPointView.setDrawingModel(drawingModel);
        measureView.setDrawingModel(drawingModel);



        onParametersFlipClick(parametersActive);
        onToolsFlipClick(toolsActive);
        onResultsFlipClick(resultsActive);
        onMainFlipClick(mainActive);
        onDesignFlipClick(designActive);
        
        
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
                Log.d("DEBUG", "undo");
            }
        });
        redoButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                floorPlanModel.redo();
                Log.d("DEBUG", "redo");
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
        
        drawingView.setModel(drawingModel);
        hScrollBar.setModel(drawingModel);
        vScrollBar.setModel(drawingModel);
        renderDataView.setDrawingModel(drawingModel);
        visualOptionsView.setDrawingModel(drawingModel);
        drawingModel.addObserver(this);
        floorPlanModel.addObserver(this);
        floorPlanModel.addObserver(renderDataView);

        drawingView.setOnTouchListener(this);
        
        taskManager = new ASyncIOTaskManager(this);
    }


	public void onMainFlipClick(View v) {
        if (v.getId() == R.id.resultsButton) {
            if(floorPlanModel.getDeusResult() == null){
                Toast.makeText(MainActivity.this, "No result to display.", Toast.LENGTH_SHORT).show();
                return;
            }else{
                drawingModel.setDrawResult(true);
            }
        }else{
            drawingModel.setDrawResult(false);
        }
        if(v.getId() == R.id.designButton){
            onDesignFlipClick(designActive);
        }else{
            drawingModel.setIdle();
        }
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
        View flippedView = onFlipClick(v, parametersFlip);
        flippedView.getTag();
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
        View flippedView = onFlipClick(v, resultsFlip);
        Object tag = flippedView.getTag();
        if (tag.equals(getResources().getString(R.string.measureText))) {
            measureView.updateDrawingModel();
        } else {
            drawingModel.setIdle();
        }
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
		
		zoomControls.setIsZoomInEnabled(!drawingModel.isZoomInMaxed());
		zoomControls.setIsZoomOutEnabled(!drawingModel.isZoomOutMaxed());
		
		undoButton.setEnabled(floorPlanModel.canUndo());
		redoButton.setEnabled(floorPlanModel.canRedo());
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if(v == drawingView){
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
		dialog.addListener(new OnFileSelectedListener() {
			
			@Override
			public void onFileSelected(Dialog source, File folder, String name) {
                // can not be reached
			}
			
			@Override
			public void onFileSelected(Dialog source, File file) {
				Log.d("DEBUG",file.getAbsolutePath());
				source.dismiss();
                taskManager.executeTask(new LoadFloorPlanTask(), file, "Loading" + file.getName() + " ...", new OnAsyncTaskCompleteListener<FloorPlan>() {
                    @Override
                    public void onTaskCompleteSuccess(FloorPlan result) {
                        floorPlanModel.setFloorPlan(result);
                        Toast.makeText(MainActivity.this, "Floorplan loaded", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onTaskFailed(Exception cause) {
                        Log.e(TAG, cause.getMessage(), cause);
                        Toast.makeText(MainActivity.this, cause.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }, false);
            }
		});
		dialog.setFilter(".*xml|.*XML");
        dialog.setShowOnlySelectable(true);
        dialog.setTitle("Select file to open");
		displayNewDialog(dialog);
		
	}
	
	public void handleSaveClick(View v){
        FileChooserDialog dialog = new FileChooserDialog(this);
        dialog.addListener(new OnFileSelectedListener() {
            @Override
            public void onFileSelected(Dialog source, File folder, String name) {
                source.dismiss();
                if(!name.toLowerCase().endsWith(".xml")){
                    name+=".xml";
                }
                File file = new File(folder,name);
                saveTofile(new SaveXMLParams(floorPlanModel.getFloorPlan(), file));
            }

            @Override
            public void onFileSelected(Dialog source, File file) {
                source.dismiss();
                saveTofile(new SaveXMLParams(floorPlanModel.getFloorPlan(), file));
            }
        });
        dialog.setFilter(".*xml|.*XML");
        dialog.setShowOnlySelectable(true);
        dialog.setCanCreateFiles(true);
        dialog.setTitle("Select file to save to or create a new one");
        displayNewDialog(dialog);
	}

    public void saveTofile(SaveXMLParams params) {
        taskManager.executeTask(new SaveXMLTask(), params, "saving...", new OnAsyncTaskCompleteListener<File>() {
            @Override
            public void onTaskCompleteSuccess(File result) {
                Toast.makeText(MainActivity.this, "Saved successful to " + result.getName(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onTaskFailed(Exception cause) {
                Log.e(TAG, cause.getMessage(), cause);
                Toast.makeText(MainActivity.this, cause.getMessage(), Toast.LENGTH_LONG).show();
            }
        }, false);
    }

    public void saveTofile(SavePlainTextParams plainTextParams) {
        taskManager.executeTask(new SavePlainTextTask(), plainTextParams, "saving...", new OnAsyncTaskCompleteListener<File>() {
            @Override
            public void onTaskCompleteSuccess(File result) {
                Toast.makeText(MainActivity.this, "Saved successful to " + result.getName(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onTaskFailed(Exception cause) {
                Log.e(TAG, cause.getMessage(), cause);
                Toast.makeText(MainActivity.this, cause.getMessage(), Toast.LENGTH_LONG).show();
            }
        }, false);
    }

    public void handleNewFileClick(View v){
        floorPlanModel.resetModel();
        onMainFlipClick(findViewById(R.id.designButton));
    }
	
	public static MainActivity getInstance(){
        return mContext;
    }
	
	public void handleStopDrawing(View view){
		drawingModel.setTouchFloorPlanObject(drawingModel.getTouchFloorPlanObject());
	}
	
	public void handleScreenshot(View v){
        drawingView.invalidate();
        drawingView.destroyDrawingCache();
        drawingView.setDrawingCacheEnabled(false);
        drawingView.setDrawingCacheEnabled(true);
        drawingView.buildDrawingCache();
        final Bitmap bm = drawingView.getDrawingCache();
		
		final Dialog d = new Dialog(this);
		d.setTitle(R.string.saveScreenshot);
		d.setContentView(R.layout.save_name);
        d.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        FileChooserDialog dialog = new FileChooserDialog(this);
        dialog.addListener(new OnFileSelectedListener() {
            @Override
            public void onFileSelected(Dialog source, File folder, String name) {
                source.dismiss();
                if (!name.toLowerCase().endsWith(".png")) {
                    name += ".png";
                }
                File file = new File(folder, name);
                performTask(file);
            }

            @Override
            public void onFileSelected(Dialog source, File file) {
                source.dismiss();
                performTask(file);
            }

            private void performTask(File file) {
                SaveImageParams params = new SaveImageParams(bm, file);
                taskManager.executeTask(new SaveImageTask(), params, "saving...", new OnAsyncTaskCompleteListener<File>() {
                    @Override
                    public void onTaskCompleteSuccess(File result) {
                        Toast.makeText(MainActivity.this, "Saved successful to " + result.getName(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onTaskFailed(Exception cause) {
                        Log.e(TAG, cause.getMessage(), cause);
                        Toast.makeText(MainActivity.this, cause.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }, true);
            }
        });
        dialog.setFilter(".*png|.*PNG");
        dialog.setShowOnlySelectable(true);
        dialog.setCanCreateFiles(true);
        dialog.setTitle("Select file to save to or create a new one");
        displayNewDialog(dialog);
	}
	
	public void handleImportImage(View v){
		FileChooserDialog dialog = new FileChooserDialog(this);
		dialog.addListener(new OnFileSelectedListener() {
			
			@Override
			public void onFileSelected(Dialog source, File folder, String name) {
                // can not be reached
			}
			
			@Override
			public void onFileSelected(Dialog source, File file) {
				source.dismiss();
				
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inPreferredConfig = Bitmap.Config.ARGB_8888;
				final Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
				
				final ImportImage iiDialog = new ImportImage(MainActivity.this);
				iiDialog.setImage(bitmap);
				iiDialog.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
				iiDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
				displayNewDialog(iiDialog);
				iiDialog.setOnDismissListener(new OnDismissListener() {
					@Override
					public void onDismiss(DialogInterface dialog) {
						if(iiDialog.isCompleted()){
							drawingModel.setBackground(bitmap, iiDialog.getScale());
						}
					}
				});
			}
		});
		dialog.setFilter(".*jpg|.*png|.*gif|.*JPG|.*PNG|.*GIF");
		dialog.setShowOnlySelectable(true);
		displayNewDialog(dialog);
	}

    public void handleLinkingConfig(final View view){
        final WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        taskManager.executeTask(new WifiDetectTask(this), wifi, "Detecting wifi signals...", new OnAsyncTaskCompleteListener<List<RealAccessPoint>>() {
            @Override
            public void onTaskCompleteSuccess(List<RealAccessPoint> result) {
                final Dialog d = new Dialog(MainActivity.this);
                d.setContentView(new ApLinkingView(MainActivity.this, result));
                displayNewDialog(d);
            }

            @Override
            public void onTaskFailed(Exception cause) {
                Log.e(TAG, cause.getMessage(), cause);
                Toast.makeText(MainActivity.this, cause.getMessage(), Toast.LENGTH_LONG).show();
            }
        }, false);
    }

    public void onPredictClick(final View v) {
        DeusRequest dr = composeDeusRequest(DeusRequest.RequestType.PREDICT_COVERAGE);
        taskManager.executeTask(new PredictCoverageTask(), dr, "ws in progress", new OnAsyncTaskCompleteListener<DeusResult>() {
            @Override
            public void onTaskCompleteSuccess(DeusResult result) {
                floorPlanModel.setDeusResult(result);
                onResultsFlipClick(findViewById(R.id.renderDataButton));
                onMainFlipClick(resultsButton);
            }

            @Override
            public void onTaskFailed(Exception cause) {
                Log.e(TAG, cause.getMessage(), cause);
                Toast.makeText(MainActivity.this, cause.getMessage(), Toast.LENGTH_LONG).show();
            }
        }, false);
        /*File file = new File(Environment.getExternalStorageDirectory(), "myMeasurements.xml");
        taskManager.executeTask(new LoadMeasurementsTask(), file, "Loading" + file.getName() + " ...", new OnAsyncTaskCompleteListener<List<ApMeasurement>>() {
            @Override
            public void onTaskCompleteSuccess(List<ApMeasurement> result) {
                floorPlanModel.setApMeasurements(result);
                Toast.makeText(MainActivity.this, "measurements loaded", Toast.LENGTH_LONG).show();
                predictRec(1.303715,result);
            }

            @Override
            public void onTaskFailed(Exception cause) {
                Log.e(TAG, cause.getMessage(), cause);
                Toast.makeText(MainActivity.this, cause.getMessage(), Toast.LENGTH_LONG).show();
            }
        });*/
    }
    private double amount;
    private void predictRec(final double amount, final List<ApMeasurement> apMeasurements){
        this.amount = amount;
        DeusRequest dr = composeDeusRequest(DeusRequest.RequestType.PREDICT_COVERAGE);
        taskManager.executeTask(new PredictCoverageTask(), dr, "ws in progress", new OnAsyncTaskCompleteListener<DeusResult>() {
            @Override
            public void onTaskCompleteSuccess(DeusResult result) {
                if(amount >= 1.303721){
                    floorPlanModel.setDeusResult(result);
                    onResultsFlipClick(findViewById(R.id.renderDataButton));
                    onMainFlipClick(resultsButton);
                }else{
                    double totalErr = 0;
                    for(ApMeasurement apMeasurement : apMeasurements){
                        CSVResult csvResult = Utils.getResultAt(apMeasurement.getPoint1(),result.getCsv());
                        totalErr+=(csvResult.getPowerRX()-apMeasurement.getSignalStrength());
                    }
                    System.out.println("amount: "+amount+" totalErr: "+Math.abs(totalErr));
                    predictRec(amount+0.000001,apMeasurements);
                }

            }

            @Override
            public void onTaskFailed(Exception cause) {
                Log.e(TAG, cause.getMessage(), cause);
                Toast.makeText(MainActivity.this, cause.getMessage(), Toast.LENGTH_LONG).show();
            }
        }, false);
    }

    public void onOptimalPlacementClick(final View v) {
        DeusRequest dr = composeDeusRequest(DeusRequest.RequestType.OPTIMAL_PLACEMENT);
        taskManager.executeTask(new OptimalPlacementTask(), dr, "ws in progress", new OnAsyncTaskCompleteListener<DeusResult>() {
            @Override
            public void onTaskCompleteSuccess(DeusResult result) {
                floorPlanModel.setDeusResult(result);
                onResultsFlipClick(findViewById(R.id.renderDataButton));
                onMainFlipClick(resultsButton);
            }

            @Override
            public void onTaskFailed(Exception cause) {
                Log.e(TAG, cause.getMessage(), cause);
                Toast.makeText(MainActivity.this, cause.getMessage(), Toast.LENGTH_LONG).show();
            }
        }, false);
    }

    public void onExposureReductionClick(final View v) {
        DeusRequest dr = composeDeusRequest(DeusRequest.RequestType.EXPOSURE_REDUCTION);
        taskManager.executeTask(new ExposureReductionTask(), dr, "ws in progress", new OnAsyncTaskCompleteListener<DeusResult>() {
            @Override
            public void onTaskCompleteSuccess(DeusResult result) {
                floorPlanModel.setDeusResult(result);
                onResultsFlipClick(findViewById(R.id.renderDataButton));
                onMainFlipClick(resultsButton);
            }

            @Override
            public void onTaskFailed(Exception cause) {
                Log.e(TAG, cause.getMessage(), cause);
                Toast.makeText(MainActivity.this, cause.getMessage(), Toast.LENGTH_LONG).show();
            }
        }, false);
    }

    public void onNetworkReductionClick(final View v) {
        DeusRequest dr = composeDeusRequest(DeusRequest.RequestType.NETWORK_REDUCTION);
        taskManager.executeTask(new NetworkReductionTask(), dr, "ws in progress", new OnAsyncTaskCompleteListener<DeusResult>() {
            @Override
            public void onTaskCompleteSuccess(DeusResult result) {
                floorPlanModel.setDeusResult(result);
                onResultsFlipClick(findViewById(R.id.renderDataButton));
                onMainFlipClick(resultsButton);
            }

            @Override
            public void onTaskFailed(Exception cause) {
                Log.e(TAG, cause.getMessage(), cause);
                Toast.makeText(MainActivity.this, cause.getMessage(), Toast.LENGTH_LONG).show();
            }
        }, false);
    }

    public void onEstimateSARClick(final View v) {
        DeusRequest dr = composeDeusRequest(DeusRequest.RequestType.ESTIMATE_SAR);
        taskManager.executeTask(new EstimateSARTask(), dr, "ws in progress", new OnAsyncTaskCompleteListener<DeusResult>() {
            @Override
            public void onTaskCompleteSuccess(DeusResult result) {
                floorPlanModel.setDeusResult(result);
                onResultsFlipClick(findViewById(R.id.renderDataButton));
                onMainFlipClick(resultsButton);
            }

            @Override
            public void onTaskFailed(Exception cause) {
                Log.e(TAG, cause.getMessage(), cause);
                Toast.makeText(MainActivity.this, cause.getMessage(), Toast.LENGTH_LONG).show();
            }
        }, false);
    }

    public void onLoadMeasurementsClick(final View v) {
        File file = new File(Environment.getExternalStorageDirectory(), "myMeasurements.xml");
        taskManager.executeTask(new LoadMeasurementsTask(), file, "Loading" + file.getName() + " ...", new OnAsyncTaskCompleteListener<List<ApMeasurement>>() {
            @Override
            public void onTaskCompleteSuccess(List<ApMeasurement> result) {
                /*List<XMLTransformable> compareList = new ArrayList<XMLTransformable>();
                for(ApMeasurement apMeasurement : result){
                    CSVResult closestResult = Utils.getResultAt(apMeasurement.getPoint1(), floorPlanModel.getDeusResult().getCsv());
                    closestResult.setApMeasurement(apMeasurement);
                    compareList.add(closestResult);
                }
                // saveStatsPerRoom(compareList);

                SaveXMLParams saveXMLParams = new SaveXMLParams(compareList,"comparelist", new File(Environment.getExternalStorageDirectory(), "compare_"+algorithmsView.getPathLossModel().getValue()+".xml"));
                saveTofile(saveXMLParams);*/
                floorPlanModel.setApMeasurements(result);
                Toast.makeText(MainActivity.this, "measurements loaded", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onTaskFailed(Exception cause) {
                Log.e(TAG, cause.getMessage(), cause);
                Toast.makeText(MainActivity.this, cause.getMessage(), Toast.LENGTH_LONG).show();
            }
        }, false);
    }

    private void saveStats(List<CSVResult> compareList){
        Random randomGenerator = new Random();
        File f = new File(Environment.getExternalStorageDirectory(), "permutations_"+algorithmsView.getPathLossModel().getValue()+".txt");
        try {
            PrintWriter fw = new PrintWriter(f,"UTF-8");

            for (int i = 1; i < compareList.size(); i++) {
                Set<Set<CSVResult>> seen = new HashSet<Set<CSVResult>>();
                for (int j = 0; j < compareList.size(); ++j) {
                    Set<CSVResult> items;
                    do {
                        items = new HashSet<CSVResult>();
                        for (int k = 0; k < i; k++) {
                            int index;
                            do{
                                index = randomGenerator.nextInt(compareList.size());
                            } while(!items.add(compareList.get(index)));

                        }
                    } while (!seen.add(items));
                    double total = 0;
                    for(CSVResult csvResult : items){
                        total+=csvResult.getApMeasurement().getSignalStrength()-csvResult.getPowerRX();
                    }
                    double shift = total/items.size();
                    total = 0;
                    for(CSVResult csvResult : compareList){
                        total+=Math.abs(csvResult.getApMeasurement().getSignalStrength()-csvResult.getPowerRX()-shift);
                    }
                    fw.print(total/compareList.size()+";");
                }
                fw.println();
            }
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveStatsPerRoom(List<CSVResult> compareList){
        HashMap<Integer, List<CSVResult>> roomMap = new HashMap<Integer, List<CSVResult>>();
        for(CSVResult csvResult : compareList){
            int roomNr = csvResult.getDrawingSize();
            if(!roomMap.containsKey(roomNr)){
                roomMap.put(roomNr,new ArrayList<CSVResult>());
            }
            roomMap.get(roomNr).add(csvResult);
        }
        List<Integer> roomNrList = new ArrayList<Integer>(roomMap.keySet());

        Random randomGenerator = new Random();
        File f = new File(Environment.getExternalStorageDirectory(), "room_permutations_"+algorithmsView.getPathLossModel().getValue()+".txt");
        try {
            PrintWriter fw = new PrintWriter(f,"UTF-8");

            for (int i = 1; i <= roomNrList.size(); i++) {

                Set<Set<CSVResult>> seen = new HashSet<Set<CSVResult>>();
                for (int j = 0; j < compareList.size(); ++j) {
                    Set<CSVResult> items;
                    do {
                        items = new HashSet<CSVResult>();
                        Collections.shuffle(roomNrList,randomGenerator);
                        for(int k = 0; k < i; k++){
                            List<CSVResult> roomList = roomMap.get(roomNrList.get(k));
                            items.add(roomList.get(randomGenerator.nextInt(roomList.size())));
                        }
                    } while (!seen.add(items));
                    double total = 0;
                    for(CSVResult csvResult : items){
                        System.out.println("nr: "+csvResult.getDrawingSize());
                        total+=csvResult.getApMeasurement().getSignalStrength()-csvResult.getPowerRX();
                    }
                    System.out.println("++++++++++++++++++++");
                    double shift = total/items.size();
                    total = 0;
                    for(CSVResult csvResult : compareList){
                        total+=Math.abs(csvResult.getApMeasurement().getSignalStrength()-csvResult.getPowerRX()-shift);
                    }
                    fw.print(total/compareList.size()+";");
                }
                fw.println();
            }
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onSaveMeasurementsClick(final View v) {
        List<XMLTransformable> tmp = new ArrayList<XMLTransformable>(floorPlanModel.getApMeasurements());
        saveTofile(new SaveXMLParams(tmp, "measurements", new File(Environment.getExternalStorageDirectory(), "myMeasurements.xml")));
    }

    private DeusRequest composeDeusRequest(DeusRequest.RequestType type) {
        String pathLossModel = algorithmsView.getPathLossModel().getValue()/*+amount*/;
        double gridSize = recieversView.getGridSize() * 100;
        double roomHeight = estimateSARView.getRoomHeight();
        String defaultActivity = optimalPlacementeView.getDefaultActivity().getText();
        String receiverName = recieversView.getReceiver();
        double receiverGain = generatedAPsView.getAntennaGain();
        double receiverHeight = recieversView.getElevation() * 100;
        double interference = marginsView.getInterferenceMargin();
        double shadowMargin = marginsView.getShadowMargin();
        double fadeMargin = marginsView.getFadeMargin();
        String apType = generatedAPsView.getGeneratedAPType().getText();
        int apFrequency = Integer.parseInt(generatedAPsView.getGeneratedAPFrequencyBand().getText());
        double apPower = generatedAPsView.getTransmitPower();
        double apGain = generatedAPsView.getAntennaGain();
        double apHeight = generatedAPsView.getHeight();
        double maxEField = 3; //TODO
        int distanceToAP = 10; // TODO
        int function = 0;

        boolean frequencyPlanning = algorithmsView.isGetFrequencies();

        return new DeusRequest(
                type,
                XMLIO.getXMLAsString(floorPlanModel.getFloorPlan()),
                pathLossModel,
                gridSize,
                roomHeight,
                defaultActivity,
                receiverName,
                receiverGain,
                receiverHeight,
                interference,
                shadowMargin,
                fadeMargin,
                apType,
                apFrequency,
                apPower,
                apGain,
                apHeight,
                maxEField, // not used
                distanceToAP, // not used
                function,
                frequencyPlanning);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (taskManager == null) {
            taskManager = new ASyncIOTaskManager(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismissDialog();
    }
    
    private void displayNewDialog(Dialog d) {
        dismissDialog();
        dialog = d;
        dialog.show();
    }
    
    private void dismissDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    public void handleSaveRawData(View view){
        FileChooserDialog dialog = new FileChooserDialog(MainActivity.this);
        dialog.addListener(new OnFileSelectedListener() {
            @Override
            public void onFileSelected(Dialog source, File folder, String name) {
                String extension = "";
                switch (exportRawDataView.getExportType()){
                    case NORMALIZED_PLAN:
                    case OPTIMIZED_PLAN:
                    case COVERAGE_DATA:
                        extension = ".xml";
                        break;
                    case EXPOSURE_INFO:
                    case BENCHMARK:
                        extension = ".txt";
                        break;
                }
                if (!name.toLowerCase().endsWith(extension)) {
                    name += extension;
                }
                File file = new File(folder,name);
                saveRawData(exportRawDataView.getExportType(), file);
            }

            @Override
            public void onFileSelected(Dialog source, File file) {
                source.dismiss();
                saveRawData(exportRawDataView.getExportType(), file);
            }
        });
        switch (exportRawDataView.getExportType()){
            case NORMALIZED_PLAN:
            case OPTIMIZED_PLAN:
            case COVERAGE_DATA:
                dialog.setFilter(".*xml|.*XML");
                break;
            case EXPOSURE_INFO:
            case BENCHMARK:
                dialog.setFilter(".*txt|.*TXT");
                break;
        }
        dialog.setShowOnlySelectable(true);
        dialog.setCanCreateFiles(true);
        dialog.setTitle("Select file to save to or create a new one");
    }

    private void saveRawData(ExportRawDataType exportType, File f) {
        DeusResult dr = floorPlanModel.getDeusResult();
        switch(exportType){
            case NORMALIZED_PLAN:
                if (dr.getNormalizedPlan() != null) {
                    saveTofile(new SaveXMLParams(dr.getNormalizedPlan(), f));
                } else {
                    Toast.makeText(MainActivity.this, "No normalized plan available.", Toast.LENGTH_SHORT).show();
                }
                break;
            case OPTIMIZED_PLAN:
                if (dr.getOptimizedPlan() != null) {
                    saveTofile(new SaveXMLParams(dr.getOptimizedPlan(), f));
                } else {
                    Toast.makeText(MainActivity.this, "No optimized plan available.", Toast.LENGTH_SHORT).show();
                }
                break;
            case COVERAGE_DATA:
                if (dr.getCsv() != null) {
                    List<XMLTransformable> tmp = new ArrayList<XMLTransformable>(dr.getCsv());
                    saveTofile(new SaveXMLParams(tmp, "results", f));
                } else {
                    Toast.makeText(MainActivity.this, "No measure results available.", Toast.LENGTH_SHORT).show();
                }
                break;
            case EXPOSURE_INFO:
                if (dr.getInfoAsString() != null) {
                    saveTofile(new SavePlainTextParams(dr.getInfoAsString(), f));
                } else {
                    Toast.makeText(MainActivity.this, "No info available.", Toast.LENGTH_SHORT).show();
                }
                break;
            case BENCHMARK:
                if (dr.getBenchmarks() != null) {
                    saveTofile(new SavePlainTextParams(dr.getBenchmarks(), f));
                } else {
                    Toast.makeText(MainActivity.this, "No benchmarks available.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public ASyncIOTaskManager getTaskManager() {
        return taskManager;
    }
}
