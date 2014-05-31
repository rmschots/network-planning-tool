package com.ugent.networkplanningtool.data;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public interface XMLTransformable {
    public Element toXML(Document doc);
}
