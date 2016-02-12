package com.stevensadler.android.bloquery.api.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;

import java.io.ByteArrayOutputStream;

/**
 * Created by Steven on 2/4/2016.
 */
@ParseClassName("ImageFile")
public class ImageFile extends ParseFile {

    private static byte[] bitmapToByteArray(Bitmap bitmap) {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    public static Bitmap parseFileToBitmap(ParseFile parseFile) {
        Bitmap bitmap = null;
        if (parseFile != null) {
            try {
                byte[] data = parseFile.getData();
                bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    public ImageFile(String name, Bitmap bitmap) {
        super(name, ImageFile.bitmapToByteArray(bitmap));
    }
}
