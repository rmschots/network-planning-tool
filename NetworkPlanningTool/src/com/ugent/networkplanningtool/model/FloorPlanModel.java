package com.ugent.networkplanningtool.model;

import android.graphics.Point;
import android.util.Log;

import com.ugent.networkplanningtool.data.AccessPoint;
import com.ugent.networkplanningtool.data.ConnectionPoint;
import com.ugent.networkplanningtool.data.DataActivity;
import com.ugent.networkplanningtool.data.DataObject;
import com.ugent.networkplanningtool.data.FloorPlan;
import com.ugent.networkplanningtool.data.ServiceData.DeusResult;
import com.ugent.networkplanningtool.data.Wall;
import com.ugent.networkplanningtool.io.FloorPlanIO;
import com.ugent.networkplanningtool.utils.Couple;
import com.ugent.networkplanningtool.utils.Utils;

import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Stack;

import javax.xml.parsers.ParserConfigurationException;

public class FloorPlanModel extends Observable {

    private static FloorPlanModel model = new FloorPlanModel();

    //	private List<Wall> wallList;
//	private List<ConnectionPoint> connectionPointList;
//	private List<AccessPoint> accessPointList;
//	private List<DataActivity> dataActivityList;
    private FloorPlan floorPlan;
    private DeusResult deusResult;

    private Stack<FloorPlanModel> undoStack;
    private Stack<FloorPlanModel> redoStack;

    private FloorPlanModel(ArrayList<Wall> wallList, ArrayList<ConnectionPoint> connectionPointList, ArrayList<AccessPoint> accessPointList, ArrayList<DataActivity> dataActivityList) {
        floorPlan = new FloorPlan(wallList, connectionPointList, accessPointList, dataActivityList);
        undoStack = new Stack<FloorPlanModel>();
        redoStack = new Stack<FloorPlanModel>();
    }

    private FloorPlanModel() {
        floorPlan = new FloorPlan();
        undoStack = new Stack<FloorPlanModel>();
        redoStack = new Stack<FloorPlanModel>();
    }

    public static FloorPlanModel getInstance() {
        return model;
    }

    public FloorPlan getFloorPlan(){
        return floorPlan;
    }

    public List<Wall> getWallList() {
        return floorPlan.getWallList();
    }

    public List<ConnectionPoint> getConnectionPointList() {
        return floorPlan.getConnectionPointList();
    }

    public List<AccessPoint> getAccessPointList() {
        return floorPlan.getAccessPointList();
    }

    public List<DataActivity> getDataActivityList() {
        return floorPlan.getDataActivityList();
    }

    public DeusResult getDeusResult() {
        return deusResult;
    }

    private void setWallList(List<Wall> wallList) {
        floorPlan.setWallList(wallList);
    }

    private void setConnectionPointList(List<ConnectionPoint> connectionPointList) {
        floorPlan.setConnectionPointList(connectionPointList);
    }

    private void setAccessPointList(List<AccessPoint> accessPointList) {
        floorPlan.setAccessPointList(accessPointList);
    }

    private void setDataActivityList(List<DataActivity> dataActivityList) {
        floorPlan.setDataActivityList(dataActivityList);
    }

    public void setDeusResult(DeusResult deusResult) {
        if (this.deusResult != deusResult) {
            this.deusResult = deusResult;
            setChanged();
            notifyObservers(deusResult);
        }
    }

    public static void resetModel() {
        model.floorPlan = new FloorPlan();
        model.undoStack = new Stack<FloorPlanModel>();
        model.redoStack = new Stack<FloorPlanModel>();
        model.deusResult = null;
        model.setChanged();
        model.notifyObservers();
    }

    public void loadFloorPlan(File file) throws ParserConfigurationException, SAXException, IOException {
        this.floorPlan = FloorPlanIO.loadFloorPlan(file);
        undoStack = new Stack<FloorPlanModel>();
        redoStack = new Stack<FloorPlanModel>();
        model.setChanged();
        model.notifyObservers();
    }

