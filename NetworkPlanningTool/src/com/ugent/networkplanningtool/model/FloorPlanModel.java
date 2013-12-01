package com.ugent.networkplanningtool.model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
			addDataObjectToList(touchDataObject);
			undoStack.push(new FloorPlanOperation(Type.ADD, touchDataObject));
			redoStack.clear();
			setChanged();
			notifyObservers();
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
