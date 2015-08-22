package com.asm.dailyselfieasm;


import android.app.Fragment;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

public class PhotoFragment extends Fragment implements SetImageCallback {
    private static final String TAG = "DailySelfieAsm";

    private Bitmap retainImage;
    private ImageView photoView;
    private String filename;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.photo_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // done TODO: landscape and portrait orientations
        // (converted from activity to fragment)

        setRetainInstance(true);

        photoView = (ImageView) getView().findViewById(R.id.imageView);

        if (null == retainImage) {
            filename = getArguments().getString(MainActivity.EXTRA_BITMAP);
            DiskAdapter.getInstance().loadImage(this, filename, false);
            Toast.makeText(getActivity(), R.string.please_wait, Toast.LENGTH_SHORT).show();
        }
        else {
            photoView.setImageBitmap(retainImage);
        }
    }

    @Override
    public void setImage(Bitmap image, String filename) {
        Log.i(TAG, "loaded photo");
        getActivity().runOnUiThread(new SetImage(image, filename));
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
                retainImage = image;
                Toast.makeText(getActivity(), filename, Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getActivity(), R.string.cannot_load, Toast.LENGTH_SHORT).show();
            }

        }
    }

}
