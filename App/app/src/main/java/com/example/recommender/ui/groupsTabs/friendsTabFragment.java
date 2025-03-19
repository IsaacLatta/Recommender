package com.example.recommender.ui.groupsTabs;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recommender.Controller;
import com.example.recommender.R;
import com.example.recommender.model.entity.Store;
import com.example.recommender.model.entity.StoreListener;
import com.example.recommender.model.entity.User;
import com.example.recommender.ui.adapter.FriendRequestsAdapter;
import com.example.recommender.ui.adapter.FriendsAdapter;
import com.example.recommender.ui.adapter.UsersAdapter;

import java.util.List;

public class friendsTabFragment extends Fragment implements StoreListener {

    private RecyclerView rvFriendRequests;
    private RecyclerView rvFriends;
    private RecyclerView rvUserSearchResults; // new for search users results
    private EditText etFriendSearch;
    private Button btnSearchFriend;

    private FriendRequestsAdapter friendRequestsAdapter;
    private FriendsAdapter friendsAdapter;
    private UsersAdapter usersAdapter; // new adapter for searched users

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout (make sure your fragment layout includes a RecyclerView with id "rvUserSearchResults")
        View view = inflater.inflate(R.layout.fragment_friends_tab, container, false);

        // Bind views
        rvFriendRequests = view.findViewById(R.id.rvFriendRequests);
        rvFriends = view.findViewById(R.id.rvFriends);
        rvUserSearchResults = view.findViewById(R.id.rvUserSearchResults); // new binding
        etFriendSearch = view.findViewById(R.id.etFriendSearch);
        btnSearchFriend = view.findViewById(R.id.btnSearchFriend);

        // Setup RecyclerViews
        rvFriendRequests.setLayoutManager(new LinearLayoutManager(getContext()));
        rvFriends.setLayoutManager(new LinearLayoutManager(getContext()));
        rvUserSearchResults.setLayoutManager(new LinearLayoutManager(getContext())); // new

        // Initialize adapters with existing data (if any)
        List<User> friendRequests = Store.getInstance().getFriendRequests(); // may be null
        List<User> friends = Store.getInstance().getFriends();               // may be null
        List<User> searchedUsers = Store.getInstance().getSearchedUsers();     // may be null

        friendRequestsAdapter = new FriendRequestsAdapter(friendRequests);
        friendsAdapter = new FriendsAdapter(friends);
        usersAdapter = new UsersAdapter(searchedUsers); // new

        rvFriendRequests.setAdapter(friendRequestsAdapter);
        rvFriends.setAdapter(friendsAdapter);
        rvUserSearchResults.setAdapter(usersAdapter); // new

        // On click for search – this will call Controller.searchFriends(query)
        btnSearchFriend.setOnClickListener(v -> {
            String query = etFriendSearch.getText().toString().trim();
            if (!TextUtils.isEmpty(query)) {
                Log.d("FRIEND_FRAG_DEBUG", "Searching for users with query: " + query);
                Controller.getInstance().searchFriends(query);
            } else {
                Log.d("FRIEND_FRAG_DEBUG", "Search query is empty.");
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Register this fragment as a listener to the Store.
        Store.getInstance().addListener(this);
        // Refresh friends and friend requests from server.
        Controller.getInstance().listFriends();
        Controller.getInstance().listFriendRequests();
        updateUIFromStore();
    }

    @Override
    public void onPause() {
        super.onPause();
        Store.getInstance().removeListener(this);
    }

    @Override
    public void onStoreUpdated() {
        updateUIFromStore();
    }

    private void updateUIFromStore() {
        if (friendsAdapter != null) {
            friendsAdapter.updateData(Store.getInstance().getFriends());
        }
        if (friendRequestsAdapter != null) {
            friendRequestsAdapter.updateData(Store.getInstance().getFriendRequests());
        }
        if (usersAdapter != null) {
            usersAdapter.updateData(Store.getInstance().getSearchedUsers());
        }
    }
}
