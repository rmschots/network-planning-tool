package com.ugent.networkplanningtool.io.xml;

import com.ugent.networkplanningtool.data.XMLTransformable;

import java.io.File;

/**
 * Created by Roel on 11/03/14.
 */
public class SaveXMLParams {
    private final XMLTransformable xmlTransformable;
    private final File file;

    public SaveXMLParams(XMLTransformable xmlTransformable, File file) {
        this.xmlTransformable = xmlTransformable;
        this.file = file;
    }

    public XMLTransformable getXmlTransformable() {
        return xmlTransformable;
    }

    public File getFile() {
        return file;
    }
}
