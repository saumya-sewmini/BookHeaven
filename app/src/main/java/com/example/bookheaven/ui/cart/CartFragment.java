package com.example.bookheaven.ui.cart;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

import com.example.bookheaven.CartAdapter;
import com.example.bookheaven.CartItem;
import com.example.bookheaven.ItemAdapter;
import com.example.bookheaven.R;
import com.example.bookheaven.databinding.FragmentCartBinding;
import com.example.bookheaven.databinding.FragmentHomeBinding;

public class CartFragment extends Fragment {

    private RecyclerView recyclerView;
    private CartAdapter adapter;
    private List<CartItem> cartItems;

    private FragmentCartBinding binding;;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCartBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        recyclerView = view.findViewById(R.id.recyclerViewCart);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));

        cartItems = new ArrayList<>();
        cartItems.add(new CartItem("Harry Potter", 1500.00, R.drawable.horror_book, 1));
        cartItems.add(new CartItem("The Alchemist", 950.00, R.drawable.book_cinderella, 1));
        cartItems.add(new CartItem("Rich Dad Poor Dad", 1125.00, R.drawable.novel_book, 1));

        // Set adapter
        adapter = new CartAdapter(requireContext(), cartItems);
        recyclerView.setAdapter(adapter);

        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(requireContext(), R.anim.layout_slide_in);
        recyclerView.setLayoutAnimation(controller);

        return view;

    }

}