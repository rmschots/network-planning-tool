package com.ugent.networkplanningtool.data;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * An interface that needs to be implemented by all objects that need to be transformable to XML
 */
public interface XMLTransformable {
    /**
     * Transforms the object to and XML Element
     * @param doc the document used to create elements with
     * @return the created Element containing the XML data
     */
    public Element toXML(Document doc);
}
