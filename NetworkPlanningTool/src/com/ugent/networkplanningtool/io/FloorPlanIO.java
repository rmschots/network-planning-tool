package com.ugent.networkplanningtool.io;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.graphics.Point;
import android.util.Log;

import com.ugent.networkplanningtool.data.AccessPoint;
import com.ugent.networkplanningtool.data.ActivityType;
import com.ugent.networkplanningtool.data.ConnectionPoint;
import com.ugent.networkplanningtool.data.ConnectionPointType;
import com.ugent.networkplanningtool.data.DataActivity;
import com.ugent.networkplanningtool.data.Frequency;
import com.ugent.networkplanningtool.data.FrequencyBand;
import com.ugent.networkplanningtool.data.Material;
import com.ugent.networkplanningtool.data.Network;
import com.ugent.networkplanningtool.data.RadioModel;
import com.ugent.networkplanningtool.data.RadioType;
import com.ugent.networkplanningtool.data.Thickness;
import com.ugent.networkplanningtool.data.Wall;
import com.ugent.networkplanningtool.data.WallType;
import com.ugent.networkplanningtool.model.FloorPlanModel;
import com.ugent.networkplanningtool.utils.Utils;

public class FloorPlanIO {

	public static void loadFloorPlan(File file, List<Wall> wallList, List<ConnectionPoint> connectionPointList, List<AccessPoint> accessPointList, List<DataActivity> dataActivityList) throws ParserConfigurationException, SAXException, IOException{
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
        	Wall w = new Wall(new Point(wallX1, wallY1), new Point(wallX2, wallY2), wallType, thickness, material);
        	
        	Log.d("ZZZZZ",""+w.getMaterial()+" "+w.getThickness()+" "+w.getWallType());
        	wallList.add(new Wall(new Point(wallX1, wallY1), new Point(wallX2, wallY2), wallType, thickness, material));
        }
        // parse connectionPoints
        NodeList dataConnectionPoints = doc.getElementsByTagName("dataconnpoint");
        for(int i = 0; i < dataConnectionPoints.getLength(); i++){
        	Node dataConnectionNode = dataConnectionPoints.item(i);
        	NamedNodeMap attributes = dataConnectionNode.getAttributes();
        	int x = Integer.parseInt(attributes.getNamedItem("x").getTextContent());
        	int y = Integer.parseInt(attributes.getNamedItem("y").getTextContent());
        	connectionPointList.add(new ConnectionPoint(new Point(x, y), ConnectionPointType.DATA));
        }
        NodeList powerConnectionPoints = doc.getElementsByTagName("powerconnpoint");
        for(int i = 0; i < powerConnectionPoints.getLength(); i++){
        	Node powerConnectionNode = powerConnectionPoints.item(i);
        	NamedNodeMap attributes = powerConnectionNode.getAttributes();
        	int x = Integer.parseInt(attributes.getNamedItem("x").getTextContent());
        	int y = Integer.parseInt(attributes.getNamedItem("y").getTextContent());
        	connectionPointList.add(new ConnectionPoint(new Point(x, y), ConnectionPointType.POWER));
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
        	Frequency freq = Frequency.getFrequencyByText(attributeList.getNamedItem("frequencyband").getTextContent());
        	FrequencyBand freqBand = FrequencyBand.getFreqBandByNumber(Integer.parseInt(attributeList.getNamedItem("frequency").getTextContent()));
        	int gain = Integer.parseInt(attributeList.getNamedItem("gain").getTextContent());
        	int power = Integer.parseInt(attributeList.getNamedItem("power").getTextContent());
        	Network network = Network.getNetworkByText(attributeList.getNamedItem("network").getTextContent());
        	accessPointList.add(new AccessPoint(new Point(x, y),name,height,type,model, freq, freqBand,gain,power,network));
        }
        // parse dataActivities
        NodeList dataActivities = doc.getElementsByTagName("activity");
        for(int i = 0; i < dataActivities.getLength(); i++){
        	Node dataActivity = dataActivities.item(i);
        	NamedNodeMap attributes = dataActivity.getAttributes();
        	int x = Integer.parseInt(attributes.getNamedItem("x").getTextContent());
        	int y = Integer.parseInt(attributes.getNamedItem("y").getTextContent());
        	ActivityType activityType = ActivityType.getActivityTypeByText(attributes.getNamedItem("type").getTextContent());
        	dataActivityList.add(new DataActivity(new Point(x, y),activityType));
        }
	}
	
	public static void saveFloorPlan(File file, FloorPlanModel fpm) throws ParserConfigurationException, TransformerException{
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		StreamResult result = new StreamResult(file);
		DOMSource source = new DOMSource(getDocument(fpm));
		transformer.transform(source, result);
		Log.i("DEBUG","File saved");
	}
	
	public static String getXMLAsString(FloorPlanModel fpm)
	{
	    try
	    {
	       DOMSource domSource = new DOMSource(getDocument(fpm));
	       StringWriter writer = new StringWriter();
	       StreamResult result = new StreamResult(writer);
	       TransformerFactory tf = TransformerFactory.newInstance();
	       Transformer transformer = tf.newTransformer();
	       transformer.transform(domSource, result);
	       return writer.toString();
	    }
	    catch(Exception ex)
	    {
	       ex.printStackTrace();
	       return null;
	    }
	}
	
	public static Document getDocument(FloorPlanModel fpm) throws ParserConfigurationException, TransformerException{
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
 
		// root elements
		Document doc = docBuilder.newDocument();
		Element rootElement = doc.createElement("plan");
		doc.appendChild(rootElement);
		
		Element levelElement = doc.createElement("level");
		levelElement.setAttribute("number", "0");
		levelElement.setAttribute("name", "");
		rootElement.appendChild(levelElement);
		
		Element extraWallsElement = doc.createElement("extraWalls");
		levelElement.appendChild(extraWallsElement);
		
		for(Wall wall: fpm.getWallList()){
			Element wallElement = doc.createElement("wall");
			extraWallsElement.appendChild(wallElement);
			wallElement.setAttribute("x1", ""+wall.getPoint1().x);
			wallElement.setAttribute("y1", ""+wall.getPoint1().y);
			wallElement.setAttribute("x2", ""+wall.getPoint2().x);
			wallElement.setAttribute("y2", ""+wall.getPoint2().y);
			wallElement.setAttribute("type", wall.getWallType().getText());
			wallElement.setAttribute("thickness", ""+wall.getThickness().getNumber());
			Element materialElement = doc.createElement("material");
			materialElement.setAttribute("name", wall.getMaterial().getText());
			wallElement.appendChild(materialElement);
		}
		
		for(ConnectionPoint cp : fpm.getConnectionPointList()){
			Element cpElement;
			if(cp.getType().equals(ConnectionPointType.DATA)){
				cpElement= doc.createElement("dataconnpoint");
			}else{
				cpElement = doc.createElement("powerconnpoint");
			}
			levelElement.appendChild(cpElement);
			cpElement.setAttribute("x", ""+cp.getPoint1().x);
			cpElement.setAttribute("y", ""+cp.getPoint1().y);
		}
		for(AccessPoint ap : fpm.getAccessPointList()){
			Element apElement = doc.createElement("accesspoint");
			apElement.setAttribute("level", "0");
			levelElement.appendChild(apElement);
			apElement.setAttribute("x", ""+ap.getPoint1().x);
			apElement.setAttribute("y", ""+ap.getPoint1().y);
			apElement.setAttribute("height", ""+ap.getHeight());
			apElement.setAttribute("name", ap.getName());
			Element radioElement = doc.createElement("radio");
			radioElement.setAttribute("type", ap.getType().getText());
			radioElement.setAttribute("model", ap.getModel().getText());
			radioElement.setAttribute("frequency", ""+ap.getFrequency().getText());
			radioElement.setAttribute("frequencyband", ""+ap.getFrequencyband().getNumber());
			radioElement.setAttribute("gain", ""+ap.getGain());
			radioElement.setAttribute("power", ""+ap.getPower());
			radioElement.setAttribute("network", ap.getNetwork().getText());
			apElement.appendChild(radioElement);
		}
		for(DataActivity da : fpm.getDataActivityList()){
			Element apElement = doc.createElement("activity");
			levelElement.appendChild(apElement);
			apElement.setAttribute("x", ""+da.getPoint1().x);
			apElement.setAttribute("y", ""+da.getPoint1().y);
			apElement.setAttribute("type", da.getType().getText());
		}
		return doc;
	}
}
