package com.ugent.networkplanningtool.utils;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;

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

}
