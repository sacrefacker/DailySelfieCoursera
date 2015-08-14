package com.asm.dailyselfieasm;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

public class PhotoActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO: landscape and portrait orientations
        //

        Intent incomingIntent = getIntent();
        Bitmap photo = incomingIntent.getParcelableExtra(MainActivity.EXTRA_BITMAP);

        setContentView(R.layout.photo_view);
        ImageView photoView = (ImageView) findViewById(R.id.imageView);
        photoView.setImageBitmap(photo);

    }
}
