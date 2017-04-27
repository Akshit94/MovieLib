package com.example.jainsaab.movielib.favouriteMovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.example.jainsaab.movielib.DetailsActivity;
import com.example.jainsaab.movielib.R;
import com.example.jainsaab.movielib.utility.Constants;

import java.util.ArrayList;
import java.util.List;

class FavouriteMoviesAdapter extends ArrayAdapter<FavouriteMovies> {
    private Context mContext;
    private ArrayList<FavouriteMovies> favouriteMoviesArrayList;

    FavouriteMoviesAdapter(Context context, ArrayList<FavouriteMovies> favouriteMovies) {
        super(context, 0, favouriteMovies);
        mContext = context;
        favouriteMoviesArrayList = favouriteMovies;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        final FavouriteMovies favouriteMovies = getItem(position);
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
        // Populate the data into the template view using the data object
        if (favouriteMovies != null) {

            title.setText(favouriteMovies.getMovieTitle());
            subtitle.setText(favouriteMovies.getReleaseDate());
            cbfcRating.setText(favouriteMovies.getCbfcRating());
            SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
            String posterStr = getPrefs.getString(favouriteMovies.getMovieId() + "poster", null);
            if (posterStr != null) {
                byte[] posterByteArray = Base64.decode(posterStr, Base64.DEFAULT);
                poster.setImageBitmap(BitmapFactory.decodeByteArray(posterByteArray, 0, posterByteArray.length));
            }

            movieCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(mContext, DetailsActivity.class);
                    intent.putExtra(Constants.TMDB_TITLE, favouriteMovies.getMovieTitle());
                    intent.putExtra(Constants.TMDB_RELEASE_DATE, favouriteMovies.getReleaseDate());
                    intent.putExtra(Constants.TMDB_ADULT, favouriteMovies.getCbfcRating());
                    intent.putExtra(Constants.TMDB_VOTE_AVG, favouriteMovies.getUserRating());
                    intent.putExtra(Constants.TMDB_GENRES_STRING, favouriteMovies.getGenre());
                    intent.putExtra(Constants.TMDB_OVERVIEW, favouriteMovies.getPlot());
                    intent.putExtra(Constants.TMDB_MOVIE_ID, favouriteMovies.getMovieId());
                    intent.putExtra(Constants.TMDB_AUTHOR, favouriteMovies.getAuthor());
                    intent.putExtra(Constants.TMDB_CONTENT, favouriteMovies.getReviewContent());
                    intent.putExtra(Constants.TMDB_REVIEW_URL, favouriteMovies.getReviewUrl());
                    intent.putExtra(Constants.TMDB_CHARACTER_NAME, favouriteMovies.getCharacterName());
                    intent.putExtra(Constants.TMDB_ACTOR_NAME_ARGUMENT, favouriteMovies.getActorName());
                    intent.putExtra(Constants.TMDB_TRAILER_NAME, favouriteMovies.getTrailerName());
                    intent.putExtra(Constants.TMDB_TRAILER_KEY, favouriteMovies.getKey());

                    mContext.startActivity(intent);
                }
            });
        }
        // Return the completed view to render on screen
        return convertView;
    }

    @Nullable
    @Override
    public FavouriteMovies getItem(int position) {
        return favouriteMoviesArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return favouriteMoviesArrayList.size();
    }

    void setFavouriteMovies(List<FavouriteMovies> favouriteMoviesList) {
        if (favouriteMoviesList != null) {
            favouriteMoviesArrayList.clear();
            favouriteMoviesArrayList.addAll(favouriteMoviesList);
            notifyDataSetChanged();
        }
    }

}
