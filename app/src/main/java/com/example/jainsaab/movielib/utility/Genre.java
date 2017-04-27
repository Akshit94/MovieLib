package com.example.jainsaab.movielib.utility;

import android.content.Context;

import com.example.jainsaab.movielib.R;

public class Genre {

    public String getGenreName(int id, Context context) {
        String genreName;
        switch (id) {
            case 28:
                genreName = context.getString(R.string.genre_action);
                break;
            case 12:
                genreName = context.getString(R.string.genre_adventure);
                break;
            case 16:
                genreName = context.getString(R.string.genre_animation);
                break;
            case 35:
                genreName = context.getString(R.string.genre_comedy);
                break;
            case 80:
                genreName = context.getString(R.string.genre_crime);
                break;
            case 99:
                genreName = context.getString(R.string.genre_documentary);
                break;
            case 18:
                genreName = context.getString(R.string.genre_drama);
                break;
            case 10751:
                genreName = context.getString(R.string.genre_family);
                break;
            case 14:
                genreName = context.getString(R.string.genre_fantasy);
                break;
            case 10769:
                genreName = context.getString(R.string.genre_foreign);
                break;
            case 36:
                genreName = context.getString(R.string.genre_history);
                break;
            case 27:
                genreName = context.getString(R.string.genre_horror);
                break;
            case 10402:
                genreName = context.getString(R.string.genre_music);
                break;
            case 9648:
                genreName = context.getString(R.string.genre_mystery);
                break;
            case 10749:
                genreName = context.getString(R.string.genre_romance);
                break;
            case 878:
                genreName = context.getString(R.string.genre_science_fiction);
                break;
            case 10770:
                genreName = context.getString(R.string.genre_tv_movie);
                break;
            case 53:
                genreName = context.getString(R.string.genre_thriller);
                break;
            case 10752:
                genreName = context.getString(R.string.genre_war);
                break;
            case 37:
                genreName = context.getString(R.string.genre_western);
                break;
            default:
                genreName = null;
        }
        return genreName;
    }
}
