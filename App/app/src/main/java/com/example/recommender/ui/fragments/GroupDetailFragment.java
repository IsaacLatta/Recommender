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
        if(getArguments() != null) {
            groupId = getArguments().getInt(ARG_GROUP_ID, -1);
        }
        Log.d("GroupDetailFragment", "Group ID: " + groupId);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group_detail, container, false);
        rvGroupBooks = view.findViewById(R.id.rvGroupBooks);
        rvGroupMembers = view.findViewById(R.id.rvGroupMembers);

        rvGroupBooks.setLayoutManager(new LinearLayoutManager(getContext()));
        rvGroupMembers.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        groupBooksAdapter = new GroupBooksAdapter(new ArrayList<>(), groupId);
        groupMembersAdapter = new GroupMembersAdapter(new ArrayList<>(), groupId);

        rvGroupBooks.setAdapter(groupBooksAdapter);
        rvGroupMembers.setAdapter(groupMembersAdapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Store.getInstance().addListener(this::refreshData);

        if (groupId != -1) {
            Controller.getInstance().listGroupRecommendations(groupId);
            Controller.getInstance().listGroupMembers(groupId);
        }

        refreshData();
    }

    @Override
    public void onPause() {
        super.onPause();
        Store.getInstance().removeListener(this::refreshData);
    }

    private void refreshData() {
        List<Book> recommendedBooks = new ArrayList<>();
        List<User> members = new ArrayList<>();
        List<GroupInfo> joinedGroups = Store.getInstance().getJoinedGroups();
        if (joinedGroups != null) {
            for (GroupInfo group : joinedGroups) {
                if (group.getGroupId() == groupId) {
                    recommendedBooks = group.getRecommendedBooks() != null ? group.getRecommendedBooks() : new ArrayList<>();
                    members = group.getMembers() != null ? group.getMembers() : new ArrayList<>();
                    break;
                }
            }
        }
        Log.d("GroupDetailFragment", "Recommended books count: " + recommendedBooks.size());
        Log.d("GroupDetailFragment", "Group members count: " + members.size());
        groupBooksAdapter.updateData(recommendedBooks);
        groupMembersAdapter.updateData(members);
    }
}
