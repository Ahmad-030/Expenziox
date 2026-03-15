package com.tooldynoapps.expenziox.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.tooldynoapps.expenziox.R;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_about);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            androidx.core.graphics.Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        if (getSupportActionBar() != null) getSupportActionBar().hide();

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

        setupBottomNav();
    }

    private void setupBottomNav() {
        findViewById(R.id.navDashboard).setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        });
        findViewById(R.id.navAnalytics).setOnClickListener(v -> {
            Intent intent = new Intent(this, AnalyticsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        });
        findViewById(R.id.navBudget).setOnClickListener(v -> {
            Intent intent = new Intent(this, BudgetActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        });
        findViewById(R.id.navGoals).setOnClickListener(v -> {
            Intent intent = new Intent(this, GoalsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        });
        findViewById(R.id.navAbout).setOnClickListener(v -> {}); // already here
        highlightNav(4);
    }

    private void highlightNav(int idx) {
        int[] ids = {R.id.navDashboard, R.id.navAnalytics, R.id.navBudget, R.id.navGoals, R.id.navAbout};
        for (int i = 0; i < ids.length; i++) {
            LinearLayout nav = findViewById(ids[i]);
            ((TextView) nav.getChildAt(1)).setTextColor(i == idx ? 0xFF00D4AA : 0xFF4A5568);
            ((TextView) nav.getChildAt(0)).setAlpha(i == idx ? 1f : 0.5f);
        }
    }
}