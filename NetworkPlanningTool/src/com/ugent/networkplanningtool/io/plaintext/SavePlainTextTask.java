package com.ugent.networkplanningtool.io.plaintext;

import com.ugent.networkplanningtool.io.AbstractASyncTask;

import java.io.File;
import java.io.PrintWriter;

public class SavePlainTextTask extends AbstractASyncTask<SavePlainTextParams, File> {

    @Override
    protected File performTaskInBackground(SavePlainTextParams parameter) throws Exception {
        PrintWriter out = new PrintWriter(parameter.getFile());
        out.println(parameter.getData());
        out.close();
        return parameter.getFile();
    }
}
