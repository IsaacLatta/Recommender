package com.example.recommender.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.recommender.R;
import com.example.recommender.model.entity.User;
import java.util.ArrayList;
import java.util.List;

public class GroupMembersAdapter extends RecyclerView.Adapter<GroupMembersAdapter.MemberViewHolder> {

    private List<User> members;

    public GroupMembersAdapter(List<User> members) {
        this.members = (members != null) ? members : new ArrayList<>();
    }

    @NonNull
    @Override
    public MemberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_group_member, parent, false);
        return new MemberViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MemberViewHolder holder, int position) {
        User member = members.get(position);
        holder.bind(member);
    }

    @Override
    public int getItemCount() {
        return members.size();
    }

    public void updateData(List<User> newMembers) {
        if(newMembers == null) {
            newMembers = new ArrayList<>();
        }
        this.members = newMembers;
        notifyDataSetChanged();
    }

    static class MemberViewHolder extends RecyclerView.ViewHolder {
        TextView tvMemberName;

        public MemberViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMemberName = itemView.findViewById(R.id.tvMemberName);
        }

        public void bind(User member) {
            tvMemberName.setText(member.getUsername());
        }
    }
}
