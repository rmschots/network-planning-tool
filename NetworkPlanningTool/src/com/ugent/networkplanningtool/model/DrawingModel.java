package com.ugent.networkplanningtool.model;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.Log;

import com.ugent.networkplanningtool.MainActivity;
import com.ugent.networkplanningtool.R;
import com.ugent.networkplanningtool.data.ConnectionPoint;
import com.ugent.networkplanningtool.data.FloorPlanObject;
import com.ugent.networkplanningtool.data.ServiceData.DeusResult;
import com.ugent.networkplanningtool.data.Wall;
import com.ugent.networkplanningtool.data.enums.Material;
import com.ugent.networkplanningtool.data.enums.SnapTo;
import com.ugent.networkplanningtool.data.enums.Thickness;
import com.ugent.networkplanningtool.data.enums.WallType;
import com.ugent.networkplanningtool.utils.Couple;
import com.ugent.networkplanningtool.utils.Utils;

import java.util.Observable;

public class DrawingModel extends Observable {

    public static enum PlaceResult {
        SUCCESS(null),
        CONNECTION_POINT_NOT_ADJACENT_TO_WALL(MainActivity.getInstance().getResources().getString(R.string.connectionPointNotAdjacentToWall)),
        NOTHING_TO_PLACE(MainActivity.getInstance().getResources().getString(R.string.nothingToPlace));
        private String errorMessage;

        private PlaceResult(String errorMessage) {
            this.errorMessage = errorMessage;
        }

        public String getErrorMessage() {
            return errorMessage;
        }
    }

    // states the drawing area can be in
    public static enum STATE {
        IDLE, PRE_PLACE, PLACING, SELECTING_INFO, PRE_SELECTING_INFO, SELECTING_EDIT, PRE_SELECTING_EDIT, SELECTING_REMOVE, PRE_SELECTING_REMOVE, PRE_MEASURE_REMOVE, MEASURE_REMOVE
    }

    // ground plan dimensions (in units)
    public static final int INTERVAL = 50;
    // maximum allowed zoom (in units)
    private static final int maxZoomIn = 200;
    private static final float DEFAULT_PIXELS_PER_INTERVAL = 100;
    public static int FLOOR_WIDTH = 10000;
    public static int FLOOR_HEIGHT = 3550;

    private boolean drawAccessPoints = true;
    private boolean drawActivities = true;
    private boolean drawLabels = true;
    private boolean drawGridPoints = true;
    private boolean drawResult;

    private DeusResult.ResultType resultRenderType;
    private STATE state;
    private boolean moving;
    // location on ground plan to view
    private float offsetX; // in units
    private float offsetY; // in units
    private float pixelsPerInterval; // in pixels
    private double distanceStart = -1; // in pixels
    private PointF dragStart = null; // in units
    private Point touchLocation = null;
    // dimensions of the actual view (in pixels)
    private int viewWidth;
    private int viewHeight;
    // Touch object info
    private FloorPlanObject touchFloorPlanObject = new Wall(WallType.WALL, Thickness.THIN, Material.BRICK);
    private boolean zoomInMaxed;
    private boolean zoomOutMaxed;
    private SnapTo snapTo = SnapTo.GRID;
    private Bitmap backgroundImage;
    private double backgroundScale;

    public DrawingModel(int viewWidth, int viewHeight) {
        offsetX = 0;
        offsetY = 0;

        state = STATE.PRE_PLACE;
        moving = false;
        this.viewWidth = viewWidth;
        this.viewHeight = viewHeight;

        if (viewWidth == 0 || viewHeight == 0) {
            this.pixelsPerInterval = DEFAULT_PIXELS_PER_INTERVAL;
        } else {
            setPixelsPerInterval(DEFAULT_PIXELS_PER_INTERVAL);
        }
    }

    public float getOffsetX() {
        return offsetX;
    }

    public float getOffsetY() {
        return offsetY;
    }

    public float getActualViewWidth() {
        return (viewWidth - 1) * INTERVAL / pixelsPerInterval;
    }

