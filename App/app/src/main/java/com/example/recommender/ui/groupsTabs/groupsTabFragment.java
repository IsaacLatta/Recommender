package com.example.recommender.ui.groupsTabs;

import android.app.AlertDialog;
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
        View view = inflater.inflate(R.layout.fragment_groups_tab, container, false);

        rvJoinedGroups = view.findViewById(R.id.rvJoinedGroups);
        rvGroupSearchResults = view.findViewById(R.id.rvGroupSearchResults);
        etGroupSearch = view.findViewById(R.id.etGroupSearch);
        btnSearchGroup = view.findViewById(R.id.btnSearchGroup);
        fabCreateGroup = view.findViewById(R.id.fabCreateGroup);

        rvJoinedGroups.setLayoutManager(new LinearLayoutManager(getContext()));
        rvGroupSearchResults.setLayoutManager(new LinearLayoutManager(getContext()));

        List<GroupInfo> joinedGroups = Store.getInstance().getJoinedGroups();
        List<ReadingGroup> searchedGroups = Store.getInstance().getSearchedReadingGroups();

        joinedGroupsAdapter = new JoinedGroupsAdapter(this, joinedGroups);
        rvJoinedGroups.setAdapter(joinedGroupsAdapter);
        searchGroupsAdapter = new SearchGroupsAdapter(searchedGroups);

        rvJoinedGroups.setAdapter(joinedGroupsAdapter);
        rvGroupSearchResults.setAdapter(searchGroupsAdapter);

        btnSearchGroup.setOnClickListener(v -> {
            String query = etGroupSearch.getText().toString().trim();
            if (!TextUtils.isEmpty(query)) {
                Log.d("GROUPS_FRAG_DEBUG", "Searching groups with query: " + query);
                Controller.getInstance().searchReadingGroups(query);
            } else {
                Log.d("GROUPS_FRAG_DEBUG", "Search query is empty.");
            }
        });

        fabCreateGroup.setOnClickListener(v -> {
            // Show a dialog to enter the group title
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Create Group");
            final EditText input = new EditText(getContext());
            input.setHint("Enter group title");
            builder.setView(input);
            builder.setPositiveButton("Create", (dialog, which) -> {
                String groupTitle = input.getText().toString().trim();
                if (!TextUtils.isEmpty(groupTitle)) {
                    Controller.getInstance().createReadingGroup(groupTitle);
                }
            });
            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
            builder.show();
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Store.getInstance().addListener(this);
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

    public void openGroupDetails(int groupId) {
        com.example.recommender.ui.groupsTabs.GroupDetailFragment detailFragment = com.example.recommender.ui.groupsTabs.GroupDetailFragment.newInstance(groupId);
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.flFragment, detailFragment)
                .addToBackStack(null)
                .commit();
    }


}
