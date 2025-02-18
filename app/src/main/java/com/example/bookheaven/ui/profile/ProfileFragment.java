package com.example.bookheaven.ui.profile;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.bookheaven.AddBookActivity;
import com.example.bookheaven.ProfileUpdateActivity;
import com.example.bookheaven.R;
import com.example.bookheaven.SigninActivity;
import com.example.bookheaven.databinding.FragmentProfileBinding;

public class ProfileFragment extends Fragment {

    private TextView textView19;
    private TextView textView21;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        textView19 = view.findViewById(R.id.textView19);
        textView21 = view.findViewById(R.id.textView21);

        TextView textView9 = view.findViewById(R.id.textView9);
        textView9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();

                Intent intent = new Intent(getActivity(), SigninActivity.class);
                startActivity(intent);

                requireActivity().finish();
            }
        });

        TextView textView6 = view.findViewById(R.id.textView6);
        textView6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ProfileUpdateActivity.class);
                startActivity(intent);
            }
        });

        TextView textView7 = view.findViewById(R.id.textView7);
        textView7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddBookActivity.class);
                startActivity(intent);
            }
        });

        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
        refreshProfileData();
    }

    private void refreshProfileData() {

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String userName = sharedPreferences.getString("user_fname", "") + " " + sharedPreferences.getString("user_lname", "");
        String userEmail = sharedPreferences.getString("user_email", "");

        textView19.setText(userName.trim());
        textView21.setText(userEmail);

    }
}