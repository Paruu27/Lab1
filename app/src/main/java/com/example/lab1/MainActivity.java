package com.example.lab1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private EditText emailEditText;
    private EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        Button loginButton = findViewById(R.id.loginButton);

        // Load email from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String savedEmail = sharedPreferences.getString("Email", "");
        emailEditText.setText(savedEmail);

        Log.w(TAG, "In onCreate() - Loading Widgets");

        loginButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            // Save email to SharedPreferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("Email", email);
            editor.apply();

            Intent nextPage = new Intent(MainActivity.this, SecondActivity.class);
            nextPage.putExtra("EmailAddress", email);
            startActivity(nextPage);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.w(TAG, "In onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.w(TAG, "In onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.w(TAG, "In onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.w(TAG, "In onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.w(TAG, "In onDestroy()");
    }
}
