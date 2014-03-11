package com.ugent.networkplanningtool.io.xml;

import android.graphics.Point;
import android.util.Log;

import com.ugent.networkplanningtool.data.AccessPoint;
import com.ugent.networkplanningtool.data.ConnectionPoint;
import com.ugent.networkplanningtool.data.DataActivity;
import com.ugent.networkplanningtool.data.DataObject;
import com.ugent.networkplanningtool.data.FloorPlan;
import com.ugent.networkplanningtool.data.Wall;
import com.ugent.networkplanningtool.data.XMLTransformable;
import com.ugent.networkplanningtool.data.enums.ActivityType;
import com.ugent.networkplanningtool.data.enums.ConnectionPointType;
import com.ugent.networkplanningtool.data.enums.Frequency;
import com.ugent.networkplanningtool.data.enums.FrequencyBand;
import com.ugent.networkplanningtool.data.enums.Material;
import com.ugent.networkplanningtool.data.enums.Network;
import com.ugent.networkplanningtool.data.enums.RadioModel;
import com.ugent.networkplanningtool.data.enums.RadioType;
import com.ugent.networkplanningtool.data.enums.Thickness;
import com.ugent.networkplanningtool.data.enums.WallType;
import com.ugent.networkplanningtool.utils.Utils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class FloorPlanIO {

    public static FloorPlan loadFloorPlan(File file) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setIgnoringElementContentWhitespace(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(file);
        return transformFloorPlan(doc);
    }

    public static FloorPlan loadFloorPlan(String xmlAsString) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setIgnoringElementContentWhitespace(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new ByteArrayInputStream(xmlAsString.getBytes()));
        return transformFloorPlan(doc);
    }

    private static FloorPlan transformFloorPlan(Document doc) {
        List<Wall> wallList = new ArrayList<Wall>();
        List<ConnectionPoint> connectionPointList = new ArrayList<ConnectionPoint>();
        List<AccessPoint> accessPointList = new ArrayList<AccessPoint>();
        List<DataActivity> dataActivityList = new ArrayList<DataActivity>();

        NodeList wallNodes = doc.getElementsByTagName("wall");
        for (int i = 0; i < wallNodes.getLength(); i++) {
            Node wallNode = wallNodes.item(i);
            NamedNodeMap attributes = wallNode.getAttributes();
            int wallX1 = Integer.parseInt(attributes.getNamedItem("x1").getTextContent());
            int wallY1 = Integer.parseInt(attributes.getNamedItem("y1").getTextContent());
            int wallX2 = Integer.parseInt(attributes.getNamedItem("x2").getTextContent());
            int wallY2 = Integer.parseInt(attributes.getNamedItem("y2").getTextContent());
            WallType wallType = WallType.getWallTypeByText(attributes.getNamedItem("type").getTextContent());
            Thickness thickness = Thickness.getThicknessByNumber((int) Double.parseDouble(attributes.getNamedItem("thickness").getTextContent()));
            Material material = Material.getMaterialByText(Utils.getChildrenWithName(wallNode, "material").get(0).getAttributes().getNamedItem("name").getTextContent());
            Wall w = new Wall(new Point(wallX1, wallY1), new Point(wallX2, wallY2), wallType, thickness, material);
            Log.d("ZZZZZ", "" + w.getMaterial() + " " + w.getThickness() + " " + w.getWallType());
            wallList.add(new Wall(new Point(wallX1, wallY1), new Point(wallX2, wallY2), wallType, thickness, material));
        }
        // parse connectionPoints
        NodeList dataConnectionPoints = doc.getElementsByTagName("dataconnpoint");
        for (int i = 0; i < dataConnectionPoints.getLength(); i++) {
            Node dataConnectionNode = dataConnectionPoints.item(i);
            NamedNodeMap attributes = dataConnectionNode.getAttributes();
            int x = Integer.parseInt(attributes.getNamedItem("x").getTextContent());
            int y = Integer.parseInt(attributes.getNamedItem("y").getTextContent());
            connectionPointList.add(new ConnectionPoint(new Point(x, y), ConnectionPointType.DATA));
        }
        NodeList powerConnectionPoints = doc.getElementsByTagName("powerconnpoint");
        for (int i = 0; i < powerConnectionPoints.getLength(); i++) {
            Node powerConnectionNode = powerConnectionPoints.item(i);
            NamedNodeMap attributes = powerConnectionNode.getAttributes();
            int x = Integer.parseInt(attributes.getNamedItem("x").getTextContent());
            int y = Integer.parseInt(attributes.getNamedItem("y").getTextContent());
            connectionPointList.add(new ConnectionPoint(new Point(x, y), ConnectionPointType.POWER));
        }
        // parse accessPoints
        NodeList accessPoints = doc.getElementsByTagName("accesspoint");
        for (int i = 0; i < accessPoints.getLength(); i++) {
            Node accessPoint = accessPoints.item(i);
            NamedNodeMap attributes = accessPoint.getAttributes();
            int x = (int) Double.parseDouble(attributes.getNamedItem("x").getTextContent());
            int y = (int) Double.parseDouble(attributes.getNamedItem("y").getTextContent());
            int height = Integer.parseInt(attributes.getNamedItem("height").getTextContent());
            String name = attributes.getNamedItem("name") != null ? attributes.getNamedItem("name").getTextContent() : "";
            NamedNodeMap attributeList = Utils.getChildrenWithName(accessPoint, "radio").get(0).getAttributes();
            RadioType type = RadioType.getRadioTypeByText(attributeList.getNamedItem("type").getTextContent());
            RadioModel model = RadioModel.getRadioModelByText(attributeList.getNamedItem("model").getTextContent());
            FrequencyBand freqBand = FrequencyBand.getFrequencyBandByText(attributeList.getNamedItem("frequencyband").getTextContent());
            Frequency freq = Frequency.getFreqByNumber(Integer.parseInt(attributeList.getNamedItem("frequency").getTextContent()));
            int gain = Integer.parseInt(attributeList.getNamedItem("gain").getTextContent());
            double power = Double.parseDouble(attributeList.getNamedItem("power").getTextContent());
            Network network = Network.getNetworkByText(attributeList.getNamedItem("network").getTextContent());
            AccessPoint ap = new AccessPoint(new Point(x, y), name, height, type, model, freqBand, freq, gain, power, network);
            accessPointList.add(ap);
            System.out.println(ap);
        }
        // parse dataActivities
        NodeList dataActivities = doc.getElementsByTagName("activity");
        for (int i = 0; i < dataActivities.getLength(); i++) {
            Node dataActivity = dataActivities.item(i);
            NamedNodeMap attributes = dataActivity.getAttributes();
            int x = Integer.parseInt(attributes.getNamedItem("x").getTextContent());
            int y = Integer.parseInt(attributes.getNamedItem("y").getTextContent());
            ActivityType activityType = ActivityType.getActivityTypeByText(attributes.getNamedItem("type").getTextContent());
            dataActivityList.add(new DataActivity(new Point(x, y), activityType));
        }
        return new FloorPlan(wallList, connectionPointList, accessPointList, dataActivityList);
    }

    public static void saveXML(File file, XMLTransformable xmlTransformable) throws ParserConfigurationException, TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        StreamResult result = new StreamResult(file);

        DOMSource source = new DOMSource(getDocument(xmlTransformable));
        transformer.transform(source, result);
        Log.i("DEBUG", "File saved");
    }

    public static String getXMLAsString(XMLTransformable xmlTransformable) {
        try {
            DOMSource domSource = new DOMSource(getDocument(xmlTransformable));
            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.transform(domSource, result);
            return writer.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static Document getDocument(XMLTransformable xmlTransformable) throws ParserConfigurationException, TransformerException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        // root elements
        Document doc = docBuilder.newDocument();
        doc.appendChild(xmlTransformable.toXML(doc));

        return doc;
    }
}