    public float getActualViewHeight() {
        return (viewHeight - 1) * INTERVAL / pixelsPerInterval;
    }

    public void setOffsetY(float offsetY) {
        if (offsetY > 0) {
            float actualViewHeight = getActualViewHeight();
            if (offsetY + actualViewHeight < FLOOR_HEIGHT) {
                this.offsetY = offsetY;
            } else {
                this.offsetY = FLOOR_HEIGHT - actualViewHeight;
            }
        } else {
            this.offsetY = 0;
        }
        setChanged();
        notifyObservers();
    }

    public void setOffsetX(float offsetX) {
        if (offsetX > 0) {
            float actualViewWidth = getActualViewWidth();
            if (offsetX + actualViewWidth < FLOOR_WIDTH) {
                this.offsetX = offsetX;
            } else {
                this.offsetX = FLOOR_WIDTH - actualViewWidth;
            }
        } else {
            this.offsetX = 0;
        }
        setChanged();
        notifyObservers();
    }

    public boolean isDrawResult() {
        if (drawResult) {
            System.out.println("LOLZ " + FloorPlanModel.INSTANCE.getDeusResult());
        }
        return drawResult;
    }

    public void setDrawResult(boolean drawResult) {
        this.drawResult = drawResult;
    }

    public boolean isDrawAccessPoints() {
        return drawAccessPoints;
    }

    public void setDrawAccessPoints(boolean drawAccessPoints) {
        if (this.drawAccessPoints != drawAccessPoints) {
            this.drawAccessPoints = drawAccessPoints;
            setChanged();
            notifyObservers();
        }

    }

    public boolean isDrawActivities() {
        return drawActivities;
    }

    public void setDrawActivities(boolean drawActivities) {
        if (this.drawActivities != drawActivities) {
            this.drawActivities = drawActivities;
            setChanged();
            notifyObservers();
        }
    }

    public boolean isDrawLabels() {
        return drawLabels;
    }

    public void setDrawLabels(boolean drawLabels) {
        if (this.drawLabels != drawLabels) {
            this.drawLabels = drawLabels;
            setChanged();
            notifyObservers();
        }
    }

    public boolean isDrawGridPoints() {
        return drawGridPoints;
    }

    public void setDrawGridPoints(boolean drawGridPoints) {
        if (this.drawGridPoints != drawGridPoints) {
            this.drawGridPoints = drawGridPoints;
            setChanged();
            notifyObservers();
        }
    }

    public DeusResult.ResultType getResultRenderType() {
        return resultRenderType;
    }

    public void setResultRenderType(DeusResult.ResultType resultRenderType) {
        if (this.resultRenderType != resultRenderType) {
            this.resultRenderType = resultRenderType;
            setChanged();
            notifyObservers();
        }
    }

    public Point getTouchLocation() {
        return touchLocation;
    }

    public boolean isZoomInMaxed() {
        return zoomInMaxed;
    }

    public boolean isZoomOutMaxed() {
        return zoomOutMaxed;
    }

    public void move(float x1, float y1, float x2, float y2) {
        double distanceMoved = calculateDistance(x1, y1, x2, y2);

        setPixelsPerInterval(pixelsPerInterval
                / (float) (distanceStart / distanceMoved));
        distanceStart = distanceMoved;

        setOffsetX(dragStart.x - Math.min(x1, x2) / pixelsPerInterval
                * DrawingModel.INTERVAL);
        setOffsetY(dragStart.y - Math.min(y1, y2) / pixelsPerInterval
                * DrawingModel.INTERVAL);
        setChanged();
        notifyObservers();
    }

    public void moveStart(float x1, float y1, float x2, float y2) {
        switch (state) {
            case PLACING:
                touchFloorPlanObject = touchFloorPlanObject.getPartialDeepCopy();
                break;
            case SELECTING_EDIT:
            case SELECTING_INFO:
            case SELECTING_REMOVE:
            case MEASURE_REMOVE:
                deselect();
                break;
            default:
                break;

        }
        moving = true;
        distanceStart = calculateDistance(x1, y1, x2, y2);
        dragStart = new PointF(offsetX + Math.min(x1, x2) / pixelsPerInterval * DrawingModel.INTERVAL,
                offsetY + Math.min(y1, y2) / pixelsPerInterval * DrawingModel.INTERVAL);
        setChanged();
        notifyObservers();
    }

