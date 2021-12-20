package com.sproboticworks.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageButton;

import com.sproboticworks.R;
import com.sproboticworks.adapter.NotificationAdapter;
import com.sproboticworks.model.NotificationModel;

import java.util.ArrayList;

public class NotificationActivity extends AppCompatActivity {

    private ImageButton imageButton;
    private RecyclerView recyclerView;
    private ArrayList<NotificationModel> notificationModelArrayList = new ArrayList<>();
    private NotificationAdapter notificationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        imageButton= findViewById(R.id.back_button);
        recyclerView= findViewById(R.id.reyclerview_noti);
        imageButton.setOnClickListener(v->onBackPressed());

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}