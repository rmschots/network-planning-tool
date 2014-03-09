package com.ugent.networkplanningtool;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
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

import com.ugent.networkplanningtool.data.ServiceData.DeusRequest;
import com.ugent.networkplanningtool.data.ServiceData.DeusResult;
import com.ugent.networkplanningtool.io.FloorPlanIO;
import com.ugent.networkplanningtool.io.ImageIO;
import com.ugent.networkplanningtool.io.ksoap2.OnAsyncTaskCompleteListener;
import com.ugent.networkplanningtool.io.ksoap2.WebServiceTaskManager;
import com.ugent.networkplanningtool.io.ksoap2.services.EstimateSARTask;
import com.ugent.networkplanningtool.io.ksoap2.services.ExposureReductionTask;
import com.ugent.networkplanningtool.io.ksoap2.services.NetworkReductionTask;
import com.ugent.networkplanningtool.io.ksoap2.services.OptimalPlacementTask;
import com.ugent.networkplanningtool.io.ksoap2.services.PredictCoverageTask;
import com.ugent.networkplanningtool.layout.DrawingView;
import com.ugent.networkplanningtool.layout.ImportImage;
import com.ugent.networkplanningtool.layout.components.MyScrollBar;
import com.ugent.networkplanningtool.layout.dataobject.AccessPointView;
import com.ugent.networkplanningtool.layout.dataobject.ConnectionPointView;
import com.ugent.networkplanningtool.layout.dataobject.DataActivityView;
import com.ugent.networkplanningtool.layout.dataobject.WallView;
import com.ugent.networkplanningtool.layout.parameters.AlgorithmsView;
import com.ugent.networkplanningtool.layout.parameters.GeneratedAPsView;
import com.ugent.networkplanningtool.layout.parameters.MarginsView;
import com.ugent.networkplanningtool.layout.parameters.ReceiversView;
import com.ugent.networkplanningtool.layout.results.RenderDataView;
import com.ugent.networkplanningtool.layout.tools.EstimateSARView;
import com.ugent.networkplanningtool.layout.tools.ExposureReductionView;
import com.ugent.networkplanningtool.layout.tools.NetworkReductionView;
import com.ugent.networkplanningtool.layout.tools.OptimalPlacementView;
import com.ugent.networkplanningtool.model.DrawingModel;
import com.ugent.networkplanningtool.model.FloorPlanModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import ar.com.daidalos.afiledialog.FileChooserDialog;
import ar.com.daidalos.afiledialog.FileChooserDialog.OnFileSelectedListener;

public class MainActivity extends Activity implements Observer,OnTouchListener{

    private static final String TAG = MainActivity.class.getName();
	
	private static MainActivity mContext;
	
	private DrawingView designView;
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

    private ZoomControls zoomControls;
	private ImageButton undoButton;
	private ImageButton redoButton;

    private Button resultsButton;

    private DrawingModel drawingModel;
	private FloorPlanModel floorPlanModel;
	
