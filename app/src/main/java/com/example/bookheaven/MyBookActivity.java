package com.example.bookheaven;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MyBookActivity extends AppCompatActivity {

    private RecyclerView recyclerViewBooks;
    private MySellBookAdapter mySellBookAdapter;
    private List<MySellBookModel> mySellBookModelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_my_book);
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

        recyclerViewBooks = findViewById(R.id.recyclerViewBooks);
        recyclerViewBooks.setLayoutManager(new LinearLayoutManager(this));

        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        int userId = sharedPreferences.getInt("user_id", -1);

        Log.i("BookHeaven-Mybook-log", "user id: " + userId);

        if (userId != -1){
            loadMyBooks(userId);
        }

        PieChart pieChart = findViewById(R.id.pieChart);
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(30f, "Horror"));
        entries.add(new PieEntry(20f, "Fairy Tales"));
        entries.add(new PieEntry(15f, "Kids Stories"));
        entries.add(new PieEntry(10f, "Novels"));
        entries.add(new PieEntry(25f, "Short stories"));


        PieDataSet dataSet = new PieDataSet(entries, "Book Categories");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setValueTextSize(12f);

        PieData pieData = new PieData(dataSet);

        pieChart.setData(pieData);


        pieChart.getDescription().setEnabled(false); // Hide description
        pieChart.setCenterText("Book Sales"); // Center text
        pieChart.setCenterTextSize(16f);
        pieChart.setHoleRadius(40f); // Hole in the center
        pieChart.setTransparentCircleRadius(45f); // Transparent circle around the hole
        pieChart.animateY(1000); // Animation

        pieChart.invalidate();



    }

    private void loadMyBooks(int userId) {
        Log.i("BookHeaven-Mybook-log", "user id: " + userId);
        new Thread(new Runnable() {
            @Override
            public void run() {

                Gson gson = new Gson();
                OkHttpClient okHttpClient = new OkHttpClient();

                JsonObject cart = new JsonObject();
                cart.addProperty("user_id", String.valueOf(userId));

                RequestBody requestBody = RequestBody.create(gson.toJson(cart), MediaType.get("application/json"));
                Request request = new Request.Builder()
                        .url(BuildConfig.URL+"/GetMyBooks")
                        .post(requestBody)
                        .build();

                try {
                    Response response = okHttpClient.newCall(request).execute();
                    String responseText = response.body().string();
                    Log.i("BookHeaven-Mybook-log", "Response1: " + responseText);

                    ResponseDTO responseDTO = gson.fromJson(responseText, ResponseDTO.class);
                    System.out.println("redda");

                    if (responseDTO.isSuccess()){
                        Log.i("BookHeaven-Mybook-log", "Response2: " + responseText);

                        mySellBookModelList = responseDTO.getMySellBookModelList();
                        Log.i("BookHeaven-Mybook-log", "Response3: " + mySellBookModelList);


                        for (MySellBookModel mySellBookModel : mySellBookModelList){
                            Log.i("BookHeaven-Mybook-log", "Response4: " + mySellBookModel.getTitle());
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mySellBookAdapter = new MySellBookAdapter(mySellBookModelList);
                                recyclerViewBooks.setAdapter(mySellBookAdapter);
                            }
                        });



                    }else {
                        Log.i("BookHeaven-Mybook-log", "Unsuccessful response");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

}