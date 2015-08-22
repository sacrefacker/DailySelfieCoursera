package com.asm.dailyselfieasm;

import android.graphics.Bitmap;
import android.util.Log;


public class PhotoRecord {
    private static final String TAG = "DailySelfieAsm";

    private String date;
    private Bitmap preview;

    public PhotoRecord(String date, Bitmap preview) {

        // done TODO: add previews, not full-res. Full-res is only for photo activity, from disk

        this.date = date;
        this.preview = preview;
    }

    public PhotoRecord() {

    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Bitmap getPreview() {
        return preview;
    }

    public void setPreview(Bitmap preview) {
        this.preview = preview;
    }

    @Override
    public String toString() {
        return "Date:" + date;
    }

}
