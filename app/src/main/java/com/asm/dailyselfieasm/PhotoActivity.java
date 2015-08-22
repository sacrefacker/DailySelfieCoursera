package com.asm.dailyselfieasm;


import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class PhotoActivity extends Activity {
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fragmentManager = getFragmentManager();

        setContentView(R.layout.photo_activity);

        Intent incomingIntent = getIntent();
        String filename = incomingIntent.getStringExtra(MainActivity.EXTRA_BITMAP);
        Toast.makeText(getApplicationContext(), R.string.please_wait, Toast.LENGTH_SHORT).show();

        Bundle args = new Bundle();
        args.putString(MainActivity.EXTRA_BITMAP, filename);
        PhotoFragment photoFragment = new PhotoFragment();
        photoFragment.setArguments(args);

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.photo_activity, photoFragment);
        fragmentTransaction.commit();
    }
}
