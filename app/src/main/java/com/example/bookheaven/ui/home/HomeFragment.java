package com.example.bookheaven.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aemerse.slider.ImageCarousel;
import com.aemerse.slider.model.CarouselItem;
import com.example.bookheaven.ItemAdapter;
import com.example.bookheaven.ItemModel;
import com.example.bookheaven.R;
import com.example.bookheaven.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private ItemAdapter itemAdapter;
    private List<ItemModel> itemList;

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

        // Initialize data
        itemList = new ArrayList<>();
        itemList.add(new ItemModel(R.drawable.book_cinderella, "Fairly"));
        itemList.add(new ItemModel(R.drawable.horror_book, "Horror"));
        itemList.add(new ItemModel(R.drawable.novel_book, "Novel"));
        itemList.add(new ItemModel(R.drawable.ic_launcher_foreground, "Kids"));

        // Set adapter
        itemAdapter = new ItemAdapter(itemList);
        recyclerView.setAdapter(itemAdapter);

        return view;

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