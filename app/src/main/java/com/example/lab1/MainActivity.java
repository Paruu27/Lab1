package com.example.lab1;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lab1.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding bindingVar;
    ImageView imgView;
    Switch sw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Using the view binding
        bindingVar = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(bindingVar.getRoot());

        // Finding the views by their IDs
        imgView = findViewById(R.id.imageView2);  // Flag ImageView
        sw = findViewById(R.id.switch1);      // Switch for flag rotation

        // Set the switch listener
        sw.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // Rotate flag if switch is ON
                RotateAnimation rotate = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                rotate.setDuration(5000);  // 5 seconds for one full rotation
                rotate.setRepeatCount(Animation.INFINITE);  // Infinite rotation
                rotate.setInterpolator(new LinearInterpolator());  // Smooth rotation
                imgView.startAnimation(rotate);
            } else {
                // Stop rotating the flag if switch is OFF
                imgView.clearAnimation();
            }
        });
    }
}
