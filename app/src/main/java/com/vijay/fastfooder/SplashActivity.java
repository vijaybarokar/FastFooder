package com.vijay.fastfooder;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    ImageView ivLogo;
    TextView tvTitle;

    VideoView videoView;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        tvTitle = findViewById(R.id.tvSplashTitle);
        videoView = findViewById(R.id.videoView); // Initialize videoView



        // Consider replacing video with a lightweight animation or image for better performance
        String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.burger;
        videoView.setVideoPath(videoPath);
        videoView.start();

        handler = new Handler();
        handler.postDelayed(() -> {
            Intent i = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(i);
            finish(); // Close the splash activity
        }, 2400);
    }
}