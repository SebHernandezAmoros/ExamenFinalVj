package com.example.examenfinal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.examenfinal.Adapters.BookAdapter;
import com.example.examenfinal.Dao.BookDao;
import com.example.examenfinal.Database.AppDataBase;
import com.example.examenfinal.Entities.Books;

import java.util.ArrayList;
import java.util.List;

public class FavActivity extends AppCompatActivity {
    AppDataBase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav);

        db=AppDataBase.getDataBase(getApplicationContext());
        BookDao bookDao = db.bookDao();

        BookAdapter adapter = new BookAdapter(bookDao.getAll());
        RecyclerView rv =findViewById(R.id.rvListFavs);
        rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rv.setHasFixedSize(true);
        rv.setAdapter(adapter);
    }
}