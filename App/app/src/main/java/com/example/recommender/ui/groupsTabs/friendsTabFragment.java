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

import java.util.List;

public class friendsTabFragment extends Fragment implements StoreListener {

    private RecyclerView rvFriendRequests;
    private RecyclerView rvFriends;
    private EditText etFriendSearch;
    private Button btnSearchFriend;

    private FriendRequestsAdapter friendRequestsAdapter;
    private FriendsAdapter friendsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends_tab, container, false);

        // Bind views
        rvFriendRequests = view.findViewById(R.id.rvFriendRequests);
        rvFriends        = view.findViewById(R.id.rvFriends);
        etFriendSearch   = view.findViewById(R.id.etFriendSearch);
        btnSearchFriend  = view.findViewById(R.id.btnSearchFriend);

        // Setup your RecyclerViews
        rvFriendRequests.setLayoutManager(new LinearLayoutManager(getContext()));
        rvFriends.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize adapters with existing data (if any)
        List<User> friendRequests = Store.getInstance().getFriendRequests(); // might be null
        List<User> friends        = Store.getInstance().getFriends();        // might be null

        friendRequestsAdapter = new FriendRequestsAdapter(friendRequests);
        friendsAdapter        = new FriendsAdapter(friends);

        rvFriendRequests.setAdapter(friendRequestsAdapter);
        rvFriends.setAdapter(friendsAdapter);

        // On click for search
        btnSearchFriend.setOnClickListener(v -> {
            String query = etFriendSearch.getText().toString().trim();
            if (!TextUtils.isEmpty(query)) {
                Log.d("FRIEND_FRAG_DEBUG", "Searching for friends with query: " + query);
                Controller.getInstance().searchFriends(query);
            }
            else {
                Log.d("FRIEND_FRAG_DEBUG", "its empty!");
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Store.getInstance().addListener(this);
        Controller.getInstance().listFriends();        // triggers an async call, result in Store
        Controller.getInstance().listFriendRequests(); // triggers an async call, result in Store
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
    }
}
