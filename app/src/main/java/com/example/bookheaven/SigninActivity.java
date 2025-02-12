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
                    Toast.makeText(SigninActivity.this, "Sign-In Successful!", Toast.LENGTH_SHORT).show();
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