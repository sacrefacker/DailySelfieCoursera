package com.asm.dailyselfieasm;

import android.graphics.Bitmap;


public class PhotoRecord {
    private String date;
    private Bitmap photo;

    public PhotoRecord(String date, Bitmap photo) {
        this.date = date;
        this.photo = photo;
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

    @Override
    public String toString() {
        return "Date:" + date;
    }
}
