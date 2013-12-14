package com.ugent.networkplanningtool.model;

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
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

import android.graphics.Point;
import android.util.Log;

import com.ugent.networkplanningtool.data.AccessPoint;
import com.ugent.networkplanningtool.data.ConnectionPoint;
import com.ugent.networkplanningtool.data.DataActivity;
import com.ugent.networkplanningtool.data.DataObject;
import com.ugent.networkplanningtool.data.Wall;
import com.ugent.networkplanningtool.io.FloorPlanIO;
import com.ugent.networkplanningtool.utils.Couple;
import com.ugent.networkplanningtool.utils.Utils;

public class FloorPlanModel extends Observable {
	
	private static FloorPlanModel model = new FloorPlanModel();
	
	private List<Wall> wallList;
	private List<ConnectionPoint> connectionPointList;
	private List<AccessPoint> accessPointList;
	private List<DataActivity> dataActivityList;
	
	private Stack<FloorPlanModel> undoStack = new Stack<FloorPlanModel>();
	private Stack<FloorPlanModel> redoStack = new Stack<FloorPlanModel>();
	
	private FloorPlanModel(ArrayList<Wall> wallList, ArrayList<ConnectionPoint> connectionPointList, ArrayList<AccessPoint> accessPointList, ArrayList<DataActivity> dataActivityList){
		this.wallList = wallList;
		this.connectionPointList = connectionPointList;
		this.accessPointList = accessPointList;
		this.dataActivityList = dataActivityList;
	}
	
	private FloorPlanModel() {
		wallList = new ArrayList<Wall>();
		connectionPointList = new ArrayList<ConnectionPoint>();
		accessPointList = new ArrayList<AccessPoint>();
		dataActivityList = new ArrayList<DataActivity>();
	}
	
	public static FloorPlanModel getInstance(){
		return model;
	}
	
	public List<Wall> getWallList() {
		return wallList;
	}
	
	public List<ConnectionPoint> getConnectionPointList() {
		return connectionPointList;
	}
	
	public List<AccessPoint> getAccessPointList() {
		return accessPointList;
	}
	
	public List<DataActivity> getDataActivityList() {
		return dataActivityList;
	}
	
	private void setWallList(List<Wall> wallList) {
		this.wallList = wallList;
	}

	private void setConnectionPointList(List<ConnectionPoint> connectionPointList) {
		this.connectionPointList = connectionPointList;
	}

	private void setAccessPointList(List<AccessPoint> accessPointList) {
		this.accessPointList = accessPointList;
	}

	private void setDataActivityList(List<DataActivity> dataActivityList) {
		this.dataActivityList = dataActivityList;
	}

	public void reset(){
		wallList.clear();
		connectionPointList.clear();
		accessPointList.clear();
		dataActivityList.clear();
		undoStack.clear();
		redoStack.clear();
		setChanged();
		notifyObservers();
	}
	
	public void loadFloorPlan(File file) throws ParserConfigurationException, SAXException, IOException{
		List<Wall> newWallList = new ArrayList<Wall>();
		List<ConnectionPoint> newConnectionPointList = new ArrayList<ConnectionPoint>();
		List<AccessPoint> newAccessPointList = new ArrayList<AccessPoint>();
		List<DataActivity> newDataActivityList = new ArrayList<DataActivity>();
		
		FloorPlanIO.loadFloorPlan(file, newWallList, newConnectionPointList, newAccessPointList, newDataActivityList);
		model.reset();
		wallList.addAll(newWallList);
		connectionPointList.addAll(newConnectionPointList);
		accessPointList.addAll(newAccessPointList);
		dataActivityList.addAll(newDataActivityList);
		// update observers
        model.setChanged();
		model.notifyObservers();
	}
	
	public void saveFloorPlan(File file) throws ParserConfigurationException, TransformerException{
		FloorPlanIO.saveFloorPlan(file, wallList, connectionPointList, accessPointList, dataActivityList);
	}

