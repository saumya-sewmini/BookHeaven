package com.example.bookheaven.ui.search;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.bookheaven.AddBookActivity;
import com.example.bookheaven.BookDTO;
import com.example.bookheaven.BuildConfig;
import com.example.bookheaven.R;
import com.example.bookheaven.SearchBottomSheet;
import com.example.bookheaven.databinding.FragmentSearchBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SearchFragment extends Fragment {

    private Spinner categorySpinner;
    private ArrayAdapter<String> categoryAdapter;
    private ArrayList<String> categoryList = new ArrayList<>();

    private Button openSearchButton;

    private boolean isDialogOpened = false;

    private View rootView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        rootView = view;

        EditText titleInput = view.findViewById(R.id.editTextText22);
        EditText authorInput = view.findViewById(R.id.editTextText);
        Button searchButton = view.findViewById(R.id.button14);

        searchButton.setOnClickListener(v -> {
            String title = titleInput.getText().toString().trim();
            String author = authorInput.getText().toString().trim();
            String category = categorySpinner.getSelectedItem().toString();

            fetchSearchResults(title, author, category);
        });

        // Only open the search dialog if it hasn't been opened yet
//        if (!isDialogOpened) {
//            openSearchDialog();
//            isDialogOpened = true;  // Set flag to prevent reopening
//        }

        categorySpinner = view.findViewById(R.id.spinner4);
        loadCategoriesFromDatabase();

        return view;
    }

    private void loadCategoriesFromDatabase() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient okHttpClient=new OkHttpClient();
                Request request = new Request.Builder()
                        .url(BuildConfig.URL+"/GetCatergories")
                        .get()
                        .build();
                try {
                    Response response = okHttpClient.newCall(request).execute();
                    String responseText = response.body().string();
                    JSONObject jsonResponse = new JSONObject(responseText);
                    //Log.i("BookHeaven-log", "success1");

                    if (jsonResponse.getBoolean("success")) {
                        //Log.i("BookHeaven-log", "success2");
                        JSONArray jsonArray = jsonResponse.getJSONArray("content");
                        categoryList.clear();
                        //Log.i("BookHeaven-log", "success3");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            categoryList.add(jsonArray.getJSONObject(i).getString("catergory"));
                            //Log.i("BookHeaven-log", "success4");
                        }
                        requireActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                categoryAdapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_spinner_dropdown_item, categoryList);
                                categorySpinner.setAdapter(categoryAdapter);
                                //Log.i("BookHeaven-log", "success5");
                            }
                        });
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    private void fetchSearchResults(String title, String author, String category){

        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i("BookHeaven-log", "Searching books: Title=" + title + ", Author=" + author + ", Category=" + category);

                OkHttpClient client = new OkHttpClient();

                HttpUrl.Builder urlBuilder = HttpUrl.parse(BuildConfig.URL+"/SearchBooks").newBuilder();
                if (!title.isEmpty()) urlBuilder.addQueryParameter("title", title);
                if (!author.isEmpty()) urlBuilder.addQueryParameter("author", author);
                if (!category.isEmpty()) urlBuilder.addQueryParameter("category", category);

                String url = urlBuilder.build().toString();
                Request request = new Request.Builder().url(url).get().build();

                try {
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful() && response.body() != null) {
                        String jsonResponse = response.body().string();
                        Log.i("BookHeaven-log", "Search Response: " + jsonResponse);

                        JSONObject jsonObject = new JSONObject(jsonResponse);
                        boolean success = jsonObject.getBoolean("success");

                        if (success) {
                            JSONArray booksArray = jsonObject.getJSONArray("content");
                            List<BookDTO> bookList = new ArrayList<>();

                            for (int i = 0; i < booksArray.length(); i++) {
                                JSONObject bookObj = booksArray.getJSONObject(i);
                                bookList.add(new BookDTO(
                                        bookObj.getString("book_title"),
                                        bookObj.getDouble("price"),
                                        bookObj.getString("image_url"),
                                        bookObj.getString("description")
                                ));
                            }

                            // Updating UI on main thread
                            requireActivity().runOnUiThread(() -> updateSearchResults(bookList));
                        } else {
                            requireActivity().runOnUiThread(() -> {
                                Toast.makeText(requireActivity(), "No results found", Toast.LENGTH_SHORT).show();
                                Log.w("BookHeaven-log", "No search results found");
                            });
                        }
                    } else {
                        requireActivity().runOnUiThread(() -> {
                            Toast.makeText(requireActivity(), "Search request unsuccessful", Toast.LENGTH_SHORT).show();
                            Log.e("BookHeaven-log", "Unsuccessful search response: " + response.code());
                        });
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    requireActivity().runOnUiThread(() -> {
                        Toast.makeText(requireActivity(), "Error fetching search results", Toast.LENGTH_SHORT).show();
                        Log.e("BookHeaven-log", "Exception: " + e.getMessage());
                    });
                }
            }
        }).start();

    }

    private void updateSearchResults(List<BookDTO> bookList) {
        ConstraintLayout searchResultsLayout = rootView.findViewById(R.id.noSearchHere);
        searchResultsLayout.removeAllViews(); // Clear previous results

        if (bookList.isEmpty()) {
            TextView noResultsText = new TextView(requireContext());
            noResultsText.setText("No books found.");
            noResultsText.setTextColor(getResources().getColor(R.color.email));
            noResultsText.setTextSize(20);
            noResultsText.setGravity(Gravity.CENTER);

            searchResultsLayout.addView(noResultsText);
        } else {
            for (BookDTO book : bookList) {
                View bookView = LayoutInflater.from(requireContext()).inflate(R.layout.home_item_card, searchResultsLayout, false);

                TextView titleView = bookView.findViewById(R.id.textView54);
                TextView priceView = bookView.findViewById(R.id.textView55);
                ImageView bookImageView = bookView.findViewById(R.id.imageView22);

                titleView.setText(book.getBookTitle());
                priceView.setText("Rs" + book.getPrice());

                Glide.with(this).load(book.getImageUrl()).into(bookImageView);

                searchResultsLayout.addView(bookView);
            }
        }

    }

//    private void openSearchDialog() {
//        SearchBottomSheet searchBottomSheet = new SearchBottomSheet(requireContext());
//        searchBottomSheet.show();
//
//        // Reset flag when the dialog is dismissed
//        searchBottomSheet.setOnDismissListener(dialog -> isDialogOpened = false);
//    }

}