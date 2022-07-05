package com.example.examenfinal;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.examenfinal.Entities.Books;
import com.example.examenfinal.Factories.RetrofitFactory;
import com.example.examenfinal.Services.BookService;
import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CreateBookActivity extends AppCompatActivity {

    static final int REQUEST_PICK_IMAGE = 1001;
    Books book = new Books();
    ImageView ivCaratulaCreate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_book);
        ivCaratulaCreate = findViewById(R.id.ivCaratulaCreate);
        TextView etTituloCreate = findViewById(R.id.etTituloCreate);
        TextView etResumenCreate = findViewById(R.id.etResumenCreate);
        TextView etLatitudCreate = findViewById(R.id.etLatitudCreate);
        TextView etLongitudCreate = findViewById(R.id.etLongitudCreate);
        Button btnGaleriaCreate = findViewById(R.id.btnGaleriaCreate);
        Button btnGuardarCreate = findViewById(R.id.btnGuardarCreate);
        book.caratula = "";

        btnGaleriaCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });
        btnGuardarCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                book.titulo = etTituloCreate.getText().toString();
                book.resumen = etResumenCreate.getText().toString();
                book.latitud = Float.parseFloat(etLatitudCreate.getText().toString());
                book.longitud = Float.parseFloat(etLongitudCreate.getText().toString());
                book.favorito = false;
                Retrofit retrofit = RetrofitFactory.build();
                BookService service = retrofit.create(BookService.class);
                Call<Books> call = service.setBook(book);
                call.enqueue(new Callback<Books>() {
                    @Override
                    public void onResponse(Call<Books> call, Response<Books> response) {
                        if(!response.isSuccessful()){
                            Log.i("APP_VJ20221","Error de aplicacion");
                            Log.i("APP_VJ20221",new Gson().toJson(response.errorBody()));
                            Log.i("APP_VJ20221",new Gson().toJson(response.code()));
                        }else{
                            Log.i("APP_VJ20221","Ingreso correcto");
                            Intent intent = new Intent(view.getContext(), MainActivity.class);
                            view.getContext().startActivity(intent);
                        }
                    }

                    @Override
                    public void onFailure(Call<Books> call, Throwable t) {
                        Log.i("APP_VJ20221","No pudo conectar con el servicio");
                    }
                });
            }
        });
    }
    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_PICK_IMAGE);
    }
    @RequiresApi(api = Build.VERSION_CODES.R)
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
                Log.i("APP_VJ20221",encodedImage);

                book.caratula = encodedImage;

                ivCaratulaCreate.setImageBitmap(imageBitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

}