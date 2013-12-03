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
	  
	public static float pointToPointDistance(Point p1, Point p2){
		return (float) Math.sqrt((p1.x-p2.x)*(p1.x-p2.x) + (p1.y-p2.y)*(p1.y-p2.y));  
	}
	
	public static float pointToLineDistance(Point a, Point b, Point p) {
		float distance = (Math.abs((p.x-a.x)*(b.y-a.y)-(p.y-a.y)*(b.x-a.x))/pointToPointDistance(a,b));
		float distAToB = pointToPointDistance(a, b);
		float distToA = pointToPointDistance(a,p);
		float distToB = pointToPointDistance(b,p);
		
		if(distance > distToA){
			return Float.POSITIVE_INFINITY;
		}else if(distAToB < distToB){
			return Float.POSITIVE_INFINITY;
		}
		return (Math.abs((p.x-a.x)*(b.y-a.y)-(p.y-a.y)*(b.x-a.x))/pointToPointDistance(a,b));
	}
	
	public static Point[] getIntersection(Point line1point1, Point line1point2, Point line2point1, Point line2point2){
		Log.d("DEBUG","dist: "+pointToLineDistance(line1point1, line1point2, line2point1));
		float dist1 = pointToLineDistance(line1point1, line1point2, line2point1);
		float dist2 = pointToLineDistance(line1point1, line1point2, line2point2);
		if(dist1 < 40 || dist2 < 40){
			if(dist1 < 40 && dist2 < 40){
				return new Point[]{line2point1,line2point2};
			}
			if(dist1 < 40){
				return new Point[]{line2point1};
			}
			if(dist2 < 40){
				return new Point[]{line2point2};
			}
		}
		float a1 = (line1point2.y-line1point1.y)/(float)(line1point2.x-line1point1.x);
		float b1 = line1point1.y - a1*line1point1.x;
		float a2 = (line2point2.y-line2point1.y)/(float)(line2point2.x-line2point1.x);
		float b2 = line2point1.y - a2*line2point1.x;
		Log.d("DEBUG","x1: "+line1point1.x+" y1: "+line1point1.y+" x2: "+line1point2.x+" y2: "+line1point2.y);
		Log.d("DEBUG","a1: "+a1+" b1: "+b1+" a2: "+a2+" b2: "+b2);
		
		if(a1 != a2){
			int x0;
			int y0;
			if(a1 == Float.POSITIVE_INFINITY || a1 == Float.NEGATIVE_INFINITY){
				x0 = line1point1.x;
				y0 = Math.round(a2*x0+b2);
			}else if(a2 == Float.POSITIVE_INFINITY || a2 == Float.NEGATIVE_INFINITY){
				x0 = line2point1.x;
				y0 =  Math.round(a1*x0+b1);
			}else{
				x0 =  Math.round((-b1+b2)/(a1-a2));
				y0 =  Math.round(a1*x0+b1);
			}
			Log.d("DEBUG","x0: "+x0+" y0: "+y0);
			if(		Math.min(line1point1.x, line1point2.x) <= x0
					&& x0 <= Math.max(line1point1.x, line1point2.x)
					&& Math.min(line2point1.x, line2point2.x) <= x0
					&& x0 <= Math.max(line2point1.x, line2point2.x)
					&& Math.min(line1point1.y, line1point2.y) <= y0
					&& y0 <= Math.max(line1point1.y, line1point2.y)
					&& Math.min(line2point1.y, line2point2.y) <= y0
					&& y0 <= Math.max(line2point1.y, line2point2.y)){
				return new Point[]{new Point(x0, y0)};
				
			}
		}
		return new Point[]{};
	}

}
