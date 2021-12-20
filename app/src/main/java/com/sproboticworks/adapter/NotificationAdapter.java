package com.sproboticworks.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.sproboticworks.R;
import com.sproboticworks.model.NotificationModel;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private final ArrayList<NotificationModel> notificationModels;
    private final Context context;

    public NotificationAdapter(ArrayList<NotificationModel> notificationModels, Context context) {
        this.notificationModels = notificationModels;
        this.context = context;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NotificationViewHolder(LayoutInflater.from(context).inflate(R.layout.recyclerview_notification, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        holder.notiDate.setText("");
        holder.notiDescription.setText("");
        holder.notiTime.setText("");
    }

    @Override
    public int getItemCount() {
        return notificationModels.size();
    }

    class NotificationViewHolder extends RecyclerView.ViewHolder {
        TextView notiDate, notiTime, notiDescription;
        MaterialButton notiButton;
        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            notiDate = itemView.findViewById(R.id.notiDate);
            notiTime = itemView.findViewById(R.id.notiTime);
            notiDescription = itemView.findViewById(R.id.notiDescription);
            notiButton = itemView.findViewById(R.id.notiButton);
        }
    }
}
