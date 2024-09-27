package com.example.lab1;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lab1.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    Button btn1;
    private ActivityMainBinding bindingVar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bindingVar = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(bindingVar.getRoot());

        btn1=findViewById(R.id.btnHello);

        bindingVar.btnHello.setOnClickListener(view -> {

                }
            );

    }
}
