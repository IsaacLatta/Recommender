package com.example.recommender.ui.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.SearchView;
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

public class HomeFragment extends Fragment implements StoreListener {

    private SearchView searchView;
    private RecyclerView rvSearchResults;
    private BookSearchAdapter adapter;
    // Local list of books is managed by the Store now
    private List<Book> books = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the modern layout for HomeFragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        searchView = view.findViewById(R.id.searchView);
        rvSearchResults = view.findViewById(R.id.rvSearchResults);

        adapter = new BookSearchAdapter(books);
        rvSearchResults.setLayoutManager(new LinearLayoutManager(getContext()));
        rvSearchResults.setAdapter(adapter);

        // Listen for search query submissions and pass them to the Controller
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("HOME_SEARCH", "Query submitted: " + query);
                Controller.getInstance().searchBook(query);
                // Optionally clear focus to hide the keyboard
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Optionally update live search here
                return false;
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Register this fragment as a listener to store updates
        Store.getInstance().addListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        // Remove listener when the fragment is paused
        Store.getInstance().removeListener(this);
    }

    // This method is called whenever Store.notifyListeners() is invoked.
    @Override
    public void onStoreUpdated() {
        // Update adapter with new searched books from the Store
        List<Book> searchedBooks = Store.getInstance().getSearchedBooks();
        if (searchedBooks != null) {
            adapter.updateData(searchedBooks);
            Log.d("HOME_SEARCH", "Adapter updated with " + searchedBooks.size() + " books.");
        } else {
            // Clear adapter if no books were found
            adapter.updateData(new ArrayList<>());
            Log.d("HOME_SEARCH", "No books found, clearing adapter.");
        }
    }
}
