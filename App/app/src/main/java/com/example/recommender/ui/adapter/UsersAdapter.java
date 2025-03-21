package com.example.recommender.ui.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.recommender.Controller;
import com.example.recommender.R;
import com.example.recommender.model.entity.User;
import java.util.ArrayList;
import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder> {

    private List<User> users;

    public UsersAdapter(List<User> users) {
        this.users = (users != null) ? users : new ArrayList<>();
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_search_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = users.get(position);
        holder.bind(user);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public void updateData(List<User> newUsers) {
        if (newUsers == null) {
            newUsers = new ArrayList<>();
        }
        this.users = newUsers;
        notifyDataSetChanged();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView tvUsername;
        ImageButton btnAddFriend;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            btnAddFriend = itemView.findViewById(R.id.btnAddFriend);
        }

        public void bind(User user) {
            tvUsername.setText(user.getUsername());
            btnAddFriend.setOnClickListener(v -> {
                Log.d("FRIEND_ADD", "Sending friend request");
                Controller.getInstance().sendFriendRequest(user);
            });
        }
    }
}
