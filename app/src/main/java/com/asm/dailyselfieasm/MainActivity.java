package com.asm.dailyselfieasm;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ListActivity;
import android.content.DialogInterface;
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
import android.app.AlertDialog;

import java.text.DateFormat;
import java.util.Date;


public class MainActivity extends ListActivity {
    private static final String TAG = "DailySelfieAsm";
    private static final int PHOTO_REQUEST = 123;
    private PhotoViewAdapter mAdapter;

    public static final String EXTRA_BITMAP = "ExtraBitmap";
    public static final String DATA_FROM_CAMERA = "data";

    // TODO: add alarm that reminds to take a selfie, pressing it opens the app
    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        ListView photoListView = getListView();
        photoListView.setId(android.R.id.list); // ??
        View footerView = getLayoutInflater().inflate(R.layout.footer_view, null, false);

        // TODO: swipe to remove entry and cancel button
        //

        footerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // done TODO: make the app open the camera and receive photo

                Log.i(TAG, "Go to camera from footer");
                takePhotoButton();

            }
        });

        photoListView.addFooterView(footerView);
        mAdapter = new PhotoViewAdapter(getApplicationContext());
        setListAdapter(mAdapter);

        mAdapter.setListTo(DiskAdapter.getInstance().
                retrievePhotosFromMemory(getApplicationContext()));
        showToast(R.string.done_loading_files);

    }

    // TODO: landscape and portrait orientations
    //

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        // done TODO: open the photo when clicking on an item in the list view

        Intent showPhotoIntent = new Intent();
        showPhotoIntent.setClass(getApplicationContext(), com.asm.dailyselfieasm.
                PhotoActivity.class);
        String filename = ((PhotoRecord)mAdapter.getItem(position)).getDate();
        showPhotoIntent.putExtra(EXTRA_BITMAP, filename);
        startActivity(showPhotoIntent);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PHOTO_REQUEST) {
            if (resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                String date = DateFormat.getDateTimeInstance().format(new Date());
                Bitmap photo = (Bitmap) extras.get(DATA_FROM_CAMERA);
                mAdapter.add(new PhotoRecord(date, photo));
                DiskAdapter.getInstance().savePhoto(getApplicationContext(), date, photo);
            }
            else if (resultCode == RESULT_CANCELED) {
                showToast(R.string.photo_not_taken);
            }
            else {
                showToast(R.string.photo_failed);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // TODO: make the action bar appear on the top
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
                Log.i(TAG, "Go to camera from menu");
                takePhotoButton();
                return true;
            case R.id.action_clear_list:

                // done TODO: get clear list confirmation

                removeAllPhotosDialog();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void removeAllPhotosDialog() {

        new AlertDialog.Builder(MainActivity.this)
                .setTitle(R.string.dialog_title)
                .setMessage(R.string.dialog_message)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mAdapter.clearList();
                        DiskAdapter.getInstance().removeAllPhotos(getApplicationContext());
                        showToast(R.string.all_photos_removed);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showToast(R.string.remove_cancel);
                    }
                })
                .show();

    }

    private void showToast(int stringResource) {
        Toast.makeText(getApplicationContext(), stringResource, Toast.LENGTH_SHORT).show();
    }

    private void takePhotoButton() {
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePhotoIntent, PHOTO_REQUEST);
    }

}
