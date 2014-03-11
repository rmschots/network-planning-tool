package com.ugent.networkplanningtool.data;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Created by Roel on 10/03/14.
 */
public interface XMLTransformable {
    public Element toXML(Document doc);
}
