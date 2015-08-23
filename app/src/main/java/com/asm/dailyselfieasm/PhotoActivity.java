package com.asm.dailyselfieasm;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

public class PhotoActivity extends Activity implements SetImageCallback {
    private static final String TAG = "DailySelfieAsm";
    public static final String IMAGE_BUNDLE = "image_bundle";

    private ImageView photoView;
    private Bitmap retainImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_activity);
        photoView = (ImageView)findViewById(R.id.imageView);

        if (null != savedInstanceState) {
            retainImage = savedInstanceState.getParcelable(IMAGE_BUNDLE);
            photoView.setImageBitmap(retainImage);
        }
        else {

            Intent incomingIntent = getIntent();
            String filename = incomingIntent.getStringExtra(MainActivity.EXTRA_BITMAP);
            Log.i(TAG, filename + " is loaded from intent");

            DiskAdapter.getInstance().loadImage(this, filename, false);
            Toast.makeText(getApplicationContext(), R.string.please_wait, Toast.LENGTH_SHORT).show();

        }
    }

    // done TODO: don't load image from disk each time orientation changes

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(IMAGE_BUNDLE, retainImage);
    }

    @Override
    public void setImage(Bitmap image, String filename) {
        Log.i(TAG, "loaded photo");
        runOnUiThread(new SetImage(image, filename));
    }

    private class SetImage implements Runnable {
        private Bitmap image;
        private String filename;

        public SetImage(Bitmap image, String filename) {
            this.image = image;
            this.filename = filename;
        }

        @Override
        public void run() {

            if (null != photoView && null != image) {
                photoView.setImageBitmap(image);
                retainImage = image;
                Toast.makeText(getApplicationContext(), filename, Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getApplicationContext(), R.string.cannot_load, Toast.LENGTH_SHORT).show();
            }

        }
    }
}
