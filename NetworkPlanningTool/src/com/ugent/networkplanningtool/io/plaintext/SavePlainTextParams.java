package com.ugent.networkplanningtool.io.plaintext;

import java.io.File;

/**
 * Parameters of the task saving data as plain text to a file
 */
public class SavePlainTextParams {

    private final String data;
    private final File file;

    /**
     * Default constructor
     * @param data the data to save
     * @param file the file to save to
     */
    public SavePlainTextParams(String data, File file) {
        this.data = data;
        this.file = file;
    }

    /**
     * Returns the data to save
     * @return the data to save
     */
    public String getData() {
        return data;
    }

    /**
     * Returns the file to save to
     * @return the file to save to
     */
    public File getFile() {
        return file;
    }
}
