package com.example.recommender.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;          // <-- IMPORTANT: needed for onCreateViewHolder
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;     // <-- IMPORTANT: for @NonNull annotation
import androidx.recyclerview.widget.RecyclerView;

import com.example.recommender.Controller;
import com.example.recommender.R;       // <-- Make sure R is imported from your package
import com.example.recommender.model.entity.User;

import java.util.ArrayList;
import java.util.List;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.FriendsViewHolder> {

    private List<User> friendsList;

    public FriendsAdapter(List<User> friendsList) {
        this.friendsList = (friendsList != null) ? friendsList : new ArrayList<>();
    }

    @NonNull
    @Override
    public FriendsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_friend, parent, false);
        return new FriendsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendsViewHolder holder, int position) {
        User friend = friendsList.get(position);
        holder.bind(friend);
    }

    @Override
    public int getItemCount() {
        return friendsList.size();
    }

    public void updateData(List<User> newFriends) {
        if (newFriends == null) {
            newFriends = new ArrayList<>();
        }
        this.friendsList = newFriends;
        notifyDataSetChanged();
    }

    static class FriendsViewHolder extends RecyclerView.ViewHolder {
        TextView tvFriendName;
        Button btnRemoveFriend;

        public FriendsViewHolder(@NonNull View itemView) {
            super(itemView);

            tvFriendName    = itemView.findViewById(R.id.tvFriendName);
            btnRemoveFriend = itemView.findViewById(R.id.btnRemoveFriend);
        }

        public void bind(User friend) {
            tvFriendName.setText(friend.getUsername());
            btnRemoveFriend.setOnClickListener(v -> {
                Controller.getInstance().removeFriend(friend);
            });
        }
    }
}
