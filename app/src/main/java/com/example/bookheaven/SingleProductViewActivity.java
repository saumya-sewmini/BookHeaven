package com.example.bookheaven;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SingleProductViewActivity extends AppCompatActivity {
    private TextView bookTitleTextView, availableQtyTextView, priceTextView, descriptionTextView, shippingPriceTextView, authorTextView, bookSellerContactText;
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

        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        int userId = sharedPreferences.getInt("user_id", -1);

        //get book id
        Intent intent = getIntent();
        int bookId = intent.getIntExtra("book_id", -1);
        String bookTitle = intent.getStringExtra("book_title");
        double bookPrice = intent.getDoubleExtra("book_price", 0.0);
        String bookImage = intent.getStringExtra("book_image");
        String bookDescription = intent.getStringExtra("description");
        String bookAuthor = intent.getStringExtra("author");
        double bookShippingPrice = intent.getDoubleExtra("shipping_price", 0.0);
        String bookSellerContact = intent.getStringExtra("user_id");
        int bookQty = intent.getIntExtra("book_qty", 0);

        Log.i("BookHeaven-log-single-product-view", "book id: " + bookId);

        if (bookId != -1){
//            fetchBookDetails(bookId);
        }else {
            finish();
        }

        bookTitleTextView = findViewById(R.id.textView12);
        availableQtyTextView = findViewById(R.id.textView25);
        priceTextView =findViewById(R.id.textView24);
        shippingPriceTextView = findViewById(R.id.textView76);
        descriptionTextView = findViewById(R.id.textView13);
        bookImageView = findViewById(R.id.imageView3);
        authorTextView = findViewById(R.id.textView78);

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

        bookTitleTextView.setText(bookTitle);
        priceTextView.setText(String.format("Rs. %.2f ", bookPrice));
        descriptionTextView.setText(bookDescription);
        authorTextView.setText(bookAuthor);
        shippingPriceTextView.setText(String.valueOf(bookShippingPrice));
        availableQtyTextView.setText("Available: " + bookQty);

        Glide.with(this)
                .load(bookImage)
                .placeholder(R.drawable.add_img)
                .into(bookImageView);


    }

//    private void updateBookDetailsUI(SingleProductModel bookDetails) {
//        bookTitleTextView.setText(bookDetails.getBook_title());
//        priceTextView.setText(String.format("$%.2f", bookDetails.getPrice()));
//        descriptionTextView.setText(bookDetails.getDescription());
//        availableQtyTextView.setText("Available: " + bookDetails.getQty());
//        shippingPriceTextView.setText(String.valueOf(bookDetails.getShipping_price()));
//        authorTextView.setText(bookDetails.getAuthor());
//
//        Glide.with(SingleProductViewActivity.this)
//                .load(bookDetails.getImageUrl())
//                .placeholder(R.drawable.add_img)
//                .into(bookImageView);
//    }
}