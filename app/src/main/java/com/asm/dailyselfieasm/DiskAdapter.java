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
import java.text.DateFormat;
import java.util.Date;


public class DiskAdapter {
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

    public void savePhoto(Context context, String filename, Bitmap picture) {

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

    public Bitmap openPhoto(Context context, String filename) {
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
}
