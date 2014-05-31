package com.ugent.networkplanningtool.model;

import android.graphics.Point;
import android.util.Log;

import com.ugent.networkplanningtool.data.AccessPoint;
import com.ugent.networkplanningtool.data.ApMeasurement;
import com.ugent.networkplanningtool.data.ConnectionPoint;
import com.ugent.networkplanningtool.data.DataActivity;
import com.ugent.networkplanningtool.data.FloorPlan;
import com.ugent.networkplanningtool.data.FloorPlanObject;
import com.ugent.networkplanningtool.data.ServiceData.DeusResult;
import com.ugent.networkplanningtool.data.Wall;
import com.ugent.networkplanningtool.utils.Couple;
import com.ugent.networkplanningtool.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Stack;

/**
 * Model for the floor plan
 */
public class FloorPlanModel extends Observable {

    /**
     * The instance used in the application
     */
    public static FloorPlanModel INSTANCE = new FloorPlanModel();

    private FloorPlan floorPlan;
    private DeusResult deusResult;
    private List<ApMeasurement> apMeasurements;


    private Stack<FloorPlan> undoStack;
    private Stack<FloorPlan> redoStack;

    /**
     * Creates a new instance with given values
     *
     * @param wallList            the list of Walls
     * @param connectionPointList the list of ConnectionPoints
     * @param accessPointList     the list of AccessPoints
     * @param dataActivityList    the list of DataActivities
     */
    private FloorPlanModel(ArrayList<Wall> wallList, ArrayList<ConnectionPoint> connectionPointList, ArrayList<AccessPoint> accessPointList, ArrayList<DataActivity> dataActivityList) {
        floorPlan = new FloorPlan(wallList, connectionPointList, accessPointList, dataActivityList);
        undoStack = new Stack<FloorPlan>();
        redoStack = new Stack<FloorPlan>();
        apMeasurements = new ArrayList<ApMeasurement>();
    }

    /**
     * Creates a new instance with default values
     */
    private FloorPlanModel() {
        floorPlan = new FloorPlan();
        undoStack = new Stack<FloorPlan>();
        redoStack = new Stack<FloorPlan>();
        apMeasurements = new ArrayList<ApMeasurement>();
    }

    /**
     * Getter for the floor plan
     * @return the floor plan
     */
    public FloorPlan getFloorPlan() {
        return floorPlan;
    }

    /**
     * Getter for the Wall list
     * @return the Wall list
     */
    public List<Wall> getWallList() {
        return floorPlan.getWallList();
    }

    /**
     * Getter for the ConnectionPoint list
     * @return the ConnectionPoint list
     */
    public List<ConnectionPoint> getConnectionPointList() {
        return floorPlan.getConnectionPointList();
    }

    /**
     * Getter for the AccessPoint list
     * @return the AccessPoint list
     */
    public List<AccessPoint> getAccessPointList() {
        return floorPlan.getAccessPointList();
    }

    /**
     * Getter for the DataActivity list
     * @return the DataActivity list
     */
    public List<DataActivity> getDataActivityList() {
        return floorPlan.getDataActivityList();
    }

    /**
     * Getter for the results of a model
     * @return the results of a model
     */
    public DeusResult getDeusResult() {
        return deusResult;
    }

    /**
     * Sets the Wall list
     * @param wallList the Wall list to be set
     */
    private void setWallList(List<Wall> wallList) {
        floorPlan.setWallList(wallList);
    }

    /**
     * Gets the user measurements
     * @return the user measurements
     */
    public List<ApMeasurement> getApMeasurements() {
        return apMeasurements;
    }

    /**
     * Sets the ConnectionPoint list
     * @param connectionPointList the ConnectionPoint list
     */
    private void setConnectionPointList(List<ConnectionPoint> connectionPointList) {
        floorPlan.setConnectionPointList(connectionPointList);
    }

    /**
     * Sets the AccessPoint list
     * @param accessPointList the AccessPoint list
     */
    private void setAccessPointList(List<AccessPoint> accessPointList) {
        floorPlan.setAccessPointList(accessPointList);
    }

    /**
     * Sets the DataActivity list
     * @param dataActivityList
     */
    private void setDataActivityList(List<DataActivity> dataActivityList) {
        floorPlan.setDataActivityList(dataActivityList);
    }

    /**
     * Sets the results of a model
     * @param deusResult the results of a model
     */
    public void setDeusResult(DeusResult deusResult) {
        if (this.deusResult != deusResult) {
            this.deusResult = deusResult;
            setChanged();
            notifyObservers(deusResult);
        }
    }

