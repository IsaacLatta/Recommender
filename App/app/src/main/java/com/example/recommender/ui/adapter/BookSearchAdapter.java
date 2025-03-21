package com.example.recommender.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;  // Import ImageView
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
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
        this.books = (newBooks != null) ? newBooks : new ArrayList<>();
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
        ImageView ivBookCover; // Reference to the ImageView

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvBookTitle);
            tvAuthors = itemView.findViewById(R.id.tvBookAuthors);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            btnView = itemView.findViewById(R.id.btnViewBook);
            btnSave = itemView.findViewById(R.id.btnSaveBook);
            ivBookCover = itemView.findViewById(R.id.ivBookCover); // Bind the ImageView
        }

        public void bind(Book book) {
            tvTitle.setText(book.getTitle());
            if (book.getAuthors() != null) {
                tvAuthors.setText(android.text.TextUtils.join(", ", book.getAuthors()));
            } else {
                tvAuthors.setText("Unknown Author");
            }
            ratingBar.setRating(book.getRating() != null ? book.getRating() : 0);

            Glide.with(itemView.getContext())
                    .load(book.getThumbnail())
                    .placeholder(R.drawable.ic_book_placeholder)
                    .into(ivBookCover);

            btnView.setOnClickListener(v -> {
                if (bookClickListener != null) {
                    bookClickListener.onBookClick(book);
                }
            });
            btnSave.setOnClickListener(v -> {
                Controller.getInstance().saveOrRateBook(book.getExternalId(), "save", null);
            });
        }
    }
}
