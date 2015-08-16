package com.asm.dailyselfieasm;

import android.graphics.Bitmap;
import android.util.Log;


public class PhotoRecord {
    private static final String TAG = "DailySelfieAsm";
    public static final int DIMENS = 360;

    private String date;
    private Bitmap photo;
    private Bitmap preview;

    public PhotoRecord(String date, Bitmap photo) {
        this.date = date;
        this.photo = photo;
        this.preview = getPreview(photo);
        Log.i(TAG, "a preview with dimensions " + preview.getWidth() + " by "
                + preview.getHeight() + " was created");
    }

    public PhotoRecord() {

    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
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

    private Bitmap getPreview(Bitmap photo) {

        // done TODO: make actual previews
        // make the dimensions sync with xml view ?

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

        return Bitmap.createScaledBitmap(preview, DIMENS, DIMENS, true);

    }

}
