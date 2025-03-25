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
import com.example.recommender.model.entity.Book;

import java.util.ArrayList;
import java.util.List;

public class GroupBooksAdapter extends RecyclerView.Adapter<GroupBooksAdapter.BookViewHolder> {

    private List<Book> books;
    private boolean isAdmin = false;
    private int groupId;

    public GroupBooksAdapter(List<Book> books, int groupId) {
        this.books = (books != null) ? books : new ArrayList<>();
        this.groupId = groupId;
    }

    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
        notifyDataSetChanged();
    }

    public void updateData(List<Book> newBooks) {
        if(newBooks == null) {
            newBooks = new ArrayList<>();
        }
        this.books = newBooks;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_group_book, parent, false);
        return new BookViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = books.get(position);
        holder.bind(book, isAdmin, groupId);
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    static class BookViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvAuthors, tvStatus;
        ImageButton btnApprove, btnDeny;
        View adminButtonsLayout;
        GroupBooksAdapter adapter;  // reference to adapter

        public BookViewHolder(@NonNull View itemView, GroupBooksAdapter adapter) {
            super(itemView);
            this.adapter = adapter;
            tvTitle = itemView.findViewById(R.id.tvBookTitle);
            tvAuthors = itemView.findViewById(R.id.tvBookAuthors);
            tvStatus = itemView.findViewById(R.id.tvBookStatus);
            adminButtonsLayout = itemView.findViewById(R.id.adminButtonsLayout);
            btnApprove = itemView.findViewById(R.id.btnApprove);
            btnDeny = itemView.findViewById(R.id.btnDeny);
        }

        public void bind(Book book, boolean isAdmin, int groupId) {
            tvTitle.setText(book.getTitle());
            if(book.getAuthors() != null) {
                tvAuthors.setText(android.text.TextUtils.join(", ", book.getAuthors()));
            } else {
                tvAuthors.setText("Unknown Author");
            }
            tvStatus.setText(book.getStatus() != null ? book.getStatus() : "No status");

            if(isAdmin) {
                adminButtonsLayout.setVisibility(View.VISIBLE);
                btnApprove.setOnClickListener(v -> {
                    Controller.getInstance().handleBookRecommendation(groupId, book.getExternalId(), true);
                    tvStatus.setText("Approved");
                });
                btnDeny.setOnClickListener(v -> {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        Controller.getInstance().handleBookRecommendation(groupId, book.getExternalId(), false);
                        adapter.books.remove(pos);
                        adapter.notifyItemRemoved(pos);
                    }
                });
            } else {
                adminButtonsLayout.setVisibility(View.GONE);
            }
        }
    }
}
