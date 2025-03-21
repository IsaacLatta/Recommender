package com.example.recommender.ui.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.recommender.Controller;
import com.example.recommender.R;
import com.example.recommender.model.entity.Book;

public class BookDetailFragment extends Fragment {

    private static final String ARG_BOOK = "arg_book";
    private Book book;

    private ImageView ivBookCover;
    private TextView tvBookTitle;
    private TextView tvBookDescription;
    private RatingBar ratingBar;
    private Button btnSubmitRating;

    // Use newInstance to pass the book object
    public static BookDetailFragment newInstance(Book book) {
        BookDetailFragment fragment = new BookDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_BOOK, book);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            book = (Book) getArguments().getSerializable(ARG_BOOK);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_detail, container, false);
        ivBookCover = view.findViewById(R.id.ivBookCover);
        tvBookTitle = view.findViewById(R.id.tvBookTitleDetail);
        tvBookDescription = view.findViewById(R.id.tvBookDescription);
        ratingBar = view.findViewById(R.id.ratingBarDetail);
        btnSubmitRating = view.findViewById(R.id.btnSubmitRating);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(book != null) {
            tvBookTitle.setText(book.getTitle());
            tvBookDescription.setText(book.getDescription());
            if(book.getRating() != null) {
                ratingBar.setRating(book.getRating());
            } else {
                ratingBar.setRating(0);
            }
            // Set the placeholder image; later you can load an actual image if available.
            ivBookCover.setImageResource(R.drawable.ic_book_placeholder);
        }
        btnSubmitRating.setOnClickListener(v -> {
            float rating = ratingBar.getRating();
            // Use the Controller's saveOrRateBook method to save the rating.
            Controller.getInstance().saveOrRateBook(book.getExternalId(), "rate", (int) rating);
            Log.d("BOOK_DETAIL", "Submitted rating: " + rating);
            // Optionally, close the fragment or show a confirmation.
        });
    }
}
