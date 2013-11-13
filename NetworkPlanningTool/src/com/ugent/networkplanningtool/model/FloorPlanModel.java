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

import com.ugent.networkplanningtool.data.Material;
import com.ugent.networkplanningtool.data.Wall;
import com.ugent.networkplanningtool.data.WallType;
import com.ugent.networkplanningtool.utils.Utils;

public class FloorPlanModel extends Observable {
	
	private static FloorPlanModel model = new FloorPlanModel();
	
	private List<WallType> wallList;

	private FloorPlanModel() {
		wallList = new ArrayList<WallType>();
	}
	
	public static FloorPlanModel getInstance(){
		return model;
	}
	
	public void addWallType(WallType wall){
		if(wall.enoughData()){
			if(wall instanceof Wall){
				wallList.add(wall);
			}
			setChanged();
			notifyObservers();
		}
	}
	
	public List<WallType> getWallList() {
		return wallList;
	}
	
	private void reset(){
		wallList.clear();
	}
	
	public static void loadFloorPlan(File file) throws Exception{
		Log.d("DEBUG","loading floor plan");
		model.reset();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    factory.setIgnoringElementContentWhitespace(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(file);
        NodeList wallNodes = doc.getElementsByTagName("wall");
        for(int i = 0; i < wallNodes.getLength(); i++){
        	Node wallNode = wallNodes.item(i);
        	NamedNodeMap attributes = wallNode.getAttributes();
        	int wallX1 = Integer.parseInt(attributes.getNamedItem("x1").getTextContent());
        	int wallY1 = Integer.parseInt(attributes.getNamedItem("y1").getTextContent());
        	int wallX2 = Integer.parseInt(attributes.getNamedItem("x2").getTextContent());
        	int wallY2 = Integer.parseInt(attributes.getNamedItem("y2").getTextContent());
        	
        	String material = Utils.getChildrenWithName(wallNode, "material").get(0).getAttributes().getNamedItem("name").getTextContent();
        	model.addWallType(new Wall(wallX1, wallY1, wallX2, wallY2, Material.getMaterialByText(material)));
        	
        	Log.d("DEBUG","wall: "+wallX1+" "+wallY1+" "+wallX2+" "+wallY2);
        }
        model.setChanged();
		model.notifyObservers();
	}

}
