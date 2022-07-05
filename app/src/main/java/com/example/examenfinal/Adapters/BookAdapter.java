package com.example.examenfinal.Adapters;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.examenfinal.EditBookActivity;
import com.example.examenfinal.Entities.Books;
import com.example.examenfinal.R;
import com.google.gson.Gson;

import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder>{
    List<Books> booksList;

    public BookAdapter(List<Books> booksList) {
        this.booksList = booksList;
    }

    @NonNull
    @Override
    public BookAdapter.BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_item, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookAdapter.BookViewHolder holder, int position) {
        Books books= booksList.get(position);
        View itemView = holder.itemView;
        ImageView ivCaratulaList = itemView.findViewById(R.id.ivCaratulaList);
        TextView tvTituloList = itemView.findViewById(R.id.tvTituloList);

        tvTituloList.setText("Titulo: "+books.titulo);

        byte[] decodedString = Base64.decode(books.caratula, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        ivCaratulaList.setImageBitmap(decodedByte);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(itemView.getContext(), EditBookActivity.class);
                String BookJson = new Gson().toJson(books);
                Log.i("APP_VJ20202", BookJson);
                intent.putExtra("BOOK", BookJson);
                itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return booksList.size();
    }

    public class BookViewHolder extends RecyclerView.ViewHolder implements AdapterView.OnItemClickListener{
        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        }
    }
}