	public void addDataObject(DataObject touchDataObject) {
		if(touchDataObject.isComplete()){
			pushStateToStack(undoStack);
			redoStack.clear();
			if(touchDataObject instanceof Wall){
				Wall newWall = (Wall) touchDataObject;
				Map<Double, Point> splitPoints = new HashMap<Double, Point>();
				List<Wall> wallListCopy = new ArrayList<Wall>(wallList);
				for(Wall w : wallListCopy){
					Point[] pl = Utils.getIntersectionSpecial(newWall.getPoint1(), newWall.getPoint2(), w.getPoint1(), w.getPoint2(),true);
					if(pl.length > 0){
						Wall add1 = null;
						if(!pl[0].equals(w.getPoint1())){
							add1 = (Wall) w.getPartialDeepCopy();
							add1.setPoint1(w.getPoint1());
							add1.setPoint2(pl[0]);
							wallList.add(add1);
						}
						Wall add2 = null;
						if(!pl[0].equals(w.getPoint2())){
							add2 = (Wall) w.getPartialDeepCopy();
							add2.setPoint1(pl[0]);
							add2.setPoint2(w.getPoint2());
							wallList.add(add2);
						}
						if(pl.length > 1){
							Wall wallToSplit;
							if(add1 != null){
								Log.d("DEBUG","ptlDist1: "+Utils.pointToLineDistance(add1.getPoint1(), add1.getPoint2(), pl[1], true));
							}
							if(add1 != null && Utils.pointToLineDistance(add1.getPoint1(), add1.getPoint2(), pl[1], true) < 3){
								wallToSplit = add1;
							}else{
								Log.d("DEBUG","ptlDist2: "+Utils.pointToLineDistance(add2.getPoint1(), add2.getPoint2(), pl[1], true));
								wallToSplit = add2;
							}
							Wall add3 = null;
							if(!pl[1].equals(wallToSplit.getPoint1())){
								add3 = (Wall) wallToSplit.getPartialDeepCopy();
								add3.setPoint1(wallToSplit.getPoint1());
								add3.setPoint2(pl[1]);
								wallList.add(add3);
							}
							Wall add4 = null;
							if(!pl[1].equals(wallToSplit.getPoint2())){
								add4 = (Wall) wallToSplit.getPartialDeepCopy();
								add4.setPoint1(pl[1]);
								add4.setPoint2(wallToSplit.getPoint2());
								wallList.add(add4);
							}
							wallList.remove(wallToSplit);
						}
						wallList.remove(w);
					}
					for(Point p : pl){
						double d1 = Utils.pointToLineDistance(w.getPoint1(), w.getPoint2(), newWall.getPoint1(), false);
						double distance = Utils.pointToPointDistance(newWall.getPoint1(), p)+(d1*0.00001);
						while(splitPoints.containsKey(distance) && !splitPoints.get(distance).equals(p)){
							distance+=0.00000000001;
						}
						splitPoints.put(distance, p);
					}
				}
				if(!splitPoints.isEmpty()){
					Double[] coordArr = splitPoints.keySet().toArray(new Double[1]);
					Arrays.sort(coordArr);
					for(Double i : coordArr){
						Point p = splitPoints.get(i);
						Wall add = (Wall) newWall.getPartialDeepCopy();
						add.setPoint1(newWall.getPoint1());
						add.setPoint2(p);
						newWall.setPoint1(p);
						
						wallListCopy = new ArrayList<Wall>(wallList);
						for(Wall w : wallListCopy){
							if(w.equalsLocation(add)){
								wallList.remove(w);
							}
						}
						if(!add.getPoint1().equals(add.getPoint2())){
							wallList.add(add);
						}
						
					}
				}
				wallListCopy = new ArrayList<Wall>(wallList);
				for(Wall w : wallListCopy){
					if(w.equalsLocation(newWall)){
						wallList.remove(w);
					}
				}
				if(!newWall.getPoint1().equals(newWall.getPoint2())){
					wallList.add(newWall);
				}
				setChanged();
				notifyObservers();
			}else{
				addDataObjectToList(touchDataObject);
				pushStateToStack(undoStack);
				redoStack.clear();
				setChanged();
				notifyObservers();
			}
		}
		Log.d("DEBUG","size: "+wallList.size());
		int i = 0;
		for(Wall w : wallList){
			Log.d("DEBUG",""+(i++)+": "+w.getPoint1()+" "+w.getPoint2());
		}
	}
	
	
	
