package com.asm.dailyselfieasm;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

public class PhotoActivity extends Activity implements SetImageCallback{
    private static final String TAG = "DailySelfieAsm";

    ImageView photoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO: landscape and portrait orientations
        // Convert to fragments?

        setContentView(R.layout.photo_activity);
        photoView = (ImageView) findViewById(R.id.imageView);

        Intent incomingIntent = getIntent();
        String filename = incomingIntent.getStringExtra(MainActivity.EXTRA_BITMAP);
        DiskAdapter.getInstance().loadImage(getApplicationContext(), this, filename);
        Toast.makeText(getApplicationContext(), R.string.please_wait, Toast.LENGTH_SHORT).show();

        // TODO: deleting photo from here
        // singleton / intent for result ?
        //

    }

    @Override
    public void setImage(Bitmap image, String filename) {
        Log.i(TAG, "loaded photo " + getApplicationContext().getFilesDir().toString());
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
                Toast.makeText(getApplicationContext(), filename, Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getApplicationContext(), R.string.cannot_load, Toast.LENGTH_SHORT).show();
            }

        }
    }
}
