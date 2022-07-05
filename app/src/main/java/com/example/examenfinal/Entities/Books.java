package com.example.examenfinal.Entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "books")
public class Books {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String caratula;
    public String titulo;
    public String resumen;
    public float latitud;
    public float longitud;
    public boolean favorito;

    public Books() {
    }
}
