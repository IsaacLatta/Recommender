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
import com.example.recommender.ui.adapter.FriendsAdapter;
import com.example.recommender.ui.adapter.UsersAdapter;
import java.util.List;

public class friendsTabFragment extends Fragment implements StoreListener {

    private RecyclerView rvUserSearchResults;
    private RecyclerView rvFriends;
    private EditText etFriendSearch;
    private Button btnSearchFriend;

    private UsersAdapter usersAdapter;
    private FriendsAdapter friendsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends_tab, container, false);

        etFriendSearch = view.findViewById(R.id.etFriendSearch);
        btnSearchFriend = view.findViewById(R.id.btnSearchFriend);
        rvUserSearchResults = view.findViewById(R.id.rvUserSearchResults);
        rvFriends = view.findViewById(R.id.rvFriends);

        rvUserSearchResults.setLayoutManager(new LinearLayoutManager(getContext()));
        rvFriends.setLayoutManager(new LinearLayoutManager(getContext()));

        List<User> searchedUsers = Store.getInstance().getSearchedUsers();
        List<User> friends = Store.getInstance().getFriends();

        usersAdapter = new UsersAdapter(searchedUsers);
        friendsAdapter = new FriendsAdapter(friends);

        rvUserSearchResults.setAdapter(usersAdapter);
        rvFriends.setAdapter(friendsAdapter);

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
        Store.getInstance().addListener(this);
        Controller.getInstance().listFriends();
    }

    @Override
    public void onPause() {
        super.onPause();
        Store.getInstance().removeListener(this);
    }

    @Override
    public void onStoreUpdated() {
        if (friendsAdapter != null) {
            friendsAdapter.updateData(Store.getInstance().getFriends());
        }
        if (usersAdapter != null) {
            usersAdapter.updateData(Store.getInstance().getSearchedUsers());
        }
    }
}
