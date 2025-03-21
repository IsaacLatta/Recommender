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
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.app.AlertDialog;

import com.example.recommender.Controller;
import com.example.recommender.R;
import com.example.recommender.model.entity.Book;
import com.example.recommender.model.response.GroupInfo;
import com.example.recommender.model.entity.Store;

import java.util.List;

public class BookDetailFragment extends Fragment {

    private static final String ARG_BOOK = "arg_book";
    private Book book;

    private ImageView ivBookCover;
    private TextView tvBookTitle;
    private TextView tvBookDescription;
    private RatingBar ratingBar;
    private Button btnSubmitRating;
    private Button btnRecommend;

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
        btnRecommend = view.findViewById(R.id.btnRecommend);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(book != null) {
            tvBookTitle.setText(book.getTitle());
            tvBookDescription.setText(book.getDescription());
            ratingBar.setRating(book.getRating() != null ? book.getRating() : 0);
            ivBookCover.setImageResource(R.drawable.ic_book_placeholder);
        }

        btnSubmitRating.setOnClickListener(v -> {
            float rating = ratingBar.getRating();
            Controller.getInstance().saveOrRateBook(book.getExternalId(), "rate", (int) rating);
            Log.d("BOOK_DETAIL", "Submitted rating: " + rating);
        });

        btnRecommend.setOnClickListener(v -> {
            showRecommendDialog();
        });
    }

    private void showRecommendDialog() {
        // Retrieve the user's joined groups from the Store.
        List<GroupInfo> joinedGroups = Store.getInstance().getJoinedGroups();
        if (joinedGroups == null || joinedGroups.isEmpty()) {
            new AlertDialog.Builder(getContext())
                    .setTitle("No Groups")
                    .setMessage("You are not a member of any reading groups. Join a group first!")
                    .setPositiveButton("OK", null)
                    .show();
            return;
        }

        // Create arrays for group names and ids.
        String[] groupNames = new String[joinedGroups.size()];
        int[] groupIds = new int[joinedGroups.size()];
        for (int i = 0; i < joinedGroups.size(); i++) {
            GroupInfo group = joinedGroups.get(i);
            groupNames[i] = group.getGroupName();
            groupIds[i] = group.getGroupId();
        }

        new AlertDialog.Builder(getContext())
                .setTitle("Select a Group")
                .setItems(groupNames, (dialog, which) -> {
                    int selectedGroupId = groupIds[which];
                    // Recommend the book to the selected group.
                    Controller.getInstance().recommendBookToGroup(selectedGroupId, book.getExternalId());
                    Log.d("BOOK_DETAIL", "Recommended book to group id: " + selectedGroupId);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
