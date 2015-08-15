package com.asm.dailyselfieasm;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class DiskAdapter {
    // Maybe just make three separate thread classes ?

    private static final String TAG = "DailySelfieAsm";
    private static final int DELAY_SHORT = 500;
    private static final int DELAY_LONG = 2000;

    private static DiskAdapter instance;

    private DiskAdapter() {

    }

    public static DiskAdapter getInstance() {
        if (null == instance) {
            instance = new DiskAdapter();
        }
        return instance;
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

    public void loadAllImages(Context context, SetImageCallback parent) {
        new LoadAllImagesThread(context, parent).start();
    }

    private void loadAllImagesBack(Context context) {
        Log.i(TAG, "all images have been loaded");
        //run on UI ?
        //Toast.makeText(context, R.string.all_photos_loaded, Toast.LENGTH_SHORT).show();
    }

    public void saveImage(Context context, String filename, Bitmap image) {
        new SaveImageThread(context, filename, image).start();
    }

    private void saveImageBack(Context context, String filename) {
        Log.i(TAG, "saved " + filename);
        //run on UI ?
        //Toast.makeText(context, filename, Toast.LENGTH_LONG).show();
    }

    // done TODO: callback methods

    public void loadImage(Context context, SetImageCallback parent, String filename) {
        new LoadImageThread(context, parent, filename).start();
    }

    private void loadImageBack(SetImageCallback parent, Bitmap image, String filename) {
        parent.setImage(image, filename);
    }

    public void removeAllImages(Context context) {

        // done TODO: delete photos from memory

        new RemoveAllImagesThread(context).start();
    }

    private void removeAllImagesBack(Context context) {
        Log.i(TAG, "all images have been removed");
        //run on UI ?
        //Toast.makeText(context, R.string.all_photos_removed, Toast.LENGTH_LONG).show();
    }

    // Maybe implement Runnables ?

    private class LoadAllImagesThread extends Thread  implements SetImageCallback{
        private Context context;
        private SetImageCallback parent;
        private File[] images;
        private int counter;

        public LoadAllImagesThread(Context context, SetImageCallback parent) {
            this.context = context;
            this.parent = parent;
            this.counter = 0;
        }

        public void run() {

            // done TODO: read photos from memory and add them to the list

            File directory = context.getFilesDir();
            if (directory.exists()) {
                images = directory.listFiles();
                nextImage();
            }
        }

        @Override
        public void setImage(Bitmap image, String filename){
            parent.setImage(image, filename);
            counter++;
            nextImage();
        }

        private void nextImage () {
            if (counter < images.length && null != images[counter]) {
                loadImage(context, this, images[counter].getName());
            }
            else {
                loadAllImagesBack(context);
            }
        }
    }

    private class SaveImageThread extends Thread {

        private Context context;
        private String filename;
        private Bitmap picture;

        public SaveImageThread(Context context, String filename, Bitmap picture) {
            this.context = context;
            this.filename = filename;
            this.picture = picture;
        }

        @Override
        public void run() {
            simulateDelay(DELAY_LONG);

            // done TODO: make the app save data

            File savePath = new File(context.getFilesDir(), filename);
            FileOutputStream outStream = null;
            try {
                outStream = new FileOutputStream(savePath);
                picture.compress(Bitmap.CompressFormat.PNG, 100, outStream);
                outStream.close();
            }
            catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
            catch (IOException ex) {
                ex.printStackTrace();
                // make one catch out of two ??
            }

            saveImageBack(context, context.getString(R.string.saved_to) + " " + context.
                    getFilesDir().toString() + "/" + filename);

        }
    }

    private class LoadImageThread extends Thread {
        private Context context;
        SetImageCallback parent;
        private String filename;

        public LoadImageThread(Context context, SetImageCallback parent, String filename) {
            this.context = context;
            this.parent = parent;
            this.filename = filename;
        }

        @Override
        public void run() {

            simulateDelay(DELAY_LONG);
            try {
                File file = new File(context.getFilesDir(), filename);
                Bitmap photo = BitmapFactory.decodeStream(new FileInputStream(file));
                loadImageBack(parent, photo, filename);
            }
            catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }

        }
    }

    private class RemoveAllImagesThread extends Thread {
        private Context context;

        public RemoveAllImagesThread(Context context) {
            this.context = context;
        }

        @Override
        public void run() {

            File directory = context.getFilesDir();
            if (directory.exists()) {
                File[] files = directory.listFiles();
                for (File file : files) {
                    if (!file.delete()) {
                        Log.i(TAG, "file deletion is failed");
                    }
                }
            }

            removeAllImagesBack(context);
        }
    }

}
