package com.example.recommender.ui.groupsTabs;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recommender.Controller;
import com.example.recommender.R;
import com.example.recommender.model.entity.Book;
import com.example.recommender.model.entity.Store;
import com.example.recommender.model.response.GroupInfo;
import com.example.recommender.model.entity.User;
import com.example.recommender.ui.adapter.GroupBooksAdapter;
import com.example.recommender.ui.adapter.GroupMembersAdapter;

import java.util.ArrayList;
import java.util.List;

public class GroupDetailFragment extends Fragment {

    private static final String ARG_GROUP_ID = "GROUP_ID";
    private int groupId = -1;
    private RecyclerView rvGroupBooks;
    private RecyclerView rvGroupMembers;
    private GroupBooksAdapter groupBooksAdapter;
    private GroupMembersAdapter groupMembersAdapter;

    public static GroupDetailFragment newInstance(int groupId) {
        GroupDetailFragment fragment = new GroupDetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_GROUP_ID, groupId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            groupId = getArguments().getInt(ARG_GROUP_ID, -1);
        }
        Log.d("GroupDetailFragment", "Group ID: " + groupId);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the detail layout which must have rvGroupBooks and rvGroupMembers
        View view = inflater.inflate(R.layout.fragment_group_detail, container, false);
        rvGroupBooks = view.findViewById(R.id.rvGroupBooks);
        rvGroupMembers = view.findViewById(R.id.rvGroupMembers);

        // Set up RecyclerViews
        rvGroupBooks.setLayoutManager(new LinearLayoutManager(getContext()));
        rvGroupMembers.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        groupBooksAdapter = new GroupBooksAdapter(new ArrayList<>());
        groupMembersAdapter = new GroupMembersAdapter(new ArrayList<>());

        rvGroupBooks.setAdapter(groupBooksAdapter);
        rvGroupMembers.setAdapter(groupMembersAdapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Request the recommended books for this group
        if (groupId != -1) {
            Controller.getInstance().listGroupRecommendations(groupId);
        }
        // For group members, if you don't have an API call, you can simulate with dummy data.
        List<User> members = new ArrayList<>();
        // If you have actual group members in your Store, use them here.
        // Otherwise, for demo purposes, we add dummy members:
        if (members.isEmpty()) {
            members.add(new User(1, "Alice"));
            members.add(new User(2, "Bob"));
            members.add(new User(3, "Charlie"));
        }
        groupMembersAdapter.updateData(members);

        // Listen for store updates so that when recommended books are fetched, we refresh the UI.
        Store.getInstance().addListener(this::refreshData);
        refreshData();
    }

    @Override
    public void onPause() {
        super.onPause();
        Store.getInstance().removeListener(this::refreshData);
    }

    private void refreshData() {
        // Retrieve the recommended books for this group from the Store.
        List<Book> recommendedBooks = new ArrayList<>();
        List<GroupInfo> joinedGroups = Store.getInstance().getJoinedGroups();
        if (joinedGroups != null) {
            for (GroupInfo group : joinedGroups) {
                if (group.getGroupId() == groupId && group.getRecommendedBooks() != null) {
                    recommendedBooks = group.getRecommendedBooks();
                    break;
                }
            }
        }
        Log.d("GroupDetailFragment", "Number of recommended books: " + recommendedBooks.size());
        groupBooksAdapter.updateData(recommendedBooks);
    }
}
