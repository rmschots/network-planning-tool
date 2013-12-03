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







import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.graphics.Point;
import android.graphics.PointF;
import android.util.Log;

import com.ugent.networkplanningtool.data.AccessPoint;
import com.ugent.networkplanningtool.data.ActivityType;
import com.ugent.networkplanningtool.data.ConnectionPoint;
import com.ugent.networkplanningtool.data.ConnectionPointType;
import com.ugent.networkplanningtool.data.DataActivity;
import com.ugent.networkplanningtool.data.DataObject;
import com.ugent.networkplanningtool.data.FloorPlanOperation;
import com.ugent.networkplanningtool.data.FloorPlanOperation.Type;
import com.ugent.networkplanningtool.data.Material;
import com.ugent.networkplanningtool.data.Network;
import com.ugent.networkplanningtool.data.RadioModel;
import com.ugent.networkplanningtool.data.RadioType;
import com.ugent.networkplanningtool.data.Thickness;
import com.ugent.networkplanningtool.data.Wall;
import com.ugent.networkplanningtool.data.WallType;
import com.ugent.networkplanningtool.io.FloorPlanIO;
import com.ugent.networkplanningtool.utils.Couple;
import com.ugent.networkplanningtool.utils.Utils;

public class FloorPlanModel extends Observable {
	
	private static FloorPlanModel model = new FloorPlanModel();
	
	private static List<Wall> wallList;
	private static List<ConnectionPoint> connectionPointList;
	private static List<AccessPoint> accessPointList;
	private static List<DataActivity> dataActivityList;
	
	private Stack<FloorPlanOperation> undoStack;
	private Stack<FloorPlanOperation> redoStack;
	

	private FloorPlanModel() {
		wallList = new ArrayList<Wall>();
		connectionPointList = new ArrayList<ConnectionPoint>();
		accessPointList = new ArrayList<AccessPoint>();
		dataActivityList = new ArrayList<DataActivity>();
		undoStack = new Stack<FloorPlanOperation>();
		redoStack = new Stack<FloorPlanOperation>();
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
	
	public static void loadFloorPlan(File file) throws ParserConfigurationException, SAXException, IOException{
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
	
	public static void saveFloorPlan(File file) throws ParserConfigurationException, TransformerException{
		FloorPlanIO.saveFloorPlan(file, wallList, connectionPointList, accessPointList, dataActivityList);
	}

	public void addDataObject(DataObject touchDataObject) {
		if(touchDataObject.isComplete()){
			if(touchDataObject instanceof Wall){
				Wall newWall = (Wall) touchDataObject;
				Map<Integer, Couple<Point, Wall>> splitWalls = new HashMap<Integer, Couple<Point,Wall>>();
				// wall is vertical?
				boolean vertical = newWall.getPoint1().x == newWall.getPoint2().x;
				for(Wall w : wallList){
					Point p = Utils.getIntersection(w.getPoint1(), w.getPoint2(), newWall.getPoint1(), newWall.getPoint2());
					if(p != null){
						Log.d("DEBUG","SPLIT");
						splitWalls.put(vertical?p.y:p.x, new Couple<Point, Wall>(p, w));
					}
				}
				if(!splitWalls.isEmpty()){
					Integer[] coordArr = splitWalls.keySet().toArray(new Integer[1]);
					Arrays.sort(coordArr);
					boolean down;
					if(vertical){
						down = newWall.getPoint1().y < newWall.getPoint2().y;
					}else{
						down = newWall.getPoint1().x < newWall.getPoint2().x;
					}
					
					for(Integer i : coordArr){
						Point p = splitWalls.get(i).getA();
						Wall w = splitWalls.get(i).getB();
						
						Wall add1 = (Wall) w.getPartialDeepCopy();
						add1.setPoint1(w.getPoint1());
						add1.setPoint2(p);
						Wall add2 = (Wall) w.getPartialDeepCopy();
						add2.setPoint1(p);
						add2.setPoint2(w.getPoint2());
						Wall add3 = (Wall) newWall.getPartialDeepCopy();
						if(down){
							add3.setPoint1(newWall.getPoint1());
							add3.setPoint2(p);
							newWall.setPoint1(p);
						}else{
							Log.d("DEBUG","UP");
							add3.setPoint1(newWall.getPoint2());
							add3.setPoint2(p);
							newWall.setPoint2(p);
						}
						wallList.remove(w);
						if(!add1.getPoint1().equals(add1.getPoint2())){
							wallList.add(add1);
						}
						if(!add2.getPoint1().equals(add2.getPoint2())){
							wallList.add(add2);
						}
						if(!add3.getPoint1().equals(add3.getPoint2())){
							wallList.add(add3);
						}
						Log.d("DEBUG","add3: "+add3.getPoint1().x + " "+add3.getPoint2().x);
					}
					
				}
				if(!newWall.getPoint1().equals(newWall.getPoint2())){
					wallList.add(newWall);
				}
				undoStack.push(new FloorPlanOperation(Type.ADD, newWall));
				redoStack.clear();
				setChanged();
				notifyObservers();
			}else{
				addDataObjectToList(touchDataObject);
				undoStack.push(new FloorPlanOperation(Type.ADD, touchDataObject));
				redoStack.clear();
				setChanged();
				notifyObservers();
			}
		}
		Log.d("DEBUG","size: "+wallList.size());
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
		removeDataObjectFromList(touchDataObject);
		undoStack.push(new FloorPlanOperation(Type.REMOVE, touchDataObject));
		redoStack.clear();
		setChanged();
		notifyObservers();
	}
	
	private void removeDataObjectFromList(DataObject dataobject){
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
		// TODO more advanced
		FloorPlanOperation operation = undoStack.pop();
		DataObject dataObject = operation.getDataObject();
		switch (operation.getType()) {
		case ADD:
			removeDataObjectFromList(dataObject);
			break;
		case REMOVE:
			addDataObjectToList(dataObject);
			break;
		default:
			break;
		}
		redoStack.push(operation);
		setChanged();
		notifyObservers();
	}
	
	public void redo(){
		// TODO more advanced
		FloorPlanOperation operation = redoStack.pop();
		DataObject dataObject = operation.getDataObject();
		switch (operation.getType()) {
		case ADD:
			addDataObjectToList(dataObject);
			break;
		case REMOVE:
			removeDataObjectFromList(dataObject);
			break;
		default:
			break;
		}
		undoStack.push(operation);
		setChanged();
		notifyObservers();
	}
	
	public boolean canUndo(){
		return !undoStack.isEmpty();
	}
	
	public boolean canRedo(){
		return !redoStack.isEmpty();
	}

}
