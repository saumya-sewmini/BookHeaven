package com.example.bookheaven;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ProfileUpdateActivity extends AppCompatActivity {

    List<Integer> districtIds = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile_update);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        int userId = sharedPreferences.getInt("user_id", -1);
        String userEmail = sharedPreferences.getString("user_email", "");
        String userFname = sharedPreferences.getString("user_fname", "");
        String userLname = sharedPreferences.getString("user_lname", "");
        String userMobile = sharedPreferences.getString("user_mobile", "");

        if (userId == -1){
            Toast.makeText(this, "User not logged in!", Toast.LENGTH_SHORT).show();
            Log.e("BookHeaven-log", "ERROR: user_id is NULL in AddBookActivity");
            finish();
            return;
        }



        ImageButton backButton = findViewById(R.id.imageButton4);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        EditText emailInput = findViewById(R.id.editTextTextEmailAddress2);
        EditText firstNameInput = findViewById(R.id.editTextText14);
        EditText lastNameInput = findViewById(R.id.editTextText15);
        EditText mobileInput = findViewById(R.id.editTextText21);
        Button button = findViewById(R.id.button9);
        Spinner districtInput = findViewById(R.id.spinner3);

//        loadDistrictToSpinner();

        emailInput.setText(userEmail);
        firstNameInput.setText(userFname);
        lastNameInput.setText(userLname);
        mobileInput.setText(userMobile);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserProfile(userId, userEmail);
                Log.i("BookHeaven-log1"," "+userId);
                updateUserAddress(userId);
            }
        });

        Spinner spinner3 = findViewById(R.id.spinner3);

        String distrct[] = new String[]{"Kalutara","Gampaha","Colombo"};

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,distrct);

        spinner3.setAdapter(arrayAdapter);

    }

    private void updateUserAddress(int userId) {

//        Log.i("BookHeaven-log1"," "+userId);

        EditText addressLine1Input = findViewById(R.id.editTextText16);
        EditText addressLine2Input = findViewById(R.id.editTextText17);
        EditText streetInput = findViewById(R.id.editTextText18);
        EditText postalCodeInput = findViewById(R.id.editTextText19);
        Spinner districtInput = findViewById(R.id.spinner3);

        districtInput.getSelectedItem();

        String districtName = districtInput.getSelectedItem().toString();
        Log.i("BookHeaven-log", "District Selected: " + districtName);

        String addressLine1 = addressLine1Input.getText().toString();
        String addressLine2 = addressLine2Input.getText().toString();
        String street = streetInput.getText().toString();
        String postalCodeStr = postalCodeInput.getText().toString();


        if (addressLine1.isEmpty()){
            Toast.makeText(ProfileUpdateActivity.this, "Address line 1 is required!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (addressLine2.isEmpty()){
            Toast.makeText(ProfileUpdateActivity.this, "Address line 2 is required!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (street.isEmpty()){
            Toast.makeText(ProfileUpdateActivity.this, "Street is required!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (postalCodeStr.isEmpty()){
            Toast.makeText(ProfileUpdateActivity.this, "Postal code is required!", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {

                    // Prepare JSON data
                    Gson gson = new Gson();
                    JsonObject addressObject = new JsonObject();

                    addressObject.addProperty("user_id", String.valueOf(userId));
                    addressObject.addProperty("line1", addressLine1);
                    addressObject.addProperty("line2", addressLine2);
                    addressObject.addProperty("street", street);
                    addressObject.addProperty("district", districtName);
                    addressObject.addProperty("postal_code", postalCodeStr);

                    Log.i("BookHeaven-log", " "+ userId);
                    Log.i("BookHeaven-log", " "+ addressLine1);
                    Log.i("BookHeaven-log", " "+ addressLine2);
                    Log.i("BookHeaven-log", " "+ street);
                    Log.i("BookHeaven-log", " "+ districtName);
                    Log.i("BookHeaven-log", " "+ postalCodeStr);

                    Log.i("BookHeaven-log", "success-data parse the frontend (address)");
                    OkHttpClient okHttpClient = new OkHttpClient();
                    // Prepare the request
                    RequestBody requestBody = RequestBody.create(gson.toJson(addressObject), MediaType.get("application/json"));
                    Request request = new Request.Builder()
                            .url(BuildConfig.URL+"/UpdateUserAddress")
                            .post(requestBody)
                            .build();
                try {
                    Response response = okHttpClient.newCall(request).execute();

                    String responseText = response.body().string();

                    Log.i("BookHeaven-logggg", " "+ responseText);
                    JSONObject jsonResponse = new JSONObject(responseText);

                    Log.i("BookHeaven-log", "success-data parse the backend (address)");

                    // Handle success or failure
                    if (jsonResponse.getBoolean("success")) {
                        Log.i("BookHeaven-log"," "+userId);
                        runOnUiThread(() -> {
                            Toast.makeText(ProfileUpdateActivity.this, "Address Updated Successfully!", Toast.LENGTH_SHORT).show();
                            Log.i("BookHeaven-log", "success-data update address");
                        });
                    } else {
                        runOnUiThread(() -> {
                            Toast.makeText(ProfileUpdateActivity.this, "Failed to Update Address!", Toast.LENGTH_SHORT).show();
                            Log.e("BookHeaven-log", "failed-data update address");
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(() -> {
                        Toast.makeText(ProfileUpdateActivity.this, "Address update failed! Check your connection.", Toast.LENGTH_SHORT).show();
                        Log.e("BookHeaven-log", "failed-data update address (exception)");
                    });
                }
            }
        }).start();

    }

    private void updateUserProfile(int userId, String userEmail) {

        EditText firstNameInput = findViewById(R.id.editTextText14);
        EditText lastNameInput = findViewById(R.id.editTextText15);
        EditText mobileInput = findViewById(R.id.editTextText21);
        Button button = findViewById(R.id.button9);

        String newFname = firstNameInput.getText().toString().trim();
        String newLname = lastNameInput.getText().toString().trim();
        String newMobile = mobileInput.getText().toString().trim();

        if (newFname.isEmpty()) {
            Toast.makeText(this, "First name is required!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (newLname.isEmpty()) {
            Toast.makeText(this, "Last name is required!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (newMobile.isEmpty()) {
            Toast.makeText(this, "Mobile number is required!", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Gson gson = new Gson();
                    JsonObject userObject = new JsonObject();
                    userObject.addProperty("id", userId);
                    userObject.addProperty("email", userEmail);
                    userObject.addProperty("first_name", newFname);
                    userObject.addProperty("last_name", newLname);
                    userObject.addProperty("mobile", newMobile);

                    Log.i("BookHeaven-log", "success-data parse the frontend");

                    RequestBody requestBody = RequestBody.create(gson.toJson(userObject), MediaType.get("application/json"));
                    Request request = new Request.Builder()
                            .url(BuildConfig.URL+"/UpdateUserProfile")
                            .post(requestBody)
                            .build();

                    OkHttpClient okHttpClient = new OkHttpClient();

                    Response response = okHttpClient.newCall(request).execute();
                    String responseText = response.body().string();
                    JSONObject jsonResponse = new JSONObject(responseText);

                    Log.i("BookHeaven-log", "success-data parse the backend");

                    if (jsonResponse.getBoolean("success")) {
                        runOnUiThread(() -> {
                            Toast.makeText(ProfileUpdateActivity.this, "Profile Updated!", Toast.LENGTH_SHORT).show();
                            Log.i("BookHeaven-log", "success-data update profile");

                            SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("user_fname", newFname);
                            editor.putString("user_lname", newLname);
                            editor.putString("user_mobile", newMobile);
                            editor.apply();

                            firstNameInput.setText(newFname);
                            lastNameInput.setText(newLname);
                            mobileInput.setText(newMobile);

                            button.setEnabled(false);
                            button.setAlpha(0.5f);
                        });
                    } else {
                        runOnUiThread(() -> {
                            Toast.makeText(ProfileUpdateActivity.this, "Failed to Update!", Toast.LENGTH_SHORT).show();
                            Log.e("BookHeaven-log", "failed-data update profile");
                        });
                    }

                }catch (IOException | org.json.JSONException e) {
                    e.printStackTrace();
                    runOnUiThread(() -> {
                        Toast.makeText(ProfileUpdateActivity.this, "Update failed! Check your connection.", Toast.LENGTH_SHORT).show();
                        Log.e("BookHeaven-log", "failed-data update profile");
                    });
                }
            }
        }).start();

    }
}