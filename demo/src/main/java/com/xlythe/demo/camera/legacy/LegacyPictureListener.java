package com.xlythe.demo.camera.legacy;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.AsyncTask;

import com.xlythe.demo.camera.BaseCameraView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class LegacyPictureListener implements Camera.PictureCallback {
    // The file we're saving the picture to.
    private final File mFile;

    // The camera's orientation. If it's not 0, we'll have to rotate the image.
    private final int mOrientation;

    // The listener to notify when we're done.
    private final BaseCameraView.OnImageCapturedListener mListener;

    private static final double MAX_UPPER = 2560.0;
    private static final double MAX_LOWER = 1440.0;

    public LegacyPictureListener(File file, int orientation, BaseCameraView.OnImageCapturedListener listener) {
        mFile = file;
        mOrientation = orientation;
        mListener = listener;
    }

    @Override
    public void onPictureTaken(final byte[] data, final Camera camera) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                byte[] cleanedUp = manuallyRotateImage(data);

                try {
                    FileOutputStream fos = new FileOutputStream(mFile);
                    fos.write(cleanedUp);
                    fos.close();

                    mListener.onImageCaptured(mFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
        camera.startPreview();
    }

    private byte[] manuallyRotateImage(byte[] data) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

        Matrix matrix = new Matrix();
        matrix.postRotate(mOrientation);

        int max = Math.max(bitmap.getHeight(), bitmap.getWidth());
        int height;
        int width;
        double scale;
        if (max > MAX_UPPER) {
            scale = MAX_UPPER / max;
            width = (int) (bitmap.getWidth() * scale);
            height = (int) (bitmap.getHeight() * scale);
            bitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
        }
        int min = Math.min(bitmap.getHeight(), bitmap.getWidth());
        if (min > MAX_LOWER) {
            scale = MAX_LOWER / min;
            width = (int) (bitmap.getWidth() * scale);
            height = (int) (bitmap.getHeight() * scale);
            bitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
        }

        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

        return stream.toByteArray();
    }
}
