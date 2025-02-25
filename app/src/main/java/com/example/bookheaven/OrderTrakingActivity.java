package com.example.bookheaven;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class OrderTrakingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_traking);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        int orderId = intent.getIntExtra("order_id", -1);
        String orderDate = intent.getStringExtra("order_date");
        String orderStatus = intent.getStringExtra("order_status");
        double totalPrice = intent.getDoubleExtra("total_price", 0.0);
        String latitudeStr = intent.getStringExtra("latitude");
        String longitudeStr = intent.getStringExtra("longitude");

        if (latitudeStr == null || latitudeStr.trim().isEmpty() ||
                longitudeStr == null || longitudeStr.trim().isEmpty()) {
            Log.e("OrderTracking", "Latitude or Longitude is missing in Intent");
            latitudeStr = "6.822510209304565";  // Default value to prevent crash
            longitudeStr = "79.8936674163921";
        }

        double latitude = Double.parseDouble(latitudeStr);
        double longitude = Double.parseDouble(longitudeStr);

        ImageButton imageButton1 = findViewById(R.id.imageButton1);
        imageButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        SupportMapFragment supportMapFragment = new SupportMapFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frameLayout1,supportMapFragment);
        fragmentTransaction.commit();

        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                Log.i("book-heaven-log","map ready");

                LatLng orderLocation = new LatLng(latitude, longitude);
                googleMap.addMarker(new MarkerOptions().position(orderLocation).title("Order Location"));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(orderLocation, 15));
            }
        });

        TextView textViewOrderId = findViewById(R.id.textView57);
        textViewOrderId.setText("#" + orderId);

        LinearLayout status1 = findViewById(R.id.linearLayout41);
        LinearLayout status2 = findViewById(R.id.linearLayout42);
        LinearLayout status3 = findViewById(R.id.linearLayout43);
        LinearLayout status4 = findViewById(R.id.linearLayout44);

        status1.setAlpha(0.2f);
        status2.setAlpha(0.2f);
        status3.setAlpha(0.2f);
        status4.setAlpha(0.2f);

        switch (orderStatus) {
            case "Order Placed":
                status1.setAlpha(1.0f);
                break;
            case "Order Confirmed":
                status2.setAlpha(1.0f);
                break;
            case "Order Processed":
                status3.setAlpha(1.0f);
                break;
            case "Ready to Pickup":
                status4.setAlpha(1.0f);
                break;
            default:
                Log.w("OrderTracking", "Unknown order status: " + orderStatus);
        }
    }
}