    public void addDataObject(DataObject touchDataObject) {
        if (touchDataObject.isComplete()) {
            pushStateToStack(undoStack);
            redoStack.clear();
            if (touchDataObject instanceof Wall) {
                Wall newWall = (Wall) touchDataObject;
                Map<Double, Point> splitPoints = new HashMap<Double, Point>();
                List<Wall> wallListCopy = new ArrayList<Wall>(getWallList());
                for (Wall w : wallListCopy) {
                    Point[] pl = Utils.getIntersectionSpecial(newWall.getPoint1(), newWall.getPoint2(), w.getPoint1(), w.getPoint2(), true);
                    if (pl.length > 0) {
                        Wall add1 = null;
                        if (!pl[0].equals(w.getPoint1())) {
                            add1 = (Wall) w.getPartialDeepCopy();
                            add1.setPoint1(w.getPoint1());
                            add1.setPoint2(pl[0]);
                            getWallList().add(add1);
                        }
                        Wall add2 = null;
                        System.out.println(pl[0].equals(w.getPoint2()));
                        if (!pl[0].equals(w.getPoint2())) {
                            add2 = (Wall) w.getPartialDeepCopy();
                            add2.setPoint1(pl[0]);
                            add2.setPoint2(w.getPoint2());
                            getWallList().add(add2);
                        }
                        if (pl.length > 1) {
                            Wall wallToSplit;
                            if (add1 != null) {
                                Log.d("DEBUG", "ptlDist1: " + Utils.pointToLineDistance(add1.getPoint1(), add1.getPoint2(), pl[1], true));
                            }
                            if (add1 != null && Utils.pointToLineDistance(add1.getPoint1(), add1.getPoint2(), pl[1], true) < 3) {
                                wallToSplit = add1;
                            } else {
                                Log.d("DEBUG", "ptlDist2: " + Utils.pointToLineDistance(add2.getPoint1(), add2.getPoint2(), pl[1], true));
                                wallToSplit = add2;
                            }
                            if (!pl[1].equals(wallToSplit.getPoint1())) {
                                Wall add3 = (Wall) wallToSplit.getPartialDeepCopy();
                                add3.setPoint1(wallToSplit.getPoint1());
                                add3.setPoint2(pl[1]);
                                getWallList().add(add3);
                            }
                            if (!pl[1].equals(wallToSplit.getPoint2())) {
                                Wall add4 = (Wall) wallToSplit.getPartialDeepCopy();
                                add4.setPoint1(pl[1]);
                                add4.setPoint2(wallToSplit.getPoint2());
                                getWallList().add(add4);
                            }
                            getWallList().remove(wallToSplit);
                        }
                        getWallList().remove(w);
                    }
                    for (Point p : pl) {
                        double d1 = Utils.pointToLineDistance(w.getPoint1(), w.getPoint2(), newWall.getPoint1(), false);
                        double distance = Utils.pointToPointDistance(newWall.getPoint1(), p) + (d1 * 0.00001);
                        while (splitPoints.containsKey(distance) && !splitPoints.get(distance).equals(p)) {
                            distance += 0.00000000001;
                        }
                        splitPoints.put(distance, p);
                    }
                }
                if (!splitPoints.isEmpty()) {
                    Double[] coordArr = splitPoints.keySet().toArray(new Double[1]);
                    Arrays.sort(coordArr);
                    for (Double i : coordArr) {
                        Point p = splitPoints.get(i);
                        Wall add = (Wall) newWall.getPartialDeepCopy();
                        add.setPoint1(newWall.getPoint1());
                        add.setPoint2(p);
                        newWall.setPoint1(p);

                        wallListCopy = new ArrayList<Wall>(getWallList());
                        for (Wall w : wallListCopy) {
                            if (w.equalsLocation(add)) {
                                getWallList().remove(w);
                            }
                        }
                        if (!add.getPoint1().equals(add.getPoint2())) {
                            getWallList().add(add);
                        }

                    }
                }
                wallListCopy = new ArrayList<Wall>(getWallList());
                for (Wall w : wallListCopy) {
                    if (w.equalsLocation(newWall)) {
                        getWallList().remove(w);
                    }
                }
                if (!newWall.getPoint1().equals(newWall.getPoint2())) {
                    getWallList().add(newWall);
                }
                setChanged();
                notifyObservers();
            } else {
                addDataObjectToList(touchDataObject);
                setChanged();
                notifyObservers();
            }
        }
        Log.d("DEBUG", "size: " + getWallList().size());
        int i = 0;
        for (Wall w : getWallList()) {
            Log.d("DEBUG", "" + (i++) + ": " + w.getPoint1() + " " + w.getPoint2());
        }
    }


    private void addDataObjectToList(DataObject dataObject) {
        if (dataObject instanceof AccessPoint) {
            getAccessPointList().add((AccessPoint) dataObject);
        } else if (dataObject instanceof Wall) {
            getWallList().add((Wall) dataObject);
        } else if (dataObject instanceof ConnectionPoint) {
            getConnectionPointList().add((ConnectionPoint) dataObject);
        } else if (dataObject instanceof DataActivity) {
            getDataActivityList().add((DataActivity) dataObject);
        } else {
            Log.e("DEBUG", "Trying to add an invalid type of DataObject");
        }
    }

    public void removeDataObject(DataObject touchDataObject) {
        pushStateToStack(undoStack);
        redoStack.clear();
        removeDataObjectFromList(touchDataObject);
        setChanged();
        notifyObservers();
    }

    private void removeDataObjectFromList(DataObject dataobject) {
        Log.d("DEBUG", "removeDataObjectFromList");
        if (dataobject instanceof AccessPoint) {
            getAccessPointList().remove(dataobject);
        } else if (dataobject instanceof Wall) {
            getWallList().remove(dataobject);
        } else if (dataobject instanceof ConnectionPoint) {
            getConnectionPointList().remove(dataobject);
        } else if (dataobject instanceof DataActivity) {
            getDataActivityList().remove(dataobject);
        } else {
            Log.e("DEBUG", "Trying to add an invalid type of DataObject");
        }
    }

