package com.burgaynet.expenziox.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.burgaynet.expenziox.R;
import com.burgaynet.expenziox.utils.DataManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class OnboardingActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);
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
