package com.example.recommender.ui.groupsTabs;

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
import com.example.recommender.model.entity.Store;
import com.example.recommender.model.entity.StoreListener;
import com.example.recommender.model.entity.User;
import com.example.recommender.ui.adapter.FriendRequestsAdapter;
import java.util.ArrayList;
import java.util.List;

public class RequestTabFragment extends Fragment implements StoreListener {

    private RecyclerView rvFriendRequests;
    private FriendRequestsAdapter friendRequestsAdapter;
    private List<User> friendRequests = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_request_tab, container, false);
        rvFriendRequests = view.findViewById(R.id.rvFriendRequests);
        rvFriendRequests.setLayoutManager(new LinearLayoutManager(getContext()));
        friendRequestsAdapter = new FriendRequestsAdapter(friendRequests);
        rvFriendRequests.setAdapter(friendRequestsAdapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Store.getInstance().addListener(this);
        Controller.getInstance().listFriendRequests();
    }

    @Override
    public void onPause() {
        super.onPause();
        Store.getInstance().removeListener(this);
    }

    @Override
    public void onStoreUpdated() {
        List<User> updatedRequests = Store.getInstance().getFriendRequests();
        if (updatedRequests != null) {
            friendRequestsAdapter.updateData(updatedRequests);
            Log.d("REQUEST_TAB", "Friend requests updated: " + updatedRequests.size());
        } else {
            friendRequestsAdapter.updateData(new ArrayList<>());
            Log.d("REQUEST_TAB", "No friend requests found.");
        }
    }
}
