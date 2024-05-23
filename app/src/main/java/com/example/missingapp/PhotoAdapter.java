package com.example.missingapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder> {

    private Context context;
    private List<ProtectedTargetReadDto> photos;

    public PhotoAdapter(Context context, List<ProtectedTargetReadDto> photos) {
        this.context = context;
        this.photos = photos;
    }

    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.photo_list_item, parent, false);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PhotoViewHolder holder, int position) {
        ProtectedTargetReadDto photo = photos.get(position);
        holder.name.setText(photo.getName());
        holder.age.setText(String.valueOf(photo.getAge()));

        // base64 인코딩된 이미지를 디코딩하여 설정
        if (photo.getImage() != null && !photo.getImage().isEmpty()) {
            String base64Image = photo.getImage().get(0);
            byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            holder.photoView.setImageBitmap(decodedByte);
        } else {
            holder.photoView.setImageResource(R.drawable.error); // 에러 이미지 설정
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, PhotoDetailActivity.class);
            intent.putExtra("photoId", photo.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    static class PhotoViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView age;
        ImageView photoView;

        PhotoViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.photoName);
            age = itemView.findViewById(R.id.photoAge);
            photoView = itemView.findViewById(R.id.photoView);
        }
    }
}
