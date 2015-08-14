package com.asm.dailyselfieasm;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

    public class PhotoActivity extends Activity {
        private static final String TAG = "DailySelfieAsm";

    ImageView photoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.photo_activity);
        photoView = (ImageView) findViewById(R.id.imageView);

        Intent incomingIntent = getIntent();
        String filename = incomingIntent.getStringExtra(MainActivity.EXTRA_BITMAP);
        Bitmap photo = DiskAdapter.getInstance().openPhoto(getApplicationContext(), filename);
        Log.i(TAG, "loaded photo " + getApplicationContext().getFilesDir().toString());
        if (null != photoView && null != photo) {
            setImage(photo);
        }
        else {
            Toast.makeText(getApplicationContext(), "cannot load", Toast.LENGTH_SHORT).show();
        }
        Log.i(TAG, "the photo is set");

        // TODO: deleting photo from here
        // singleton / intent for result ?
        //

    }

    // This will be a callback eventually
    public void setImage(Bitmap photo) {

        photoView.setImageBitmap(photo);

    }
}
