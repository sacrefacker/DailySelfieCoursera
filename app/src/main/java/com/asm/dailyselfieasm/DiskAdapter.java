package com.asm.dailyselfieasm;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class DiskAdapter {
    // Maybe just make three separate thread classes ?

    private static final String TAG = "DailySelfieAsm";
    private static final int DELAY = 400;
    public static final int MAX_DIMENS = 1920;
    public static final String PREVIEW_DIR = "previews";

    private static DiskAdapter instance;
    public static File IMAGE_PATH;

    private DiskAdapter() {

    }

    public static DiskAdapter getInstance() {
        if (null == instance) {
            instance = new DiskAdapter();
        }
        return instance;
    }

    // seems like a not very good idea
    // must call ths before any file operations

    public static void setSaveFolder(Context context) {
        if (null == instance) {
            instance = new DiskAdapter();
        }
        IMAGE_PATH = context.getExternalFilesDir(null);
    }

    private void simulateDelay(int delay) {
        try {
            // Pretend downloading takes a long time
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // done TODO: async threading

    public void loadPreviews(SetImageCallback parent, ToastCallback toastParent) {
        new loadPreviews(parent, toastParent).start();
    }

    private void loadPreviewsBack(ToastCallback parent) {
        parent.toastCallback(R.string.all_photos_loaded);
    }

    public void savePreview(String filename, Bitmap image) {
        new SavePreviewThread(filename, image).start();
    }

    private void savePreviewBack(String text) {
        Log.i(TAG, text);
    }

    // done TODO: callback methods

    public void loadImage(SetImageCallback parent, String filename, Boolean onlyPreview) {
        new LoadImageThread(parent, filename, onlyPreview).start();
    }

    private void loadImageBack(SetImageCallback parent, Bitmap image, String filename) {
        parent.setImage(image, filename);
    }

    public void removeAllImages(ToastCallback parent) {

        // done TODO: delete photos from memory

        new RemoveAllImagesThread(parent).start();
    }

    private void removeAllImagesBack(ToastCallback parent) {
        parent.toastCallback(R.string.all_photos_removed);
    }

    // done TODO: scale bitmap to prevent overusage of memory and rotate correctly

    private Bitmap normalizeImage(Bitmap image, String filename) {

        int newWidth = MAX_DIMENS;
        int newHeight = MAX_DIMENS;

        if (image.getWidth() >= image.getHeight()) {
            newHeight = (MAX_DIMENS * image.getHeight()) / image.getWidth();
        }
        else {
            newWidth = (MAX_DIMENS * image.getWidth()) / image.getHeight();
        }

        Bitmap scaled = Bitmap.createScaledBitmap(image, newWidth, newHeight, true);

        File path = new File (IMAGE_PATH, filename);
        ExifInterface ei = null;
        try {
            ei = new ExifInterface(path.toString());
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        int orientation = 0;
        if (null != ei) {
            orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
        }
        Bitmap rotated = scaled;
        switch(orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                rotated = rotateImage(scaled, 90);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                rotated = rotateImage(scaled, 180);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                rotated = rotateImage(scaled, 270);
                break;
            default:
                break;
        }

        return rotated;
    }

    private Bitmap rotateImage(Bitmap image, int rotateDegree) {

        Matrix matrix = new Matrix();
        matrix.postRotate(rotateDegree);

        return Bitmap.createBitmap(image , 0, 0, image.getWidth(),
                image.getHeight(), matrix, true);
    }

    // Maybe implement Runnables ?

    private class loadPreviews extends Thread  implements SetImageCallback{
        private SetImageCallback parent;
        private ToastCallback toastParent;
        private File path;

        private File[] images;
        private int counter;

        public loadPreviews(SetImageCallback parent, ToastCallback toastParent) {
            this.parent = parent;
            this.toastParent = toastParent;
            this.path = new File(IMAGE_PATH, PREVIEW_DIR);
            this.counter = 0;
        }

        public void run() {

            // done TODO: read photos from memory and add them to the list

            Log.i(TAG, "loading a preview: " + path.getPath());
            if (null != path && path.exists()) {
                images = path.listFiles();
                nextImage();
            }
            else {
                Log.i(TAG, "failed to start loading the previews");
            }
        }

        private void nextImage () {
            if (counter < images.length && null != images[counter]) {
                Log.i(TAG, counter + " is going");
                loadImage(this, images[counter].getName(), true);
            }
            else {
                loadPreviewsBack(toastParent);
            }
        }

        @Override
        public void setImage(Bitmap image, String filename){
            parent.setImage(image, filename);
            counter++;
            nextImage();
        }
    }

    private class SavePreviewThread extends Thread {

        private String filename;
        private Bitmap image;

        public SavePreviewThread(String filename, Bitmap image) {
            this.filename = filename;
            this.image = image;
        }

        @Override
        public void run() {
            simulateDelay(DELAY);

            // done TODO: make the app save data

            File path = new File(IMAGE_PATH, PREVIEW_DIR);
            if (path.mkdirs()) {
                Log.i(TAG, "directory creation is successful");
            }
            File saveFile = new File(path, filename);
            try {
                FileOutputStream outStream = new FileOutputStream(saveFile);
                image.compress(Bitmap.CompressFormat.PNG, 100, outStream);
                outStream.close();
                savePreviewBack(saveFile.toString());
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    // TODO: separate loading and normalization
    //

    private class LoadImageThread extends Thread {
        SetImageCallback parent;
        private String filename;
        private File path;
        private Boolean onlyPreview;

        public LoadImageThread(SetImageCallback parent, String filename, Boolean onlyPreview) {
            this.parent = parent;
            this.filename = filename;
            if (onlyPreview) {
                path = new File (IMAGE_PATH, PREVIEW_DIR);
            }
            else {
                path = IMAGE_PATH;
            }
            this.onlyPreview = onlyPreview;
        }

        @Override
        public void run() {

            simulateDelay(DELAY);
            try {
                File file = new File(path, filename);
                Log.i(TAG, "trying to load " + file.getPath());
                Bitmap image = BitmapFactory.decodeStream(new FileInputStream(file));
                Bitmap result;
                if (!onlyPreview) {
                    result = normalizeImage(image, filename);
                }
                else {
                    result = image;
                }
                loadImageBack(parent, result, filename);
            }
            catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }

        }
    }

    private class RemoveAllImagesThread extends Thread {
        private ToastCallback parent;

        public RemoveAllImagesThread(ToastCallback parent) {
            this.parent = parent;
        }

        @Override
        public void run() {

            cleanDirectory(new File(IMAGE_PATH, PREVIEW_DIR));
            cleanDirectory(IMAGE_PATH);

            removeAllImagesBack(parent);
        }

        private void cleanDirectory(File directory) {
            if (null != directory && directory.exists()) {
                File[] files = directory.listFiles();
                for (File file : files) {
                    if (!file.delete()) {
                        Log.i(TAG, "file deletion is failed");
                    }
                }
            }
        }
    }

}