	private WebServiceTaskManager taskManager;
	private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_main);
        
        designView = (DrawingView) findViewById(R.id.drawingView);
        drawingModel = new DrawingModel(designView.getWidth(), designView.getHeight());
        floorPlanModel = FloorPlanModel.getInstance();

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
        renderDataView.setDrawingModel(drawingModel);
        drawingModel.addObserver(this);
        floorPlanModel.addObserver(this);
        floorPlanModel.addObserver(renderDataView);

        designView.setOnTouchListener(this);
        
        taskManager = new WebServiceTaskManager(this);
    }


	public void onMainFlipClick(View v) {
        if (v.equals(findViewById(R.id.resultsButton)) && floorPlanModel.getDeusResult() == null) {
            Toast.makeText(MainActivity.this, "No result to display.", Toast.LENGTH_SHORT).show();
            return;
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
		dialog.setFilter(".*xml|.*XML");
		displayNewDialog(dialog);
		
	}
	
	public void handleSaveClick(View v){
		// TODO gemeenschappelijke delen screenshot & xml eruit halen.
		final Dialog d = new Dialog(this);
		d.setTitle(R.string.savePlanTitle);
		d.setContentView(R.layout.save_name);
		Button saveButton = (Button)d.findViewById(R.id.saveButton);
		saveButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				d.dismiss();
				String fnTemp = ((EditText)d.findViewById(R.id.fileNameEditText)).getText().toString();
				if(!fnTemp.toLowerCase().endsWith(".xml")){
					fnTemp+=".xml";
				}
				final String fileName = fnTemp;
				
				if(((CheckBox)d.findViewById(R.id.saveInDefaultFolderCheckBox)).isChecked()){
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
					saveTofile(f);
				}else{
					FileChooserDialog dialog = new FileChooserDialog(MainActivity.this);
					dialog.addListener(new OnFileSelectedListener() {
						@Override
						public void onFileSelected(Dialog source, File folder, String name) {}
						
						@Override
						public void onFileSelected(Dialog source, File file) {
							source.dismiss();
							File f = new File(file, fileName);
							saveTofile(f);
						}
					});
					dialog.setFolderMode(true);
					dialog.setShowOnlySelectable(true);
					dialog.setCanCreateFiles(true);
					displayNewDialog(dialog);
				}
				
				
			}
		});
		displayNewDialog(d);
	}
	
	public void saveTofile(File f){
		try {
			FloorPlanIO.saveFloorPlan(f, floorPlanModel);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			String StackTrace = "";
			for(StackTraceElement s : e.getStackTrace()){
				StackTrace+=" "+s.toString();
			}
			Log.d("DEBUG","error saving to "+f.getAbsolutePath()+" "+StackTrace);
			e.printStackTrace();
		}
	}
	
	public void handleNewFileClick(View v){
        floorPlanModel.resetModel();
        onMainFlipClick(findViewById(R.id.designButton));
    }
	
	public static MainActivity getInstance(){
        return mContext;
    }
	
	public void handleStopDrawing(View view){
		drawingModel.setTouchDataObject(drawingModel.getTouchDataObject());
	}
	
	public void handleScreenshot(View v){
		
		final Dialog d = new Dialog(this);
		d.setTitle(R.string.saveScreenshot);
		d.setContentView(R.layout.save_name);
		Button saveButton = (Button)d.findViewById(R.id.saveButton);
		saveButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				d.dismiss();
				String fnTemp = ((EditText)d.findViewById(R.id.fileNameEditText)).getText().toString();
				if(!fnTemp.toLowerCase().endsWith(".png")){
					fnTemp+=".png";
				}
				final String fileName = fnTemp;
				
				if(((CheckBox)d.findViewById(R.id.saveInDefaultFolderCheckBox)).isChecked()){
					
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
						ImageIO.saveImage(designView.getDrawingCache(), f);
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else{
					FileChooserDialog dialog = new FileChooserDialog(MainActivity.this);
					dialog.addListener(new OnFileSelectedListener() {
						
						@Override
						public void onFileSelected(Dialog source, File folder, String name) {
						}
						
						@Override
						public void onFileSelected(Dialog source, File file) {
							source.dismiss();
							File f = new File(file, fileName);
							try {
								ImageIO.saveImage(designView.getDrawingCache(), f);
								Log.d("DEBUG","JJJJJGKLDHNGKPFJKSDBGKLJMSNDBJFKPBGNKSJDNG");
							} catch (FileNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					});
					dialog.setFolderMode(true);
					dialog.setShowOnlySelectable(true);
					dialog.setCanCreateFiles(true);
					displayNewDialog(dialog);
				}
				
				
			}
		});
		displayNewDialog(d);
	}
	
	public void handleImportImage(View v){
		FileChooserDialog dialog = new FileChooserDialog(this,Environment.getExternalStorageDirectory().getAbsolutePath());
		dialog.addListener(new OnFileSelectedListener() {
			
			@Override
			public void onFileSelected(Dialog source, File folder, String name) {
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

    public void onPredictClick(final View v) {
        DeusRequest dr = composeDeusRequest(DeusRequest.RequestType.PREDICT_COVERAGE);
        taskManager.executeTask(new PredictCoverageTask(), dr, "ws in progress", new OnAsyncTaskCompleteListener<DeusResult>() {
            @Override
            public void onTaskCompleteSuccess(DeusResult result) {
                FloorPlanModel.getInstance().setDeusResult(result);
                onResultsFlipClick(findViewById(R.id.renderDataButton));
                onMainFlipClick(resultsButton);
            }

            @Override
            public void onTaskFailed(Exception cause) {
                Log.e(TAG, cause.getMessage(), cause);
                Toast.makeText(MainActivity.this, cause.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void onOptimalPlacementClick(final View v) {
        DeusRequest dr = composeDeusRequest(DeusRequest.RequestType.OPTIMAL_PLACEMENT);
        taskManager.executeTask(new OptimalPlacementTask(), dr, "ws in progress", new OnAsyncTaskCompleteListener<DeusResult>() {
            @Override
            public void onTaskCompleteSuccess(DeusResult result) {
                FloorPlanModel.getInstance().setDeusResult(result);
                onResultsFlipClick(findViewById(R.id.renderDataButton));
                onMainFlipClick(resultsButton);
            }

            @Override
            public void onTaskFailed(Exception cause) {
                Log.e(TAG, cause.getMessage(), cause);
                Toast.makeText(MainActivity.this, cause.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void onExposureReductionClick(final View v) {
        DeusRequest dr = composeDeusRequest(DeusRequest.RequestType.EXPOSURE_REDUCTION);
        taskManager.executeTask(new ExposureReductionTask(), dr, "ws in progress", new OnAsyncTaskCompleteListener<DeusResult>() {
            @Override
            public void onTaskCompleteSuccess(DeusResult result) {
                FloorPlanModel.getInstance().setDeusResult(result);
                onResultsFlipClick(findViewById(R.id.renderDataButton));
                onMainFlipClick(resultsButton);
            }

            @Override
            public void onTaskFailed(Exception cause) {
                Log.e(TAG, cause.getMessage(), cause);
                Toast.makeText(MainActivity.this, cause.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void onNetworkReductionClick(final View v) {
        DeusRequest dr = composeDeusRequest(DeusRequest.RequestType.NETWORK_REDUCTION);
        taskManager.executeTask(new NetworkReductionTask(), dr, "ws in progress", new OnAsyncTaskCompleteListener<DeusResult>() {
            @Override
            public void onTaskCompleteSuccess(DeusResult result) {
                FloorPlanModel.getInstance().setDeusResult(result);
                onResultsFlipClick(findViewById(R.id.renderDataButton));
                onMainFlipClick(resultsButton);
            }

            @Override
            public void onTaskFailed(Exception cause) {
                Log.e(TAG, cause.getMessage(), cause);
                Toast.makeText(MainActivity.this, cause.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void onEstimateSARClick(final View v) {
        DeusRequest dr = composeDeusRequest(DeusRequest.RequestType.ESTIMATE_SAR);
        taskManager.executeTask(new EstimateSARTask(), dr, "ws in progress", new OnAsyncTaskCompleteListener<DeusResult>() {
            @Override
            public void onTaskCompleteSuccess(DeusResult result) {
                FloorPlanModel.getInstance().setDeusResult(result);
                onResultsFlipClick(findViewById(R.id.renderDataButton));
                onMainFlipClick(resultsButton);
            }

            @Override
            public void onTaskFailed(Exception cause) {
                Log.e(TAG, cause.getMessage(), cause);
                Toast.makeText(MainActivity.this, cause.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private DeusRequest composeDeusRequest(DeusRequest.RequestType type) {
        String pathLossModel = algorithmsView.getPathLossModel().getValue();
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
        double apHeight = generatedAPsView.getHeight() * 100;
        double maxEField = 3; //TODO
        int distanceToAP = 10; // TODO
        int function = 0;

        boolean frequencyPlanning = algorithmsView.isGetFrequencies();

        return new DeusRequest(
                type,
                FloorPlanIO.getXMLAsString(FloorPlanModel.getInstance()),
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
            taskManager = new WebServiceTaskManager(this);
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
}
