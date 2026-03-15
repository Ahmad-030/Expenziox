package com.burgaynet.expenziox.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import androidx.appcompat.app.AppCompatActivity;

import com.burgaynet.expenziox.R;
import com.burgaynet.expenziox.utils.DataManager;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        View logo     = findViewById(R.id.tvLogo);
        View appName  = findViewById(R.id.tvAppName);
        View tagline  = findViewById(R.id.tvTagline);
        View glowRing = findViewById(R.id.glowRing);

        logo.setScaleX(0f); logo.setScaleY(0f); logo.setAlpha(0f);
        appName.setAlpha(0f); appName.setTranslationY(30f);
        tagline.setAlpha(0f); glowRing.setAlpha(0f);

        logo.animate().scaleX(1f).scaleY(1f).alpha(1f).setDuration(600)
                .setInterpolator(new OvershootInterpolator(1.2f)).start();
        glowRing.animate().alpha(0.5f).setStartDelay(200).setDuration(800).start();
        appName.animate().alpha(1f).translationY(0f).setStartDelay(400).setDuration(500).start();
        tagline.animate().alpha(1f).setStartDelay(650).setDuration(500).start();

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            DataManager dm = new DataManager(this);
            Class<?> next = dm.isOnboarded() ? MainActivity.class : OnboardingActivity.class;
            startActivity(new Intent(this, next));
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        }, 2400);
    }
}