    public void undo() {
        pushStateToStack(redoStack);
        restoreStateFromStack(undoStack);
        setChanged();
        notifyObservers();
    }

    public void redo() {
        pushStateToStack(undoStack);
        restoreStateFromStack(redoStack);
        setChanged();
        notifyObservers();
    }

    public boolean canUndo() {
        return !undoStack.isEmpty();
    }

    public boolean canRedo() {
        return !redoStack.isEmpty();
    }

    public void deleteAllAccessPoints() {
        if (!getAccessPointList().isEmpty()) {
            pushStateToStack(undoStack);
            redoStack.clear();
            getAccessPointList().clear();
            setChanged();
            notifyObservers();
        }
    }

    public void deleteAllDataActivities() {
        if (!getDataActivityList().isEmpty()) {
            pushStateToStack(undoStack);
            redoStack.clear();
            getDataActivityList().clear();
            setChanged();
            notifyObservers();
        }
    }

    public void deleteAllConnectionPoints() {
        if (!getConnectionPointList().isEmpty()) {
            pushStateToStack(undoStack);
            redoStack.clear();
            getConnectionPointList().clear();
            setChanged();
            notifyObservers();
        }
    }

    public Point getClosestCornerToPoint(Point p) {
        Point closest = null;
        double distance = Double.POSITIVE_INFINITY;
        for (Wall w : getWallList()) {
            double dist = Utils.pointToPointDistance(p, w.getPoint1());
            if (dist < distance) {
                distance = dist;
                closest = w.getPoint1();
            }
            dist = Utils.pointToPointDistance(p, w.getPoint2());
            if (dist < distance) {
                distance = dist;
                closest = w.getPoint2();
            }
        }
        return closest;
    }

    public Couple<Double, Wall> getClosestWallToPoint(Point p, boolean upright) {
        Wall closest = null;
        double distance = Double.POSITIVE_INFINITY;
        for (Wall w : getWallList()) {
            double dist = Utils.pointToLineDistance(w.getPoint1(), w.getPoint2(), p, upright);
            if (dist < distance) {
                distance = dist;
                closest = w;
            }
        }
        if (distance == Double.POSITIVE_INFINITY) {
            return null;
        }
        return new Couple<Double, Wall>(distance, closest);
    }

    private void pushStateToStack(Stack<FloorPlanModel> stack) {
        ArrayList<Wall> wallsClone = new ArrayList<Wall>();
        for (Wall w : getWallList()) {
            wallsClone.add(new Wall(w));
        }
        ArrayList<ConnectionPoint> connectionPointsClone = new ArrayList<ConnectionPoint>();
        for (ConnectionPoint cp : getConnectionPointList()) {
            connectionPointsClone.add(new ConnectionPoint(cp));
        }
        ArrayList<AccessPoint> accessPointsClone = new ArrayList<AccessPoint>();
        for (AccessPoint ap : getAccessPointList()) {
            accessPointsClone.add(new AccessPoint(ap));
        }
        ArrayList<DataActivity> dataActivitiesClone = new ArrayList<DataActivity>();
        for (DataActivity da : getDataActivityList()) {
            dataActivitiesClone.add(new DataActivity(da));
        }
        FloorPlanModel fpm = new FloorPlanModel(
                wallsClone,
                connectionPointsClone,
                accessPointsClone,
                dataActivitiesClone);
        stack.push(fpm);
    }

    private void restoreStateFromStack(Stack<FloorPlanModel> stack) {
        FloorPlanModel fpm = stack.pop();
        model.setWallList(fpm.getWallList());
        model.setDataActivityList(fpm.getDataActivityList());
        model.setConnectionPointList(fpm.getConnectionPointList());
        model.setAccessPointList(fpm.getAccessPointList());
    }

    public Couple<Double, DataObject> getClosestDataObjectToPoint(Point touchPoint) {
        Couple<Double, Wall> couple = getClosestWallToPoint(touchPoint, false);
        double minDist = couple == null ? Double.POSITIVE_INFINITY : couple.getA();
        Log.d("DEBUG", "mindist: " + minDist);
        DataObject closest = couple == null ? null : couple.getB();
        for (DataObject dObj : getAccessPointList()) {
            double dist = Utils.pointToPointDistance(touchPoint, dObj.getPoint1());
            if (dist < minDist) {
                minDist = dist;
                closest = dObj;
            }
        }
        for (DataObject dObj : getDataActivityList()) {
            double dist = Utils.pointToPointDistance(touchPoint, dObj.getPoint1());
            if (dist < minDist) {
                minDist = dist;
                closest = dObj;
            }
        }
        for (DataObject dObj : getConnectionPointList()) {
            double dist = Utils.pointToPointDistance(touchPoint, dObj.getPoint1());
            if (dist < minDist) {
                minDist = dist;
                closest = dObj;
            }
        }

        if (minDist == Double.POSITIVE_INFINITY) {
            return null;
        }
        return new Couple<Double, DataObject>(minDist, closest);
    }
}
