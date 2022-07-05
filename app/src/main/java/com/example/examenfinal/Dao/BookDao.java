package com.example.examenfinal.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.examenfinal.Entities.Books;

import java.util.List;

@Dao
public interface BookDao {
    @Query("SELECT * FROM books")
    List<Books> getAll();

    @Query("SELECT *FROM books WHERE id = :idBooks")
    Books find (int idBooks);

    @Insert
    void create(Books books);

    @Query("DELETE FROM books")
    void VaciarLista();
}
