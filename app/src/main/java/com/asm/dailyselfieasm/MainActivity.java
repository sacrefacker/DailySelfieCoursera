package com.asm.dailyselfieasm;

import android.app.AlarmManager;
import android.app.ListActivity;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import android.app.AlertDialog;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class MainActivity extends ListActivity implements SetImageCallback, ToastCallback {
    private static final String TAG = "DailySelfieAsm";
    private static final int PHOTO_REQUEST = 123;
    private PhotoViewAdapter mAdapter;

    public static final String EXTRA_BITMAP = "ExtraBitmap";
    public static final String DATA_FROM_CAMERA = "data";
    private static File tempPhotoFile;

    // Alarm Section
    private static final int ALARM_DELAY = 20000;
    public static final String NOTE_TITLE = "Time for a selfie";
    public static final String NOTE_TEXT = "This reminds you to take a selfie once in a while";
    public static final int NOTE_ID = 12345;

    private PendingIntent alarmPending;
    private AlarmManager alarmManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO: landscape and portrait orientations
        //

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

        DiskAdapter.getInstance().loadAllImages(getApplicationContext(), this, this);

        // done TODO: add alarm that reminds to take a selfie, pressing it opens the app

        Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        alarmIntent.putExtra(AlarmReceiver.NOTE_ID, NOTE_ID);
        alarmIntent.putExtra(AlarmReceiver.NOTE, getRestartNotification(NOTE_TEXT));
        alarmPending = PendingIntent.getBroadcast(this, 0, alarmIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Log.i(TAG, "made the alarm intent");

    }

    private Notification getRestartNotification(String content) {

        // done TODO: make the app start from notification

        Intent restartIntent = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent intent = PendingIntent.getActivity(MainActivity.this, 0, restartIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        Log.i(TAG, "made the restart intent");
        return new Notification.Builder(this)
                .setContentTitle(NOTE_TITLE)
                .setContentText(content)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setContentIntent(intent)
                .build();
    }

    @Override
    protected void onResume() {
        super.onResume();
        alarmManager.cancel(alarmPending);
        Log.i(TAG, "canceled the alarm");
    }

    @Override
    protected void onPause() {
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + ALARM_DELAY, alarmPending);
        Log.i(TAG, "set the alarm");
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
    public boolean onCreateOptionsMenu(Menu menu) {

        // TODO: make the action bar appear on the top
        //

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    // TODO: stop/resume notifications button
    //

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clickfos here. The action bar will
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
                        DiskAdapter.getInstance().removeAllImages(getApplicationContext(),
                                MainActivity.this);
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

    private File createImageFile() throws IOException {

        String timeStamp = DateFormat.getDateTimeInstance(DateFormat.SHORT,
                DateFormat.SHORT).format(new Date());
        String imageFileName = "_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        tempPhotoFile = image;

        return image;
    }

    private void takePhotoButton() {

        // done TODO: don't save photos in camera folder

        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        /*takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(
                new File(getApplicationContext().getFilesDir().toString() + File.separator
                        + DateFormat.getDateTimeInstance().format(new Date()))) + ".jpg");*/
        if (takePhotoIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (null != photoFile) {
                takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
            }

            startActivityForResult(takePhotoIntent, PHOTO_REQUEST);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PHOTO_REQUEST) {
            if (resultCode == RESULT_OK) {

                // done TODO: get full resolution pictures

                Log.i(TAG, "received the camera intent back");

                // TODO: rotate the photos correctly
                //

                File oldFile = tempPhotoFile;
                String filename = oldFile.getName();
                File file = new File(getApplicationContext().getExternalFilesDir(null), filename);
                Log.i(TAG, "trying to move the photo...");
                if (oldFile.renameTo(file)) {
                    Log.i(TAG, "the photo has been moved to " + file.getPath());
                    tempPhotoFile = null;
                    DiskAdapter.getInstance().loadImage(getApplicationContext(), this, filename);
                }

                /*
                Bundle extras = data.getExtras();
                String timestamp = new SimpleDateFormat("dd/MM.YY/HH:mm:ss", Locale.US)
                        .format(new Date());
                String filename = "_" + timestamp + "_";
                String date = DateFormat.getDateTimeInstance(DateFormat.SHORT,
                        DateFormat.SHORT).format(new Date());
                Bitmap photo = (Bitmap) extras.get(DATA_FROM_CAMERA);

                mAdapter.add(new PhotoRecord(date, photo));

                DiskAdapter.getInstance().saveImage(getApplicationContext(), date, photo,
                        MainActivity.this);
                */
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
    public void setImage(Bitmap photo, String filename) {
        runOnUiThread(new SetImage(photo, filename));
    }

    @Override
    public void toastCallback(String text) {
        runOnUiThread(new ToastCallback(text));
    }

    private class SetImage implements Runnable {
        private Bitmap photo;
        private String filename;

        public SetImage(Bitmap photo, String filename) {
            this.filename = filename;
            this.photo = photo;
        }

        @Override
        public void run() {
            mAdapter.add(new PhotoRecord(filename, photo));
            Log.i(TAG, "added a photo from file");
        }
    }

    private class ToastCallback implements Runnable {
        private String text;

        public ToastCallback(String text) {
            this.text = text;
        }

        @Override
        public void run() {
            Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
        }
    }

}
