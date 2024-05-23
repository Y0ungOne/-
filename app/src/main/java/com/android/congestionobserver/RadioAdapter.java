package com.android.congestionobserver;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.missingapp.R;

import java.util.List;

public class RadioAdapter extends RecyclerView.Adapter<RadioAdapter.ViewHolder> {
    private List<RadioData> radios;
    private Context context;
    private OnItemClickListener listener;

    public RadioAdapter(List<RadioData> radios, Context context, OnItemClickListener listener) {
        this.radios = radios;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_radio, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(radios.get(position));
    }

    @Override
    public int getItemCount() {
        return radios.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        private RadioButton rbName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rbName = itemView.findViewById(R.id.rb_number);

            rbName.setOnClickListener(v -> {
               listener.onItemClick(getAdapterPosition());
            });

        }

        public void bind(RadioData radioData) {
            rbName.setText(radioData.name);
            rbName.setChecked(radioData.isChecked);
        }
    }

    interface OnItemClickListener {
        void onItemClick(int position);
    }
}
