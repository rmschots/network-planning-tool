package com.ugent.networkplanningtool.io.xml;

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

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        File f = parameters.getFile();
        StreamResult result = new StreamResult(parameters.getFile());
        DOMSource source;
        if (parameters.isList()) {
            source = new DOMSource(XMLIO.getDocument(parameters.getXmlTransformableList(), parameters.getRootName()));
        } else {
            source = new DOMSource(XMLIO.getDocument(parameters.getXmlTransformable()));
        }
        transformer.transform(source, result);

        return parameters.getFile();
    }


}
