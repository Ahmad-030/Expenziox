package com.tooldynoapps.expenziox.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.tooldynoapps.expenziox.R;
import com.tooldynoapps.expenziox.models.GoalModel;
import com.tooldynoapps.expenziox.utils.AppConstants;
import com.tooldynoapps.expenziox.utils.DataManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;
import java.util.UUID;

public class AddGoalActivity extends AppCompatActivity {

    private TextInputEditText etGoalName, etTarget, etSaved;
    private Spinner spinnerPriority;
    private TextView tvDeadline;
    private String selectedDeadline = "";
    private DataManager dm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_goal);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("New Goal");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        dm              = new DataManager(this);
        etGoalName      = findViewById(R.id.etGoalName);
        etTarget        = findViewById(R.id.etTarget);
        etSaved         = findViewById(R.id.etSaved);
        spinnerPriority = findViewById(R.id.spinnerPriority);
        tvDeadline      = findViewById(R.id.tvDeadline);

        ArrayAdapter<String> prioAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, AppConstants.PRIORITIES);
        prioAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPriority.setAdapter(prioAdapter);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, 6);
        selectedDeadline = String.format("%04d-%02d-%02d",
                cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH));
        tvDeadline.setText(selectedDeadline);

        tvDeadline.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            new DatePickerDialog(this, (dp, y, m, d) -> {
                selectedDeadline = String.format("%04d-%02d-%02d", y, m + 1, d);
                tvDeadline.setText(selectedDeadline);
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
        });

        View container = findViewById(R.id.goalContainer);
        container.setAlpha(0f); container.setTranslationY(40f);
        container.animate().alpha(1f).translationY(0f).setDuration(400)
                .setInterpolator(new android.view.animation.DecelerateInterpolator()).start();

        MaterialButton btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(v -> saveGoal());
    }

    private void saveGoal() {
        String name   = etGoalName.getText() != null ? etGoalName.getText().toString().trim() : "";
        String target = etTarget.getText() != null ? etTarget.getText().toString().trim() : "";
        String saved  = etSaved.getText() != null ? etSaved.getText().toString().trim() : "";

        if (name.isEmpty())   { etGoalName.setError("Goal name required"); return; }
        if (target.isEmpty()) { etTarget.setError("Target amount required"); return; }

        try {
            double targetAmt = Double.parseDouble(target);
            double savedAmt  = saved.isEmpty() ? 0 : Double.parseDouble(saved);
            String priority  = spinnerPriority.getSelectedItem().toString();
            GoalModel g = new GoalModel(UUID.randomUUID().toString(),
                    name, targetAmt, savedAmt, selectedDeadline, priority);
            dm.addGoal(g);
            Toast.makeText(this, "✓ Goal created!", Toast.LENGTH_SHORT).show();
            finish();
            overridePendingTransition(R.anim.fade_in, R.anim.slide_down_out);
        } catch (NumberFormatException e) {
            etTarget.setError("Invalid amount");
        }
    }

    @Override
    public boolean onSupportNavigateUp() { finish(); overridePendingTransition(R.anim.fade_in, R.anim.slide_down_out); return true; }
}
