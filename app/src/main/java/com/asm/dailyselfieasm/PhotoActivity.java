package com.asm.dailyselfieasm;


import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class PhotoActivity extends Activity {
    private static final String TAG = "DailySelfieAsm";
    private static final String TAG_PHOTO_FRAGMENT = "photo_fragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_activity);

        Intent incomingIntent = getIntent();
        String filename = incomingIntent.getStringExtra(MainActivity.EXTRA_BITMAP);
        Log.i(TAG, filename + " is loaded from intent");

        Bundle args = new Bundle();
        args.putString(MainActivity.EXTRA_BITMAP, filename);
        FragmentManager fragmentManager = getFragmentManager();
        PhotoFragment photoFragment = new PhotoFragment();
        photoFragment.setArguments(args);

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame, photoFragment, TAG_PHOTO_FRAGMENT);
        fragmentTransaction.commit();
    }
}
