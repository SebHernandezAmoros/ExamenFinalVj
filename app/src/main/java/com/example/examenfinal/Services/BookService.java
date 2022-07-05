package com.example.examenfinal.Services;

import com.example.examenfinal.Entities.Books;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface BookService {
    @GET("books")
    Call<List<Books>> getBooks();

    @GET("books/{id}")
    Call<Books> getBookId(@Path("id") int id);

    @POST("books")
    Call<Books> setBook(@Body Books books);

    @DELETE("books/{id}")
    Call<Books> deleteBook(@Path("id") int id);

    @PUT("books/{id}")
    Call<Books> updateBook(@Path("id") int id, @Body Books books);
}
