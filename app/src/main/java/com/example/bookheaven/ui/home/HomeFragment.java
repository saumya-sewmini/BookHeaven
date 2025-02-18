package com.example.bookheaven.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aemerse.slider.ImageCarousel;
import com.aemerse.slider.model.CarouselItem;
import com.example.bookheaven.HomeItemAdapter;
import com.example.bookheaven.HomeItemModel;
import com.example.bookheaven.ItemAdapter;
import com.example.bookheaven.ItemModel;
import com.example.bookheaven.R;
import com.example.bookheaven.databinding.FragmentHomeBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private ItemAdapter itemAdapter;
    private List<ItemModel> itemList;
    private HomeItemAdapter adapter;
    private List<HomeItemModel> homeItemList;

    private FragmentHomeBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_home, container, false);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        setupImageCarousel();

        recyclerView = view.findViewById(R.id.recyclerView1);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));

        itemList = new ArrayList<>();
        itemAdapter = new ItemAdapter(itemList);
        recyclerView.setAdapter(itemAdapter);

        loadCategories();

//        itemList = new ArrayList<>();
//        itemList.add(new ItemModel(R.drawable.book_cinderella, "Fairly"));
//        itemList.add(new ItemModel(R.drawable.horror_book, "Horror"));
//        itemList.add(new ItemModel(R.drawable.novel_book, "Novel"));
//        itemList.add(new ItemModel(R.drawable.ic_launcher_foreground, "Kids"));
//
//        itemAdapter = new ItemAdapter(itemList);
//        recyclerView.setAdapter(itemAdapter);

        recyclerView = view.findViewById(R.id.recyclerView2);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));

        homeItemList = new ArrayList<>();
        adapter = new HomeItemAdapter(homeItemList, new HomeItemAdapter.OnItemClickListener() {
            @Override
            public void onAddToCartClick(HomeItemModel item) {
                // Handle add to cart click event
                Toast.makeText(requireContext(), item.getTitle() + " added to cart!", Toast.LENGTH_SHORT).show();
            }


        });


        recyclerView.setAdapter(adapter);

        loadBooks();
//        homeItemList = new ArrayList<>();
//        homeItemList.add(new HomeItemModel("Harry Potter", 1500.00, R.drawable.novel_book));
//        homeItemList.add(new HomeItemModel("The Alchemist", 950.00, R.drawable.horror_book));
//        homeItemList.add(new HomeItemModel("Rich Dad Poor Dad", 1125.00, R.drawable.book_cinderella));


        return view;

    }

    private void loadBooks() {
        Log.i("BookHeaven-log", "Loading books...");

        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient okHttpClient = new OkHttpClient();

                Request request = new Request.Builder()
                        .url("http://192.168.8.126:8080/BookHeaven/GetBooks") // Correct API endpoint
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
                                        int id = bookObj.has("id") ? bookObj.getInt("id") : -1;

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
                        .url("http://192.168.8.126:8080/BookHeaven/GetCatergories") // Ensure correct API endpoint
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
        carouselItems.add(new CarouselItem(R.drawable.book_cinderella)); // Replace with actual drawable images
        carouselItems.add(new CarouselItem(R.drawable.horror_book));
        carouselItems.add(new CarouselItem(R.drawable.novel_book));

        carousel.setData(carouselItems);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}