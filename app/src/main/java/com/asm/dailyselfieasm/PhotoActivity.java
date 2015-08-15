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

        // TODO: deleting photo from here
        // singleton / intent for result ?
        //

    }

    @Override
    public void setImage(Bitmap image, String filename) {
        Log.i(TAG, "loaded photo " + getApplicationContext().getFilesDir().toString());

        if (null != photoView && null != image) {
            photoView.setImageBitmap(image);
            Toast.makeText(getApplicationContext(), filename, Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getApplicationContext(), "cannot load", Toast.LENGTH_SHORT).show();
        }

    }
}
