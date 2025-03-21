package com.example.recommender.ui.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.recommender.Controller;
import com.example.recommender.R;
import com.example.recommender.model.entity.Book;
import com.example.recommender.model.entity.Store;
import com.example.recommender.model.entity.StoreListener;
import com.example.recommender.ui.adapter.BookSearchAdapter;
import java.util.ArrayList;
import java.util.List;

public class SavedFragment extends Fragment implements StoreListener, BookClickListener {

    private RecyclerView rvSavedBooks;
    private BookSearchAdapter adapter;
    private List<Book> savedBooks = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_saved, container, false);
        rvSavedBooks = view.findViewById(R.id.rvSavedBooks);
        adapter = new BookSearchAdapter(savedBooks, this);
        rvSavedBooks.setLayoutManager(new LinearLayoutManager(getContext()));
        rvSavedBooks.setAdapter(adapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Store.getInstance().addListener(this);
        Controller.getInstance().listSavedBooks();
    }

    @Override
    public void onPause() {
        super.onPause();
        Store.getInstance().removeListener(this);
    }

    @Override
    public void onStoreUpdated() {
        List<Book> updatedSavedBooks = Store.getInstance().getSavedBooks();
        if (updatedSavedBooks != null) {
            adapter.updateData(updatedSavedBooks);
            Log.d("SAVED_FRAGMENT", "Adapter updated with " + updatedSavedBooks.size() + " saved books.");
        } else {
            adapter.updateData(new ArrayList<>());
            Log.d("SAVED_FRAGMENT", "No saved books found.");
        }
    }

    @Override
    public void onBookClick(Book book) {
        BookDetailFragment detailFragment = BookDetailFragment.newInstance(book);
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.flFragment, detailFragment)
                .addToBackStack(null)
                .commit();
    }
}
