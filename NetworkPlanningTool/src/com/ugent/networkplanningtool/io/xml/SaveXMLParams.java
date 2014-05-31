package com.ugent.networkplanningtool.io.xml;

import com.ugent.networkplanningtool.data.XMLTransformable;

import java.io.File;
import java.util.List;

public class SaveXMLParams {
    private final XMLTransformable xmlTransformable;
    private final List<XMLTransformable> xmlTransformableList;
    private final File file;
    private final String rootName;
    private final boolean isList;

    public SaveXMLParams(XMLTransformable xmlTransformable, File file) {
        this.xmlTransformable = xmlTransformable;
        this.file = file;
        xmlTransformableList = null;
        rootName = null;
        isList = false;
    }

    public SaveXMLParams(List<XMLTransformable> xmlTransformableList, String rootName, File file) {
        this.xmlTransformableList = xmlTransformableList;
        this.rootName = rootName;
        this.file = file;
        xmlTransformable = null;
        isList = true;
    }

    public XMLTransformable getXmlTransformable() {
        return xmlTransformable;
    }

    public File getFile() {
        return file;
    }

    public List<XMLTransformable> getXmlTransformableList() {
        return xmlTransformableList;
    }

    public String getRootName() {
        return rootName;
    }

    public boolean isList() {
        return isList;
    }
}
