package com.example.lab1;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    Button btn1, btn2;
    TextView txt1;
    CheckBox checkBox;
    RadioButton radioButton;
    Switch switch1;
    ImageButton imageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn1 = findViewById(R.id.btnHello);
        btn2 = findViewById(R.id.butChange);
        txt1 = findViewById(R.id.txtName);
        checkBox = findViewById(R.id.checkBox);
        radioButton = findViewById(R.id.radioButton);
        switch1 = findViewById(R.id.switch1);
        imageButton = findViewById(R.id.imageButton);

        btn1.setOnClickListener(view -> {
            CharSequence text = "Hello toast from Parvathi";
            Toast.makeText(MainActivity.this, text, Toast.LENGTH_LONG).show();
        });

        btn2.setOnClickListener(view -> {
            String currentText = txt1.getText().toString();
            if (!currentText.contains("Nair Parvathi")) {
                txt1.setText(currentText + " Nair Parvathi");
            }
        });

        checkBox.setOnClickListener(view -> {
            if (checkBox.isChecked()) {
                Toast.makeText(MainActivity.this, "Checkbox is checked!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Checkbox is unchecked!", Toast.LENGTH_SHORT).show();
            }
        });

        radioButton.setOnClickListener(view -> {
            if (radioButton.isChecked()) {
                Toast.makeText(MainActivity.this, "Radio button is selected!", Toast.LENGTH_SHORT).show();
            }
        });

        switch1.setOnClickListener(view -> {
            if (switch1.isChecked()) {
                Toast.makeText(MainActivity.this, "Switch is ON!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Switch is OFF!", Toast.LENGTH_SHORT).show();
            }
        });

        imageButton.setOnClickListener(view -> {
            int width = imageButton.getWidth();
            int height = imageButton.getHeight();
            String message = "ImageButton width: " + width + "px, height: " + height + "px";
            Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
        });
    }
}
