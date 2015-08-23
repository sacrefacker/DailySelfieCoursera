package com.asm.dailyselfieasm;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;


public class PhotoRecord {
    private static final String TAG = "DailySelfieAsm";
    public static final int PREVIEW_DIMENS = 360;
    public static final int ROUNDED_PIXELS = 50;

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

        if (photo.getHeight() == PREVIEW_DIMENS) {
            return photo;
        }

        // done TODO: make actual previews
        // make the dimensions sync with xml view ?

        Bitmap preview;

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

        return getRoundedCornerBitmap(scaled, PREVIEW_DIMENS/2);

    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {

        // done TODO: make the previews round

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

}
