package com.ugent.networkplanningtool.io.img;

import android.graphics.Bitmap;

import java.io.File;

public class SaveImageParams {

    private Bitmap bitmap;
    private File file;

    public SaveImageParams(Bitmap bitmap, File file) {
        this.bitmap = bitmap;
        this.file = file;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public File getFile() {
        return file;
    }
}
