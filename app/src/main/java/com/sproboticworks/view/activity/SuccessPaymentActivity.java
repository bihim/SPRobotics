package com.sproboticworks.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.sproboticworks.R;
import com.sproboticworks.util.MethodClass;

public class SuccessPaymentActivity extends AppCompatActivity {

    TextView tvPaidAmount;


    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_successful);

        bundle = getIntent().getBundleExtra("get_bundle");
        tvPaidAmount = findViewById(R.id.tvPaidAmount);

        tvPaidAmount.setText("Rs. "+bundle.getString("tv_cart_total"));





    }

    @Override
    public void onBackPressed() {
        MethodClass.go_to_next_activity_clear_task(this, MainActivity.class);

        super.onBackPressed();
    }
}