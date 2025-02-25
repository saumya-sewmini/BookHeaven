package com.example.bookheaven.ui.home;

import static android.app.ProgressDialog.show;
import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aemerse.slider.ImageCarousel;
import com.aemerse.slider.model.CarouselItem;
import com.example.bookheaven.BookDTO;
import com.example.bookheaven.BuildConfig;
import com.example.bookheaven.HomeItemAdapter;
import com.example.bookheaven.HomeItemModel;
import com.example.bookheaven.ItemAdapter;
import com.example.bookheaven.ItemModel;
import com.example.bookheaven.ProfileUpdateActivity;
import com.example.bookheaven.R;
//import com.example.bookheaven.TestActivity;
import com.example.bookheaven.databinding.FragmentHomeBinding;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private ItemAdapter itemAdapter;
    private List<ItemModel> itemList;
    private HomeItemAdapter adapter;
    private List<HomeItemModel> homeItemList;
    private ArrayList<BookDTO> bookList = new ArrayList<>();
    private FragmentHomeBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_home, container, false);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserPrefs", MODE_PRIVATE);
        int userId = sharedPreferences.getInt("user_id", -1);
        Log.i("BookHeaven-log-single", "user id: " + userId);

        setupImageCarousel();

        recyclerView = view.findViewById(R.id.recyclerView1);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));

        itemList = new ArrayList<>();
        itemAdapter = new ItemAdapter(itemList);
        recyclerView.setAdapter(itemAdapter);

        loadCategories();

        recyclerView = view.findViewById(R.id.recyclerView2);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));

        loadBooks();
        homeItemList = new ArrayList<>();
        adapter = new HomeItemAdapter(homeItemList, new HomeItemAdapter.OnItemClickListener() {
            @Override
            public void onAddToCartClick(HomeItemModel item) {
                // Handle add to cart click event
                Toast.makeText(requireContext(), item.getTitle() + " added to cart!", Toast.LENGTH_SHORT).show();
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserPrefs", MODE_PRIVATE);
                int userId = sharedPreferences.getInt("user_id", -1);
                int bookId = item.getId();
                int qty = 1;

                Log.i("BookHeaven-log-single", "book idqqqqq: " + userId);

                addBookToCart(userId, bookId, qty);
            }


        });

        recyclerView.setAdapter(adapter);

        return view;

    }

    private void addBookToCart(int userId, int bookId, int qty) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Gson gson = new Gson();
                JsonObject cart = new JsonObject();

                cart.addProperty("user_id", String.valueOf(userId));
                cart.addProperty("book_id", String.valueOf(bookId));
                cart.addProperty("quantity", String.valueOf(qty));

                Log.i("cart-log", "user_id: " + userId);
                Log.i("cart-log", "book_id: " + bookId);
                Log.i("cart-log", "quantity: " + qty);

                OkHttpClient client = new OkHttpClient();
                RequestBody requestBody = RequestBody.create(gson.toJson(cart), MediaType.get("application/json"));
                Request request = new Request.Builder()
                        .url(BuildConfig.URL+"/AddCart")
                        .post(requestBody)
                        .build();
