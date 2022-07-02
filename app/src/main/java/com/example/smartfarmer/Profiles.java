package com.example.smartfarmer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Profiles extends AppCompatActivity {
    EditText phoneED, locationED;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profiles);
        phoneED = findViewById(R.id.phone_ed);
        locationED = findViewById(R.id.location_ed);
        
        fetchSharedPrefs();
    }

    private void fetchSharedPrefs() {
        @SuppressLint("WrongConstant") SharedPreferences sharedPreferences = getSharedPreferences("mySharedPrefs", MODE_APPEND);
        String phoneStr = sharedPreferences.getString("phone", "9876543210");
        String locationStr = sharedPreferences.getString("location", "Bathinda");
        locationED.setHint(locationStr);
        phoneED.setHint(phoneStr);
    }


    public void updateProfile(View view) {
        if (!phoneED.getText().toString().isEmpty() && !locationED.getText().toString().isEmpty()) {
            if(phoneED.getText().toString().length()!=10){
                Toast.makeText(getApplicationContext(), "Enter exact 10 digit phone number", Toast.LENGTH_SHORT).show();
            }else {
                SharedPreferences sharedPreferences;
                sharedPreferences = getSharedPreferences("mySharedPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("phone", phoneED.getText().toString());

                editor.putString("location", locationED.getText().toString());
                editor.apply();
                Intent intent = new Intent(Profiles.this, MainActivity.class);
                startActivity(intent);
            }
        } else if (phoneED.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Enter new Phone Number", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Enter new Location", Toast.LENGTH_SHORT).show();
        }

    }

    public void cancelUpdate(View view) {
        Intent intent = new Intent(Profiles.this, MainActivity.class);
        startActivity(intent);
    }
}