    public void deselect() {
        Log.d("DEBUG", "DESELECT");
        switch (state) {
            case SELECTING_EDIT:
                state = STATE.PRE_SELECTING_EDIT;
                touchFloorPlanObject = null;
                setChanged();
                notifyObservers();
                break;
            case SELECTING_INFO:
                state = STATE.PRE_SELECTING_INFO;
                touchFloorPlanObject = null;
                setChanged();
                notifyObservers();
                break;
            case SELECTING_REMOVE:
                state = STATE.PRE_SELECTING_REMOVE;
                touchFloorPlanObject = null;
                setChanged();
                notifyObservers();
                break;
            case MEASURE_REMOVE:
                state = STATE.PRE_MEASURE_REMOVE;
                touchFloorPlanObject = null;
                setChanged();
                notifyObservers();
                break;
            default:
                break;
        }
    }

    private double calculateDistance(float x1, float y1, float x2, float y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    public void moveStop() {
        moving = false;
        setChanged();
        notifyObservers();
    }

    public PlaceResult place() {
        Log.d("debug", "place");
        state = STATE.PRE_PLACE;
        if (touchFloorPlanObject != null) {
            if (touchFloorPlanObject instanceof Wall) {
                Wall wall = (Wall) touchFloorPlanObject;
                if (wall.isComplete()) {
                    if (!(wall.getPoint1().x == wall.getPoint2().x && wall.getPoint1().y == wall.getPoint2().y)) {
                        FloorPlanModel.INSTANCE.addFloorPlanObject(wall);
                    }
                    touchFloorPlanObject = touchFloorPlanObject.getPartialDeepCopy();
                } else {
                    wall.setPoint2(new Point(wall.getPoint1()));
                }
            } else {
                if (touchFloorPlanObject.isComplete()) {
                    if (touchFloorPlanObject instanceof ConnectionPoint) {
                        Couple<Double, Wall> closestWallCouple = FloorPlanModel.INSTANCE.getClosestWallToPoint(touchFloorPlanObject.getPoint1(), true);
                        if (closestWallCouple != null) {
                            double dist = closestWallCouple.getA();
                            if (dist <= INTERVAL / 4 && dist != 0) {
                                FloorPlanModel.INSTANCE.addFloorPlanObject(touchFloorPlanObject);
                                touchFloorPlanObject = touchFloorPlanObject.getPartialDeepCopy();
                                setChanged();
                                notifyObservers();
                                return PlaceResult.SUCCESS;
                            }
                        }
                        touchFloorPlanObject = touchFloorPlanObject.getPartialDeepCopy();
                        setChanged();
                        notifyObservers();
                        return PlaceResult.CONNECTION_POINT_NOT_ADJACENT_TO_WALL;
                    }
                    FloorPlanModel.INSTANCE.addFloorPlanObject(touchFloorPlanObject);
                    touchFloorPlanObject = touchFloorPlanObject.getPartialDeepCopy();
                } else {
                    // should not happen (trying to place non-complete non-wall)
                    Log.e("DEBUG", "error: trying to place non-complete non-wall");
                    touchFloorPlanObject = touchFloorPlanObject.getPartialDeepCopy();
                }

            }
            setChanged();
            notifyObservers();
            return PlaceResult.SUCCESS;
        }
        // else nothing to place
        return PlaceResult.NOTHING_TO_PLACE;
    }

    public void setTouchLocation(float x, float y) {
        if (touchFloorPlanObject != null) {
            state = STATE.PLACING;
            Point wallPoint = getActualTouchLocation(x, y);
            if (touchFloorPlanObject instanceof Wall) {
                Couple<Double, Point> closestCouple = FloorPlanModel.INSTANCE.getClosestCornerToPoint(wallPoint);
                if (closestCouple.getB() != null && closestCouple.getA() <= INTERVAL / 2) {
                    wallPoint = closestCouple.getB();
                } else {
                    switch (snapTo) {
                        case GRID:
                            int rest = wallPoint.x % INTERVAL;
                            if (rest < INTERVAL / 2) {
                                wallPoint.x = wallPoint.x - rest;
                            } else {
                                wallPoint.x = wallPoint.x + INTERVAL - rest;
                            }
                            rest = wallPoint.y % INTERVAL;
                            if (rest < INTERVAL / 2) {
                                wallPoint.y = wallPoint.y - rest;
                            } else {
                                wallPoint.y = wallPoint.y + INTERVAL - rest;
                            }
                            break;
                        case WALLS:
                            Couple<Double, Wall> closestWallCouple = FloorPlanModel.INSTANCE.getClosestWallToPoint(wallPoint, false);
                            if (closestWallCouple != null && closestWallCouple.getA() <= INTERVAL / 2) {
                                Wall closestWall = closestWallCouple.getB();
                                Point p = Utils.pointProjectionOnLine(closestWall.getPoint1(), closestWall.getPoint2(), wallPoint);
                                if (p != null) {
                                    wallPoint = p;
                                } else {
                                    Log.e("DEBUG", "closestWall NULL");
                                }
                            }
                        default:
                            break;
                    }
                }
                if ((touchFloorPlanObject).isComplete()) {
                    Wall wall = (Wall) touchFloorPlanObject;
                    wall.setPoint2(wallPoint);
                } else {
                    touchFloorPlanObject.setPoint1(wallPoint);
                }
            } else {
                touchFloorPlanObject.setPoint1(wallPoint);
            }
            touchLocation = wallPoint;
            setChanged();
            notifyObservers();
        }
    }

    public Point getActualTouchLocation(float x, float y) {
        return new Point((int) (offsetX + x / pixelsPerInterval * DrawingModel.INTERVAL),
                (int) (offsetY + y / pixelsPerInterval * DrawingModel.INTERVAL));
    }

    public void setViewSize(int width, int height) {
        System.out.println("setViewSize: " + width + " " + height);
        this.viewWidth = width;
        this.viewHeight = height;
        setPixelsPerInterval(this.pixelsPerInterval);
        setChanged();
        notifyObservers();
    }

    public void zoomIn() {
        setPixelsPerInterval(pixelsPerInterval * 2f);
        setChanged();
        notifyObservers();
    }

    public void zoomOut() {
        setPixelsPerInterval(pixelsPerInterval * 0.5f);
        setChanged();
        notifyObservers();
    }

    /**
     * @return the touchWall
     */
    public FloorPlanObject getTouchFloorPlanObject() {
        return touchFloorPlanObject;
    }

    public void setTouchFloorPlanObject(FloorPlanObject drawItem) {
        if (touchFloorPlanObject == drawItem) {
            touchFloorPlanObject = touchFloorPlanObject.getPartialDeepCopy();
        } else {
            touchFloorPlanObject = drawItem;
        }
        setChanged();
        notifyObservers();
    }

    public void setSnapToGrid(SnapTo snapToGrid) {
        this.snapTo = snapToGrid;
    }

    public void setInfoSelectionMode() {
        Log.d("DEBUG", "INFOSELECTION MODE");
        state = STATE.PRE_SELECTING_INFO;
        touchFloorPlanObject = null;
        setChanged();
        notifyObservers();
    }

    public void setEditSelectionMode() {
        Log.d("DEBUG", "INFOEDIT MODE");
        state = STATE.PRE_SELECTING_EDIT;
        touchFloorPlanObject = null;
        setChanged();
        notifyObservers();
    }

    public void setRemoveSelectionMode() {
        Log.d("DEBUG", "INFOREMOVE MODE");
        state = STATE.PRE_SELECTING_REMOVE;
        touchFloorPlanObject = null;
        setChanged();
        notifyObservers();
    }

    public void setMeasureRemoveMode() {
        Log.d("DEBUG", "MEASUREREMOVE MODE");
        state = STATE.PRE_MEASURE_REMOVE;
        touchFloorPlanObject = null;
        setChanged();
        notifyObservers();
    }

    public void setPlaceMode() {
        state = STATE.PRE_PLACE;
        setChanged();
        notifyObservers();
    }

    public void setPlaceMode(FloorPlanObject floorPlanObject) {
        System.out.println("PREPACCCCEE");
        state = STATE.PRE_PLACE;
        setTouchFloorPlanObject(floorPlanObject);
        setChanged();
        notifyObservers();
    }

    public void startSelect(float x, float y, boolean select) {
        switch (state) {
            case PRE_SELECTING_EDIT:
                state = STATE.SELECTING_EDIT;
                break;
            case PRE_SELECTING_INFO:
                state = STATE.SELECTING_INFO;
                break;
            case PRE_SELECTING_REMOVE:
                state = STATE.SELECTING_REMOVE;
                break;
            case PRE_MEASURE_REMOVE:
                state = STATE.MEASURE_REMOVE;
                break;
            default:
                break;
        }
        select(x, y, select);
    }

    public void select(float x, float y, boolean select) {
        touchLocation = getActualTouchLocation(x, y);
        // get closest
        Couple<Double, FloorPlanObject> closestCouple = FloorPlanModel.INSTANCE.getClosestFloorPlanObjectToPoint(touchLocation, select);
        if (closestCouple == null) {
            touchFloorPlanObject = null;
            return;
        }
        double distance = closestCouple.getA();
        if (distance < 40) {
            touchFloorPlanObject = closestCouple.getB();
            touchLocation = touchFloorPlanObject.getPoint1();
        } else {
            touchFloorPlanObject = null;
        }
        setChanged();
        notifyObservers();
    }

    public FloorPlanObject getSelected() {
        switch (state) {
            case SELECTING_EDIT:
            case SELECTING_INFO:
            case SELECTING_REMOVE:
            case MEASURE_REMOVE:
                return touchFloorPlanObject;
            default:
                return null;

        }
    }

    public void setIdle() {
        state = STATE.IDLE;
        touchFloorPlanObject = null;
        setChanged();
        notifyObservers();
    }

    public STATE getState() {
        return state;
    }

    public boolean isMoving() {
        return moving;
    }

    public void setBackground(Bitmap bgImg, double bgScale) {
        this.backgroundImage = bgImg;
        this.backgroundScale = bgScale;
        setChanged();
        notifyObservers();
    }

    public Bitmap getBackgroundImage() {
        return backgroundImage;
    }

    public double getBackgroundScale() {
        return backgroundScale;
    }

    public float convertCoordinateToLocation(boolean isXCoord, float coordinate) {
        return (coordinate - (isXCoord ? getOffsetX() : getOffsetY()))
                * getPixelsPerInterval() / DrawingModel.INTERVAL;
    }

    public float getPixelsPerInterval() {
        return pixelsPerInterval;
    }

    public void setPixelsPerInterval(float pixelsPerInterval) {
        if (pixelsPerInterval > 0) {
            if (pixelsPerInterval < Math.min(viewWidth - 1, viewHeight - 1)
                    * INTERVAL / (float) maxZoomIn) {
                zoomInMaxed = false;
                float maxX = (viewWidth - 1) * INTERVAL / (float) FLOOR_WIDTH;
                float maxY = (viewHeight - 1) * INTERVAL / (float) FLOOR_HEIGHT;
                float max = Math.max(maxX, maxY);
                if (max < pixelsPerInterval) {
                    zoomOutMaxed = false;
                    this.pixelsPerInterval = pixelsPerInterval;
                } else {
                    zoomOutMaxed = true;
                    this.pixelsPerInterval = max;
                    setOffsetX(getOffsetX());
                    setOffsetY(getOffsetY());
                }

            } else {
                zoomInMaxed = true;
                zoomOutMaxed = false;
                this.pixelsPerInterval = Math
                        .min(viewWidth - 1, viewHeight - 1)
                        * INTERVAL
                        / (float) maxZoomIn;
            }
        }
    }
}
