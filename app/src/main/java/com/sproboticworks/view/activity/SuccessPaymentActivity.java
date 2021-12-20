package com.sproboticworks.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.sproboticworks.R;
import com.sproboticworks.util.MethodClass;

public class SuccessPaymentActivity extends AppCompatActivity {

    TextView tvPaidAmount, textVieworderId;
    private ImageButton imageButton;
    private MaterialButton materialButton;
    private String orderId;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_successful);

        textVieworderId = findViewById(R.id.orderId);

        bundle = getIntent().getBundleExtra("get_bundle");
        orderId = getIntent().getStringExtra("orderId");
        tvPaidAmount = findViewById(R.id.tvPaidAmount);

        tvPaidAmount.setText("Rs. " + bundle.getString("tv_cart_total"));
        textVieworderId.setText(orderId);

        imageButton = findViewById(R.id.back_button);
        materialButton = findViewById(R.id.rate_us);
        materialButton.setOnClickListener(v->{
            final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
            } catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
            }
            //https://play.google.com/store/apps/details?id=package
        });
        imageButton.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();
        });


    }

    @Override
    public void onBackPressed() {
        MethodClass.go_to_next_activity_clear_task(this, MainActivity.class);

        super.onBackPressed();
    }
}