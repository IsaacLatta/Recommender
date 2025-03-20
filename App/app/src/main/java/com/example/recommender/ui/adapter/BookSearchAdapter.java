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
import java.util.List;

public class BookSearchAdapter extends RecyclerView.Adapter<BookSearchAdapter.BookViewHolder> {

    private List<Book> books;

    public BookSearchAdapter(List<Book> books) {
        this.books = books;
    }

    public void updateData(List<Book> newBooks) {
        this.books = newBooks;
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
        return books != null ? books.size() : 0;
    }

    static class BookViewHolder extends RecyclerView.ViewHolder {
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
            if(book.getAuthors() != null)
                tvAuthors.setText(android.text.TextUtils.join(", ", book.getAuthors()));
            else
                tvAuthors.setText("Unknown Author");

            // Set rating if available
            if(book.getRating() != null)
                ratingBar.setRating(book.getRating());
            else
                ratingBar.setRating(0);

            // Wire up view button to open a detailed book view (implement this in your Controller or Fragment)
            btnView.setOnClickListener(v -> {
//                Controller.getInstance().openBookView(book);
            });

            // Wire up save button to call the save or rate endpoint
            btnSave.setOnClickListener(v -> {
                Controller.getInstance().saveOrRateBook(book.getExternalId(), "save", null);
            });
        }
    }
}
