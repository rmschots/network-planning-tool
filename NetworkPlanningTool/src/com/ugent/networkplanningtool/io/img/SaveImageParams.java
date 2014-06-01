package com.ugent.networkplanningtool.io.img;

import android.graphics.Bitmap;

import java.io.File;

/**
 * Parameters for the task that saves a bitmap image to the given file
 */
public class SaveImageParams {

    private Bitmap bitmap;
    private File file;

    /**
     * Default constructor setting the bitmap to save and the file to save to
     * @param bitmap the bitmap to save
     * @param file the file to save to
     */
    public SaveImageParams(Bitmap bitmap, File file) {
        this.bitmap = bitmap;
        this.file = file;
    }

    /**
     * Returns the bitmap to save
     * @return the bitmap to save
     */
    public Bitmap getBitmap() {
        return bitmap;
    }

    /**
     * Returns the file to save to
     * @return the file to save to
     */
    public File getFile() {
        return file;
    }
}
