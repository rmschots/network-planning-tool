package com.ugent.networkplanningtool.io.img;

import android.graphics.Bitmap.CompressFormat;

import com.ugent.networkplanningtool.io.AbstractASyncTask;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Saves a bitmap image to the given file
 */
public class SaveImageTask extends AbstractASyncTask<SaveImageParams, File> {

    /**
     * Performs the task saving the image to the given file
     * @param parameter the bitmap to save and the file to save to
     * @return the file that was used for storage
     * @throws Exception any exception preventing the task from being executed
     */
    @Override
    protected File performTaskInBackground(SaveImageParams parameter) throws Exception {
        parameter.getBitmap().compress(CompressFormat.PNG, 100, new FileOutputStream(parameter.getFile()));
        return parameter.getFile();
    }
}
