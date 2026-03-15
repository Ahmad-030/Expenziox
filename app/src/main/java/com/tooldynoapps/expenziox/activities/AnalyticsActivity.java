package com.tooldynoapps.expenziox.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.tooldynoapps.expenziox.R;
import com.tooldynoapps.expenziox.models.TransactionModel;
import com.tooldynoapps.expenziox.utils.AppConstants;
import com.tooldynoapps.expenziox.utils.BarChartView;
import com.tooldynoapps.expenziox.utils.DataManager;
import com.tooldynoapps.expenziox.utils.PieChartView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AnalyticsActivity extends AppCompatActivity {

    private DataManager dm;
    private PieChartView pieChart;
    private BarChartView barChart;
    private LinearLayout legendContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analytics);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Analytics");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        dm              = new DataManager(this);
        pieChart        = findViewById(R.id.pieChart);
        barChart        = findViewById(R.id.barChart);
        legendContainer = findViewById(R.id.legendContainer);

        View container = findViewById(R.id.analyticsContainer);
        container.setAlpha(0f);
        container.animate().alpha(1f).setDuration(400).start();

        loadCharts();
        setupBottomNav();
    }

    private void loadCharts() {
        List<PieChartView.Slice> slices = new ArrayList<>();
        legendContainer.removeAllViews();
        double totalExpense = 0;
        for (int i = 0; i < AppConstants.CATEGORIES.length; i++) {
            double amt = dm.getCategoryExpense(AppConstants.CATEGORIES[i]);
            if (amt > 0) {
                slices.add(new PieChartView.Slice(AppConstants.CATEGORIES[i],
                        (float) amt, AppConstants.CATEGORY_COLORS[i]));
                totalExpense += amt;
                addLegendRow(AppConstants.CATEGORIES[i], amt, AppConstants.CATEGORY_COLORS[i]);
            }
        }
        if (slices.isEmpty()) slices.add(new PieChartView.Slice("No data", 1f, 0xFF2A3F5F));
        pieChart.setData(slices, "Total", "PKR " + AppConstants.formatAmount(totalExpense));

        List<BarChartView.Bar> bars = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        String[] monthNames = {"J","F","M","A","M","J","J","A","S","O","N","D"};
        for (int i = 5; i >= 0; i--) {
            Calendar c = (Calendar) cal.clone();
            c.add(Calendar.MONTH, -i);
            String monthKey = String.format(Locale.getDefault(), "%04d-%02d",
                    c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1);
            double monthTotal = 0;
            for (TransactionModel t : dm.getTransactions()) {
                if (t.isExpense() && t.getDate().startsWith(monthKey)) monthTotal += t.getAmount();
            }
            bars.add(new BarChartView.Bar(monthNames[c.get(Calendar.MONTH)], (float) monthTotal,
                    i == 0 ? 0xFF00D4AA : 0xFF1565C0));
        }
        barChart.setData(bars);
    }

    private void addLegendRow(String category, double amount, int color) {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(android.view.Gravity.CENTER_VERTICAL);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 0, 0, 10);
        row.setLayoutParams(lp);

        View dot = new View(this);
        LinearLayout.LayoutParams dotLp = new LinearLayout.LayoutParams(12, 12);
        dotLp.setMarginEnd(12);
        dot.setLayoutParams(dotLp);
        android.graphics.drawable.GradientDrawable gd = new android.graphics.drawable.GradientDrawable();
        gd.setShape(android.graphics.drawable.GradientDrawable.OVAL);
        gd.setColor(color);
        dot.setBackground(gd);

        TextView tvCat = new TextView(this);
        tvCat.setText(AppConstants.getCategoryEmoji(category) + " " + category);
        tvCat.setTextColor(0xFFD0DCF0);
        tvCat.setTextSize(13f);
        tvCat.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));

        TextView tvAmt = new TextView(this);
        tvAmt.setText("PKR " + String.format("%,.0f", amount));
        tvAmt.setTextColor(color);
        tvAmt.setTextSize(13f);
        tvAmt.setTypeface(null, android.graphics.Typeface.BOLD);

        row.addView(dot);
        row.addView(tvCat);
        row.addView(tvAmt);
        legendContainer.addView(row);
    }

    private void setupBottomNav() {
        findViewById(R.id.navDashboard).setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        });
        findViewById(R.id.navAnalytics).setOnClickListener(v -> {}); // already here
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
        findViewById(R.id.navAbout).setOnClickListener(v -> {
            Intent intent = new Intent(this, AboutActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        });
        highlightNav(1);
    }

    private void highlightNav(int idx) {
        int[] ids = {R.id.navDashboard, R.id.navAnalytics, R.id.navBudget, R.id.navGoals, R.id.navAbout};
        for (int i = 0; i < ids.length; i++) {
            LinearLayout nav = findViewById(ids[i]);
            ((TextView) nav.getChildAt(1)).setTextColor(i == idx ? 0xFF00D4AA : 0xFF4A5568);
            ((TextView) nav.getChildAt(0)).setAlpha(i == idx ? 1f : 0.5f);
        }
    }

    @Override
    public boolean onSupportNavigateUp() { finish(); overridePendingTransition(R.anim.fade_in, R.anim.fade_out); return true; }
}