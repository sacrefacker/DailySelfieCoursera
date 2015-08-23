package com.asm.dailyselfieasm;

import android.graphics.Bitmap;
import android.util.Log;


public class PhotoRecord {
    private static final String TAG = "DailySelfieAsm";
    public static final int PREVIEW_DIMENS = 360;

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
        return "File: " + date;
    }

    public static Bitmap makePreview(Bitmap photo) {

        // done TODO: make actual previews
        // make the dimensions sync with xml view ?

        Bitmap preview = null;

        if (photo.getWidth() >= photo.getHeight()){
            preview = Bitmap.createBitmap(
                    photo,
                    photo.getWidth()/2 - photo.getHeight()/2,
                    0,
                    photo.getHeight(),
                    photo.getHeight()
            );
        }
        else {
            preview = Bitmap.createBitmap(
                    photo,
                    0,
                    photo.getHeight()/2 - photo.getWidth()/2,
                    photo.getWidth(),
                    photo.getWidth()
            );
        }
        Bitmap scaled = Bitmap.createScaledBitmap(preview, PREVIEW_DIMENS, PREVIEW_DIMENS, true);

        Log.i(TAG, "a preview with dimensions " + scaled.getWidth() + " by "
                + scaled.getHeight() + " was created");

        // TODO: make the previews round
        //

        return scaled;

    }

}
