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
import android.widget.Button;
import android.widget.EditText;
import com.example.recommender.Controller;
import com.example.recommender.R;
import com.example.recommender.model.entity.Book;
import com.example.recommender.model.entity.Store;
import com.example.recommender.model.entity.StoreListener;
import com.example.recommender.ui.adapter.BookSearchAdapter;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements StoreListener, BookClickListener {

    private EditText etBookSearch;
    private Button btnSearchBook;
    private RecyclerView rvSearchResults;
    private BookSearchAdapter adapter;
    private List<Book> books = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        etBookSearch = view.findViewById(R.id.etBookSearch);
        btnSearchBook = view.findViewById(R.id.btnSearchBook);
        rvSearchResults = view.findViewById(R.id.rvSearchResults);

        adapter = new BookSearchAdapter(books, this);
        rvSearchResults.setLayoutManager(new LinearLayoutManager(getContext()));
        rvSearchResults.setAdapter(adapter);

        btnSearchBook.setOnClickListener(v -> {
            String query = etBookSearch.getText().toString().trim();
            if (!query.isEmpty()) {
                Log.d("HOME_SEARCH", "Search query: " + query);
                Controller.getInstance().searchBook(query);
            } else {
                Log.d("HOME_SEARCH", "Empty search query");
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Store.getInstance().addListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        Store.getInstance().removeListener(this);
    }

    @Override
    public void onStoreUpdated() {
        List<Book> searchedBooks = Store.getInstance().getSearchedBooks();
        if (searchedBooks != null) {
            adapter.updateData(searchedBooks);
            Log.d("HOME_SEARCH", "Adapter updated with " + searchedBooks.size() + " books.");
        } else {
            adapter.updateData(new ArrayList<>());
            Log.d("HOME_SEARCH", "No books found, clearing adapter.");
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
