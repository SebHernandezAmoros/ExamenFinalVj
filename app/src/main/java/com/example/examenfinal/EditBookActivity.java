package com.example.examenfinal;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.examenfinal.Entities.Books;
import com.example.examenfinal.Factories.RetrofitFactory;
import com.example.examenfinal.Services.BookService;
import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class EditBookActivity extends AppCompatActivity {

    static final int REQUEST_PICK_IMAGE = 1001;
    ImageView ivCaratulaEdit;
    Books book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_book);
        ivCaratulaEdit = findViewById(R.id.ivCaratulaEdit);
        EditText etTituloEdit = findViewById(R.id.etTituloEdit);
        EditText etResumenEdit = findViewById(R.id.etResumenEdit);
        Button btnGaleriaEdit = findViewById(R.id.btnGaleriaEdit);
        Button btnUpdate = findViewById(R.id.btnUpdate);
        Button btnMapView = findViewById(R.id.btnMapView);
        Button btnFavorito = findViewById(R.id.btnFavorito);

        String getBookContext = getIntent().getStringExtra("BOOK");
        book = new Gson().fromJson(getBookContext, Books.class);

        if(book.favorito){
            btnFavorito.setText("No favorito");
        }else {
            btnFavorito.setText("Favorito");
        }

        etTituloEdit.setText(book.titulo);
        etResumenEdit.setText(book.resumen);
        //Convertir el base 64 a bitmap
        byte[] decodedString = Base64.decode(book.caratula, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        ivCaratulaEdit.setImageBitmap(decodedByte);
        btnGaleriaEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Books bookUpdate = new Books();
                bookUpdate.id = book.id;
                bookUpdate.titulo = etTituloEdit.getText().toString();
                bookUpdate.resumen = etResumenEdit.getText().toString();
                bookUpdate.caratula = book.caratula;
                bookUpdate.latitud = book.latitud;
                bookUpdate.longitud= book.longitud;
                bookUpdate.favorito = book.favorito;

                Retrofit retrofit= RetrofitFactory.build();
                BookService service= retrofit.create(BookService.class);
                Call<Books> call =service.updateBook(bookUpdate.id, bookUpdate);
                call.enqueue(new Callback<Books>() {
                    @Override
                    public void onResponse(Call<Books> call, Response<Books> response) {
                        if(!response.isSuccessful()) {
                            Log.e("APP_VJ20202", "Error de aplicación");
                            Intent intent = new Intent(view.getContext(), MainActivity.class);
                            view.getContext().startActivity(intent);
                        } else {
                            Log.i("APP_VJ20202", "Respuesta Correcta UPDATE");
                            Intent intent = new Intent(view.getContext(), MainActivity.class);
                            view.getContext().startActivity(intent);
                        }
                    }

                    @Override
                    public void onFailure(Call<Books> call, Throwable t) {
                        Log.e("APP_VJ20202", "No hubo conectividad con el servicio web");
                    }
                });
            }
        });
        btnFavorito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(book.favorito){
                    book.favorito = false;
                }else {
                    book.favorito = true;
                }
            }
        });
        btnMapView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MapsActivity.class);
                String BookJson = new Gson().toJson(book);
                Log.i("APP_VJ20202", BookJson);
                intent.putExtra("LOCATION", BookJson);
                view.getContext().startActivity(intent);
            }
        });
    }
    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_PICK_IMAGE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            try {
                InputStream inputStream = getContentResolver().openInputStream(data.getData());
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                Bitmap imageBitmap = BitmapFactory.decodeStream(bufferedInputStream);
                //Conversion de bitmap(foto) a base 64
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 1, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream .toByteArray();
                //guardar el base64 en un string
                String encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
                Log.i("APP_VJ20202",encodedImage);
                book.caratula = encodedImage;

                ivCaratulaEdit.setImageBitmap(imageBitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}