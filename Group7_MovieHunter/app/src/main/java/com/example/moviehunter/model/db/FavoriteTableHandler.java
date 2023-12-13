package com.example.moviehunter.model.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.moviehunter.model.Movie;

import java.util.ArrayList;

public class FavoriteTableHandler implements TableHandler{
    static final String TABLE_NAME="Favorite";
    static final String COL_MID = "mId";
    static final String COL_IMDBID = "imdbId";
    static final String COL_TITLE= "title";
    static final String COL_RATED = "rated";
    static final String COL_RELEASED = "released";
    static final String COL_RUNTIME = "runtime";
    static final String COL_GENRE = "genre";
    static final String COL_DIRECTOR = "director";
    static final String COL_ACTORS = "actors";
    static final String COL_DESCRIPTION = "description";
    static final String COL_POSTER = "poster";
    static final String COL_METASCORE = "metaScore";
    static final String COL_BOXOFFICE = "boxOffice";
    static final String COL_ISFAVORITE = "isFavorite";

    static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
            COL_MID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_IMDBID + " TEXT UNIQUE, " +
            COL_TITLE + " TEXT, " +
            COL_RATED + " TEXT, " +
            COL_RELEASED + " TEXT, " +
            COL_RUNTIME + " TEXT, " +
            COL_GENRE + " TEXT, " +
            COL_DIRECTOR + " TEXT, " +
            COL_ACTORS + " TEXT, " +
            COL_DESCRIPTION + " TEXT, " +
            COL_POSTER + " TEXT, " +
            COL_METASCORE + " TEXT, " +
            COL_BOXOFFICE + " TEXT, " +
            COL_ISFAVORITE + " BOOLEAN); ";

    static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
//        insertOrUpdateMovie(db, getTestData());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE);
        onCreate(db);
    }

    public boolean insertOrUpdateMovie(SQLiteDatabase db, Movie movie) {
        ContentValues cv = new ContentValues();

        cv.put(COL_IMDBID, movie.getImdbID());
        cv.put(COL_TITLE, movie.getTitle());
        cv.put(COL_RATED, movie.getRated());
        cv.put(COL_RELEASED, movie.getReleased());
        cv.put(COL_RUNTIME, movie.getRunTime());
        cv.put(COL_GENRE, movie.getGenre());
        cv.put(COL_DIRECTOR, movie.getDirector());
        cv.put(COL_ACTORS, movie.getActors());
        cv.put(COL_DESCRIPTION, movie.getDescription());
        cv.put(COL_POSTER, movie.getPoster());
        cv.put(COL_METASCORE, movie.getMetascore());
        cv.put(COL_BOXOFFICE, movie.getBoxOffice());
        cv.put(COL_ISFAVORITE, movie.isFavorite());

        long result = db.insert(TABLE_NAME, null, cv);
        if (result == -1) {
            result = db.update(TABLE_NAME, cv, COL_IMDBID + "=?", new String[] {movie.getImdbID()});
        }
        return result != -1;
    }

    public int deleteMovie(DBHelper dbHelper, String imdbId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int result = db.delete(TABLE_NAME, COL_IMDBID + "=?", new String[] { imdbId });
        return result;
    }

    public Movie getMovie(DBHelper dbHelper, String imdbId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sql = "select * from " + TABLE_NAME;
        sql += " where " + COL_IMDBID + " = " + "\"" + imdbId + "\"";

        Movie movie = null;
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                movie = createMovie(cursor);
            }while (cursor.moveToNext());
        }
        db.close();
        return movie;
    }

    @SuppressLint("Range")
    public ArrayList<Movie> getAllFavoirteMovies(DBHelper dbHelper) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sql = "select * from " + TABLE_NAME;
        sql += " where " + COL_ISFAVORITE + " = 1";

        ArrayList<Movie> list = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                Movie movie = createMovie(cursor);
                list.add(movie);
            }while (cursor.moveToNext());
        }
        db.close();
        return list;
    }

    @SuppressLint("Range")
    private Movie createMovie(Cursor cursor) {
        Movie movie = new Movie();
        movie.setmId(cursor.getInt(cursor.getColumnIndex(COL_MID)));
        movie.setImdbID(cursor.getString(cursor.getColumnIndex(COL_IMDBID)));
        movie.setTitle(cursor.getString(cursor.getColumnIndex(COL_TITLE)));
        movie.setRated(cursor.getString(cursor.getColumnIndex(COL_RATED)));
        movie.setReleased(cursor.getString(cursor.getColumnIndex(COL_RELEASED)));
        movie.setRunTime(cursor.getString(cursor.getColumnIndex(COL_RUNTIME)));
        movie.setGenre(cursor.getString(cursor.getColumnIndex(COL_GENRE)));
        movie.setDirector(cursor.getString(cursor.getColumnIndex(COL_DIRECTOR)));
        movie.setActors(cursor.getString(cursor.getColumnIndex(COL_ACTORS)));
        movie.setDescription(cursor.getString(cursor.getColumnIndex(COL_DESCRIPTION)));
        movie.setPoster(cursor.getString(cursor.getColumnIndex(COL_POSTER)));
        movie.setMetascore(cursor.getString(cursor.getColumnIndex(COL_METASCORE)));
        movie.setBoxOffice(cursor.getString(cursor.getColumnIndex(COL_BOXOFFICE)));
        movie.setFavorite(cursor.getInt(cursor.getColumnIndex(COL_ISFAVORITE)) == 1);
        return movie;
    }

    private Movie getTestData() {
        Movie m = new Movie();
        m.setTitle("uardians of the Galaxy Vol. 2");
        m.setImdbID("tt3896198");
        m.setFavorite(true);
        m.setMetascore("67");
        m.setPoster("https://m.media-amazon.com/images/M/MV5BNjM0NTc0NzItM2FlYS00YzEwLWE0YmUtNTA2ZWIzODc2OTgxXkEyXkFqcGdeQXVyNTgwNzIyNzg@._V1_SX300.jpg");
        m.setDescription("The Guardians struggle to keep together as a team while dealing with their personal family issues, notably Star-Lord's encounter with his father the ambitious celestial being Ego.");
        m.setActors("Chris Pratt, Zoe Saldana, Dave Bautista");
        m.setDirector("James Gunn");
        m.setGenre("Action, Adventure, Comedy");
        m.setBoxOffice("$389,813,101");
        m.setRunTime("36 min");
        m.setReleased("05 May 2017");
        m.setRated("PG-13");
        return m;
    }
}
