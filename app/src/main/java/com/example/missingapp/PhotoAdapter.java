package com.example.missingapp;

/*import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class PhotoAdapter extends BaseAdapter {

    private Context context;
    private List<Photo> photos;

    public PhotoAdapter(Context context, List<Photo> photos) {
        this.context = context;
        this.photos = photos;
    }

    @Override
    public int getCount() {
        return photos.size();
    }

    @Override
    public Object getItem(int position) {
        return photos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_photo, parent, false);
        }

        Photo photo = photos.get(position);

        TextView name = convertView.findViewById(R.id.photoName);
        TextView age = convertView.findViewById(R.id.photoAge);
        ImageView photoView = convertView.findViewById(R.id.photoView);

        name.setText(photo.getName());
        age.setText(String.valueOf(photo.getAge()));
        Glide.with(context).load(photo.getImage().get(0)).into(photoView);

        return convertView;
    }
}*/