	private void addDataObjectToList(DataObject dataObject){
		if(dataObject instanceof AccessPoint){
			accessPointList.add((AccessPoint) dataObject);
		}else if(dataObject instanceof Wall){
			wallList.add((Wall) dataObject);
		}else if(dataObject instanceof ConnectionPoint){
			connectionPointList.add((ConnectionPoint) dataObject);
		}else if(dataObject instanceof DataActivity){
			dataActivityList.add((DataActivity) dataObject);
		}else{
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
	
	private void removeDataObjectFromList(DataObject dataobject){
		Log.d("DEBUG","removeDataObjectFromList");
		if(dataobject instanceof AccessPoint){
			accessPointList.remove(dataobject);
		}else if(dataobject instanceof Wall){
			wallList.remove(dataobject);
		}else if(dataobject instanceof ConnectionPoint){
			connectionPointList.remove(dataobject);
		}else if(dataobject instanceof DataActivity){
			dataActivityList.remove(dataobject);
		}else{
			Log.e("DEBUG", "Trying to add an invalid type of DataObject");
		}
	}
	
	public void undo(){
		pushStateToStack(redoStack);
		restoreStateFromStack(undoStack);
		setChanged();
		notifyObservers();
	}
	
	public void redo(){
		pushStateToStack(undoStack);
		restoreStateFromStack(redoStack);
		setChanged();
		notifyObservers();
	}
	
	public boolean canUndo(){
		return !undoStack.isEmpty();
	}
	
	public boolean canRedo(){
		return !redoStack.isEmpty();
	}
	
	public Point getClosestCornerToPoint(Point p){
		Point closest = null;
		double distance = Double.POSITIVE_INFINITY;
		for(Wall w : wallList){
			double dist = Utils.pointToPointDistance(p, w.getPoint1());
			if(dist < distance){
				distance = dist;
				closest = w.getPoint1();
			}
			dist = Utils.pointToPointDistance(p, w.getPoint2());
			if(dist < distance){
				distance = dist;
				closest = w.getPoint2();
			}
		}
		return closest;
	}
	
	public Couple<Double,Wall> getClosestWallToPoint(Point p, boolean upright){
		Wall closest = null;
		double distance = Double.POSITIVE_INFINITY;
		for(Wall w : wallList){
			double dist = Utils.pointToLineDistance(w.getPoint1(), w.getPoint2(), p, upright);
			if(dist < distance){
				distance = dist;
				closest = w;
			}
		}
		if(distance == Double.POSITIVE_INFINITY){
			return null;
		}
		return new Couple<Double, Wall>(distance, closest);
	}

	private void pushStateToStack(Stack<FloorPlanModel> stack){
		ArrayList<Wall> wallsClone = new ArrayList<Wall>();
		for(Wall w : wallList){
			wallsClone.add(new Wall(w));
		}
		ArrayList<ConnectionPoint> connectionPointsClone = new ArrayList<ConnectionPoint>();
		for(ConnectionPoint cp : connectionPointList){
			connectionPointsClone.add(new ConnectionPoint(cp));
		}
		ArrayList<AccessPoint> accessPointsClone = new ArrayList<AccessPoint>();
		for(AccessPoint ap : accessPointList){
			accessPointsClone.add(new AccessPoint(ap));
		}
		ArrayList<DataActivity> dataActivitiesClone = new ArrayList<DataActivity>();
		for(DataActivity da : dataActivityList){
			dataActivitiesClone.add(new DataActivity(da));
		}
		FloorPlanModel fpm = new FloorPlanModel(
				wallsClone,
				connectionPointsClone,
				accessPointsClone,
				dataActivitiesClone);
		stack.push(fpm);
	}
	
	private void restoreStateFromStack(Stack<FloorPlanModel> stack){
		FloorPlanModel fpm = stack.pop();
		model.setWallList(fpm.getWallList());
		model.setDataActivityList(fpm.getDataActivityList());
		model.setConnectionPointList(fpm.getConnectionPointList());
		model.setAccessPointList(fpm.getAccessPointList());
	}

	public Couple<Double, DataObject> getClosestDataObjectToPoint(Point touchPoint) {
		Couple<Double, Wall> couple = getClosestWallToPoint(touchPoint, false);
		double minDist = couple==null?Double.POSITIVE_INFINITY:couple.getA();
		Log.d("DEBUG","mindist: "+minDist);
		DataObject closest = couple==null?null:couple.getB();
		for(DataObject dObj : accessPointList){
			double dist = Utils.pointToPointDistance(touchPoint, dObj.getPoint1());
			if(dist < minDist){
				minDist = dist;
				closest = dObj;
			}
		}
		for(DataObject dObj : dataActivityList){
			double dist = Utils.pointToPointDistance(touchPoint, dObj.getPoint1());
			if(dist < minDist){
				minDist = dist;
				closest = dObj;
			}
		}
		for(DataObject dObj : connectionPointList){
			double dist = Utils.pointToPointDistance(touchPoint, dObj.getPoint1());
			if(dist < minDist){
				minDist = dist;
				closest = dObj;
			}
		}
		
		if(minDist==Double.POSITIVE_INFINITY){
			return null;
		}
		return new Couple<Double, DataObject>(minDist, closest);
	}
}
