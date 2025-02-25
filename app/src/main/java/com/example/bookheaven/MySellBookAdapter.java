package com.example.bookheaven;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.chip.Chip;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MySellBookAdapter extends RecyclerView.Adapter<MySellBookAdapter.MySellBookViewHolder>{
    private List<MySellBookModel> bookList;
    public MySellBookAdapter(List<MySellBookModel> bookList) {

        this.bookList = bookList;
    }

    @NonNull
    @Override
    public MySellBookAdapter.MySellBookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_book_item, parent, false);
        return new MySellBookViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MySellBookAdapter.MySellBookViewHolder holder, int position) {

        MySellBookModel mySellBookModel = bookList.get(position);

        holder.title.setText("Title: " + mySellBookModel.getTitle());
        holder.availableQty.setText("Available Qty: " + String.valueOf(mySellBookModel.getAvailableQty()));
        holder.sellingQty.setText("Selling Qty: " + String.valueOf(mySellBookModel.getSellingQty()));
        holder.mtPrfit.setText("Profit: Rs." + String.valueOf(mySellBookModel.getProfit()));

        Glide.with(holder.sellBookImage.getContext())
                .load(mySellBookModel.getImageUrl())
                .placeholder(R.drawable.add_img) // Placeholder image
                .error(R.drawable.error_image) // Error image
                .into(holder.sellBookImage);
//
        holder.chip5.setText(mySellBookModel.getStatus() == 1 ? "Available" : "Unavailable");
//
        holder.chip5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleBookStatus(mySellBookModel, holder);
            }
        });

    }

    private void toggleBookStatus(MySellBookModel mySellBookModel, MySellBookViewHolder holder) {
        int newStatus = mySellBookModel.getStatus() == 1 ? 2 : 1;
        mySellBookModel.setStatus(newStatus);

        holder.chip5.setText(newStatus == 1 ? "Available" : "Unavailable");

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    OkHttpClient client = new OkHttpClient();
                    Gson gson = new Gson();

                    JsonObject requestJson = new JsonObject();
                    requestJson.addProperty("book_id", mySellBookModel.getId());
                    requestJson.addProperty("status_id", newStatus);

                    RequestBody requestBody = RequestBody.create(gson.toJson(requestJson), MediaType.get("application/json"));
                    Request request = new Request.Builder()
                            .url("http://192.168.8.126:8080/BookHeaven/UpdateBookStatus")
                            .post(requestBody)
                            .build();

                    Response response = client.newCall(request).execute();
                    if (!response.isSuccessful()) {
                        throw new IOException("Unexpected code " + response);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public class MySellBookViewHolder extends RecyclerView.ViewHolder {

        ImageView sellBookImage;
        TextView title, availableQty, sellingQty, mtPrfit;
        Chip chip5;
        public MySellBookViewHolder(@NonNull View itemView) {
            super(itemView);

            sellBookImage = itemView.findViewById(R.id.imageView21);
            title = itemView.findViewById(R.id.textView58);
            availableQty = itemView.findViewById(R.id.textView67);
            sellingQty = itemView.findViewById(R.id.textView68);
            mtPrfit = itemView.findViewById(R.id.textView69);
            chip5 = itemView.findViewById(R.id.chip5);
        }
    }
}
