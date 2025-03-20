package com.example.recommender.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.recommender.R;
import com.example.recommender.model.entity.Book;
import java.util.ArrayList;
import java.util.List;

public class GroupBooksAdapter extends RecyclerView.Adapter<GroupBooksAdapter.BookViewHolder> {

    private List<Book> books;

    public GroupBooksAdapter(List<Book> books) {
        this.books = (books != null) ? books : new ArrayList<>();
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_group_book, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = books.get(position);
        holder.bind(book);
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public void updateData(List<Book> newBooks) {
        if(newBooks == null) {
            newBooks = new ArrayList<>();
        }
        this.books = newBooks;
        notifyDataSetChanged();
    }

    static class BookViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvAuthors, tvStatus;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvBookTitle);
            tvAuthors = itemView.findViewById(R.id.tvBookAuthors);
            tvStatus = itemView.findViewById(R.id.tvBookStatus);
        }

        public void bind(Book book) {
            tvTitle.setText(book.getTitle());
            if(book.getAuthors() != null) {
                tvAuthors.setText(android.text.TextUtils.join(", ", book.getAuthors()));
            } else {
                tvAuthors.setText("Unknown Author");
            }
            tvStatus.setText(book.getStatus() != null ? book.getStatus() : "No status");
        }
    }
}
