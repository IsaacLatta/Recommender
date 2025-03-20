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
import com.example.recommender.model.entity.ReadingGroup;

import java.util.ArrayList;
import java.util.List;

public class SearchGroupsAdapter extends RecyclerView.Adapter<SearchGroupsAdapter.GroupViewHolder> {

    private List<ReadingGroup> groupsList;

    public SearchGroupsAdapter(List<ReadingGroup> groupsList) {
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
        ReadingGroup group = groupsList.get(position);
        holder.bind(group);
    }

    @Override
    public int getItemCount() {
        return groupsList.size();
    }

    public void updateData(List<ReadingGroup> newGroups) {
        if(newGroups == null) {
            newGroups = new ArrayList<>();
        }
        this.groupsList = newGroups;
        notifyDataSetChanged();
    }

    static class GroupViewHolder extends RecyclerView.ViewHolder {
        TextView tvGroupName;
        TextView tvStatus; // Used to indicate the join status
        Button btnJoinGroup;

        public GroupViewHolder(@NonNull View itemView) {
            super(itemView);
            tvGroupName = itemView.findViewById(R.id.tvGroupName);
            tvStatus = itemView.findViewById(R.id.tvMemberCount); // repurposed to show status text
            btnJoinGroup = itemView.findViewById(R.id.btnJoinOrView);
        }

        public void bind(ReadingGroup group) {
            tvGroupName.setText(group.getGroupName());
            tvStatus.setText("Not joined");
            btnJoinGroup.setText("Join");
            btnJoinGroup.setOnClickListener(v -> {
                Controller.getInstance().joinReadingGroup(group.getGroupId());
            });
        }
    }
}
