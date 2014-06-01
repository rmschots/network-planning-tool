package com.ugent.networkplanningtool.io.xml;

import com.ugent.networkplanningtool.io.AbstractASyncTask;

import java.io.File;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * Saves XML to a file
 */
public class SaveXMLTask extends AbstractASyncTask<SaveXMLParams, File> {

    /**
     * Performs the XML saving task
     * @param parameters parameters including the file to save to and the data to save
     * @return the file to which the data was saved
     * @throws Exception any exception preventing the task from being executed
     */
    @Override
    protected File performTaskInBackground(SaveXMLParams parameters) throws Exception {

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
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
