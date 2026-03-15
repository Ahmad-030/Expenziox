package com.tooldynoapps.expenziox.activities;

import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.tooldynoapps.expenziox.R;

public class PrivacyPolicyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Privacy Policy");
        }

        LinearLayout container = findViewById(R.id.policyContainer);
        if (container != null) {
            container.setAlpha(0f);
            container.setTranslationY(30f);
            container.animate().alpha(1f).translationY(0f).setDuration(350)
                    .setInterpolator(new android.view.animation.DecelerateInterpolator()).start();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.slide_down_out);
        return true;
    }
}
