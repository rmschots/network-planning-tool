package com.ugent.networkplanningtool.utils;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;

import android.graphics.Paint;
import android.graphics.Rect;

public class Utils {

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
	  
	  public static int determineMaxTextSize(String str, float maxHeight)
		{
		    int size = 0;       
		    Paint paint = new Paint();

		    Rect r = new Rect();
		    paint.getTextBounds(str, 0, 1, r);
		    do {
		        paint.setTextSize(++ size);
		        paint.getTextBounds(str, 0, str.length()-1, r);
		    } while(r.height() < maxHeight);

		    return size;
		}

}
