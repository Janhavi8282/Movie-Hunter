package com.example.moviehunter.favorite;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.example.moviehunter.R;
import com.example.moviehunter.model.db.DBHelper;
import com.example.moviehunter.model.Movie;

import java.util.ArrayList;
/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class FavoriteFragment extends Fragment {

    private GridView mGridView;
    private GridViewAdapter mGridAdapter;
    private ArrayList<Movie> mGridData;
    private DBHelper dbHelper;

    public FavoriteFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mGridData = dbHelper.favoriteTableHandler.getAllFavoirteMovies(dbHelper);
        mGridAdapter = new GridViewAdapter(getActivity(), R.layout.favorite_item_layout, mGridData);
        mGridView.setAdapter(mGridAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);
        mGridView = view.findViewById(R.id.gridView);
        dbHelper = new DBHelper(getActivity());

        mGridData = dbHelper.favoriteTableHandler.getAllFavoirteMovies(dbHelper);
        mGridAdapter = new GridViewAdapter(getActivity(), R.layout.favorite_item_layout, mGridData);
        mGridView.setAdapter(mGridAdapter);

        return view;
    }

}