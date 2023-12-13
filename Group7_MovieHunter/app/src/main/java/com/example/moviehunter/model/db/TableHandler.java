package com.example.moviehunter.model.db;
import android.database.sqlite.SQLiteDatabase;

public interface TableHandler {
    void onCreate(SQLiteDatabase db);
    void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion);
}
