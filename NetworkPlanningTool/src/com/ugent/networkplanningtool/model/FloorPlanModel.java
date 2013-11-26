package com.ugent.networkplanningtool.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.util.Log;

import com.ugent.networkplanningtool.data.AccessPoint;
import com.ugent.networkplanningtool.data.ActivityType;
import com.ugent.networkplanningtool.data.ConnectionPoint;
import com.ugent.networkplanningtool.data.DataActivity;
import com.ugent.networkplanningtool.data.DataConnectionPoint;
import com.ugent.networkplanningtool.data.DataObject;
import com.ugent.networkplanningtool.data.Material;
import com.ugent.networkplanningtool.data.Network;
import com.ugent.networkplanningtool.data.PowerConnectionPoint;
import com.ugent.networkplanningtool.data.RadioModel;
import com.ugent.networkplanningtool.data.RadioType;
import com.ugent.networkplanningtool.data.Thickness;
import com.ugent.networkplanningtool.data.Wall;
import com.ugent.networkplanningtool.data.WallType;
import com.ugent.networkplanningtool.utils.Utils;

public class FloorPlanModel extends Observable {
	
	private static FloorPlanModel model = new FloorPlanModel();
	
	private static List<Wall> wallList;
	private static List<ConnectionPoint> connectionPointList;
	private static List<AccessPoint> accessPointList;
	private static List<DataActivity> dataActivityList;
	

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

	public void reset(){
		wallList.clear();
		connectionPointList.clear();
		accessPointList.clear();
		dataActivityList.clear();
		setChanged();
		notifyObservers();
	}
	
	public static void loadFloorPlan(File file) throws Exception{
		Log.d("DEBUG","loading floor plan");
		model.reset();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    factory.setIgnoringElementContentWhitespace(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(file);
        // parse walls
        NodeList wallNodes = doc.getElementsByTagName("wall");
        for(int i = 0; i < wallNodes.getLength(); i++){
        	Node wallNode = wallNodes.item(i);
        	NamedNodeMap attributes = wallNode.getAttributes();
        	int wallX1 = Integer.parseInt(attributes.getNamedItem("x1").getTextContent());
        	int wallY1 = Integer.parseInt(attributes.getNamedItem("y1").getTextContent());
        	int wallX2 = Integer.parseInt(attributes.getNamedItem("x2").getTextContent());
        	int wallY2 = Integer.parseInt(attributes.getNamedItem("y2").getTextContent());
        	WallType wallType = WallType.getWallTypeByText(attributes.getNamedItem("type").getTextContent());
        	Thickness thickness = Thickness.getThicknessByNumber(Integer.parseInt(attributes.getNamedItem("thickness").getTextContent()));
        	
        	Material material = Material.getMaterialByText(Utils.getChildrenWithName(wallNode, "material").get(0).getAttributes().getNamedItem("name").getTextContent());
        	wallList.add(new Wall(wallX1, wallY1, wallX2, wallY2, wallType, thickness, material));
        }
        // parse connectionPoints
        NodeList dataConnectionPoints = doc.getElementsByTagName("dataconnpoint");
        for(int i = 0; i < dataConnectionPoints.getLength(); i++){
        	Node dataConnectionNode = dataConnectionPoints.item(i);
        	NamedNodeMap attributes = dataConnectionNode.getAttributes();
        	int x = Integer.parseInt(attributes.getNamedItem("x").getTextContent());
        	int y = Integer.parseInt(attributes.getNamedItem("y").getTextContent());
        	connectionPointList.add(new DataConnectionPoint(x, y));
        }
        NodeList powerConnectionPoints = doc.getElementsByTagName("powerconnpoint");
        for(int i = 0; i < powerConnectionPoints.getLength(); i++){
        	Node powerConnectionNode = powerConnectionPoints.item(i);
        	NamedNodeMap attributes = powerConnectionNode.getAttributes();
        	int x = Integer.parseInt(attributes.getNamedItem("x").getTextContent());
        	int y = Integer.parseInt(attributes.getNamedItem("y").getTextContent());
        	connectionPointList.add(new PowerConnectionPoint(x, y));
        }
        // parse accessPoints
        NodeList accessPoints = doc.getElementsByTagName("accesspoint");
        for(int i = 0; i < accessPoints.getLength(); i++){
        	Node accessPoint = accessPoints.item(i);
        	NamedNodeMap attributes = accessPoint.getAttributes();
        	int x = Integer.parseInt(attributes.getNamedItem("x").getTextContent());
        	int y = Integer.parseInt(attributes.getNamedItem("y").getTextContent());
        	int height = Integer.parseInt(attributes.getNamedItem("height").getTextContent());
        	String name = attributes.getNamedItem("name").getTextContent();
        	NamedNodeMap attributeList = Utils.getChildrenWithName(accessPoint, "radio").get(0).getAttributes();
        	RadioType type = RadioType.getRadioTypeByText(attributeList.getNamedItem("type").getTextContent());
        	RadioModel model = RadioModel.getRadioModelByText(attributeList.getNamedItem("model").getTextContent());
        	int freq = Integer.parseInt(attributeList.getNamedItem("frequency").getTextContent());
        	int freqBand = Integer.parseInt(attributeList.getNamedItem("frequencyband").getTextContent());
        	int gain = Integer.parseInt(attributeList.getNamedItem("gain").getTextContent());
        	int power = Integer.parseInt(attributeList.getNamedItem("power").getTextContent());
        	Network network = Network.getNetworkByText(attributeList.getNamedItem("network").getTextContent());
        	accessPointList.add(new AccessPoint(x,y,name,height,type,model, freq, freqBand,gain,power,network));
        }
        // parse dataActivities
        NodeList dataActivities = doc.getElementsByTagName("activity");
        for(int i = 0; i < dataActivities.getLength(); i++){
        	Node dataActivity = dataActivities.item(i);
        	NamedNodeMap attributes = dataActivity.getAttributes();
        	int x = Integer.parseInt(attributes.getNamedItem("x").getTextContent());
        	int y = Integer.parseInt(attributes.getNamedItem("y").getTextContent());
        	ActivityType activityType = ActivityType.getActivityTypeByText(attributes.getNamedItem("type").getTextContent());
        	dataActivityList.add(new DataActivity(x, y,activityType));
        }
        // update observers
        model.setChanged();
		model.notifyObservers();
	}

	public void addDataObject(DataObject touchDataObject) {
		if(touchDataObject.isComplete()){
			if(touchDataObject instanceof AccessPoint){
				accessPointList.add((AccessPoint) touchDataObject);
			}else if(touchDataObject instanceof Wall){
				wallList.add((Wall) touchDataObject);
			}else if(touchDataObject instanceof ConnectionPoint){
				connectionPointList.add((ConnectionPoint) touchDataObject);
			}else if(touchDataObject instanceof DataActivity){
				dataActivityList.add((DataActivity) touchDataObject);
			}else{
				Log.e("DEBUG", "Trying to add an invalid type of DataObject");
			}
			setChanged();
			notifyObservers();
		}
	}

}
