package com.asm.dailyselfieasm;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;


public class MainActivity extends ListActivity {
    private static final String TAG = "Dayly-Selfie-Asm";
    private PhotoViewAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        ListView photoListView = getListView();
        photoListView.setId(android.R.id.list); // ??
        View footerView = getLayoutInflater().inflate(R.layout.footer_view, null, false);

        footerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showToast("Go to camera from footer");

            }
        });

        photoListView.addFooterView(footerView);
        mAdapter = new PhotoViewAdapter(getApplicationContext());
        setListAdapter(mAdapter);
    }

    private void showToast(String input) {
        Toast.makeText(this, input, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_take_photo) {

            showToast("Go to camera from menu");

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
