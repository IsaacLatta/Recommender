package com.example.recommender;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.recommender.network.api.API;
import com.example.recommender.network.service.AuthService;
import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {
    Controller controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.controller = Controller.getInstance();

        Log.d("LOGIN_INFO", "Auth Service and Controller Created!");
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void buttonHandler(View view) {
        TextInputEditText usr = findViewById(R.id.userName_text);
        TextInputEditText pass = findViewById(R.id.password_text);
        String username_string = usr.getText().toString();
        String password_string = pass.getText().toString();
        controller.login(username_string, password_string);
        Intent intent = new Intent(this, MainScreen.class);
        startActivity(intent);
    }
}