    /**
     * Shifts the results of the model
     * @param shift the shift to be applied on the results
     */
    public void shiftDeusResult(double shift) {
        getDeusResult().shiftResults(shift);
        setChanged();
        notifyObservers();
    }

    /**
     * Resets the model to default values
     */
    public void resetModel() {
        floorPlan = new FloorPlan();
        undoStack = new Stack<FloorPlan>();
        redoStack = new Stack<FloorPlan>();
        deusResult = null;
        apMeasurements = new ArrayList<ApMeasurement>();
        setChanged();
        notifyObservers();
    }

    /**
     * Sets the floor plan
     * @param floorPlan the floor plan
     */
    public void setFloorPlan(FloorPlan floorPlan) {
        this.floorPlan = floorPlan;
        undoStack = new Stack<FloorPlan>();
        redoStack = new Stack<FloorPlan>();
        setChanged();
        notifyObservers();
    }

    /**
     * Sets the user measurements
     * @param measurements the user measurements
     */
    public void setApMeasurements(List<ApMeasurement> measurements) {
        this.apMeasurements = measurements;
        setChanged();
        notifyObservers();
    }

    /**
     * Adds a FloorPlanObject to the floor plan
     * @param touchFloorPlanObject the FloorPlanObject to be added
     */
    public void addFloorPlanObject(FloorPlanObject touchFloorPlanObject) {
        if (touchFloorPlanObject.isComplete()) {
            if (!(touchFloorPlanObject instanceof ApMeasurement)) {
                pushStateToStack(undoStack);
                redoStack.clear();
            }
            if (touchFloorPlanObject instanceof Wall) {
                Wall newWall = (Wall) touchFloorPlanObject;
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
                addFloorPlanObjectToList(touchFloorPlanObject);
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

    /**
     * Adds a FloorPlanObject to the appropriate list
     * @param floorPlanObject the FloorPlanObject to add
     */
    private void addFloorPlanObjectToList(FloorPlanObject floorPlanObject) {
        if (floorPlanObject instanceof AccessPoint) {
            getAccessPointList().add((AccessPoint) floorPlanObject);
        } else if (floorPlanObject instanceof Wall) {
            getWallList().add((Wall) floorPlanObject);
        } else if (floorPlanObject instanceof ConnectionPoint) {
            getConnectionPointList().add((ConnectionPoint) floorPlanObject);
        } else if (floorPlanObject instanceof DataActivity) {
            getDataActivityList().add((DataActivity) floorPlanObject);
        } else if (floorPlanObject instanceof ApMeasurement) {
            apMeasurements.add((ApMeasurement) floorPlanObject);
        } else {
            Log.e("DEBUG", "Trying to add an invalid type of DataObject");
        }
    }

    /**
     * Removes a floorPlanObject
     * @param touchFloorPlanObject the FloorPlanObject to be removed
     */
    public void removeFloorPlanObject(FloorPlanObject touchFloorPlanObject) {
        if (!(touchFloorPlanObject instanceof ApMeasurement)) {
            pushStateToStack(undoStack);
            redoStack.clear();
        }
        removeFloorPlanObjectFromList(touchFloorPlanObject);
        setChanged();
        notifyObservers();
    }

    /**
     * Removes a FloorPlanObject from the appropriate list
     * @param dataobject the FloorPlanObject to remove
     */
    private void removeFloorPlanObjectFromList(FloorPlanObject dataobject) {
        Log.d("DEBUG", "removeFloorPlanObjectFromList");
        if (dataobject instanceof AccessPoint) {
            getAccessPointList().remove(dataobject);
        } else if (dataobject instanceof Wall) {
            getWallList().remove(dataobject);
        } else if (dataobject instanceof ConnectionPoint) {
            getConnectionPointList().remove(dataobject);
        } else if (dataobject instanceof DataActivity) {
            getDataActivityList().remove(dataobject);
        } else if (dataobject instanceof ApMeasurement) {
            getApMeasurements().remove(dataobject);
        } else {
            Log.e("DEBUG", "Trying to add an invalid type of DataObject");
        }
    }

    /**
     * Undo operation
     */
    public void undo() {
        pushStateToStack(redoStack);
        restoreStateFromStack(undoStack);
        setChanged();
        notifyObservers();
    }

    /**
     * Redo operation
     */
    public void redo() {
        pushStateToStack(undoStack);
        restoreStateFromStack(redoStack);
        setChanged();
        notifyObservers();
    }

    /**
     * Returns whether an undo operation is possible
     * @return whether an undo operation is possible
     */
    public boolean canUndo() {
        return !undoStack.isEmpty();
    }

    /**
     * Returns whether a redo operation is possible
     * @return whether a redo operation is possible
     */
    public boolean canRedo() {
        return !redoStack.isEmpty();
    }

    /**
     * Deletes all access points
     */
    public void deleteAllAccessPoints() {
        if (!getAccessPointList().isEmpty()) {
            pushStateToStack(undoStack);
            redoStack.clear();
            getAccessPointList().clear();
            setChanged();
            notifyObservers();
        }
    }

    /**
     * Deletes all data activities
     */
    public void deleteAllDataActivities() {
        if (!getDataActivityList().isEmpty()) {
            pushStateToStack(undoStack);
            redoStack.clear();
            getDataActivityList().clear();
            setChanged();
            notifyObservers();
        }
    }

    /**
     * Deletes all connection points
     */
    public void deleteAllConnectionPoints() {
        if (!getConnectionPointList().isEmpty()) {
            pushStateToStack(undoStack);
            redoStack.clear();
            getConnectionPointList().clear();
            setChanged();
            notifyObservers();
        }
    }

    /**
     * Returns the distance and location of the closest edge of a Wall to the given point
     * @param p the point
     * @return the distance and location of the closest edge of a Wall to the given point
     */
    public Couple<Double, Point> getClosestCornerToPoint(Point p) {
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
        return new Couple<Double, Point>(distance, closest);
    }

    /**
     * Returns the distance and location of the closest Wall to the given point
     * @param p the point
     * @param upright whether only upright locations to the Walls need to be considered
     * @return the distance and location of the closest Wall to the given point
     */
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

    /**
     * Pushes the current state to the stack
     * @param stack the stack to push the current state to
     */
    private void pushStateToStack(Stack<FloorPlan> stack) {
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
        FloorPlan fpm = new FloorPlan(
                wallsClone,
                connectionPointsClone,
                accessPointsClone,
                dataActivitiesClone);
        stack.push(fpm);
    }

    /**
     * Restore the most recent state from the given stack
     * @param stack the stack to restore a state of
     */
    private void restoreStateFromStack(Stack<FloorPlan> stack) {
        FloorPlan fpm = stack.pop();
        setWallList(fpm.getWallList());
        setDataActivityList(fpm.getDataActivityList());
        setConnectionPointList(fpm.getConnectionPointList());
        setAccessPointList(fpm.getAccessPointList());
    }

    /**
     * Returns the closest FloorPlanObject to the given location
     * @param touchPoint
     * @param select whether general FloorPlanObjects or measurements should be selected
     * @return the closest FloorPlanObject to the given location
     */
    public Couple<Double, FloorPlanObject> getClosestFloorPlanObjectToPoint(Point touchPoint, boolean select) {
        double minDist = Double.POSITIVE_INFINITY;
        FloorPlanObject closest = null;
        if (select) {
            Couple<Double, Wall> couple = getClosestWallToPoint(touchPoint, false);
            minDist = couple == null ? Double.POSITIVE_INFINITY : couple.getA();
            Log.d("DEBUG", "mindist: " + minDist);
            closest = couple == null ? null : couple.getB();
            for (FloorPlanObject dObj : getAccessPointList()) {
                double dist = Utils.pointToPointDistance(touchPoint, dObj.getPoint1());
                if (dist < minDist) {
                    minDist = dist;
                    closest = dObj;
                }
            }
            for (FloorPlanObject dObj : getDataActivityList()) {
                double dist = Utils.pointToPointDistance(touchPoint, dObj.getPoint1());
                if (dist < minDist) {
                    minDist = dist;
                    closest = dObj;
                }
            }
            for (FloorPlanObject dObj : getConnectionPointList()) {
                double dist = Utils.pointToPointDistance(touchPoint, dObj.getPoint1());
                if (dist < minDist) {
                    minDist = dist;
                    closest = dObj;
                }
            }
        } else {
            for (ApMeasurement dObj : apMeasurements) {
                double dist = Utils.pointToPointDistance(touchPoint, dObj.getPoint1());
                if (dist < minDist) {
                    minDist = dist;
                    closest = dObj;
                }
            }
        }

        if (minDist == Double.POSITIVE_INFINITY) {
            return null;
        }
        return new Couple<Double, FloorPlanObject>(minDist, closest);
    }
}
