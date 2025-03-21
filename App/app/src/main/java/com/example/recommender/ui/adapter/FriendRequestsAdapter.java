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
import java.util.ArrayList;
import java.util.List;

public class FriendRequestsAdapter extends RecyclerView.Adapter<FriendRequestsAdapter.RequestViewHolder> {

    private List<User> requests;

    public FriendRequestsAdapter(List<User> requests) {
        this.requests = (requests != null) ? requests : new ArrayList<>();
    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_friend_request, parent, false);
        return new RequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder holder, int position) {
        User user = requests.get(position);
        holder.bind(user);
    }

    @Override
    public int getItemCount() {
        return requests.size();
    }

    public void updateData(List<User> newRequests) {
        this.requests = (newRequests != null) ? newRequests : new ArrayList<>();
        notifyDataSetChanged();
    }

    static class RequestViewHolder extends RecyclerView.ViewHolder {
        TextView tvRequestUsername;
        ImageButton btnApproveRequest, btnDenyRequest;

        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRequestUsername = itemView.findViewById(R.id.tvRequestUsername);
            btnApproveRequest = itemView.findViewById(R.id.btnApproveRequest);
            btnDenyRequest = itemView.findViewById(R.id.btnDenyRequest);
        }

        public void bind(User user) {
            tvRequestUsername.setText(user.getUsername());
            btnApproveRequest.setOnClickListener(v ->
                    Controller.getInstance().handleFriendRequest(user, true)
            );
            btnDenyRequest.setOnClickListener(v ->
                    Controller.getInstance().handleFriendRequest(user, false)
            );
        }
    }
}
