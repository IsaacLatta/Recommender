package com.example.recommender.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recommender.R;
import com.example.recommender.model.response.GroupInfo;
import com.example.recommender.ui.groupsTabs.GroupDetailFragment;
import com.example.recommender.ui.groupsTabs.groupsTabFragment;

import java.util.ArrayList;
import java.util.List;

public class JoinedGroupsAdapter extends RecyclerView.Adapter<JoinedGroupsAdapter.GroupViewHolder> {

    private groupsTabFragment parentFragment;
    private List<GroupInfo> groupsList;

    public JoinedGroupsAdapter(groupsTabFragment parentFragment, List<GroupInfo> groupsList) {
        this.parentFragment = parentFragment;
        this.groupsList = (groupsList != null) ? groupsList : new ArrayList<>();
    }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_group, parent, false);
        return new GroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
        GroupInfo group = groupsList.get(position);
        holder.bind(group, parentFragment);
    }

    @Override
    public int getItemCount() {
        return groupsList.size();
    }

    public void updateData(List<GroupInfo> newGroups) {
        if (newGroups == null) {
            newGroups = new ArrayList<>();
        }
        this.groupsList = newGroups;
        notifyDataSetChanged();
    }

    static class GroupViewHolder extends RecyclerView.ViewHolder {
        TextView tvGroupName;
        TextView tvGroupRole; // repurposed to show role info
        Button btnViewGroup;

        public GroupViewHolder(@NonNull View itemView) {
            super(itemView);
            tvGroupName = itemView.findViewById(R.id.tvGroupName);
            tvGroupRole = itemView.findViewById(R.id.tvMemberCount);
            btnViewGroup = itemView.findViewById(R.id.btnJoinOrView);
        }

        public void bind(GroupInfo group, groupsTabFragment parentFragment) {
            tvGroupName.setText(group.getGroupName());
            tvGroupRole.setText("Role: " + group.getRole());
            btnViewGroup.setText("View");
            btnViewGroup.setOnClickListener(v -> {
                parentFragment.openGroupDetails(group.getGroupId());
            });
        }
    }
}
