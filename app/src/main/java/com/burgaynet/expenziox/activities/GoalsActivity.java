package com.burgaynet.expenziox.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.burgaynet.expenziox.R;
import com.burgaynet.expenziox.adapters.GoalAdapter;
import com.burgaynet.expenziox.models.GoalModel;
import com.burgaynet.expenziox.utils.DataManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class GoalsActivity extends AppCompatActivity {

    private DataManager dm;
    private GoalAdapter adapter;
    private TextView tvEmpty;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goals);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Goals");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        dm           = new DataManager(this);
        recyclerView = findViewById(R.id.recyclerGoals);
        tvEmpty      = findViewById(R.id.tvEmpty);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new GoalAdapter(this, dm.getGoals(), this::confirmDelete, this::showAddSavings);
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            startActivity(new Intent(this, AddGoalActivity.class));
            overridePendingTransition(R.anim.slide_up_in, R.anim.fade_out);
        });

        setupBottomNav();
    }

    @Override
    protected void onResume() {
        super.onResume();
        List<GoalModel> goals = dm.getGoals();
        adapter.updateList(goals);
        tvEmpty.setVisibility(goals.isEmpty() ? View.VISIBLE : View.GONE);
        recyclerView.setVisibility(goals.isEmpty() ? View.GONE : View.VISIBLE);
    }

    private void confirmDelete(GoalModel g) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Goal")
                .setMessage("Delete goal \"" + g.getName() + "\"?")
                .setPositiveButton("Delete", (d, w) -> { dm.deleteGoal(g.getId()); onResume(); })
                .setNegativeButton("Cancel", null).show();
    }

    private void showAddSavings(GoalModel g) {
        EditText et = new EditText(this);
        et.setHint("Amount to add");
        et.setInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);
        new AlertDialog.Builder(this)
                .setTitle("Add Savings to " + g.getName())
                .setView(et)
                .setPositiveButton("Add", (d, w) -> {
                    String val = et.getText().toString().trim();
                    if (val.isEmpty()) return;
                    try {
                        double add = Double.parseDouble(val);
                        g.setSavedAmount(g.getSavedAmount() + add);
                        dm.updateGoal(g);
                        Toast.makeText(this, "✓ Savings updated!", Toast.LENGTH_SHORT).show();
                        onResume();
                    } catch (NumberFormatException ex) {
                        Toast.makeText(this, "Invalid amount", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null).show();
    }

    private void setupBottomNav() {
        findViewById(R.id.navDashboard).setOnClickListener(v -> { finish(); overridePendingTransition(R.anim.fade_in, R.anim.fade_out); });
        findViewById(R.id.navAnalytics).setOnClickListener(v -> { startActivity(new Intent(this, AnalyticsActivity.class)); overridePendingTransition(R.anim.fade_in, R.anim.fade_out); });
        findViewById(R.id.navBudget).setOnClickListener(v -> { startActivity(new Intent(this, BudgetActivity.class)); overridePendingTransition(R.anim.fade_in, R.anim.fade_out); });
        findViewById(R.id.navGoals).setOnClickListener(v -> {});
        findViewById(R.id.navSettings).setOnClickListener(v -> { startActivity(new Intent(this, SettingsActivity.class)); overridePendingTransition(R.anim.fade_in, R.anim.fade_out); });
        highlightNav(3);
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
