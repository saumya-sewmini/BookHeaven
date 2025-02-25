package com.example.bookheaven;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OrderHistoryActivity extends AppCompatActivity {
    private RecyclerView orderHistoryRecyclerView;
    private OrderHistoryAdapter adapter;
    private List<OrderModel> orders;
    private int userId;
    private SQLiteHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_history);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageButton imageButton1 = findViewById(R.id.imageButton1);
        imageButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        orderHistoryRecyclerView = findViewById(R.id.order_recyclerView);
        orderHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        databaseHelper = new SQLiteHelper(this, "orderHistory.db", null, 1);

        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        int userId = sharedPreferences.getInt("user_id", -1);

        if (userId != -1){
            if (isOnline()){
                orderHistrory(userId);
            }else {
                loadOfflineOrders();
            }
        }
    }

    private void loadOfflineOrders() {
        List<OrderModel> offlineOrders = databaseHelper.getOrders(userId);
        if (offlineOrders.isEmpty()){
            Toast.makeText(this,"No offline orders found", Toast.LENGTH_SHORT).show();
        }else {
            adapter = new OrderHistoryAdapter(offlineOrders);
            orderHistoryRecyclerView.setAdapter(adapter);
        }
    }

    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null){
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            return  networkInfo != null && networkInfo.isConnected();
        }
        return false;
    }

    private void orderHistrory(int userId) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                Gson gson = new Gson();
                OkHttpClient okHttpClient = new OkHttpClient();

                JsonObject cart = new JsonObject();
                cart.addProperty("user_id", String.valueOf(userId));

                RequestBody requestBody = RequestBody.create(gson.toJson(cart), MediaType.get("application/json"));
                Request request = new Request.Builder()
                        .url(BuildConfig.URL+"/GetOrder")
                        .post(requestBody)
                        .build();

                try {
                    Response response = okHttpClient.newCall(request).execute();
                    String responseText = response.body().string();
                    Log.i("order-log", "Response: " + responseText);

                    if (response.isSuccessful()){
                        Log.i("order-loggg", "Response: " + responseText);
                        ResponseDTO responseDTO = gson.fromJson(responseText, ResponseDTO.class);

                        List<OrderModel> orderModelList = responseDTO.getOrderModelList();
                        Log.i("budu-ammo", "Response: " + orderModelList);
//                        List<OrderModel> orderList = databaseHelper.getOrders(userId);
                        databaseHelper.deleteOrder();


                        for (OrderModel orderModel : orderModelList){
                            Log.i("order-logggg-loop", "Response: " + orderModel.getOrder_Status());
                        databaseHelper.insertOrder(orderModel);

                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter = new OrderHistoryAdapter(orderModelList);
                                orderHistoryRecyclerView.setAdapter(adapter);
                                adapter.notifyDataSetChanged();

//                                adapter = new OrderHistoryAdapter(orderList);
//                                orderHistoryRecyclerView.setAdapter(adapter);
                            }
                        });



                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();

    }

}