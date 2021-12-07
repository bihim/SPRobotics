package com.sprobotics.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;
import com.sprobotics.R;

public class BillingActivity extends AppCompatActivity {
    private TextView billingName, billingNumber, billingAddress, billingEdit, billingDelete, billingValueOfProduct, billingDiscount, billingGst, billingShipping, billingTotal;
    private MaterialCardView materialCardViewPayNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing);
        findViewById();
        setButtonCallBacks();
    }

    private void setButtonCallBacks(){
        billingEdit.setOnClickListener(v->{

        });

        billingDelete.setOnClickListener(v->{

        });

        materialCardViewPayNow.setOnClickListener(v->{

        });
    }

    private void findViewById(){
        billingName = findViewById(R.id.billing_name);
        billingNumber = findViewById(R.id.billing_phone);
        billingAddress = findViewById(R.id.billing_address);
        billingEdit = findViewById(R.id.billing_edit);
        billingDelete = findViewById(R.id.billing_delete);
        billingValueOfProduct = findViewById(R.id.billing_value_of_products);
        billingDiscount = findViewById(R.id.billing_discount);
        billingGst = findViewById(R.id.billing_estimated_gst);
        billingShipping = findViewById(R.id.billing_shipping);
        billingTotal = findViewById(R.id.billing_total);
        materialCardViewPayNow = findViewById(R.id.billing_pay_now);
    }
}