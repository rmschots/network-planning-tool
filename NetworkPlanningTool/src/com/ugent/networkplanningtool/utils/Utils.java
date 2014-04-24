package com.ugent.networkplanningtool.utils;

import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

import com.ugent.networkplanningtool.data.ServiceData.CSVResult;

import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

public class Utils {
	
	private static Rect r = new Rect();
	private static Paint paint = new Paint();

	/**
	   * Return child elements with specified name.
	   * 
	   * @param parent parent node
	   * @param nodeName name of node
	   * @return children
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
	  
	public static double pointToPointDistance(Point p1, Point p2){
		return Math.sqrt((p1.x-p2.x)*(p1.x-p2.x) + (p1.y-p2.y)*(p1.y-p2.y));  
	}
	
	public static double pointToLineDistance(Point a, Point b, Point p, boolean upright) {
		double distAToB = pointToPointDistance(a, b);
		double distToA = pointToPointDistance(a,p);
		double distToB = pointToPointDistance(b,p);
		
		if(distAToB < distToA){
			return upright?Float.POSITIVE_INFINITY:distToB;
		}else if(distAToB < distToB){
			return upright?Float.POSITIVE_INFINITY:distToA;
		}
		if(a.x == b.x && a.x == p.x){
			return 0;
		}else if(a.y == b.y && a.y == p.y){
			return 0;
		}
		return (Math.abs((p.x-a.x)*(b.y-a.y)-(p.y-a.y)*(b.x-a.x))/pointToPointDistance(a,b));
	}
	
	public static Point[] getIntersectionSpecial(Point line1point1, Point line1point2, Point line2point1, Point line2point2, boolean upright){
		double dist1 = pointToLineDistance(line1point1, line1point2, line2point1,upright);
		double dist2 = pointToLineDistance(line1point1, line1point2, line2point2,upright);
		
		double dist3 = pointToLineDistance(line2point1, line2point2, line1point1,upright);
		double dist4 = pointToLineDistance(line2point1, line2point2, line1point2,upright);
		
		if(dist1 < 40 || dist2 < 40){
			if(dist1 < 40 && dist2 < 40){
				return new Point[]{line2point1,line2point2};
			}else{
				Point[] points;
				if(dist3 < 3 || dist4 < 3){
					points = new Point[2];
					if(dist3 < 3){
						points[1] = line1point1;
					}
					if(dist4 < 3){
						points[1] = line1point2;
					}
				}else{
					points = new Point[1];
				}
				if(dist1 < 40){
					points[0] = line2point1;
				}else if(dist2 < 40){
					points[0] = line2point2;
				}
				return points;
			}
		}else if(dist3 < 3 || dist4 < 3){
			if(dist3 < 3 && dist4 < 3){
				return new Point[]{line1point1,line1point2};
			}else if(dist3 < 3){
				return new Point[]{line1point1};
			}else if(dist4 < 3){
				return new Point[]{line1point2};
			}
		}
		float a1 = (line1point2.y-line1point1.y)/(float)(line1point2.x-line1point1.x);
		float b1 = line1point1.y - a1*line1point1.x;
		float a2 = (line2point2.y-line2point1.y)/(float)(line2point2.x-line2point1.x);
		float b2 = line2point1.y - a2*line2point1.x;
		
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
				y0 =  Math.round(a1*((-b1+b2)/(a1-a2))+b1);
			}
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
	
	public static Point pointProjectionOnLine(Point a, Point b, Point p){
		int x;
		int y;
		if(a.y == b.y){
			x = p.x;
			y = a.y;
		}else if(a.x == b.x){
			x = a.x;
			y = p.y;
		}else{
			float a1 = (b.y-a.y)/(float)(b.x-a.x);
			float b1 = a.y - a1*a.x;
			float a2 = -1/a1;
			float b2 = p.y - a2*p.x;
			float xx = (b2-b1)/(a1-a2);
			x = Math.round(xx);
			y = Math.round(a2*xx+b2);
		}
		if(		Math.min(a.x, b.x) <= x
				&& x <= Math.max(a.x, b.x)
				&& Math.min(a.y, b.y) <= y
				&& y <= Math.max(a.y, b.y)){
			return new Point(x, y);
		}
		return null;
	}

    public static CSVResult getResultAt(Point point, List<CSVResult> csvResults) {
        Point p;

        CSVResult closest = csvResults.get(0);
        double dist = Double.POSITIVE_INFINITY;
        for (CSVResult csvResult : csvResults) {
            p = csvResult.getPoint1();
            double tmpDist = pointToPointDistance(p,point);
            if(tmpDist < dist){
                dist = tmpDist;
                closest = csvResult;
            }
            /*if (p.x - point.x < 10 && p.x - point.x >= -10 && p.y - point.y < 10 && p.y - point.y >= -10) {
                return csvResult;
            }*/
        }
        return closest;
    }
}
