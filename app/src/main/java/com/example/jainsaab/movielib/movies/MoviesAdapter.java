package com.example.jainsaab.movielib.movies;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.example.jainsaab.movielib.DetailsActivity;
import com.example.jainsaab.movielib.R;
import com.example.jainsaab.movielib.utility.Constants;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MoviesAdapter extends ArrayAdapter<Movies> {
    private Context mContext;
    private ArrayList<Movies> moviesArrayList;
    private FirebaseAnalytics mFirebaseAnalytics;

    public MoviesAdapter(Context context, ArrayList<Movies> movies) {
        super(context, 0, movies);
        mContext = context;
        moviesArrayList = movies;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        final Movies movies = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.movies_list_item, parent, false);
        }
        // Lookup view for data population
        AppCompatTextView title = (AppCompatTextView) convertView.findViewById(R.id.movie_title);
        AppCompatTextView subtitle = (AppCompatTextView) convertView.findViewById(R.id.subtitle);
        AppCompatTextView cbfcRating = (AppCompatTextView) convertView.findViewById(R.id.cbfc_rating);
        final ImageView poster = (ImageView) convertView.findViewById(R.id.movie_image);
        CardView movieCard = (CardView) convertView.findViewById(R.id.movie_item_card);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(mContext);
        // Populate the data into the template view using the data object
        if (movies != null) {
            title.setText(movies.getMovieTitle());
            subtitle.setText(movies.getMovieYear());
            cbfcRating.setText(movies.getCbfcRating());
            final String POSTER_FINAL_URL = Constants.IMAGE_BASE_URL + Constants.POSTER_SIZE_LARGE + movies.getMoviePoster();
            Picasso.with(mContext).load(POSTER_FINAL_URL.trim()).into(poster);
            movieCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, DetailsActivity.class);
                    intent.putExtra(Constants.TMDB_TITLE,movies.getMovieTitle());
                    intent.putExtra(Constants.TMDB_BACKDROP,movies.getMovieBackdrop());
                    intent.putExtra(Constants.TMDB_RELEASE_DATE,movies.getMovieYear());
                    intent.putExtra(Constants.TMDB_ADULT,movies.getCbfcRating());
                    intent.putExtra(Constants.TMDB_POSTER, movies.getMoviePoster());
                    intent.putExtra(Constants.TMDB_VOTE_AVG, movies.getUserRating());
                    intent.putExtra(Constants.TMDB_GENRES, movies.getGenre());
                    intent.putExtra(Constants.TMDB_OVERVIEW, movies.getPlot());
                    intent.putExtra(Constants.TMDB_MOVIE_ID, movies.getMovieId());
                    intent.putExtra(Constants.TMDB_ADULT, movies.getCbfcRating());

                    Bundle bundle = new Bundle();
                    bundle.putString(FirebaseAnalytics.Param.ITEM_ID, movies.getMovieId());
                    bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, movies.getMovieTitle());
                    bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "card_view");
                    mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

                    mContext.startActivity(intent);
                }
            });
        }
        // Return the completed view to render on screen
        return convertView;
    }

    @Nullable
    @Override
    public Movies getItem(int position) {
        return moviesArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return moviesArrayList.size();
    }

    public void setMovies(List<Movies> moviesArrayList1){
        if(moviesArrayList1 != null){
            moviesArrayList.clear();
            moviesArrayList.addAll(moviesArrayList1);
            notifyDataSetChanged();
        }
    }
}
