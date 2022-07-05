package com.example.examenfinal.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.examenfinal.Dao.BookDao;
import com.example.examenfinal.Entities.Books;

@Database(entities = {Books.class }, version = 1)
public abstract class AppDataBase extends RoomDatabase {
    public abstract BookDao bookDao();

    public static AppDataBase getDataBase(Context context){
        return Room.databaseBuilder(context, AppDataBase.class, "com.example.examenfinal.Database_db")
                .allowMainThreadQueries()
                .build();
    }
}