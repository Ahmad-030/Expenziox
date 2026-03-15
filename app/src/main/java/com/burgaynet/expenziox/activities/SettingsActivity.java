package com.burgaynet.expenziox.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.burgaynet.expenziox.R;
import com.burgaynet.expenziox.utils.DataManager;
import com.google.android.material.button.MaterialButton;

public class SettingsActivity extends AppCompatActivity {

    private DataManager dm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Settings");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        dm = new DataManager(this);

        View container = findViewById(R.id.settingsContainer);
        container.setAlpha(0f);
        container.animate().alpha(1f).setDuration(400).start();

        Switch switchNotifications = findViewById(R.id.switchNotifications);
        Switch switchAutoReset     = findViewById(R.id.switchAutoReset);
        MaterialButton btnAbout    = findViewById(R.id.btnAbout);

        switchNotifications.setChecked(dm.isNotificationsEnabled());
        switchAutoReset.setChecked(dm.isAutoReset());


        switchNotifications.setOnCheckedChangeListener((b, checked) -> dm.setNotifications(checked));
        switchAutoReset.setOnCheckedChangeListener((b, checked) -> dm.setAutoReset(checked));


        setupBottomNav();
    }

    private void showPinDialog() {
        EditText et = new EditText(this);
        et.setHint("Enter 4-digit PIN");
        et.setInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        et.setTextColor(0xFFF0F4FF);
        new AlertDialog.Builder(this)
                .setTitle("Set PIN")
                .setView(et)
                .setPositiveButton("Save", (d, w) -> {
                    String pin = et.getText().toString().trim();
                    if (pin.length() < 4) { Toast.makeText(this, "PIN must be 4 digits", Toast.LENGTH_SHORT).show(); return; }
                    dm.setPin(pin);
                    dm.setPinEnabled(true);
                    Toast.makeText(this, "✓ PIN saved", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null).show();
    }

    private void setupBottomNav() {
        findViewById(R.id.navDashboard).setOnClickListener(v -> { finish(); overridePendingTransition(R.anim.fade_in, R.anim.fade_out); });
        findViewById(R.id.navAnalytics).setOnClickListener(v -> { startActivity(new Intent(this, AnalyticsActivity.class)); overridePendingTransition(R.anim.fade_in, R.anim.fade_out); });
        findViewById(R.id.navBudget).setOnClickListener(v -> { startActivity(new Intent(this, BudgetActivity.class)); overridePendingTransition(R.anim.fade_in, R.anim.fade_out); });
        findViewById(R.id.navGoals).setOnClickListener(v -> { startActivity(new Intent(this, GoalsActivity.class)); overridePendingTransition(R.anim.fade_in, R.anim.fade_out); });
        findViewById(R.id.navSettings).setOnClickListener(v -> {});
        highlightNav(4);
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
