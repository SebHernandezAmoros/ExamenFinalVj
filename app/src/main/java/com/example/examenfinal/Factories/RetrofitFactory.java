package com.example.examenfinal.Factories;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitFactory {
    public static Retrofit build() {

        return new Retrofit.Builder()
                .baseUrl("https://6284e8eaa48bd3c40b77c280.mockapi.io/api/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

    }
}
