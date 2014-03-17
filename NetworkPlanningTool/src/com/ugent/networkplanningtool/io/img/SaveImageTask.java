package com.ugent.networkplanningtool.io.img;

import android.graphics.Bitmap.CompressFormat;

import com.ugent.networkplanningtool.io.AbstractASyncTask;

import java.io.File;
import java.io.FileOutputStream;

public class SaveImageTask extends AbstractASyncTask<SaveImageParams, File> {

    @Override
    protected File performTaskInBackground(SaveImageParams parameter) throws Exception {
        parameter.getBitmap().compress(CompressFormat.PNG, 100, new FileOutputStream(parameter.getFile()));
        return parameter.getFile();
    }
}
