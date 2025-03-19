package com.example.recommender.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.recommender.Controller;
import com.example.recommender.R;
import com.example.recommender.model.entity.Book;

import java.util.ArrayList;


public class HomeFragment extends Fragment {


    private Button searchButton;
    private EditText searchBarText;
    private ArrayList<Book> books;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        searchButton = view.findViewById(R.id.search_button);
        searchBarText = view.findViewById(R.id.search_bar);

        searchButton.setOnClickListener(v ->{
            String search_input = searchBarText.getText().toString().trim();
            Log.d("HOME_SEARCH_", search_input);
           Controller.getInstance().searchBook(search_input);

        });
        return view;

    }

}
