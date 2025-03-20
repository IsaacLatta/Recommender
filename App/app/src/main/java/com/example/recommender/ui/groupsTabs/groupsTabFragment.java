package com.example.recommender.ui.groupsTabs;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recommender.Controller;
import com.example.recommender.R;
import com.example.recommender.model.entity.Store;
import com.example.recommender.model.entity.StoreListener;
import com.example.recommender.model.response.GroupInfo;
import com.example.recommender.model.entity.ReadingGroup;
import com.example.recommender.ui.adapter.JoinedGroupsAdapter;
import com.example.recommender.ui.adapter.SearchGroupsAdapter;

import java.util.List;

public class groupsTabFragment extends Fragment implements StoreListener {

    private RecyclerView rvJoinedGroups;
    private RecyclerView rvGroupSearchResults;
    private EditText etGroupSearch;
    private Button btnSearchGroup;
    private FloatingActionButton fabCreateGroup;

    private JoinedGroupsAdapter joinedGroupsAdapter;
    private SearchGroupsAdapter searchGroupsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_groups_tab, container, false);

        // Bind views from layout
        rvJoinedGroups = view.findViewById(R.id.rvJoinedGroups);
        rvGroupSearchResults = view.findViewById(R.id.rvGroupSearchResults);
        etGroupSearch = view.findViewById(R.id.etGroupSearch);
        btnSearchGroup = view.findViewById(R.id.btnSearchGroup);
        fabCreateGroup = view.findViewById(R.id.fabCreateGroup);

        // Setup RecyclerViews with linear layouts
        rvJoinedGroups.setLayoutManager(new LinearLayoutManager(getContext()));
        rvGroupSearchResults.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize adapters with existing Store data
        List<GroupInfo> joinedGroups = Store.getInstance().getJoinedGroups();
        List<ReadingGroup> searchedGroups = Store.getInstance().getSearchedReadingGroups();

        joinedGroupsAdapter = new JoinedGroupsAdapter(joinedGroups);
        searchGroupsAdapter = new SearchGroupsAdapter(searchedGroups);

        rvJoinedGroups.setAdapter(joinedGroupsAdapter);
        rvGroupSearchResults.setAdapter(searchGroupsAdapter);

        // On-click listener for group search button
        btnSearchGroup.setOnClickListener(v -> {
            String query = etGroupSearch.getText().toString().trim();
            if (!TextUtils.isEmpty(query)) {
                Log.d("GROUPS_FRAG_DEBUG", "Searching groups with query: " + query);
                Controller.getInstance().searchReadingGroups(query);
            } else {
                Log.d("GROUPS_FRAG_DEBUG", "Search query is empty.");
            }
        });

        // On-click listener for FloatingActionButton to create a new group
        fabCreateGroup.setOnClickListener(v -> {
            // For demonstration, we're using a hardcoded group name.
            // In production, open a dialog to let the user enter a group name.
            Controller.getInstance().createReadingGroup("New Group");
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Register this fragment as a listener to the Store updates.
        Store.getInstance().addListener(this);
        // Refresh the joined groups from the backend.
        Controller.getInstance().listUserGroups();
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
        if (joinedGroupsAdapter != null) {
            joinedGroupsAdapter.updateData(Store.getInstance().getJoinedGroups());
        }
        if (searchGroupsAdapter != null) {
            searchGroupsAdapter.updateData(Store.getInstance().getSearchedReadingGroups());
        }
    }
}
