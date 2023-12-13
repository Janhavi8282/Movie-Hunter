package com.example.moviehunter.favorite;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moviehunter.R;
import com.example.moviehunter.home.MovieDetailsActivity;
import com.example.moviehunter.model.Movie;

import com.squareup.picasso.Picasso;


public class GridViewAdapter extends ArrayAdapter<Movie> {

    //private final ColorMatrixColorFilter grayscaleFilter;
    private Context mContext;
    private int layoutResourceId;
    private ArrayList<Movie> mGridData = new ArrayList<Movie>();
    public GridViewAdapter(Context mContext, int layoutResourceId, ArrayList<Movie> mGridData) {
        super(mContext, layoutResourceId, mGridData);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.mGridData = mGridData;
    }

    /**
     * Updates grid data and refresh grid items.
     *
     * @param mGridData
     */
    public void setGridData(ArrayList<Movie> mGridData) {
        this.mGridData = mGridData;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.titleTextView = row.findViewById(R.id.tvTitle);
            holder.imageView =  row.findViewById(R.id.imgPoster);
            row.setTag(holder);

        } else {
            holder = (ViewHolder) row.getTag();
        }

        Movie item = mGridData.get(position);
        holder.titleTextView.setText(item.getTitle());
        holder.imageView.setImageResource(R.drawable.poster);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(view.getContext(), "Data : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(view.getContext(), MovieDetailsActivity.class);
                intent1.putExtra("ImdbID", item.getImdbID());
                mContext.startActivity(intent1);
            }
        });

        Picasso.get()
                .load(item.getPoster())
                .resize(620, 800)
                .into(holder.imageView);
        return row;
    }

    static class ViewHolder {
        TextView titleTextView;
        ImageView imageView;
    }
}
