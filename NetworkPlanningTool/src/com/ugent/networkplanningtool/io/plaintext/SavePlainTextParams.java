package com.ugent.networkplanningtool.io.plaintext;

import java.io.File;

public class SavePlainTextParams {

    private final String data;
    private final File file;

    public SavePlainTextParams(String data, File file) {
        this.data = data;
        this.file = file;
    }

    public String getData() {
        return data;
    }

    public File getFile() {
        return file;
    }
}
