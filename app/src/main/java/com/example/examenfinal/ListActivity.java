package com.example.examenfinal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.examenfinal.Adapters.BookAdapter;
import com.example.examenfinal.Entities.Books;
import com.example.examenfinal.Factories.RetrofitFactory;
import com.example.examenfinal.Services.BookService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ListActivity extends AppCompatActivity {
    List<Books> booksList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("APP_VJ20202", "CREAR LIBRO");
                Intent intent = new Intent(view.getContext(), CreateBookActivity.class);
                view.getContext().startActivity(intent);
            }
        });
        Retrofit retrofit = RetrofitFactory.build();
        BookService service = retrofit.create(BookService.class);
        Call<List<Books>> call =service.getBooks();
        call.enqueue(new Callback<List<Books>>() {
            @Override
            public void onResponse(Call<List<Books>> call, Response<List<Books>> response) {
                if(!response.isSuccessful()) {
                    Log.e("APP_VJ20202", "Error de aplicación");
                } else {
                    Log.i("APP_VJ20202", "Respuesta Correcta ListActivity");
                    booksList = response.body();
                    BookAdapter adapter = new BookAdapter(booksList);
                    RecyclerView rv =findViewById(R.id.rvListBooks);
                    rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    rv.setHasFixedSize(true);
                    rv.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Books>> call, Throwable t) {
                Log.e("APP_VJ20202", "No hubo conectividad con el servicio web");
            }
        });
    }
}