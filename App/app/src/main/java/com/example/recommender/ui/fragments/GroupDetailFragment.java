package com.example.recommender.ui.groupsTabs;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.recommender.Controller;
import com.example.recommender.R;
import com.example.recommender.model.entity.Book;
import com.example.recommender.model.entity.Store;
import com.example.recommender.model.response.GroupInfo;
import com.example.recommender.ui.adapter.GroupBooksAdapter;
import com.example.recommender.ui.adapter.GroupMembersAdapter;
import com.example.recommender.model.entity.User;

import java.util.ArrayList;
import java.util.List;

public class GroupDetailFragment extends Fragment {

    private static final String ARG_GROUP_ID = "GROUP_ID";

    private int groupId = -1;
    private RecyclerView rvBooks, rvMembers;
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
        if (groupId == -1) {
            Log.e("GroupDetailFragment", "Invalid group ID passed");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group_detail, container, false);

        rvBooks = view.findViewById(R.id.rvGroupBooks);
        rvMembers = view.findViewById(R.id.rvGroupMembers);

        rvBooks.setLayoutManager(new LinearLayoutManager(getContext()));
        rvMembers.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        groupBooksAdapter = new GroupBooksAdapter(new ArrayList<>());
        groupMembersAdapter = new GroupMembersAdapter(new ArrayList<>());

        rvBooks.setAdapter(groupBooksAdapter);
        rvMembers.setAdapter(groupMembersAdapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Store.getInstance().addListener(this::refreshData);
        loadGroupDetails();
        refreshData();
    }

    @Override
    public void onPause() {
        super.onPause();
        Store.getInstance().removeListener(this::refreshData);
    }

    private void loadGroupDetails() {
        if (groupId != -1) {
            Controller.getInstance().listGroupRecommendations(groupId);
        }
    }

    private void refreshData() {
        if (groupId == -1) return;
        List<Book> recommendedBooks = new ArrayList<>();
        List<GroupInfo> joinedGroups = Store.getInstance().getJoinedGroups();
        if (joinedGroups != null) {
            for (GroupInfo g : joinedGroups) {
                if (g.getGroupId() == groupId && g.getRecommendedBooks() != null) {
                    recommendedBooks = g.getRecommendedBooks();
                    break;
                }
            }
        }
        groupBooksAdapter.updateData(recommendedBooks);
    }
}
