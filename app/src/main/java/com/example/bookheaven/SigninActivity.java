package com.example.bookheaven;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SigninActivity extends AppCompatActivity {

    private EditText emailInput, passwordInput;
    private Button signinButton;
    private SharedPreferences sharedPreferences;

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

        sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);

        if (sharedPreferences.getBoolean("isLoggedIn", false)) {
            navigateToHome();
        }

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

                    loginUser();

                }
            }
        });
    }

    private void loginUser(){
        new Thread(new Runnable() {
            @Override
            public void run() {

                Gson gson = new Gson();
                JsonObject user = new JsonObject();
                user.addProperty("email", emailInput.getText().toString().trim());
                user.addProperty("password", passwordInput.getText().toString().trim());


                OkHttpClient okHttpClient = new OkHttpClient();
                RequestBody requestBody = RequestBody.create(gson.toJson(user), MediaType.get("application/json"));
                Request request = new Request.Builder()
                        .url("http://192.168.8.126:8080/BookHeaven/Signin")
                        .post(requestBody)
                        .build();

                Log.i("BookHeaven-log", "success-data parse the signin backend");

                try {

                    Response response = okHttpClient.newCall(request).execute();
                    String responseText = response.body().string();

                    if (responseText == null || responseText.isEmpty()) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(SigninActivity.this, "Server Error: Empty Response", Toast.LENGTH_SHORT).show();
                            }
                        });
                        return;
                    }

                    JSONObject jsonResponse;
                    try {
                        jsonResponse = new JSONObject(responseText);
                    } catch (JSONException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(SigninActivity.this, "Invalid server response!", Toast.LENGTH_SHORT).show();
                            }
                        });
                        return;
                    }

                    boolean success = jsonResponse.optBoolean("success", false);

                    JSONObject content = jsonResponse.optJSONObject("content");

                    int userId = (content != null) ? content.optInt("user_id", -1) : -1;
                    String userEmail = (content != null) ? content.optString("user_email",""):"";
                    String userFname = (content != null) ? content.optString("user_fname",""):"";
                    String userLname = (content != null) ? content.optString("user_lname",""):"";
                    String userName = userFname + " " + userLname;
                    String userMobile = (content != null) ? content.optString("user_mobile",""):"";

                    Log.i("BookHeaven-log", "success-data parse the frontend");

                   runOnUiThread(new Runnable() {
                       @Override
                       public void run() {

                           Log.i("BookHeaven-log", "success runOnUiThread");

                           if (success && userId != -1) {
                               Toast.makeText(SigninActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                               Log.i("BookHeaven-log", "User ID received: " + userId);
                               Log.i("BookHeaven-log", "User Email received: " + userEmail);
                               Log.i("BookHeaven-log", "User Name received: " + userName);
                               Log.i("BookHeaven-log", "User Mobile received: " + userMobile);

                               Log.i("BookHeaven-log", "success if");

                               SharedPreferences.Editor editor = sharedPreferences.edit();
                               editor.putBoolean("isLoggedIn", true);
                               editor.putInt("user_id", userId);
                               editor.putString("user_email", userEmail);
                               editor.putString("user_fname", userFname);
                               editor.putString("user_lname", userLname);
                               editor.putString("user_name",userName);
                               editor.putString("user_mobile",userMobile);
                               editor.apply();

                               Log.i("BookHeaven-log", "User ID stored: " + userId);
                               Log.i("BookHeaven-log", "User Email stored: " + userEmail);
                               Log.i("BookHeaven-log", "User Name stored: " + userName);
                               Log.i("BookHeaven-log", "User Mobile stored: " + userMobile);

                               navigateToHome();
                           } else {
                               Toast.makeText(SigninActivity.this, "Invalid email or password!", Toast.LENGTH_SHORT).show();
                           }

                       }
                   });

                } catch (IOException e) {

                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(SigninActivity.this, "Network error. Try again!", Toast.LENGTH_SHORT).show();
                        }
                    });

                }

            }
        }).start();
    }

    private void navigateToHome() {
        Intent intent = new Intent(SigninActivity.this, HomeActivity2.class);
        startActivity(intent);
        finish();
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