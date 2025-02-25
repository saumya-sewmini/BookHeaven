package com.example.bookheaven;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddBookActivity extends AppCompatActivity {

    //img loading
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;
    private String uploadedImageUrl = "";

    //author loding
    private AutoCompleteTextView authorInput;
    private ArrayAdapter<String> authorAdapter;
    private ArrayList<String> authorList = new ArrayList<>();

    //catergory loding
    private Spinner categorySpinner;
    private ArrayAdapter<String> categoryAdapter;
    private ArrayList<String> categoryList = new ArrayList<>();

    //qty
    private TextView quantityTextView;
    private int quantity = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_book);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        int userId = sharedPreferences.getInt("user_id", -1);

        Button myBook = findViewById(R.id.button10);
        myBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddBookActivity.this, MyBookActivity.class);
                startActivity(intent);
            }
        });

        authorInput = findViewById(R.id.autoCompleteTextView);
        loadAuthorsFromDatabase();

        categorySpinner = findViewById(R.id.spinner);
        loadCategoriesFromDatabase();

        ImageButton imageButton3 = findViewById(R.id.imageButton3);
        imageButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ImageView imageView6 = findViewById(R.id.imageView6);
        imageView6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        quantityTextView = findViewById(R.id.textView33);
        ImageView minusButton = findViewById(R.id.imageView8);
        ImageView plusButton = findViewById(R.id.imageView9);

        minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantity>0){
                    quantity--;
                    quantityTextView.setText(String.valueOf(quantity));
                }
            }
        });

        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity++;
                quantityTextView.setText(String.valueOf(quantity));
            }
        });

        Button button6 = findViewById(R.id.button6);
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (imageUri == null) {
                    Toast.makeText(AddBookActivity.this, "Select an image first!", Toast.LENGTH_SHORT).show();
                    return;
                }

                uploadImageToFirebase(new ImageUploadCallback() {
                    @Override
                    public void onUploadSuccess(String imageUrl) {
                        saveBook(imageUrl);
                    }

                    @Override
                    public void onUploadFailure() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(AddBookActivity.this, "Image upload failed!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        });
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            ImageView imageView6 = findViewById(R.id.imageView6);
            imageView6.setImageURI(imageUri);
        }
    }

    interface ImageUploadCallback {
        void onUploadSuccess(String imageUrl);
        void onUploadFailure();
    }

    private void uploadImageToFirebase(ImageUploadCallback callback) {
        if (imageUri != null) {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference().child("images/" + System.currentTimeMillis() + ".jpg");

            storageRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        uploadedImageUrl = uri.toString();  // Save the image URL from Firebase
                        Log.d("Firebase-log", "Image URL: " + uploadedImageUrl);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(AddBookActivity.this, "Image Uploaded!", Toast.LENGTH_SHORT).show();
                            }
                        });

                        callback.onUploadSuccess(uploadedImageUrl);
                    }).addOnFailureListener(e -> {
                        Log.e("Firebase-log", "Failed to get download URL", e);
                        callback.onUploadFailure();
                    }))
                    .addOnFailureListener(e -> {
                        Log.e("Firebase-log", "Upload failed", e);
                        callback.onUploadFailure();
                    });

        } else {
            Toast.makeText(this, "No Image Selected!", Toast.LENGTH_SHORT).show();
            callback.onUploadFailure();
        }
    }

    private void saveBook(String uploadedImageUrl) {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        int userId = sharedPreferences.getInt("user_id", -1);

        EditText bookNameInput = findViewById(R.id.editTextText2);
        EditText descriptionInput = findViewById(R.id.editTextTextMultiLine);
        EditText priceInput = findViewById(R.id.editTextText7);
        EditText shippingInput = findViewById(R.id.editTextText8);
        AutoCompleteTextView authorInput = findViewById(R.id.autoCompleteTextView);
        Spinner categorySpinner = findViewById(R.id.spinner);
        TextView qtyInput = findViewById(R.id.textView33);
        EditText latitudeInput = findViewById(R.id.editTextText23);
        EditText longitudeInput = findViewById(R.id.editTextText24);

        String bookTitle = bookNameInput.getText().toString();
        String description = descriptionInput.getText().toString();
        String priceText = priceInput.getText().toString();
        String shippingText = shippingInput.getText().toString();
        String author = authorInput.getText().toString();
        String category = categorySpinner.getSelectedItem().toString();
        String qty = qtyInput.getText().toString();
        String latitude = latitudeInput.getText().toString();
        String longitude = longitudeInput.getText().toString();

        if (bookTitle.isEmpty()){
            Toast.makeText(AddBookActivity.this, "Book title is required!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (description.isEmpty()){
            Toast.makeText(AddBookActivity.this, "Description is required!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (priceText.isEmpty()){
            Toast.makeText(AddBookActivity.this, "Price is required!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (shippingText.isEmpty()){
            Toast.makeText(AddBookActivity.this, "Shipping cost is required!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (author.isEmpty()){
            Toast.makeText(AddBookActivity.this, "Author is required!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (category.isEmpty()){
            Toast.makeText(AddBookActivity.this, "Category is required!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (qty.isEmpty()){
            Toast.makeText(AddBookActivity.this, "Quantity is required!", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                Gson gson = new Gson();
                JsonObject addBook = new JsonObject();

                addBook.addProperty("title", bookTitle);
                addBook.addProperty("description", description);
                addBook.addProperty("price", priceText);
                addBook.addProperty("shipping_cost", shippingText);
                addBook.addProperty("author", author);
                addBook.addProperty("category", category);
                addBook.addProperty("quantity", qty);
                addBook.addProperty("image_url", uploadedImageUrl);
                addBook.addProperty("user_id", userId);
                addBook.addProperty("latitude", latitude);
                addBook.addProperty("longitude", longitude);

                Log.i("BookHeaven-log", " "+ bookTitle);
                Log.i("BookHeaven-log", " "+ description);
                Log.i("BookHeaven-log", " "+ priceText);
                Log.i("BookHeaven-log", " "+ shippingText);
                Log.i("BookHeaven-log", " "+ author);
                Log.i("BookHeaven-log", " "+ category);
                Log.i("BookHeaven-log", " "+ qty);
                Log.i("BookHeaven-log", " "+ uploadedImageUrl);
                Log.i("BookHeaven-log", " "+ userId);
                Log.i("BookHeaven-log", " "+ latitude);
                Log.i("BookHeaven-log", " "+ longitude);

                Log.i("BookHeaven-log", "success-data parse the frontend (address)");
                OkHttpClient okHttpClient = new OkHttpClient();
                // Prepare the request
                RequestBody requestBody = RequestBody.create(gson.toJson(addBook), MediaType.get("application/json"));
                Request request = new Request.Builder()
                        .url(BuildConfig.URL+"/SaveBook")
                        .post(requestBody)
                        .build();

                try {
                    Response response = okHttpClient.newCall(request).execute();

                    String responseText = response.body().string();

                    Log.i("BookHeaven-logggg", " "+ responseText);
                    JSONObject jsonResponse = new JSONObject(responseText);

                    Log.i("BookHeaven-log", "success-data parse the backend (book)");
                    // Handle success or failure
                    if (jsonResponse.getBoolean("success")) {

                        runOnUiThread(() -> {
                            Toast.makeText(AddBookActivity.this, "Book Updated Successfully!", Toast.LENGTH_SHORT).show();
                            Log.i("BookHeaven-log", "success-data update book");
                        });
                    } else {
                        runOnUiThread(() -> {
                            Toast.makeText(AddBookActivity.this, "Failed to Update Book!", Toast.LENGTH_SHORT).show();
                            Log.e("BookHeaven-log", "failed-data update Book");
                        });
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    private void loadAuthorsFromDatabase() {
        new Thread(() -> {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(BuildConfig.URL+"/GetAuthors")
                    .get()
                    .build();
            try {
                Response response = client.newCall(request).execute();
                String responseText = response.body().string();
                JSONObject jsonResponse = new JSONObject(responseText);
                Log.i("BookHeaven-log", responseText);

                if (jsonResponse.getBoolean("success")) {
                    JSONArray jsonArray = jsonResponse.getJSONArray("content");
                    authorList.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        authorList.add(jsonArray.getJSONObject(i).getString("name"));
                    }
                    runOnUiThread(() -> {
                        authorAdapter = new ArrayAdapter<>(AddBookActivity.this, android.R.layout.simple_dropdown_item_1line, authorList);
                        authorInput.setAdapter(authorAdapter);
                        authorInput.setThreshold(1);
                    });
                }

            } catch (IOException | JSONException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    private void loadCategoriesFromDatabase(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient okHttpClient=new OkHttpClient();
                Request request = new Request.Builder()
                        .url(BuildConfig.URL+"/GetCatergories")
                        .get()
                        .build();
                try {
                    Response response = okHttpClient.newCall(request).execute();
                    String responseText = response.body().string();
                    JSONObject jsonResponse = new JSONObject(responseText);
                    //Log.i("BookHeaven-log", "success1");

                    if (jsonResponse.getBoolean("success")) {
                        //Log.i("BookHeaven-log", "success2");
                        JSONArray jsonArray = jsonResponse.getJSONArray("content");
                        categoryList.clear();
                        //Log.i("BookHeaven-log", "success3");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            categoryList.add(jsonArray.getJSONObject(i).getString("catergory"));
                            //Log.i("BookHeaven-log", "success4");
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                categoryAdapter = new ArrayAdapter<>(AddBookActivity.this, android.R.layout.simple_spinner_dropdown_item, categoryList);
                                categorySpinner.setAdapter(categoryAdapter);
                                //Log.i("BookHeaven-log", "success5");
                            }
                        });
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

}