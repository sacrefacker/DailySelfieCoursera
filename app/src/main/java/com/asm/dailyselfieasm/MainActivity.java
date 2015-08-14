package com.asm.dailyselfieasm;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Date;


public class MainActivity extends ListActivity {

    private static final String TAG = "Dayly-Selfie-Asm";
    private static final int PHOTO_REQUEST = 123;
    private PhotoViewAdapter mAdapter;

    public static final String EXTRA_BITMAP = "ExtraBitmap";

    // TODO: add alarm that reminds to take a selfie, pressing it opens the app
    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        ListView photoListView = getListView();

        // TODO: make the app save data
        //

        photoListView.setId(android.R.id.list); // ??
        View footerView = getLayoutInflater().inflate(R.layout.footer_view, null, false);

        // TODO: swipe to remove entry and cancel button
        //

        footerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // done TODO: make the app open the camera and receive photo

                showToast("Go to camera from footer");
                takePhotoButton();

            }
        });

        photoListView.addFooterView(footerView);
        mAdapter = new PhotoViewAdapter(getApplicationContext());
        setListAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void showToast(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }

    private void takePhotoButton() {

        // TODO: camera emulation
        // ??

        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePhotoIntent, PHOTO_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PHOTO_REQUEST) {
            if (resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                addSelfie((Bitmap) extras.get("data"));
            }
            else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(), "You didn't take a photo",
                        Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getApplicationContext(), "Photo capture failed",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void addSelfie(Bitmap picture) {
        mAdapter.add(new PhotoRecord(DateFormat.getDateTimeInstance().format(new Date()), picture));
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        // TODO: open the photo when clicking on an item in the list view

        Intent openPhotoIntent = new Intent();
        openPhotoIntent.setClass(getApplicationContext(), com.asm.dailyselfieasm.PhotoActivity.class);
        Bitmap photo = mAdapter.getItemPhoto(position);
        openPhotoIntent.putExtra(EXTRA_BITMAP, photo);
        startActivity(openPhotoIntent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //TODO: make the action bar appear on the top
        //

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case R.id.action_take_photo:
                showToast("Go to camera from menu");
                takePhotoButton();
                return true;
            case R.id.action_clear_list:

                // TODO: get clear list confirmation
                //

                Log.i(TAG, "The list has been cleared");
                mAdapter.clearList();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void exitRequested() {
        super.onBackPressed();
    }

}
