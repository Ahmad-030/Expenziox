package com.burgaynet.expenziox.activities;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.burgaynet.expenziox.R;
import com.burgaynet.expenziox.utils.AppConstants;
import com.burgaynet.expenziox.utils.DataManager;

public class BudgetActivity extends AppCompatActivity {

    private DataManager dm;
    private LinearLayout budgetContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Budget");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        dm = new DataManager(this);
        budgetContainer = findViewById(R.id.budgetContainer);

        View container = findViewById(R.id.budgetScrollContainer);
        container.setAlpha(0f);
        container.animate().alpha(1f).setDuration(400).start();

        buildBudgetRows();
        setupBottomNav();
    }

    private void buildBudgetRows() {
        budgetContainer.removeAllViews();
        float dp = getResources().getDisplayMetrics().density;

        for (int i = 0; i < AppConstants.CATEGORIES.length; i++) {
            String cat   = AppConstants.CATEGORIES[i];
            int color    = AppConstants.CATEGORY_COLORS[i];
            String emoji = AppConstants.getCategoryEmoji(cat);
            double spent = dm.getCategoryExpense(cat);
            double limit = dm.getBudgetLimit(cat);

            // Card
            com.google.android.material.card.MaterialCardView card =
                    new com.google.android.material.card.MaterialCardView(this);
            LinearLayout.LayoutParams cardLp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            cardLp.setMargins(0, 0, 0, (int)(14 * dp));
            card.setLayoutParams(cardLp);
            card.setRadius(20 * dp);
            card.setCardElevation(0f);
            card.setCardBackgroundColor(0xFF161F2E);
            card.setStrokeColor(0xFF1E3050);
            card.setStrokeWidth((int) dp);

            LinearLayout col = new LinearLayout(this);
            col.setOrientation(LinearLayout.VERTICAL);
            int pad = (int)(18 * dp);
            col.setPadding(pad, pad, pad, pad);

            // Header row
            LinearLayout header = new LinearLayout(this);
            header.setOrientation(LinearLayout.HORIZONTAL);
            header.setGravity(Gravity.CENTER_VERTICAL);

            TextView tvEmoji = new TextView(this);
            tvEmoji.setText(emoji);
            tvEmoji.setTextSize(22f);
            LinearLayout.LayoutParams eLp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            eLp.setMarginEnd((int)(12 * dp));
            tvEmoji.setLayoutParams(eLp);

            TextView tvCat = new TextView(this);
            tvCat.setText(cat);
            tvCat.setTextColor(0xFFF0F4FF);
            tvCat.setTextSize(15f);
            tvCat.setTypeface(null, android.graphics.Typeface.BOLD);
            tvCat.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));

            double pct = limit > 0 ? Math.min((spent / limit) * 100, 100) : 0;
            boolean overBudget = limit > 0 && spent > limit;
            TextView tvPct = new TextView(this);
            tvPct.setText(limit > 0 ? (int) pct + "%" : "—");
            tvPct.setTextColor(overBudget ? 0xFFFF6B6B : color);
            tvPct.setTextSize(14f);
            tvPct.setTypeface(null, android.graphics.Typeface.BOLD);

            header.addView(tvEmoji);
            header.addView(tvCat);
            header.addView(tvPct);
            col.addView(header);

            // Progress bar
            ProgressBar pb = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
            LinearLayout.LayoutParams pbLp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, (int)(6 * dp));
            pbLp.setMargins(0, (int)(10 * dp), 0, (int)(10 * dp));
            pb.setLayoutParams(pbLp);
            pb.setMax(100);
            pb.setProgress((int) pct);
            pb.getProgressDrawable().setTint(overBudget ? 0xFFFF6B6B : color);
            pb.setBackgroundColor(0);
            col.addView(pb);

            // Spent / Limit row
            LinearLayout spentRow = new LinearLayout(this);
            spentRow.setOrientation(LinearLayout.HORIZONTAL);
            spentRow.setGravity(Gravity.CENTER_VERTICAL);

            TextView tvSpent = new TextView(this);
            tvSpent.setText("Spent: PKR " + String.format("%,.0f", spent));
            tvSpent.setTextColor(0xFF7B90A8);
            tvSpent.setTextSize(12f);
            tvSpent.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));

            // Limit input
            final EditText etLimit = new EditText(this);
            etLimit.setHint("Set limit");
            etLimit.setHintTextColor(0xFF4A5568);
            etLimit.setTextColor(0xFFF0F4FF);
            etLimit.setTextSize(12f);
            etLimit.setInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);
            etLimit.setBackground(null);
            etLimit.setGravity(Gravity.END);
            if (limit > 0) etLimit.setText(String.format("%.0f", limit));
            LinearLayout.LayoutParams elLp = new LinearLayout.LayoutParams(
                    (int)(100 * dp), LinearLayout.LayoutParams.WRAP_CONTENT);
            etLimit.setLayoutParams(elLp);

            TextView btnSet = new TextView(this);
            btnSet.setText("Set");
            btnSet.setTextColor(color);
            btnSet.setTextSize(13f);
            btnSet.setTypeface(null, android.graphics.Typeface.BOLD);
            LinearLayout.LayoutParams bsLp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            bsLp.setMarginStart((int)(10 * dp));
            btnSet.setLayoutParams(bsLp);
            btnSet.setOnClickListener(v -> {
                String val = etLimit.getText().toString().trim();
                if (val.isEmpty()) { Toast.makeText(this, "Enter a limit", Toast.LENGTH_SHORT).show(); return; }
                try {
                    dm.setBudget(cat, Double.parseDouble(val));
                    Toast.makeText(this, "✓ Budget set for " + cat, Toast.LENGTH_SHORT).show();
                    buildBudgetRows();
                } catch (NumberFormatException ex) {
                    Toast.makeText(this, "Invalid amount", Toast.LENGTH_SHORT).show();
                }
            });

            spentRow.addView(tvSpent);
            spentRow.addView(etLimit);
            spentRow.addView(btnSet);
            col.addView(spentRow);

            card.addView(col);

            // Stagger animation
            final int delay = i * 60;
            card.setAlpha(0f); card.setTranslationX(40f);
            budgetContainer.addView(card);
            card.postDelayed(() -> card.animate().alpha(1f).translationX(0f)
                    .setDuration(300).setInterpolator(new android.view.animation.DecelerateInterpolator()).start(), delay);
        }
    }

    private void setupBottomNav() {
        findViewById(R.id.navDashboard).setOnClickListener(v -> { finish(); overridePendingTransition(R.anim.fade_in, R.anim.fade_out); });
        findViewById(R.id.navAnalytics).setOnClickListener(v -> { startActivity(new android.content.Intent(this, AnalyticsActivity.class)); overridePendingTransition(R.anim.fade_in, R.anim.fade_out); });
        findViewById(R.id.navBudget).setOnClickListener(v -> {});
        findViewById(R.id.navGoals).setOnClickListener(v -> { startActivity(new android.content.Intent(this, GoalsActivity.class)); overridePendingTransition(R.anim.fade_in, R.anim.fade_out); });
        findViewById(R.id.navSettings).setOnClickListener(v -> { startActivity(new android.content.Intent(this, SettingsActivity.class)); overridePendingTransition(R.anim.fade_in, R.anim.fade_out); });
        highlightNav(2);
    }

    private void highlightNav(int idx) {
        int[] ids = {R.id.navDashboard, R.id.navAnalytics, R.id.navBudget, R.id.navGoals, R.id.navSettings};
        for (int i = 0; i < ids.length; i++) {
            LinearLayout nav = findViewById(ids[i]);
            ((TextView) nav.getChildAt(1)).setTextColor(i == idx ? 0xFF00D4AA : 0xFF4A5568);
            ((TextView) nav.getChildAt(0)).setAlpha(i == idx ? 1f : 0.5f);
        }
    }

    @Override
    public boolean onSupportNavigateUp() { finish(); overridePendingTransition(R.anim.fade_in, R.anim.fade_out); return true; }
}
