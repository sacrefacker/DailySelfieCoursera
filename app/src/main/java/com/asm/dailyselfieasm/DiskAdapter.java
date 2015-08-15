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
import java.util.ArrayList;


public class DiskAdapter {
    private static final String TAG = "DailySelfieAsm";
    private static final int DELAY = 2000;

    private static DiskAdapter instance;

    private DiskAdapter() {

    }

    public static DiskAdapter getInstance() {
        if (null == instance) {
            instance = new DiskAdapter();
        }
        return instance;
    }

    // TODO: async task
    //

    public void retrievePhotosFromMemory(Context context, SetImageCallback parent) {

        // done TODO: read photos from memory and add them to the list

        File directory = context.getFilesDir();

        if (directory.exists()) {
            File[] files = directory.listFiles();
            for (File file : files) {
                Log.i(TAG, "found a file");
                String filename = file.getName();
                DiskAdapter.getInstance().loadImage(context, parent, filename);
            }
        }

    }

    public void savePhoto(Context context, String filename, Bitmap picture) {
        simulateDelay(DELAY);

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

        Toast.makeText(context, "saved to " + context.getFilesDir().toString()
                + "/" + filename, Toast.LENGTH_LONG).show();

    }

    // done TODO: callback methods

    public void loadImage(Context context, SetImageCallback parent, String filename) {

        parent.setImage(openImage(context, filename), filename);

    }

    private Bitmap openImage(Context context, String filename) {
        simulateDelay(DELAY);
        Bitmap photo = null;
        try {
            File file = new File(context.getFilesDir(), filename);
            photo = BitmapFactory.decodeStream(new FileInputStream(file));
        }
        catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }

        return photo;

    }

    public void removeAllPhotos(Context context) {

        // done TODO: delete photos from memory

        File directory = context.getFilesDir();
        if (directory.exists()) {
            File[] files = directory.listFiles();
            for (File file : files) {
                file.delete();
            }
        }
    }

    public void simulateDelay(int delay) {
        try {
            // Pretend downloading takes a long time
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
