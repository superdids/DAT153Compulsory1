package com.example.didrik.compulsory1v3.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Rotates an Image based on its orientation. This class is inspired from
 * stackoverflow, and is slightly modified. It is not providing the expected
 * results on all devices, but works as a temporary solution.
 * @author Didrik Emil Aubert
 * @author Ståle André Mikalsen
 * @author Viljar Buen Rolfsen
 */
public class RotateUtil {

    public static void rotate(ExifInterface exif, File file) throws Exception {
        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        int angle = 90;
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90 :
                angle = 90;
                break;
            case ExifInterface.ORIENTATION_ROTATE_180 :
                angle = 180;
                break;
            case ExifInterface.ORIENTATION_ROTATE_270 :
                angle = 270;
                break;
            default :
                break;
        }
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);

        Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file), null, null);
        Bitmap rotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), matrix, true);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        rotated.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        byte [] bitmapData = bos.toByteArray();

        FileOutputStream fos = new FileOutputStream(file);
        fos.write(bitmapData);
        fos.flush();
        fos.close();
    }
}
