package com.asm.dailyselfieasm;

import android.graphics.Bitmap;


public class PhotoRecord {
    private String date;
    private Bitmap photo;
    private Bitmap preview;

    public PhotoRecord(String date, Bitmap photo) {
        this.date = date;
        this.photo = photo;
        this.preview = getPreview(photo);
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

    // TODO: make actual previews
    //

    private Bitmap getPreview(Bitmap photo) {
        Bitmap preview = photo;
        return preview;
    }

}
