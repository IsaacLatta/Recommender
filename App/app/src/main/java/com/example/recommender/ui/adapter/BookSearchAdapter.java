package com.example.recommender.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recommender.Controller;
import com.example.recommender.R;
import com.example.recommender.model.entity.Book;
import com.example.recommender.ui.fragments.BookClickListener;

import java.util.ArrayList;
import java.util.List;

public class BookSearchAdapter extends RecyclerView.Adapter<BookSearchAdapter.BookViewHolder> {

    private List<Book> books;
    private BookClickListener bookClickListener;

    public BookSearchAdapter(List<Book> books, BookClickListener listener) {
        this.books = (books != null) ? books : new ArrayList<>();
        this.bookClickListener = listener;
    }

    public void updateData(List<Book> newBooks) {
        this.books = newBooks != null ? newBooks : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_book, parent, false);
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

    class BookViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvAuthors;
        RatingBar ratingBar;
        ImageButton btnView, btnSave;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvBookTitle);
            tvAuthors = itemView.findViewById(R.id.tvBookAuthors);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            btnView = itemView.findViewById(R.id.btnViewBook);
            btnSave = itemView.findViewById(R.id.btnSaveBook);
        }

        public void bind(Book book) {
            tvTitle.setText(book.getTitle());
            if(book.getAuthors() != null) {
                tvAuthors.setText(android.text.TextUtils.join(", ", book.getAuthors()));
            } else {
                tvAuthors.setText("Unknown Author");
            }
            if(book.getRating() != null)
                ratingBar.setRating(book.getRating());
            else
                ratingBar.setRating(0);

            // Instead of calling Controller.openBookView, we invoke the callback.
            btnView.setOnClickListener(v -> {
                if(bookClickListener != null) {
                    bookClickListener.onBookClick(book);
                }
            });

            btnSave.setOnClickListener(v -> {
                // Keep your save logic; you may call Controller.saveOrRateBook here.
                Controller.getInstance().saveOrRateBook(book.getExternalId(), "save", null);
            });
        }
    }
}
