package com.example.bookheaven;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);

        if (isLoggedIn){
            Intent intent = new Intent(MainActivity.this, HomeActivity2.class);
            startActivity(intent);
            finish();
        }else {

            EdgeToEdge.enable(this);
            setContentView(R.layout.activity_main);
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;

            });

            Button b1 = findViewById(R.id.button);
            b1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, SigninActivity.class);
                    startActivity(intent);
                }
            });

            TextView textView1 = findViewById(R.id.textView);
            ObjectAnimator fadeIn = ObjectAnimator.ofFloat(textView1, "alpha", 0f, 1f);
            fadeIn.setDuration(3000);
            fadeIn.start();

            Animation fadeIn1 = AnimationUtils.loadAnimation(this, R.anim.fade_in);
            textView1.startAnimation(fadeIn1);

            Button button = findViewById(R.id.button);
            ObjectAnimator fadeIn2 = ObjectAnimator.ofFloat(button, "alpha", 0f, 1f);
            fadeIn2.setDuration(6000);
            fadeIn2.start();

            Animation fadeIn3 = AnimationUtils.loadAnimation(this, R.anim.fade_in);
            textView1.startAnimation(fadeIn3);

        }

    }
}