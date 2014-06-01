package com.ugent.networkplanningtool.io.plaintext;

import com.ugent.networkplanningtool.io.AbstractASyncTask;

import java.io.File;
import java.io.PrintWriter;

/**
 * Saves data as plain text to a file
 */
public class SavePlainTextTask extends AbstractASyncTask<SavePlainTextParams, File> {

    /**
     * Performs the task saving data as plain text to a file
     * @param parameter the text to save and the file to save to
     * @return the file used for storage
     * @throws Exception any exception preventing the task from being executed
     */
    @Override
    protected File performTaskInBackground(SavePlainTextParams parameter) throws Exception {
        PrintWriter out = new PrintWriter(parameter.getFile());
        out.println(parameter.getData());
        out.close();
        return parameter.getFile();
    }
}
