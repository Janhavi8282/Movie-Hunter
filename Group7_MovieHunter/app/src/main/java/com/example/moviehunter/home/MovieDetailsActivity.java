package com.example.moviehunter.home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.moviehunter.R;
import com.example.moviehunter.model.db.DBHelper;
import com.example.moviehunter.model.Movie;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class MovieDetailsActivity extends AppCompatActivity {
    ImageView imgView, imgView1, imgFavorite, imgThumbsUp, imgThumbsDown;
    TextView txtMovieName, txtDescription, txtReleased, txtGenres, txtCast, txtDirectors, txtMetascore, txtBoxoffice;
    String baseUrl, ImdbID;
    Intent intent;
//    boolean isFavorite = false;
    DBHelper dbHelper;
    Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        imgView = findViewById(R.id.imgView);
        imgView1 = findViewById(R.id.imgView1);
        imgFavorite = findViewById(R.id.btnLike);
        imgThumbsUp = findViewById(R.id.imgThumbsUp);
        imgThumbsDown = findViewById(R.id.imgThumbsDown);
        txtMovieName = findViewById(R.id.txtMovieName);
        txtReleased = findViewById(R.id.txtReleased);
        txtDescription = findViewById(R.id.txtDescription);
        txtGenres = findViewById(R.id.txtGenres);
        txtCast = findViewById(R.id.txtCast);
        txtDirectors = findViewById(R.id.txtDirectors);
        txtMetascore = findViewById(R.id.txtMetascore);
        txtBoxoffice = findViewById(R.id.txtBoxoffice);
        dbHelper = new DBHelper(this);
        intent= getIntent();
        ImdbID = intent.getStringExtra("ImdbID");

        setup();
    }

    private void setup() {
        // get movie from favorite db
        movie = dbHelper.favoriteTableHandler.getMovie(dbHelper, ImdbID);

        // set like/unlike image
        setupImage();
        imgFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (movie != null) {
                    movie.setFavorite(!movie.isFavorite());
                    dbHelper.favoriteTableHandler.insertOrUpdateMovie(dbHelper.getWritableDatabase(), movie);
                }
                setupImage();
            }
        });

        imgThumbsUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!movie.isLike())
                    movie.setLike(!movie.isLike());
                setupLikeImage();
            }
        });

        imgThumbsDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (movie.isLike())
                    movie.setLike(!movie.isLike());
                setupLikeImage();
            }
        });

        // set movie info
        if (movie != null) {
            setupMovieInfo(movie);
        } else {
            getMovieList(ImdbID);
        }
    }

    private void setupImage() {
        if (movie != null && movie.isFavorite()) {
            imgFavorite.setImageResource(R.drawable.like);
        } else {
            imgFavorite.setImageResource(R.drawable.unlike);
        }
    }


    private void setupLikeImage() {
        if (movie != null && movie.isLike() ) {
            // Like button
            imgThumbsUp.setColorFilter(Color.argb(255, 106, 153, 78));
            imgThumbsDown.setColorFilter(Color.argb(255, 0, 0, 0));
        } else {
            // Dislike button
            imgThumbsDown.setColorFilter(Color.argb(255, 214, 40, 40));
            imgThumbsUp.setColorFilter(Color.argb(255, 0, 0, 0));
        }
    }

    private void getMovieList(String ImdbID) {
        // Search movie API
        baseUrl = "https://www.omdbapi.com/?i=" + ImdbID + "&apikey=e6b50611";

        // Set up Request Movie
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());         // Context
        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.POST,
                baseUrl,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
//                            createMovieInfo(response);
                        movie = createMovie(response);
                        setupMovieInfo(movie);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Error Response " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        );

        queue.add(objectRequest);
    }

    private void setupMovieInfo(Movie movie) {
        // Add Grid
        try {
            // Title, Released, Runtime, Plot, Director, Actors, Metascore, BoxOffice
            txtMovieName.setText(movie.getTitle());
            txtReleased.setText(movie.getReleased());;
            txtDescription.setText(movie.getDescription());
            txtGenres.setText(movie.getGenre());
            txtCast.setText(movie.getActors());
            txtDirectors.setText(movie.getDirector());
            txtMetascore.setText(movie.getMetascore());
            txtBoxoffice.setText(movie.getBoxOffice());

            Picasso.get()
                    .load(movie.getPoster())
                    .into(imgView);
            Picasso.get()
                    .load(movie.getPoster())
                    .into(imgView1);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "None Result ", Toast.LENGTH_SHORT).show();
        }
    }

    private Movie createMovie(JSONObject response) {
        Movie movie = new Movie();
        try {
            // Title, Released, Runtime, Plot, Director, Actors, Metascore, BoxOffice
            movie.setTitle(response.getString("Title"));
            movie.setReleased(response.getString("Released"));;
            movie.setDescription(response.getString("Plot"));
            movie.setGenre(response.getString("Genre"));
            movie.setActors(response.getString("Actors"));
            movie.setDirector(response.getString("Director"));
            movie.setMetascore(response.getString("Metascore"));
            movie.setBoxOffice(response.getString("BoxOffice"));
            movie.setPoster(response.getString("Poster"));
            movie.setImdbID(ImdbID);

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "None Result ", Toast.LENGTH_SHORT).show();
        }

        return movie;
    }

    private void createMovieInfo(JSONObject response) {

        // Add Grid
        try {
            // Title, Released, Runtime, Plot, Director, Actors, Metascore, BoxOffice
            txtMovieName.setText(response.getString("Title"));
            txtReleased.setText(response.getString("Released"));;
            txtDescription.setText(response.getString("Plot"));
            txtGenres.setText(response.getString("Genre"));
            txtCast.setText(response.getString("Actors"));
            txtDirectors.setText(response.getString("Director"));
            txtMetascore.setText(response.getString("Metascore"));
            txtBoxoffice.setText(response.getString("BoxOffice"));

            Picasso.get()
                    .load(response.getString("Poster"))
                    .into(imgView);
            Picasso.get()
                    .load(response.getString("Poster"))
                    .into(imgView1);

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "None Result ", Toast.LENGTH_SHORT).show();
        }
    }
}