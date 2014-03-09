package com.ugent.networkplanningtool.io;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class ImageIO {
	public static void saveImage(Bitmap img, File f) throws FileNotFoundException{
		img.compress(CompressFormat.PNG, 100, new FileOutputStream(f));
	}
}