////
                try {
                    Response response = client.newCall(request).execute();

                    String responseText = response.body().string();

                    Log.i("cart-log", " "+ responseText);
                    JSONObject jsonResponse = new JSONObject(responseText);

                    Log.i("cart-log", "success-data parse the backend (address)");

                    // Handle success or failure
                    if (jsonResponse.getBoolean("success")) {
                        requireActivity().runOnUiThread(() -> {
                            Toast.makeText(requireActivity(), "Cart Updated Successfully!", Toast.LENGTH_SHORT).show();
                            Log.i("cart-log", "success-data update address");
                        });
                    } else {
                        requireActivity().runOnUiThread(() -> {
                            Toast.makeText(requireActivity(), "Failed to Update Cart!", Toast.LENGTH_SHORT).show();
                            Log.e("cart-log", "failed-data update cart");
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    requireActivity()
                            .runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(requireActivity(),"Network Error", Toast.LENGTH_SHORT).show();
                                }
                            });
                }

            }
        }).start();
    }


    private void loadBooks() {
        Log.i("BookHeaven-log", "Loading books...");

        new Thread(new Runnable() {
            @Override
            public void run() {
                Gson gson = new Gson();

                OkHttpClient okHttpClient = new OkHttpClient();

                Request request = new Request.Builder()
                        .url(BuildConfig.URL+"/GetBooks")
                        .get()
                        .build();

                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        e.printStackTrace();
                        requireActivity()
                                .runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(requireContext(), "Failed to fetch books", Toast.LENGTH_SHORT).show();
                                        Log.e("BookHeaven-log", "Network request failed: " + e.getMessage());
                                    }
                                });
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        if (response.isSuccessful() && response.body() != null) {
                            String jsonResponse = response.body().string();
                            Log.i("BookHeaven-log", "Raw JSON Response: " + jsonResponse);

                            try {
                                JSONObject jsonObject = new JSONObject(jsonResponse);
                                boolean success = jsonObject.getBoolean("success");

                                if (success) {
                                    JSONArray books = jsonObject.getJSONArray("content");
                                    List<HomeItemModel> fetchedBooks = new ArrayList<>();

                                    for (int i = 0; i < books.length(); i++) {

                                        JSONObject bookObj = books.getJSONObject(i);
                                        int id = bookObj.has("book_id") ? bookObj.getInt("book_id") : -1;
                                        Log.d("xxxxxxxxxx", "book id:" +id);

                                        String title = bookObj.getString("book_title");
                                        double price = bookObj.getDouble("price");
                                        String imageUrl = bookObj.getString("image_url");

                                        Log.i("BookHeaven-log", "Loaded book: " + title);

                                        fetchedBooks.add(new HomeItemModel(id, title, price, imageUrl));

                                    }

                                    requireActivity()
                                            .runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    homeItemList.clear();
                                                    homeItemList.addAll(fetchedBooks);
                                                    adapter.notifyDataSetChanged();
                                                    Log.i("BookHeaven-log", "Books updated successfully");
                                                }
                                            });
                                } else {
                                    requireActivity()
                                            .runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(requireContext(), "No books found", Toast.LENGTH_SHORT).show();
                                                    Log.w("BookHeaven-log", "No books found");
                                                }
                                            });
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                requireActivity()
                                        .runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(requireContext(), "Error parsing JSON", Toast.LENGTH_SHORT).show();
                                                Log.e("BookHeaven-log", "Error parsing JSON: " + e.getMessage());
                                            }
                                        });
                            }
                        } else {
                            requireActivity()
                                    .runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(requireContext(), "Response unsuccessful", Toast.LENGTH_SHORT).show();
                                            Log.e("BookHeaven-log", "Unsuccessful response: " + response.code());
                                        }
                                    });
                        }
                    }
                });
            }
        }).start();
    }

    private void loadCategories() {
        Log.i("BookHeaven-log", "load categories");

        new Thread(new Runnable() {
            @Override
            public void run() {

                Log.i("BookHeaven-log", "Thread started");

                OkHttpClient okHttpClient = new OkHttpClient();

                Request request = new Request.Builder()
                        .url(BuildConfig.URL+"/GetCatergories") // Ensure correct API endpoint
                        .get()
                        .build();

                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        e.printStackTrace();
                        requireActivity()
                                .runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(requireContext(), "Failed to fetch categories", Toast.LENGTH_SHORT).show();
                                        Log.e("BookHeaven-log", "Network request failed: " + e.getMessage());
                                    }
                                });
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        if (response.isSuccessful() && response.body() != null) {
                            String jsonResponse = response.body().string();
                            Log.i("BookHeaven-log", "Raw JSON Response: " + jsonResponse);

                            try {

                                JSONObject jsonObject = new JSONObject(jsonResponse);
                                boolean success = jsonObject.getBoolean("success");

                                if (success) {

                                    JSONArray categories = jsonObject.getJSONArray("content");
                                    List<ItemModel> fetchedCategories = new ArrayList<>();

                                    for (int i = 0; i < categories.length(); i++) {
                                        JSONObject categoryObj = categories.getJSONObject(i);
                                        int id = categoryObj.getInt("id");

                                        String categoryName = categoryObj.getString("catergory");

                                        int categoryImage = getCategoryImage(categoryName);

                                        Log.i("BookHeaven-log", "Loaded category: " + categoryName);
                                        fetchedCategories.add(new ItemModel(R.drawable.add_img, categoryName));
                                    }

                                    requireActivity()
                                            .runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    itemList.clear();
                                                    itemList.addAll(fetchedCategories);
                                                    itemAdapter.notifyDataSetChanged();
                                                    Log.i("BookHeaven-log", "Categories updated successfully");
                                                }
                                            });

                                } else {

                                    requireActivity()
                                            .runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(requireContext(), "No categories found", Toast.LENGTH_SHORT).show();
                                                    Log.w("BookHeaven-log", "No categories found");
                                                }
                                            });

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                                requireActivity()
                                        .runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(requireContext(), "Error parsing JSON", Toast.LENGTH_SHORT).show();
                                                Log.e("BookHeaven-log", "Error parsing JSON: " + e.getMessage());
                                            }
                                        });
                            }
                        } else {
                            requireActivity()
                                    .runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(requireContext(), "Response unsuccessful", Toast.LENGTH_SHORT).show();
                                            Log.e("BookHeaven-log", "Unsuccessful response: " + response.code());
                                        }
                                    });
                        }
                    }
                });

            }
        }).start();

    }

    private int getCategoryImage(String categoryName) {
        switch (categoryName.toLowerCase()) {
            case "fiction":
                return R.drawable.fiction;
            case "science":
                return R.drawable.science;
            case "history":
                return R.drawable.history;
            case "technology":
                return R.drawable.technology;
            case "mystery":
                return R.drawable.mystery;
            default:
                return R.drawable.add_img; // Default image
        }
    }

    private void setupImageCarousel() {
        ImageCarousel carousel = binding.carousel;
        carousel.registerLifecycle(getLifecycle());
        carousel.setAutoPlay(true);
        carousel.setAutoPlayDelay(3000);
        carousel.setShowNavigationButtons(false);

        List<CarouselItem> carouselItems = new ArrayList<>();
        carouselItems.add(new CarouselItem(R.drawable.caro_one)); // Replace with actual drawable images
        carouselItems.add(new CarouselItem(R.drawable.caro_two));
        carouselItems.add(new CarouselItem(R.drawable.caro_three));

        carousel.setData(carouselItems);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}