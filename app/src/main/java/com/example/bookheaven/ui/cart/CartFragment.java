package com.example.bookheaven.ui.cart;

import static android.content.Context.MODE_PRIVATE;

import static com.google.api.AnnotationsProto.http;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import com.example.bookheaven.BuildConfig;
import com.example.bookheaven.CartAdapter;
import com.example.bookheaven.CartItem;
import com.example.bookheaven.HomeItemModel;
import com.example.bookheaven.ItemAdapter;
import com.example.bookheaven.R;
import com.example.bookheaven.ResponseDTO;
import com.example.bookheaven.databinding.FragmentCartBinding;
import com.example.bookheaven.databinding.FragmentHomeBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import lk.payhere.androidsdk.PHConfigs;
import lk.payhere.androidsdk.PHConstants;
import lk.payhere.androidsdk.PHMainActivity;
import lk.payhere.androidsdk.PHResponse;
import lk.payhere.androidsdk.model.InitRequest;
import lk.payhere.androidsdk.model.Item;
import lk.payhere.androidsdk.model.StatusResponse;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CartFragment extends Fragment {


    private static final String TAG = "PayHereDemo";

    private final ArrayList<CartItem> getCartItemArrayList = new ArrayList<>();
    private View view;
    private RecyclerView recyclerView;

    private double totalPrice;
    private double totalShippingPrice;

    private CartItem c;
    private CartAdapter adapter;
//    private List<CartItem> cartItems;

    private FragmentCartBinding binding;

    private int userId;

    double total = totalPrice + totalShippingPrice;

    private String userName;

    Random random = new Random();
    int randomNum = 100000 + random.nextInt(900000); // Generates a 6-digit random number
    String orderId = "ORD"+randomNum;

    private  final ActivityResultLauncher<Intent> payHareLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result ->{
                if(result.getResultCode() == Activity.RESULT_OK && result.getData() != null){
                    Intent data = result.getData();
                    if (data.hasExtra(PHConstants.INTENT_EXTRA_RESULT)){
                        Serializable serializable = data.getSerializableExtra(PHConstants.INTENT_EXTRA_RESULT);
                        if(serializable instanceof PHResponse){
                            PHResponse<StatusResponse> response = (PHResponse<StatusResponse>) serializable;

                            if (response.isSuccess()){

                                saveOrder();

                            }

                        }
                    }
                }else if (result.getResultCode() == Activity.RESULT_CANCELED){
//                    textView.setText("user canceld the request");
                }
        }
    );

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_cart, container, false);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserPrefs", MODE_PRIVATE);

        userId = sharedPreferences.getInt("user_id", -1);
        userName = sharedPreferences.getString("user_name", "");

        RecyclerView recyclerView1 = view.findViewById(R.id.recyclerViewCart);
        recyclerView1.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CartAdapter(getCartItemArrayList);
        recyclerView1.setAdapter(adapter);

        TextView tot_price = view.findViewById(R.id.textView16);
        TextView tot_shipping = view.findViewById(R.id.textView20);

        Button checkout = view.findViewById(R.id.button4);
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkOutProcess();

            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                Gson gson = new Gson();
                OkHttpClient okHttpClient = new OkHttpClient();

                JsonObject cart = new JsonObject();
                cart.addProperty("user_id", String.valueOf(userId));

                RequestBody requestBody = RequestBody.create(gson.toJson(cart), MediaType.get("application/json"));
                Request request = new Request.Builder()
                        .url(BuildConfig.URL+"/GetCartItem")
                        .post(requestBody)
                        .build();
                try {
                    Response response = okHttpClient.newCall(request).execute();
                    String responseText = response.body().string();
                    Log.i("cart-log", "Response: " + responseText);

                    if (response.isSuccessful()){
                        Log.i("cart-loggg", "Response: " + responseText);
                        ResponseDTO responseDTO = gson.fromJson(responseText, ResponseDTO.class);

                        List<CartItem> cartItems = responseDTO.getCartItemsList();
                        Log.i("budu-ammo", "Response: " + cartItems);

                        for (CartItem cartItem : cartItems){
                            Log.i("cart-logggg-loop", "Response: " + cartItem.getTitle());
                            totalPrice += cartItem.getTot_price();
                            totalShippingPrice += cartItem.getTot_shipping();


                        }
                        requireActivity()
                                .runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        getCartItemArrayList.clear();
                                        getCartItemArrayList.addAll(cartItems);
                                        adapter.notifyDataSetChanged();

                                        tot_price.setText("Rs. " + String.valueOf(totalPrice));
                                        tot_shipping.setText("Rs. " + String.valueOf(totalShippingPrice));
                                    }

                                });
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();

        return view;
    }

    private void checkOutProcess() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserPrefs", MODE_PRIVATE);

        int userId = sharedPreferences.getInt("user_id", -1);

        new Thread(new Runnable() {

            @Override
            public void run() {
                Gson gson = new Gson();
                JsonObject userObject = new JsonObject();
                userObject.addProperty("id", userId);

                RequestBody requestBody = RequestBody.create(gson.toJson(userObject), MediaType.get("application/json"));
                Request request = new Request.Builder()
                        .url(BuildConfig.URL+"/GetUserAddress") // Change this to your actual API endpoint
                        .post(requestBody)
                        .build();

                try {
                    OkHttpClient okHttpClient = new OkHttpClient();
                    Response response = okHttpClient.newCall(request).execute();
                    String responseText = response.body().string();
                    Log.i("address-check-log", "Response: " + responseText);

                    if (response.isSuccessful()){
                        JSONObject jsonResponse = new JSONObject(responseText);
                        boolean success = jsonResponse.getBoolean("success");
                        initiatePayment();

                    }else{

                        requireActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(), "Please update your profile with an address before checkout.", Toast.LENGTH_LONG).show();
                            }
                        });

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    private void initiatePayment() {

        InitRequest req = new InitRequest();
        req.setMerchantId("1227466");       // Merchant ID
        req.setCurrency("LKR");             // Currency code LKR/USD/GBP/EUR/AUD
        req.setAmount(1000.00);             // Final Amount to be charged
        req.setOrderId("230000123");        // Unique Reference ID
        req.setItemsDescription("Door bell wireless");  // Item description title
        req.setCustom1("This is the custom message 1");
        req.setCustom2("This is the custom message 2");
        req.getCustomer().setFirstName("Saman");
        req.getCustomer().setLastName("Perera");
        req.getCustomer().setEmail("samanp@gmail.com");
        req.getCustomer().setPhone("+94771234567");
        req.getCustomer().getAddress().setAddress("No.1, Galle Road");
        req.getCustomer().getAddress().setCity("Colombo");
        req.getCustomer().getAddress().setCountry("Sri Lanka");

//Optional Params

        req.getCustomer().getDeliveryAddress().setAddress("No.2, Mathugama Road");
        req.getCustomer().getDeliveryAddress().setCity("Kalutara");
        req.getCustomer().getDeliveryAddress().setCountry("Sri Lanka");
        req.getItems().add(new Item(null, "Door bell wireless", 1, 1000.0));

        Intent intent = new Intent(getActivity(), PHMainActivity.class);
        intent.putExtra(PHConstants.INTENT_EXTRA_DATA, req);
        PHConfigs.setBaseUrl(PHConfigs.SANDBOX_URL);

        payHareLauncher.launch(intent);

    }

    public void saveOrder(){

        new Thread(new Runnable() {
            @Override
            public void run() {

                Gson gson = new Gson();

                OkHttpClient okHttpClient = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(BuildConfig.URL+"/SaveOrder?id=" + userId)
                        .get()
                        .build();

                Log.i("cart-log", "user_id cart eke checkout eka uanata passe yana user id eka: " + userId);

                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        Log.i("cart-log", "Error");
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        firebaseInsert();
                        Log.i("cart-log", "Success");
                    }
                });

            }
        }).start();

    }

    public void  firebaseInsert(){

        HashMap<String,Object> document = new HashMap<>();
        document.put("user",String.valueOf(userId));
        document.put("order_id",orderId);
        document.put("total",String.valueOf("5000"));
        document.put("user_name",userName);
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        firestore.collection("firebase_notification").add(document)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.i("cart-log", "onSuccess");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("carts-log", "error");
                    }
                });

        firestore.collection("firebase_notification")
                .whereEqualTo("user",String.valueOf(userId))
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.e("cart-log", "Listen failed.", error);
                            return;
                        }

                        if (snapshots != null) {
                            for (DocumentChange dc : snapshots.getDocumentChanges()) {

                                if (dc.getType().equals(DocumentChange.Type.ADDED)){
                                    Log.d("cart-log", "notifed ");
                                    notification();
                                }

                            }
                        }
                    }
          });

    }

    private void notification() {
            // Use getActivity() to access the context of the fragment's parent activity
            NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel notificationChannel = new NotificationChannel(
                        "C1",
                        "Channel 1",
                        NotificationManager.IMPORTANCE_DEFAULT
                );
                notificationManager.createNotificationChannel(notificationChannel);
            }


            Notification notification = new NotificationCompat.Builder(getContext(), "C1")
                    .setSmallIcon(R.drawable.bell)
                    .setContentTitle("Order Placed!")  // Title of the notification
                    .setContentText("Your order #" + orderId + " has been placed successfully. Total: RS." + total+"0")  // Content with Order ID and Total Amount
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)  // Priority
                    .setAutoCancel(true)
                    .setVibrate(new long[]{0, 500, 1000, 500})
                    .build();

            notificationManager.notify(1, notification);
    }


}