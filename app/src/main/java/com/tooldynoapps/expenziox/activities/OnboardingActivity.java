package com.tooldynoapps.expenziox.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.tooldynoapps.expenziox.R;
import com.tooldynoapps.expenziox.utils.DataManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class OnboardingActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_onboarding);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            androidx.core.graphics.Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        if (getSupportActionBar() != null) getSupportActionBar().hide();

        View container = findViewById(R.id.onboardingContainer);
        container.setAlpha(0f);
        container.setTranslationY(40f);
        container.animate().alpha(1f).translationY(0f).setDuration(500)
                .setInterpolator(new android.view.animation.DecelerateInterpolator()).start();

        TextInputEditText etName = findViewById(R.id.etName);
        MaterialButton btnStart  = findViewById(R.id.btnStart);

        btnStart.setOnClickListener(v -> {
            String name = etName.getText() != null ? etName.getText().toString().trim() : "";
            if (name.isEmpty()) {
                Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show();
                return;
            }
            DataManager dm = new DataManager(this);
            dm.setUserName(name);
            dm.setOnboarded(true);
            startActivity(new Intent(this, MainActivity.class));
            overridePendingTransition(R.anim.slide_up_in, R.anim.fade_out);
            finish();
        });
    }
}