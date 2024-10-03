package com.example.lab1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class SecondActivity extends AppCompatActivity {

    private static final String TAG = "SecondActivity";
    private EditText phoneNumberEditText;
    private ImageView profileImageView;

    // Define an ActivityResultLauncher for the camera intent
    private ActivityResultLauncher<Intent> cameraResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Intent fromPrevious = getIntent();

        phoneNumberEditText = findViewById(R.id.phoneNumberEditText);
        Button callButton = findViewById(R.id.callButton);
        Button changePictureButton = findViewById(R.id.changePictureButton);
        profileImageView = findViewById(R.id.profileImageView);

        // Load email from Intent
        fromPrevious = getIntent();
        String emailAddress = fromPrevious.getStringExtra("EmailAddress");
        TextView welcomeTextView = findViewById(R.id.welcomeTextView);
        welcomeTextView.setText(String.format("Welcome back %s", emailAddress));

        // Load phone number from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String savedPhoneNumber = sharedPreferences.getString("PhoneNumber", "");
        phoneNumberEditText.setText(savedPhoneNumber); // Set the phone number in the EditText

        // Load saved profile image
        loadProfileImage();

        // Initialize the ActivityResultLauncher for camera
        cameraResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Bitmap thumbnail = (Bitmap) result.getData().getExtras().get("data");
                        if (thumbnail != null) {
                            profileImageView.setImageBitmap(thumbnail);
                            saveBitmapToDisk(thumbnail);
                        }
                    }
                }
        );

        // Handle "Call number" button click
        callButton.setOnClickListener(v -> {
            String phoneNumber = phoneNumberEditText.getText().toString();
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:" + phoneNumber));
            startActivity(callIntent);
        });

        // Handle "Change picture" button click
        changePictureButton.setOnClickListener(v -> {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraResultLauncher.launch(cameraIntent);
        });
    }

    private void loadProfileImage() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String imagePath = sharedPreferences.getString("ProfileImagePath", null);
        if (imagePath != null) {
            File imgFile = new File(imagePath);
            if (imgFile.exists()) {
                Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                profileImageView.setImageBitmap(bitmap);
            }
        }
    }

    private void saveBitmapToDisk(Bitmap bitmap) {
        File imageFile = new File(getExternalFilesDir(null), "profile_image.png");
        try (FileOutputStream fos = new FileOutputStream(imageFile)) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            // Save the image path in SharedPreferences
            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("ProfileImagePath", imageFile.getAbsolutePath());
            editor.apply();
        } catch (IOException e) {
            Log.e(TAG, "Error saving image", e);
        }
    }
}
