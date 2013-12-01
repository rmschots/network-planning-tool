package com.ugent.networkplanningtool.utils;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;

import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;

public class Utils {
	
	private static Rect r = new Rect();
	private static Paint paint = new Paint();

	/**
	   * Return child elements with specified name.
	   * 
	   * @param parent
	   * @param ns
	   * @param nodeName
	   * @return
	   */
	public static List<Node> getChildrenWithName(Node parent, String nodeName) {
	    List<Node> r = new ArrayList<Node>();
	    for (Node n = parent.getFirstChild(); n != null; n = n.getNextSibling()) {
	        if (nodeName.equals(n.getNodeName())) {
	          r.add(n);
	        }
	    }
	    return r;
	}
  
	public static int determineMaxTextSize(String str, float maxHeight, int prediction){
		int size = prediction;
		do{
			paint.setTextSize(++ size);
		    paint.getTextBounds(str, 0, str.length()-1, r);
		}while(r.height() < maxHeight);
		do{
			paint.setTextSize(-- size);
		    paint.getTextBounds(str, 0, str.length()-1, r);
		}while(r.height() >= maxHeight);
		return size;
	}
	  
	public static float getDistance(float x1, float y1, float x2, float y2){
		return (float) Math.sqrt((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2));  
	}
	
	public static Point getIntersection(Point line1point1, Point line1point2, Point line2point1, Point line2point2){
		
		return null;
	}

}
