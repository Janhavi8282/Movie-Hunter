package com.example.moviehunter.model.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBHelper extends SQLiteOpenHelper {
    static final String DBNAME = "Movie.db";
    static final int VERSION = 6;

    public FavoriteTableHandler favoriteTableHandler;
    public RegisterTableHandler registerTableHandler;
    // registerHandler

    public DBHelper(Context context) {
        super(context, DBNAME, null, VERSION);
        favoriteTableHandler = new FavoriteTableHandler();
        registerTableHandler = new RegisterTableHandler();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        favoriteTableHandler.onCreate(db);
        registerTableHandler.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        favoriteTableHandler.onUpgrade(db, oldVersion, newVersion);
        registerTableHandler.onUpgrade(db, oldVersion, newVersion);
    }
}
