package com.stevensadler.android.bloquery.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.nio.ByteBuffer;

/**
 * Created by Steven on 1/31/2016.
 */
public class BitmapUtils {

    public static Bitmap scaleDownBitmap(Bitmap realImage, float maxSize, boolean filter) {
        float ratio = Math.min(
                (float) maxSize / realImage.getWidth(),
                (float) maxSize / realImage.getHeight()
        );
        if (ratio > 1f) {
            return realImage;
        }
        int width = Math.round((float) ratio * realImage.getWidth());
        int height = Math.round((float) ratio * realImage.getHeight());
        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width, height, filter);
        return newBitmap;
    }

    public static byte[] bitmapToByteArray(Bitmap bitmap) {
        //int numBytes = bitmap.getWidth() + bitmap.getHeight() * 4;
        int numBytes =  bitmap.getByteCount();
        ByteBuffer byteBuffer = ByteBuffer.allocate(numBytes);
        bitmap.copyPixelsToBuffer(byteBuffer);
        return byteBuffer.array();
    }

    public static Bitmap byteArrayToBitmap(byte[] data) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        return bitmap;
    }
}
