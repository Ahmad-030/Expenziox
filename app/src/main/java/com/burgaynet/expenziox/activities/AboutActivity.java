package com.burgaynet.expenziox.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.burgaynet.expenziox.R;

public class AboutActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("About");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        LinearLayout container = findViewById(R.id.aboutContainer);
        container.setAlpha(0f);
        container.setTranslationY(30f);
        container.animate().alpha(1f).translationY(0f).setDuration(400)
                .setInterpolator(new android.view.animation.DecelerateInterpolator()).start();

        android.view.View heroIcon = findViewById(R.id.tvHeroIcon);
        heroIcon.postDelayed(() -> heroIcon.animate().scaleX(1.08f).scaleY(1.08f).setDuration(800)
                .withEndAction(() -> heroIcon.animate().scaleX(1f).scaleY(1f)
                        .setDuration(600).start()).start(), 400);

        LinearLayout rowPrivacyPolicy = findViewById(R.id.rowPrivacyPolicy);
        rowPrivacyPolicy.setOnClickListener(v -> {
            startActivity(new Intent(this, PrivacyPolicyActivity.class));
            overridePendingTransition(R.anim.slide_up_in, R.anim.fade_out);
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.slide_down_out);
        return true;
    }
}