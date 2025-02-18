package com.example.bookheaven;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SingleProductViewActivity extends AppCompatActivity {
    private TextView bookTitleTextView, availableQtyTextView, priceTextView, descriptionTextView, shippingPriceTextView, authorTextView;
    private ImageView bookImageView;
    private int bookId;

    //qty
    private TextView qtyTextView;
    private int qty = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_single_product_view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //get book id
        bookId = getIntent().getIntExtra("id",-1);
        if (bookId == -1){
            finish();
            return;
        }

        bookTitleTextView = findViewById(R.id.textView12);
        availableQtyTextView = findViewById(R.id.textView25);
        priceTextView =findViewById(R.id.textView24);
        shippingPriceTextView = findViewById(R.id.textView76);
        descriptionTextView = findViewById(R.id.textView13);
        bookImageView = findViewById(R.id.imageView3);
        authorTextView = findViewById(R.id.textView78);

        fetchBookDetails(bookId);

        ImageButton backButton = findViewById(R.id.imageButton2);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        qtyTextView = findViewById(R.id.textView23);
        ImageView minusButton = findViewById(R.id.imageView5);
        ImageView plusButton = findViewById(R.id.imageView7);

        minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (qty>0){
                    qty--;
                    qtyTextView.setText(String.valueOf(qty));
                }
            }
        });

        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qty++;
                qtyTextView.setText(String.valueOf(qty));
            }
        });
    }

    private void fetchBookDetails(int bookId) {
        Log.i("BookHeaven-log", "Fetching book details for ID: " + bookId);

        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient okHttpClient = new OkHttpClient();

                Request request = new Request.Builder()
                        .url("http://192.168.8.126:8080/BookHeaven/SingleProductView?bookId=" + bookId)
                        .get()
                        .build();

                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(SingleProductViewActivity.this, "Failed to fetch book details", Toast.LENGTH_SHORT).show();
                                Log.e("BookHeaven-log", "Network request failed: " + e.getMessage());
                            }
                        });
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        if (response.isSuccessful() && response.body() != null){
                            String jsonResponse = response.body().string();
                            Log.i("BookHeaven-log", "Raw JSON Response: " + jsonResponse);

                            try {
                                JSONObject jsonObject = new JSONObject(jsonResponse);
                                boolean success = jsonObject.getBoolean("success");

                                if (success){
                                    JSONObject bookObj = jsonObject.getJSONObject("content");

                                    int id = bookObj.getInt("id");
                                    String title = bookObj.getString("book_title");
                                    String description = bookObj.getString("description");
                                    double price = bookObj.getDouble("price");
                                    int qty = bookObj.getInt("available_quantity");
                                    double shippingPrice = bookObj.getDouble("shipping_price");
                                    String author = bookObj.getString("author");
                                    String sellerContact = bookObj.getString("sellerContact");
                                    String imageUrl = bookObj.getString("imageUrl");

                                    Log.i("BookHeaven-log", "Fetched book: " + title);

                                    SingleProductModel bookDetails = new SingleProductModel(id, title, description, price, qty, shippingPrice, author, sellerContact, imageUrl);

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            updateBookDetailsUI(bookDetails);
                                        }
                                    });

                                }else {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(SingleProductViewActivity.this, "Book details not found", Toast.LENGTH_SHORT).show();
                                            Log.w("BookHeaven-log", "No book details found");
                                        }
                                    });
                                }

                            }catch (JSONException e){
                                e.printStackTrace();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(SingleProductViewActivity.this, "Error parsing JSON", Toast.LENGTH_SHORT).show();
                                        Log.e("BookHeaven-log", "Error parsing JSON: " + e.getMessage());
                                    }
                                });
                            }
                        }else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(SingleProductViewActivity.this, "Response unsuccessful", Toast.LENGTH_SHORT).show();
                                    Log.e("BookHeaven-log", "Unsuccessful response: " + response.code());
                                }
                            });
                        }
                    }
                });
            }
        }).start();

    }

    private void updateBookDetailsUI(SingleProductModel bookDetails) {
        bookTitleTextView.setText(bookDetails.getBook_title());
        priceTextView.setText(String.format("$%.2f", bookDetails.getPrice()));
        descriptionTextView.setText(bookDetails.getDescription());
        availableQtyTextView.setText("Available: " + bookDetails.getQty());
        shippingPriceTextView.setText(String.valueOf(bookDetails.getShipping_price()));
        authorTextView.setText(bookDetails.getAuthor());

        Glide.with(SingleProductViewActivity.this)
                .load(bookDetails.getImageUrl())
                .placeholder(R.drawable.add_img)
                .into(bookImageView);
    }
}