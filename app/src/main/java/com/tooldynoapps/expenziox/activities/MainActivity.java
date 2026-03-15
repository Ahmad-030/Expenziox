package com.tooldynoapps.expenziox.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import androidx.activity.EdgeToEdge;

import com.tooldynoapps.expenziox.R;
import com.tooldynoapps.expenziox.adapters.TransactionAdapter;
import com.tooldynoapps.expenziox.models.TransactionModel;
import com.tooldynoapps.expenziox.utils.AppConstants;
import com.tooldynoapps.expenziox.utils.DataManager;
import com.tooldynoapps.expenziox.utils.PieChartView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DataManager dm;
    private TransactionAdapter adapter;
    private PieChartView pieChart;
    private TextView tvGreeting, tvBalance, tvIncome, tvExpense;
    private TextView tvEmpty;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            androidx.core.graphics.Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        if (getSupportActionBar() != null) getSupportActionBar().hide();

        dm = new DataManager(this);

        tvGreeting   = findViewById(R.id.tvGreeting);
        tvBalance    = findViewById(R.id.tvBalance);
        tvIncome     = findViewById(R.id.tvIncome);
        tvExpense    = findViewById(R.id.tvExpense);
        tvEmpty      = findViewById(R.id.tvEmpty);
        pieChart     = findViewById(R.id.pieChart);
        recyclerView = findViewById(R.id.recyclerTransactions);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TransactionAdapter(this, new ArrayList<>(), this::confirmDelete);
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            v.animate().scaleX(0.85f).scaleY(0.85f).setDuration(80)
                    .withEndAction(() -> v.animate().scaleX(1f).scaleY(1f).setDuration(80)
                            .withEndAction(() -> {
                                startActivity(new Intent(this, AddTransactionActivity.class));
                                overridePendingTransition(R.anim.slide_up_in, R.anim.fade_out);
                            }).start()).start();
        });

        setupBottomNav();
        animateEntrance();
    }

    private void setupBottomNav() {
        findViewById(R.id.navDashboard).setOnClickListener(v -> updateNavSelection(0));
        findViewById(R.id.navAnalytics).setOnClickListener(v -> {
            Intent intent = new Intent(this, AnalyticsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        });
        findViewById(R.id.navBudget).setOnClickListener(v -> {
            Intent intent = new Intent(this, BudgetActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        });
        findViewById(R.id.navGoals).setOnClickListener(v -> {
            Intent intent = new Intent(this, GoalsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        });
        findViewById(R.id.navAbout).setOnClickListener(v -> {
            Intent intent = new Intent(this, AboutActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        });
        updateNavSelection(0);
    }

    private void updateNavSelection(int idx) {
        int[] navIds = {R.id.navDashboard, R.id.navAnalytics, R.id.navBudget, R.id.navGoals, R.id.navAbout};
        for (int i = 0; i < navIds.length; i++) {
            View nav = findViewById(navIds[i]);
            TextView tv   = (TextView) ((LinearLayout) nav).getChildAt(1);
            TextView icon = (TextView) ((LinearLayout) nav).getChildAt(0);
            tv.setTextColor(i == idx ? 0xFF00D4AA : 0xFF4A5568);
            icon.setAlpha(i == idx ? 1f : 0.5f);
        }
    }

    private void animateEntrance() {
        View header = findViewById(R.id.headerCard);
        View pie    = findViewById(R.id.pieSection);
        View recent = findViewById(R.id.recentSection);
        header.setAlpha(0f); header.setTranslationY(-30f);
        pie.setAlpha(0f);    pie.setTranslationY(30f);
        recent.setAlpha(0f); recent.setTranslationY(30f);
        header.animate().alpha(1f).translationY(0f).setDuration(400).start();
        pie.animate().alpha(1f).translationY(0f).setDuration(400).setStartDelay(150).start();
        recent.animate().alpha(1f).translationY(0f).setDuration(400).setStartDelay(280).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshDashboard();
    }

    private void refreshDashboard() {
        String name = dm.getUserName();
        tvGreeting.setText("Hello, " + (name.isEmpty() ? "there" : name) + " 👋");

        double income  = dm.getTotalIncomeThisMonth();
        double expense = dm.getTotalExpenseThisMonth();
        double balance = income - expense;

        tvBalance.setText("PKR " + String.format("%,.0f", balance));
        tvIncome.setText("PKR " + AppConstants.formatAmount(income));
        tvExpense.setText("PKR " + AppConstants.formatAmount(expense));

        List<TransactionModel> all    = dm.getTransactions();
        List<TransactionModel> recent = all.subList(0, Math.min(10, all.size()));
        adapter.updateList(recent);
        tvEmpty.setVisibility(recent.isEmpty() ? View.VISIBLE : View.GONE);
        recyclerView.setVisibility(recent.isEmpty() ? View.GONE : View.VISIBLE);

        List<PieChartView.Slice> slices = new ArrayList<>();
        for (int i = 0; i < AppConstants.CATEGORIES.length; i++) {
            double amt = dm.getCategoryExpense(AppConstants.CATEGORIES[i]);
            if (amt > 0) {
                slices.add(new PieChartView.Slice(AppConstants.CATEGORIES[i],
                        (float) amt, AppConstants.CATEGORY_COLORS[i]));
            }
        }
        if (slices.isEmpty()) slices.add(new PieChartView.Slice("No data", 1f, 0xFF2A3F5F));
        pieChart.setData(slices, "Spent", "PKR " + AppConstants.formatAmount(expense));
    }

    private void confirmDelete(TransactionModel t) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Transaction")
                .setMessage("Remove this transaction?")
                .setPositiveButton("Delete", (d, w) -> { dm.deleteTransaction(t.getId()); refreshDashboard(); })
                .setNegativeButton("Cancel", null).show();
    }
}