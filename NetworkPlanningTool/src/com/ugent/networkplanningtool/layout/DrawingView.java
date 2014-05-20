package com.ugent.networkplanningtool.layout;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.ugent.networkplanningtool.MainActivity;
import com.ugent.networkplanningtool.data.AccessPoint;
import com.ugent.networkplanningtool.data.ApMeasurement;
import com.ugent.networkplanningtool.data.ConnectionPoint;
import com.ugent.networkplanningtool.data.DataActivity;
import com.ugent.networkplanningtool.data.FloorPlanObject;
import com.ugent.networkplanningtool.data.ServiceData.CSVResult;
import com.ugent.networkplanningtool.data.ServiceData.DeusResult;
import com.ugent.networkplanningtool.data.Wall;
import com.ugent.networkplanningtool.io.OnAsyncTaskCompleteListener;
import com.ugent.networkplanningtool.io.wifi.MeasureParams;
import com.ugent.networkplanningtool.io.wifi.MeasureSignalStrengthTask;
import com.ugent.networkplanningtool.layout.design.AccessPointView;
import com.ugent.networkplanningtool.layout.design.ConnectionPointView;
import com.ugent.networkplanningtool.layout.design.DataActivityView;
import com.ugent.networkplanningtool.layout.design.DataObjectView;
import com.ugent.networkplanningtool.layout.design.DataObjectView.ViewType;
import com.ugent.networkplanningtool.layout.design.WallView;
import com.ugent.networkplanningtool.model.DrawingModel;
import com.ugent.networkplanningtool.model.DrawingModel.PlaceResult;
import com.ugent.networkplanningtool.model.FloorPlanModel;
import com.ugent.networkplanningtool.utils.Utils;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class DrawingView extends View implements Observer {

    private static final String TAG = DrawingView.class.getName();

    private Paint paint = new Paint();

    private DrawingModel drawingModel = null;

    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        FloorPlanModel.INSTANCE.addObserver(this);
        setDrawingCacheEnabled(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (drawingModel != null) {
            drawBackground(canvas);
            if(drawingModel.isDrawResult()){
                if(drawingModel.isDrawGridPoints()){
                    drawGrid(canvas);
                }
                DeusResult dr = FloorPlanModel.INSTANCE.getDeusResult();
                if(dr != null && drawingModel.getResultRenderType() != null){
                    dr.drawResult(canvas, drawingModel, drawingModel.getResultRenderType());
                }
                drawWalls(canvas);
                if(drawingModel.isDrawActivities()){
                    drawActivities(canvas);
                }
                drawConnectionPoints(canvas);
                if(drawingModel.isDrawAccessPoints()){
                    drawAccessPoints(canvas);
                }
                for (ApMeasurement apMeasurement : FloorPlanModel.INSTANCE.getApMeasurements()) {
                    apMeasurement.drawOnCanvas(canvas, drawingModel, paint, false);
                }
                drawTouch(canvas);
            }else{
                drawGrid(canvas);
                drawWalls(canvas);
                drawActivities(canvas);
                drawConnectionPoints(canvas);
                drawAccessPoints(canvas);
                drawTouch(canvas);
            }
        }
        super.onDraw(canvas);
    }

//	private void drawResults(Canvas canvas) {
//		OptimizeResultModel orm = OptimizeResultModel.getInstance();
//		if(!orm.getResultList().isEmpty()){
//			for(CSVResult or : orm.getResultList()){
//				or.drawOnCanvas(canvas, drawingModel, paint, false);
//			}
//		}
//	}

    private void drawBackground(Canvas canvas) {
        if (drawingModel.getBackgroundImage() != null) {
            Bitmap bgImg = drawingModel.getBackgroundImage();
            double scale = drawingModel.getBackgroundScale();
            System.out.println("bgscale: " + scale);
            Matrix m = new Matrix();
            double newScale = drawingModel.getPixelsPerInterval() / (scale * DrawingModel.INTERVAL);
            m.postScale((float) newScale, (float) newScale);
            float offsetX = drawingModel.getOffsetX();
            float offsetY = drawingModel.getOffsetY();

            float offsetXpx = offsetX * drawingModel.getPixelsPerInterval() / DrawingModel.INTERVAL;
            float offsetYpx = offsetY * drawingModel.getPixelsPerInterval() / DrawingModel.INTERVAL;
            m.postTranslate(-offsetXpx, -offsetYpx);
            canvas.drawBitmap(bgImg, m, paint);
        }
    }

    private void drawAccessPoints(Canvas canvas) {
        List<AccessPoint> accessPointList = FloorPlanModel.INSTANCE.getAccessPointList();
        for (AccessPoint ap : accessPointList) {
            ap.drawOnCanvas(canvas, drawingModel, paint, false);
        }
    }

    private void drawConnectionPoints(Canvas canvas) {
        List<ConnectionPoint> connectionPointList = FloorPlanModel.INSTANCE.getConnectionPointList();
        for (ConnectionPoint cp : connectionPointList) {
            cp.drawOnCanvas(canvas, drawingModel, paint, false);
        }

    }

    private void drawActivities(Canvas canvas) {
        List<DataActivity> activityList = FloorPlanModel.INSTANCE.getDataActivityList();
        for (DataActivity cp : activityList) {
            cp.drawOnCanvas(canvas, drawingModel, paint, false);
        }
    }

    private void drawWalls(Canvas canvas) {
        List<Wall> wallList = FloorPlanModel.INSTANCE.getWallList();
        for (Wall w : wallList) {
            w.drawOnCanvas(canvas, drawingModel, paint, false);
        }
    }

    private void drawTouch(Canvas canvas) {
        FloorPlanObject tw = drawingModel.getTouchFloorPlanObject();
        if (tw != null && tw.canDraw()) {
            tw.drawOnCanvas(canvas, drawingModel, paint, true);
        }
    }

    private void drawGrid(Canvas canvas) {
        paint.setColor(Color.BLACK);

        int viewWidth = getWidth();
        int viewHeight = getHeight();

        float percentOffsetX = (DrawingModel.INTERVAL - (drawingModel.getOffsetX() % DrawingModel.INTERVAL)) / DrawingModel.INTERVAL % 1;
        float percentOffsetY = (DrawingModel.INTERVAL - (drawingModel.getOffsetY() % DrawingModel.INTERVAL)) / DrawingModel.INTERVAL % 1;

        for (float i = percentOffsetX * drawingModel.getPixelsPerInterval(); i < viewWidth; i += drawingModel.getPixelsPerInterval()) {
            canvas.drawLine(i, 0f, i, viewHeight, paint);
        }

        for (float i = percentOffsetY * drawingModel.getPixelsPerInterval(); i < viewWidth; i += drawingModel.getPixelsPerInterval()) {
            canvas.drawLine(0, i, viewWidth, i, paint);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        System.out.println(drawingModel.getState());
        MotionEvent movedEvent = MotionEvent.obtain(event);
        movedEvent.setLocation(event.getX() - 50, event.getY() - 50);

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                Log.d("DEBUG", "ACION: " + drawingModel.getState());
                switch (drawingModel.getState()) {
                    case PRE_SELECTING_EDIT:
                    case PRE_SELECTING_INFO:
                    case PRE_SELECTING_REMOVE:
                        drawingModel.startSelect(movedEvent.getX(0), movedEvent.getY(0), true);
                        break;
                    case PRE_MEASURE_REMOVE:
                        drawingModel.startSelect(movedEvent.getX(0), movedEvent.getY(0), false);
                        break;
                    case PRE_PLACE:
                    case PLACING:
                        drawingModel.setTouchLocation(movedEvent.getX(0), movedEvent.getY(0));
                        break;
                    default:
                        break;

                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (drawingModel.isMoving()) {
                    System.out.println("ISMOVINg");
                    drawingModel.move(event.getX(0), event.getY(0), event.getX(1), event.getY(1));
                } else {
                    switch (drawingModel.getState()) {
                        case PLACING:
                            drawingModel.setTouchLocation(movedEvent.getX(0), movedEvent.getY(0));
                            break;
                        case SELECTING_EDIT:
                        case SELECTING_INFO:
                        case SELECTING_REMOVE:
                            drawingModel.select(movedEvent.getX(0), movedEvent.getY(0), true);
                            break;
                        case MEASURE_REMOVE:
                            drawingModel.select(movedEvent.getX(0), movedEvent.getY(0), false);
                            break;
                        default:
                            break;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                switch (drawingModel.getState()) {
                    case PLACING:
                        if (drawingModel.getTouchFloorPlanObject() instanceof ApMeasurement) {
                            final ApMeasurement apm = (ApMeasurement) drawingModel.getTouchFloorPlanObject();

                            final AlertDialog.Builder adb = new AlertDialog.Builder(getContext());
                            adb.setMessage(apm.getSamplePoolSize() + " signal strength samples will be taken at your current location.\n" +
                                    "Each sample measured can take several seconds.\n" +
                                    "Please do not move the device during the sampling.");
                            adb.setTitle("Sampling info");
                            adb.setIcon(android.R.drawable.ic_dialog_alert);
                            adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    MeasureParams mp = new MeasureParams(apm.getSamplePoolSize(), FloorPlanModel.INSTANCE.getAccessPointList());
                                    MainActivity.getInstance().getTaskManager().executeTask(new MeasureSignalStrengthTask(getContext()), mp, "Sampling...", new OnAsyncTaskCompleteListener<Integer>() {
                                        @Override
                                        public void onTaskCompleteSuccess(Integer result) {
                                            apm.setSignalStrength(result);
                                            PlaceResult pr = drawingModel.place();
                                            CSVResult closestResult = Utils.getResultAt(apm.getPoint1(), FloorPlanModel.INSTANCE.getDeusResult().getCsv());
                                            if (pr.equals(PlaceResult.SUCCESS)) {
                                                if (closestResult != null) {
                                                    Toast.makeText(getContext(), "difference in RSSI is: " + (result - closestResult.getPowerRX()), Toast.LENGTH_LONG).show();
                                                } else {
                                                    Toast.makeText(getContext(), "Cannot compare RSSI at this location", Toast.LENGTH_SHORT).show();
                                                }
                                            } else {
                                                Toast.makeText(getContext(), pr.getErrorMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onTaskFailed(Exception cause) {
                                            drawingModel.setTouchFloorPlanObject(drawingModel.getTouchFloorPlanObject());
                                            Log.e(TAG, cause.getMessage(), cause);
                                            Toast.makeText(getContext(), cause.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    }, false);
                                }
                            });


                            adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    drawingModel.setTouchFloorPlanObject(drawingModel.getTouchFloorPlanObject());
                                }
                            });
                            adb.setCancelable(false);
                            adb.show();
                        } else {
                            PlaceResult pr = drawingModel.place();
                            if (!pr.equals(PlaceResult.SUCCESS)) {
                                Toast.makeText(getContext(), pr.getErrorMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        break;
                    case SELECTING_EDIT:
                        FloorPlanObject dObj = drawingModel.getSelected();
                        if (dObj != null) {
                            AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                            alert.setTitle("Edit");
                            DataObjectView view = null;
                            switch (dObj.DATA_OBJECT_TYPE) {
                                case ACCESS_POINT:
                                    view = new AccessPointView(getContext(), ViewType.EDIT, drawingModel);
                                    break;
                                case CONNECTION_POINT:
                                    view = new ConnectionPointView(getContext(), ViewType.EDIT, drawingModel);
                                    break;
                                case DATA_ACTIVITY:
                                    view = new DataActivityView(getContext(), ViewType.EDIT, drawingModel);
                                    break;
                                case WALL:
                                    view = new WallView(getContext(), ViewType.EDIT, drawingModel);
                                    break;
                                default:
                                    break;
                            }
                            alert.setView(view);
                            alert.setPositiveButton(android.R.string.ok, null);
                            AlertDialog d = alert.create();
                            d.setOnDismissListener(new OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialog) {
                                    drawingModel.deselect();
                                }
                            });
                            d.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                            d.show();
                        } else {
                            drawingModel.deselect();
                        }
                        break;
                    case SELECTING_INFO:
                        dObj = drawingModel.getSelected();
                        if (dObj != null) {
                            AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                            alert.setTitle("Info");
                            DataObjectView view = null;
                            switch (dObj.DATA_OBJECT_TYPE) {
                                case ACCESS_POINT:
                                    view = new AccessPointView(getContext(), ViewType.INFO, drawingModel);
                                    break;
                                case CONNECTION_POINT:
                                    view = new ConnectionPointView(getContext(), ViewType.INFO, drawingModel);
                                    break;
                                case DATA_ACTIVITY:
                                    view = new DataActivityView(getContext(), ViewType.INFO, drawingModel);
                                    break;
                                case WALL:
                                    view = new WallView(getContext(), ViewType.INFO, drawingModel);
                                    break;
                                default:
                                    break;
                            }
                            alert.setView(view);
                            alert.setPositiveButton(android.R.string.ok, null);
                            AlertDialog d = alert.create();
                            d.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                            d.show();
                        }
                        drawingModel.deselect();
                        break;
                    case SELECTING_REMOVE:
                    case MEASURE_REMOVE:
                        dObj = drawingModel.getSelected();
                        if (dObj != null) {
                            FloorPlanModel.INSTANCE.removeFloorPlanObject(dObj);
                        }
                        drawingModel.deselect();
                        break;
                    default:
                        break;

                }
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                drawingModel.moveStart(event.getX(0), event.getY(0), event.getX(1), event.getY(1));
                break;
            case MotionEvent.ACTION_POINTER_UP:
                if (drawingModel.isMoving()) {
                    drawingModel.moveStop();
                    switch (drawingModel.getState()) {
                        case PLACING:
                            drawingModel.setPlaceMode();
                            break;
                        default:
                            break;
                    }

                }
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void update(Observable observable, Object data) {
        invalidate();
    }

    /**
     * @return the model
     */
    public DrawingModel getModel() {
        return drawingModel;
    }

    /**
     * @param drawingModel the model to set
     */
    public void setModel(DrawingModel drawingModel) {
        this.drawingModel = drawingModel;
        drawingModel.addObserver(this);
    }

    /* (non-Javadoc)
     * @see android.view.View#onSizeChanged(int, int, int, int)
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (drawingModel != null) {
            drawingModel.setViewSize(w, h);
            invalidate();
        }
        super.onSizeChanged(w, h, oldw, oldh);
    }

}
