package com.tooldynoapps.expenziox.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.tooldynoapps.expenziox.R;
import com.tooldynoapps.expenziox.models.TransactionModel;
import com.tooldynoapps.expenziox.utils.AppConstants;
import com.tooldynoapps.expenziox.utils.DataManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;
import java.util.UUID;

public class AddTransactionActivity extends AppCompatActivity {

    private TextInputEditText etAmount, etNote;
    private Spinner spinnerCategory;
    private TextView tvDate, btnExpense, btnIncome;
    private String selectedType = "expense";
    private String selectedDate = "";
    private DataManager dm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_transaction);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            androidx.core.graphics.Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Add Transaction");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        dm              = new DataManager(this);
        etAmount        = findViewById(R.id.etAmount);
        etNote          = findViewById(R.id.etNote);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        tvDate          = findViewById(R.id.tvDate);
        btnExpense      = findViewById(R.id.btnExpense);
        btnIncome       = findViewById(R.id.btnIncome);

        Calendar cal = Calendar.getInstance();
        selectedDate = String.format("%04d-%02d-%02d",
                cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH));
        tvDate.setText(selectedDate);

        ArrayAdapter<String> catAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, AppConstants.CATEGORIES);
        catAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(catAdapter);

        updateTypeUI();
        btnExpense.setOnClickListener(v -> { selectedType = "expense"; updateTypeUI(); });
        btnIncome.setOnClickListener(v -> { selectedType = "income"; updateTypeUI(); });

        tvDate.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            new DatePickerDialog(this, (dp, y, m, d) -> {
                selectedDate = String.format("%04d-%02d-%02d", y, m + 1, d);
                tvDate.setText(selectedDate);
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
        });

        View container = findViewById(R.id.addContainer);
        container.setAlpha(0f); container.setTranslationY(40f);
        container.animate().alpha(1f).translationY(0f).setDuration(400)
                .setInterpolator(new android.view.animation.DecelerateInterpolator()).start();

        MaterialButton btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(v -> {
            v.animate().scaleX(0.96f).scaleY(0.96f).setDuration(80)
                    .withEndAction(() -> v.animate().scaleX(1f).scaleY(1f).setDuration(80)
                            .withEndAction(this::saveTransaction).start()).start();
        });
    }

    private void updateTypeUI() {
        boolean isExpense = "expense".equals(selectedType);
        btnExpense.setBackgroundColor(isExpense ? 0xFFFF6B6B : 0xFF1E2A3A);
        btnExpense.setTextColor(isExpense ? 0xFFFFFFFF : 0xFF7B90A8);
        btnIncome.setBackgroundColor(!isExpense ? 0xFF95D5B2 : 0xFF1E2A3A);
        btnIncome.setTextColor(!isExpense ? 0xFF0A0F1A : 0xFF7B90A8);
    }

    private void saveTransaction() {
        String amtStr = etAmount.getText() != null ? etAmount.getText().toString().trim() : "";
        String note   = etNote.getText() != null ? etNote.getText().toString().trim() : "";
        if (amtStr.isEmpty()) { etAmount.setError("Amount required"); return; }
        double amount;
        try { amount = Double.parseDouble(amtStr); } catch (NumberFormatException e) {
            etAmount.setError("Invalid amount"); return;
        }
        String category = spinnerCategory.getSelectedItem().toString();
        TransactionModel t = new TransactionModel(UUID.randomUUID().toString(),
                amount, category, selectedType, selectedDate, note, System.currentTimeMillis());
        dm.addTransaction(t);
        Toast.makeText(this, "✓ Transaction saved!", Toast.LENGTH_SHORT).show();
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.slide_down_out);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish(); overridePendingTransition(R.anim.fade_in, R.anim.slide_down_out); return true;
    }
}