package com.asm.dailyselfieasm;

import android.app.AlarmManager;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListActivity;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import java.util.Date;


public class MainActivity extends ListActivity implements SetImageCallback, ToastCallback {
    private static final String TAG = "DailySelfieAsm";
    private static final int PHOTO_REQUEST = 123;

    private PhotoViewAdapter mAdapter;
    private static boolean SET_NOTIFICATIONS = true;
    private static final String SET_NOTIFICATIONS_SHARED = "set-notifications";
    private static SharedPreferences preferences;

    public static final String EXTRA_BITMAP = "ExtraBitmap";
    private static File tempPhotoFile;
    public static final int PREVIEW_DIMENS = 360;

    // Alarm Section
    private static final int ALARM_DELAY = 20000;
    private static final int ALARM_REPEATING_DELAY = 60000;
    public static final int NOTE_ID = 12345;

    private PendingIntent alarmPending;
    private AlarmManager alarmManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        //ListView photoListView = (ListView) findViewById(R.id.list);
        //photoListView.addFooterView(footerView);
        //photoListView.setId(android.R.id.list); // ??

        preferences = getPreferences(MODE_PRIVATE);

        DiskAdapter.setSaveFolder(getApplicationContext());

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

        mAdapter = new PhotoViewAdapter(getApplicationContext());
        setListAdapter(mAdapter);

        DiskAdapter.getInstance().loadPreviews(this, this);

        // done TODO: add alarm that reminds to take a selfie, pressing it opens the app

        Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        alarmIntent.putExtra(AlarmReceiver.NOTE_ID, NOTE_ID);
        alarmIntent.putExtra(AlarmReceiver.NOTE, getRestartNotification(getApplicationContext()
                .getString(R.string.notification_text)));
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
                .setContentTitle(getApplicationContext().getString(R.string.notification_title))
                .setContentText(content)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setContentIntent(intent)
                .build();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SET_NOTIFICATIONS = preferences.getBoolean(SET_NOTIFICATIONS_SHARED, true);
        alarmManager.cancel(alarmPending);
        Log.i(TAG, "canceled the alarms if there were any");
    }

    @Override
    protected void onPause() {
        if (SET_NOTIFICATIONS) {
            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime() + ALARM_DELAY,
                    ALARM_REPEATING_DELAY, alarmPending);
            Log.i(TAG, "set the alarm");
        }

        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(SET_NOTIFICATIONS_SHARED, SET_NOTIFICATIONS);
        editor.apply();

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

    // done TODO: stop/resume notifications button

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_take_photo:
                Log.i(TAG, "Go to camera from menu");
                takePhotoButton();
                return true;

            case R.id.action_notification_switch:
                SET_NOTIFICATIONS = !SET_NOTIFICATIONS;
                showNotificationsStatus();
                return true;

            case R.id.action_clear_list:

                // done TODO: get clear list confirmation

                removeAllPhotosDialog();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showNotificationsStatus() {
        if (SET_NOTIFICATIONS) {
            showToast(R.string.notifications_are_on);
        }
        else {
            showToast(R.string.notifications_are_off);
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
                        DiskAdapter.getInstance().removeAllImages(MainActivity.this);
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

        String dateStamp = DateFormat.getDateInstance(DateFormat.SHORT).format(new Date());
        String timeStamp = DateFormat.getTimeInstance(DateFormat.MEDIUM).format(new Date());
        File path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = new File(path, dateStamp + "_" + timeStamp);

        tempPhotoFile = image;

        return image;
    }

    private void takePhotoButton() {

        // done TODO: don't save photos in camera folder

        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

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
                Log.i(TAG, "received the camera intent back");

                // done TODO: get full resolution pictures

                File oldFile = tempPhotoFile;
                String filename = oldFile.getName();
                File file = new File(getApplicationContext().getExternalFilesDir(null), filename);
                Log.i(TAG, "trying to move the photo...");
                if (oldFile.renameTo(file)) {
                    Log.i(TAG, "the photo has been moved to " + file.getPath());
                    tempPhotoFile = null;
                    DiskAdapter.getInstance().loadImage(this, filename, false);
                }
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
        runOnUiThread(new SetImage(makePreview(photo), filename));
    }

    @Override
    public void toastCallback(int textResource) {
        runOnUiThread(new ToastCallback(textResource));
    }

    private class SetImage implements Runnable {
        private Bitmap preview;
        private String filename;

        public SetImage(Bitmap preview, String filename) {
            this.filename = filename;
            this.preview = preview;
        }

        @Override
        public void run() {
            mAdapter.add(new PhotoRecord(filename, preview));
            Log.i(TAG, "added a photo from file: " + filename);
            DiskAdapter.getInstance().savePreview(filename, preview);
        }
    }

    private Bitmap makePreview(Bitmap photo) {

        // done TODO: make actual previews
        // make the dimensions sync with xml view ?

        Bitmap preview = null;

        if (photo.getWidth() >= photo.getHeight()){
            preview = Bitmap.createBitmap(
                    photo,
                    photo.getWidth()/2 - photo.getHeight()/2,
                    0,
                    photo.getHeight(),
                    photo.getHeight()
            );
        }
        else {
            preview = Bitmap.createBitmap(
                    photo,
                    0,
                    photo.getHeight()/2 - photo.getWidth()/2,
                    photo.getWidth(),
                    photo.getWidth()
            );
        }
        Bitmap scaled = Bitmap.createScaledBitmap(preview, PREVIEW_DIMENS, PREVIEW_DIMENS, true);

        Log.i(TAG, "a preview with dimensions " + scaled.getWidth() + " by "
                + scaled.getHeight() + " was created");

        // TODO: make the previews round
        //

        return scaled;

    }

    private class ToastCallback implements Runnable {
        private int textResource;

        public ToastCallback(int textResource) {
            this.textResource = textResource;
        }

        @Override
        public void run() {
            Toast.makeText(getApplicationContext(), getApplicationContext()
                    .getString(textResource), Toast.LENGTH_SHORT).show();
        }
    }

}
