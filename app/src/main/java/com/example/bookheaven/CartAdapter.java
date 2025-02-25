package com.example.bookheaven;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder>{

    private ArrayList<CartItem> cartItemArrayList;
    public CartAdapter(ArrayList<CartItem> getCartItemArrayList) {
        this.cartItemArrayList = getCartItemArrayList;
    }


    @NonNull
    @Override
    public CartAdapter.CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.cart_item, parent, false);
//        CartViewHolder cartViewHolder = new CartViewHolder(view);

        return new CartViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.CartViewHolder holder, int position) {

        if (position >= cartItemArrayList.size()) return;

        CartItem cartItem = cartItemArrayList.get(position);
        holder.title.setText(cartItem.getTitle());
        holder.price.setText("Rs." + (cartItem.getPrice() * cartItem.getQuantity()));
        holder.quantity.setText(String.valueOf(cartItem.getQuantity()));

        Glide.with(holder.imageUrl.getContext())
                .load(cartItem.getImageUrl())
                .placeholder(R.drawable.add_img)
                .error(R.drawable.error_image)
                .into(holder.imageUrl);

        holder.btnIncrease.setOnClickListener(v -> {
            cartItem.setQuantity(cartItem.getQuantity() + 1);
            notifyItemChanged(position);
            cartQTY(cartItem.getId(), "increase");
        });

        holder.btnDecrease.setOnClickListener(v -> {
            if (cartItem.getQuantity() > 1) {
                cartItem.setQuantity(cartItem.getQuantity() - 1);
                notifyItemChanged(position);
                cartQTY(cartItem.getId(), "descrease");
            }
        });

        holder.remove.setOnClickListener(v -> {
            cartItemArrayList.remove(position);

            notifyDataSetChanged();
            new Thread(new Runnable() {

                @Override
                public void run() {
                    OkHttpClient okHttpClient = new OkHttpClient();

                    String itemId = String.valueOf(cartItem.getId());
                    Log.i("CartAdapter-log", "item id: " + itemId);

                   HttpUrl url = HttpUrl.parse(BuildConfig.URL+"/DeleteCartItem")
                                   .newBuilder()
                                           .addQueryParameter("itemId", String.valueOf(cartItem.getId()))
                                                   .build();
                   Request request = new Request.Builder().url(url.toString()).build();

                    okHttpClient.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(@NonNull Call call, @NonNull IOException e) {
                            e.printStackTrace();
                            Log.e("CartAdapter-log", "Network request failed: " + e.getMessage());
                        }

                        @Override
                        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                            if (response.isSuccessful()) {

                                Log.i("CartAdapter-log", "Network request successful");
                            } else {
                                Log.e("CartAdapter-log", "Network request failed with code: " + response.code());
                            }

                        }
                    });

                }
            }).start();
        });

        Log.d("CartAdapter-log", "Binding item: " + cartItem.getTitle() + " | Quantity: " + cartItem.getQuantity());

    }

    @Override
    public int getItemCount() {
        return cartItemArrayList.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {
        TextView title, price, quantity;
        ImageView imageUrl, remove, btnIncrease, btnDecrease;
        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.order_name);
            price = itemView.findViewById(R.id.order_price);
            quantity = itemView.findViewById(R.id.order_date);
            imageUrl = itemView.findViewById(R.id.cart_item_image);
            remove = itemView.findViewById(R.id.cart_item_remove);
            btnIncrease = itemView.findViewById(R.id.btn_increase);
            btnDecrease = itemView.findViewById(R.id.btn_decrease);
        }
    }

    public void cartQTY (int id, String status){

        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient okHttpClient = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(BuildConfig.URL+"/GetCartQTY?status=" + status + "&id=" + id)
                        .get()
                        .build();

                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        Log.e("CartAdapter-log", "Network request failed: " + e.getMessage());
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        Log.i("CartAdapter-log", "done");
                    }
                });

            }
        }).start();

    }
}
