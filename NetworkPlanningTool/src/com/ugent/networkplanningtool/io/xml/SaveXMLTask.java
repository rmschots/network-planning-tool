package com.ugent.networkplanningtool.io.xml;

import com.ugent.networkplanningtool.data.XMLTransformable;
import com.ugent.networkplanningtool.io.AbstractASyncTask;

import java.io.File;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * Created by Roel on 11/03/14.
 */
public class SaveXMLTask extends AbstractASyncTask<SaveXMLParams, File> {

    @Override
    protected File performTaskInBackground(SaveXMLParams parameters) throws Exception {
        File file = parameters.getFile();
        XMLTransformable xmlTransformable = parameters.getXmlTransformable();
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        StreamResult result = new StreamResult(file);

        DOMSource source = new DOMSource(FloorPlanIO.getDocument(xmlTransformable));
        transformer.transform(source, result);
        return file;
    }
}
