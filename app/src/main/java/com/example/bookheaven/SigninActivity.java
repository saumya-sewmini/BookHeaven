package com.example.bookheaven;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SigninActivity extends AppCompatActivity {

    private EditText emailInput, passwordInput;
    private Button signinButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signin);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView textView4 = findViewById(R.id.textView4);
        textView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SigninActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        emailInput = findViewById(R.id.editTextTextEmailAddress3);
        passwordInput = findViewById(R.id.editTextTextPassword);
        signinButton = findViewById(R.id.button2);

        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInputs()){

                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            Gson gson = new Gson();
                            JsonObject user = new JsonObject();
                            user.addProperty("email", emailInput.getText().toString());
                            user.addProperty("password", passwordInput.getText().toString());

                            OkHttpClient okHttpClient = new OkHttpClient();

                            RequestBody requestBody = RequestBody.create(gson.toJson(user), MediaType.get("application/json"));
                            Request request = new Request.Builder()
                                    .url("http://192.168.8.126:8080/BookHeaven/Signin")
                                    .post(requestBody)
                                    .build();

                            try {
                                Response response = okHttpClient.newCall(request).execute();
                                String responseText = response.body().string();

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (responseText.contains("Login Successful")) {
                                            Toast.makeText(SigninActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(SigninActivity.this, HomeActivity2.class);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            Toast.makeText(SigninActivity.this, "Invalid email or password!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();

                }
            }
        });
    }

    private boolean validateInputs() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        if (TextUtils.isEmpty(email)){
            showError(emailInput,"Email is required!");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showError(emailInput,"Invalid Email!");
            return false;
        }else {
            resetError(emailInput);
        }

        if (TextUtils.isEmpty(password)){
            showError(passwordInput,"Password is required!");
            return false;
        } else if (password.length() < 8) {
            showError(passwordInput,"Password must be at least 8 characters long");
            return false;
        }else {
            resetError(passwordInput);
        }

        return true;
    }

    private void showError(EditText inputField, String errorMessage) {
        inputField.setError(errorMessage);
        Animation shake = AnimationUtils.loadAnimation(this,R.anim.shake);
        inputField.startAnimation(shake);
    }

    private void resetError(EditText inputField) {
        inputField.setError(null);
    }
}