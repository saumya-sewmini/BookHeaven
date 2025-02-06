package com.example.bookheaven;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import androidx.annotation.NonNull;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchBottomSheet extends BottomSheetDialog {

    private Context context;
    private ListView searchListView;
    private EditText searchInput;
    private List<String> bookList;
    private ArrayAdapter<String> adapter;

    public SearchBottomSheet(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_search, null);
        setContentView(view);

        searchListView = view.findViewById(R.id.search_list);
        searchInput = view.findViewById(R.id.search_input);

        // Sample book list
        bookList = new ArrayList<>(Arrays.asList(
                "Harry Potter", "Lord of the Rings", "Percy Jackson",
                "The Alchemist", "Game of Thrones"
        ));

        // Adapter for ListView
        adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, bookList);
        searchListView.setAdapter(adapter);

        // Search filter
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

}
