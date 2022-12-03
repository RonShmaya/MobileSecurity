package com.example.mobilesecurity.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;

import android.util.Base64;

import com.example.mobilesecurity.R;
import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {
    private MaterialButton main_BTN_login;
    private MaterialButton main_BTN_enter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        initActions();
    }

    private void initActions() {
        main_BTN_login.setOnClickListener(view -> go_next(LoginActivity.class));
        main_BTN_enter.setOnClickListener(view -> go_next(RegistrationActivity.class));
    }

    private void findViews() {
        main_BTN_login = findViewById(R.id.main_BTN_login);
        main_BTN_enter = findViewById(R.id.main_BTN_enter);
    }

    private <T extends AppCompatActivity> void go_next(Class<T> nextActivity ) {
        Intent intent = new Intent(this, nextActivity);
        startActivity(intent);
        finish();
    }

}