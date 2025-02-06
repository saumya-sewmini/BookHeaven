package com.example.bookheaven.ui.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bookheaven.R;
import com.example.bookheaven.SearchBottomSheet;
import com.example.bookheaven.databinding.FragmentSearchBinding;

public class SearchFragment extends Fragment {

    private Button openSearchButton;

    private boolean isDialogOpened = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        // Only open the search dialog if it hasn't been opened yet
        if (!isDialogOpened) {
            openSearchDialog();
            isDialogOpened = true;  // Set flag to prevent reopening
        }

        return view;
    }

    private void openSearchDialog() {
        SearchBottomSheet searchBottomSheet = new SearchBottomSheet(requireContext());
        searchBottomSheet.show();

        // Reset flag when the dialog is dismissed
        searchBottomSheet.setOnDismissListener(dialog -> isDialogOpened = false);
    }

}