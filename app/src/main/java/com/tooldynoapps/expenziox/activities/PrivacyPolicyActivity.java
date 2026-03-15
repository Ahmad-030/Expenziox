package com.tooldynoapps.expenziox.activities;

import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.tooldynoapps.expenziox.R;

public class PrivacyPolicyActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_privacy_policy);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            androidx.core.graphics.Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Privacy Policy");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        LinearLayout container = findViewById(R.id.policyContainer);
        container.setAlpha(0f);
        container.setTranslationY(30f);
        container.animate().alpha(1f).translationY(0f).setDuration(400)
                .setInterpolator(new android.view.animation.DecelerateInterpolator()).start();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.slide_down_out);
        return true;
    }
}