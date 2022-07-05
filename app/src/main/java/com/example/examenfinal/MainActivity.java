package com.example.examenfinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.examenfinal.Dao.BookDao;
import com.example.examenfinal.Database.AppDataBase;
import com.example.examenfinal.Entities.Books;
import com.example.examenfinal.Factories.RetrofitFactory;
import com.example.examenfinal.Services.BookService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {
    AppDataBase db;
    List<Books> booksList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnListLibros = findViewById(R.id.btnListLibros);
        Button btnFavLibros = findViewById(R.id.btnFavLibros);

        btnListLibros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ListActivity.class);
                view.getContext().startActivity(intent);
            }
        });
        btnFavLibros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), FavActivity.class);
                view.getContext().startActivity(intent);
            }
        });
        Retrofit retrofit = RetrofitFactory.build();
        BookService service = retrofit.create(BookService.class);
        Call<List<Books>> call= service.getBooks();
        call.enqueue(new Callback<List<Books>>() {
            @Override
            public void onResponse(Call<List<Books>> call, Response<List<Books>> response) {
                if(!response.isSuccessful()){
                    Log.i("APP_VJ20221","Error de aplicacion");
                }else{
                    booksList = response.body();
                    registrarBooksFav(booksList);
                }
            }

            @Override
            public void onFailure(Call<List<Books>> call, Throwable t) {
                Log.i("APP_VJ20221","No pudo conectar con el servicio");
            }
        });

    }
    void registrarBooksFav(List<Books> booksListFav){
        db=AppDataBase.getDataBase(getApplicationContext());
        BookDao bookDao = db.bookDao();

        bookDao.VaciarLista();
        for (Books boo : booksListFav){
            if(boo.favorito){
                bookDao.create(boo);
            }
        }
    }
}