package com.asm.dailyselfieasm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class PhotoViewAdapter extends BaseAdapter {
    private ArrayList<PhotoRecord> list = new ArrayList<>();
    private static LayoutInflater inflater = null;
    private Context mContext;

    public PhotoViewAdapter(Context context) {
        mContext = context;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    // done TODO: Add padding to the preview in the listview

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View newView = convertView;
        ViewHolder holder;

        PhotoRecord curr = list.get(position);

        if (null == convertView) {

            holder = new ViewHolder();
            newView = inflater.inflate(R.layout.photo_view, parent, false);
            holder.preview = (ImageView) newView.findViewById(R.id.photo_img);
            holder.date = (TextView) newView.findViewById(R.id.photo_date);
            newView.setTag(holder);

        } else {
            holder = (ViewHolder) newView.getTag();
        }

        holder.preview.setImageBitmap(curr.getPreview());
        holder.date.setText(curr.getDate());

        return newView;
    }

    static class ViewHolder {
        ImageView preview;
        TextView date;
    }

    public void add(PhotoRecord listItem) {
        list.add(listItem);
        notifyDataSetChanged();
    }

    public void remove(int position) {
        list.remove(position);
        notifyDataSetChanged();
    }

    public void clearList() {
        list.clear();
        this.notifyDataSetChanged();
    }

}
