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

import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {

    private EditText fnameInput, lnameInput, emailInput, passwordInput, mobileInput;
    private Button signupButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView textView7 = findViewById(R.id.textView7);
        textView7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, SigninActivity.class);
                startActivity(intent);
            }
        });

        fnameInput = findViewById(R.id.editTextText4);
        lnameInput = findViewById(R.id.editTextText5);
        emailInput = findViewById(R.id.editTextTextEmailAddress);
        passwordInput = findViewById(R.id.editTextTextPassword2);
        mobileInput = findViewById(R.id.editTextText6);
        signupButton = findViewById(R.id.button3);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInputs()){
                    Toast.makeText(SignUpActivity.this, "Sign-Up Successful!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private boolean validateInputs() {
        String fname = fnameInput.getText().toString().trim();
        String lname = lnameInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        String mobile = mobileInput.getText().toString().trim();

        if (TextUtils.isEmpty(fname)){
            showError(fnameInput,"First Name is required!");
            return false;
        }else{
            resetError(fnameInput);
        }

        if (TextUtils.isEmpty(lname)){
            showError(lnameInput,"Last Name is required!");
            return false;
        }else {
            resetError(lnameInput);
        }

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

        if (TextUtils.isEmpty(mobile)){
            showError(mobileInput,"Mobile Number is required!");
            return false;
        } else if (mobile.length() != 10) {
            showError(mobileInput,"Enter a valid 10-digit mobile number");
            return false;
        }else {
            resetError(mobileInput);
        }

        return true;
    }

    private void showError(EditText inputField, String errorMessage) {
        inputField.setError(errorMessage);
//        inputField.setBackgroundResource(R.drawable.input_field_error_background);
        Animation shake = AnimationUtils.loadAnimation(this,R.anim.shake);
        inputField.startAnimation(shake);
    }

    private void resetError(EditText inputField){
        inputField.setError(null);
//        inputField.setBackgroundResource(R.drawable.input_field_background);
    }

}