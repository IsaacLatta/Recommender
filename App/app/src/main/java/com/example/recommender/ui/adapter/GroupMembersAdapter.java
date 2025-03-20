package com.example.recommender.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recommender.Controller;
import com.example.recommender.R;
import com.example.recommender.model.entity.User;
import com.example.recommender.model.entity.Store;

import java.util.ArrayList;
import java.util.List;

public class GroupMembersAdapter extends RecyclerView.Adapter<GroupMembersAdapter.MemberViewHolder> {

    private List<User> members;
    private boolean isAdmin = false;
    private int groupId;

    public GroupMembersAdapter(List<User> members, int groupId) {
        this.members = (members != null) ? members : new ArrayList<>();
        this.groupId = groupId;
    }

    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
        notifyDataSetChanged();
    }

    public void updateData(List<User> newMembers) {
        if(newMembers == null) {
            newMembers = new ArrayList<>();
        }
        this.members = newMembers;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MemberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the updated layout (item_group_member.xml)
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_group_member, parent, false);
        return new MemberViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MemberViewHolder holder, int position) {
        User member = members.get(position);
        holder.bind(member, isAdmin, groupId);
    }

    @Override
    public int getItemCount() {
        return members.size();
    }

    static class MemberViewHolder extends RecyclerView.ViewHolder {
        TextView tvMemberName;
        ImageButton btnPromote;

        public MemberViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMemberName = itemView.findViewById(R.id.tvMemberName);
            btnPromote = itemView.findViewById(R.id.btnPromote);
        }

        public void bind(User member, boolean isAdmin, int groupId) {
            tvMemberName.setText(member.getUsername());
            // Only show promote button if the current user is admin and this member is not the current user.
            int currentUserId = Integer.parseInt(Store.getInstance().getUserId());
            if(isAdmin && member.getUserId() != currentUserId) {
                btnPromote.setVisibility(View.VISIBLE);
                btnPromote.setOnClickListener(v -> {
                    Controller.getInstance().promoteGroupMember(groupId, member.getUserId());
                });
            } else {
                btnPromote.setVisibility(View.GONE);
            }
        }
    }
}
