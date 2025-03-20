package com.example.recommender.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recommender.Controller;
import com.example.recommender.R;
import com.example.recommender.model.response.GroupInfo;

import java.util.ArrayList;
import java.util.List;

public class JoinedGroupsAdapter extends RecyclerView.Adapter<JoinedGroupsAdapter.GroupViewHolder> {

    private List<GroupInfo> groupsList;

    public JoinedGroupsAdapter(List<GroupInfo> groupsList) {
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
        holder.bind(group);
    }

    @Override
    public int getItemCount() {
        return groupsList.size();
    }

    public void updateData(List<GroupInfo> newGroups) {
        if(newGroups == null) {
            newGroups = new ArrayList<>();
        }
        this.groupsList = newGroups;
        notifyDataSetChanged();
    }

    static class GroupViewHolder extends RecyclerView.ViewHolder {
        TextView tvGroupName;
        TextView tvGroupRole; // Used to display the user's role
        Button btnViewGroup;

        public GroupViewHolder(@NonNull View itemView) {
            super(itemView);
            tvGroupName = itemView.findViewById(R.id.tvGroupName);
            tvGroupRole = itemView.findViewById(R.id.tvMemberCount); // repurposing this TextView to show role info
            btnViewGroup = itemView.findViewById(R.id.btnJoinOrView);
        }

        public void bind(GroupInfo group) {
            tvGroupName.setText(group.getGroupName());
            tvGroupRole.setText("Role: " + group.getRole());
            btnViewGroup.setText("View");
            btnViewGroup.setOnClickListener(v -> {
                // TODO: Implement navigation to detailed group view.
                // e.g., Controller.getInstance().openGroupDetail(group.getGroupId());
            });
        }
    }
}
