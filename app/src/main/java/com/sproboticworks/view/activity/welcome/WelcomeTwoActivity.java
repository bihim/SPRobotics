package com.sproboticworks.view.activity.welcome;

import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.sproboticworks.R;
import com.sproboticworks.preferences.SessionManager;
import com.sproboticworks.util.NetworkCallActivity;
import com.sproboticworks.view.activity.MainActivity;

public class WelcomeTwoActivity extends NetworkCallActivity implements View.OnClickListener {

    MaterialCardView cardAgeTen, cardAgeThirteen;
    TextView textViewAgeTen, textViewAgeThirteen;
    private Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_two);
        activity = this;


        MaterialButton materialButton = findViewById(R.id.finish);


        textViewAgeTen = findViewById(R.id.textViewAgeTen);
        textViewAgeThirteen = findViewById(R.id.textViewAgeThirteen);
        //textViewAboveThirteen = findViewById(R.id.textViewAboveThirteen);


        cardAgeTen = findViewById(R.id.cardAgeTen);
        cardAgeThirteen = findViewById(R.id.cardAgeThirteen);
        //cardAboveThirteen = findViewById(R.id.cardAboveThirteen);

        cardAgeTen.setOnClickListener(this);
        cardAgeThirteen.setOnClickListener(this);
        //cardAboveThirteen.setOnClickListener(this);

        SessionManager.setValue(this, SessionManager.CHILD_AGE, "10");
        //selected_bottom_layout(activity, cardAgeTen, textViewAgeTen);
        selectedTopButtonColor(cardAgeTen, textViewAgeTen);


        materialButton.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });


    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public void selected_bottom_layout(Activity activity, CardView view, TextView textView) {
        initial_bottom_menu(activity);
        view.setCardBackgroundColor(activity.getResources().getColor(R.color.color_blue));
        textView.setTextColor(activity.getResources().getColor(R.color.white));
    }

    public void initial_bottom_menu(Activity activity) {

        cardAgeTen.setCardBackgroundColor(activity.getResources().getColor(R.color.transparent));
        //textViewAgeTen.setTextColor(activity.getResources().getColor(R.color.gray_text));

        cardAgeThirteen.setCardBackgroundColor(activity.getResources().getColor(R.color.transparent));
        //textViewAgeThirteen.setTextColor(activity.getResources().getColor(R.color.gray_text));

        /*cardAboveThirteen.setCardBackgroundColor(activity.getResources().getColor(R.color.transparent));
        textViewAboveThirteen.setTextColor(activity.getResources().getColor(R.color.gray_text));*/

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.cardAgeTen:
                SessionManager.setValue(this, SessionManager.CHILD_AGE, "10");
                //selected_bottom_layout(activity, cardAgeTen, textViewAgeTen);
                selectedTopButtonColor(cardAgeTen, textViewAgeTen);
                unSelectedTopButtonColor(cardAgeThirteen, textViewAgeThirteen);
                break;

            case R.id.cardAgeThirteen:
                SessionManager.setValue(this, SessionManager.CHILD_AGE, "13");
                //selected_bottom_layout(activity, cardAgeThirteen, textViewAgeThirteen);
                unSelectedTopButtonColor(cardAgeTen, textViewAgeTen);
                selectedTopButtonColor(cardAgeThirteen, textViewAgeThirteen);
                break;

            /*case R.id.cardAboveThirteen:
                SessionManager.setValue(SessionManager.CHILD_AGE, "14");
                selected_bottom_layout(activity, cardAboveThirteen, textViewAboveThirteen);
                break;*/


        }


    }

    private void selectedTopButtonColor(MaterialCardView materialCardView, TextView textView) {
        materialCardView.setStrokeWidth(0);
        materialCardView.setStrokeColor(getResources().getColor(R.color.transparent));
        materialCardView.invalidate();
        textView.setTextColor(getResources().getColor(R.color.white));
        textView.setBackgroundColor(getResources().getColor(R.color.colors1));
    }

    private void unSelectedTopButtonColor(MaterialCardView materialCardView, TextView textView) {
        materialCardView.setStrokeWidth(2);
        materialCardView.setStrokeColor(getResources().getColor(R.color.fragment_home_card_text_color));
        materialCardView.invalidate();
        textView.setTextColor(getResources().getColor(R.color.fragment_home_card_text_color));
        textView.setBackgroundColor(getResources().getColor(R.color.white));
    }


}