package com.example.moviehunter.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.moviehunter.R;
import com.example.moviehunter.favorite.GridViewAdapter;
import com.example.moviehunter.model.Movie;
import com.example.moviehunter.register.RegistrationFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class HomeFragment extends Fragment {
    View v;
    DatabaseReference fireRef;
    SearchView searchView;
    GridView mGridView;
    GridViewAdapter mGridAdapter;
    ArrayList<Movie> mGridData = new ArrayList<>();
    String baseUrl, title;
    private int beforeLastItem;
    ProgressBar progressBar;
    int page = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_home, container, false);
        mGridView = v.findViewById(R.id.gridView);

        // search and display
        searchView = v.findViewById(R.id.searchView);
        title="home";
        getMovieList(title, page);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if (s.length() > 0) {
                    title = s;
                    getMovieList(title, page);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

//        mGridView.setOnScrollListener(new AbsListView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(AbsListView absListView, int i) {
//
//            }
//
//            @Override
//            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//                final int lastItem = firstVisibleItem + visibleItemCount;
//                if (lastItem == totalItemCount && page < 5) {
//                    // last item in grid is on the screen, show footer:
//                    if (beforeLastItem != lastItem){ //to avoid multiple calls for the last item
//                        page++;
//                        getMovieList(title, page);
//                        beforeLastItem = lastItem;
//                    }
//                } else {
//                    beforeLastItem = lastItem;
//                }
//            }
//        });

        return v;

    }

    private void getMovieList(String title, int page) {
        // Search movie API
        baseUrl = "https://www.omdbapi.com/?s=" + title + "&apikey=e6b50611&page=" + page;

        // Set up Request Movie
        RequestQueue queue = Volley.newRequestQueue(v.getContext());         // Context
        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.POST,
                baseUrl,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        // Add Grid
                        try {
                            JSONArray photos = response.getJSONArray("Search");
                            if (photos.length() > 0) {
                                mGridData = new ArrayList<>(photos.length());
                                for (int i = 0; i < photos.length(); i++) {
                                    if(!"N/A".equals(photos.getJSONObject(i).getString("Poster")) )
                                        mGridData.add(new Movie(photos.getJSONObject(i)));
                                }
                                mGridAdapter = new GridViewAdapter(v.getContext(), R.layout.favorite_item_layout, mGridData);

                                mGridView.setAdapter(mGridAdapter);
                            }else{
                                Toast.makeText(getActivity(), "None Result ", Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "None Result ", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "Error Response " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        );

        queue.add(objectRequest);
    }

}