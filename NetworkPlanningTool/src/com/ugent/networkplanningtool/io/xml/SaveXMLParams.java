package com.ugent.networkplanningtool.io.xml;

import com.ugent.networkplanningtool.data.XMLTransformable;

import java.io.File;
import java.util.List;

/**
 * Parameters for the task that saves xml to a file
 */
public class SaveXMLParams {
    private final XMLTransformable xmlTransformable;
    private final List<XMLTransformable> xmlTransformableList;
    private final File file;
    private final String rootName;
    private final boolean isList;

    /**
     * Constructor for saving one data object
     * @param xmlTransformable the data to save
     * @param file the file to save to
     */
    public SaveXMLParams(XMLTransformable xmlTransformable, File file) {
        this.xmlTransformable = xmlTransformable;
        this.file = file;
        xmlTransformableList = null;
        rootName = null;
        isList = false;
    }

    /**
     * Constructor for saving a list of data objects with specified parent name to a file
     * @param xmlTransformableList the data to save
     * @param rootName the parent name to put the data under
     * @param file the file to save to
     */
    public SaveXMLParams(List<XMLTransformable> xmlTransformableList, String rootName, File file) {
        this.xmlTransformableList = xmlTransformableList;
        this.rootName = rootName;
        this.file = file;
        xmlTransformable = null;
        isList = true;
    }

    /**
     * Returns the data object to be saved
     * @return the data object to be saved
     */
    public XMLTransformable getXmlTransformable() {
        return xmlTransformable;
    }

    /**
     * Returns the file to save to
     * @return the file to save to
     */
    public File getFile() {
        return file;
    }

    /**
     * Returns the data list to be saved
     * @return the data list to be saved
     */
    public List<XMLTransformable> getXmlTransformableList() {
        return xmlTransformableList;
    }

    /**
     * Returns the root name to put the data under
     * @return the root name to put the data under
     */
    public String getRootName() {
        return rootName;
    }

    /**
     * Returns if the data to be saved is a list or a single object
     * @return if the data to be saved is a list or a single objects
     */
    public boolean isList() {
        return isList;
    }
}
