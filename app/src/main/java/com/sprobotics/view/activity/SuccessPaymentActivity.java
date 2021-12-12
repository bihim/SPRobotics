package com.sprobotics.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.sprobotics.R;
import com.sprobotics.preferences.SessionManager;
import com.sprobotics.util.MethodClass;
import com.sprobotics.view.activity.welcome.WelcomeOneActivity;

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


        new Handler().postDelayed(() -> {

            Intent intent = new Intent(SuccessPaymentActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();

        }, 2000);


    }

    @Override
    public void onBackPressed() {
        MethodClass.go_to_next_activity(this, MainActivity.class);

        super.onBackPressed();
    }
}