package com.example.jainsaab.movielib.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class MoviesContract {

    // The "Content authority" is a name for the entire content provider, similar to the
    // relationship between a domain name and its website.  A convenient string to use for the
    // content authority is the package name for the app, which is guaranteed to be unique on the
    // device.
    static final String CONTENT_AUTHORITY = "com.example.jainsaab.movielib";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Possible paths (appended to base content URI for possible URI's)
    static final String PATH_MOVIES = "movies";
    static final String PATH_TRAILER = "trailer";
    static final String PATH_REVIEW = "review";
    static final String PATH_CAST = "cast";

    public static final class CastEntry implements BaseColumns{

        static final String TABLE_NAME = "casts";

        public static final String MOVIE_ID = "movie_id";

        public static final String ACTOR_NAME = "actor";

        public static final String CHARACTER_NAME = "character";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_CAST).build();

        static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CAST;
        static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CAST;

        static String getMovieIdFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }

        public static Uri buildMovieCastUri(long movieId) {
            /**
             * "content://com.example.jainsaab.movielib/cast/135397"
             *  movieId = 135397
             */
            return CONTENT_URI.buildUpon().appendPath(String.valueOf(movieId)).build();
        }

    }

    public static final class ReviewsEntry implements BaseColumns {

        static final String TABLE_NAME = "reviews";

        public static final String MOVIE_ID = "movie_id";

        public static final String AUTHOR_NAME = "author";

        public static final String REVIEW_URL = "review_url";

        public static final String CONTENT = "content";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_REVIEW).build();

        static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REVIEW;
        static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REVIEW;

        static String getMovieIdFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }

        public static Uri buildMovieReviewsUri(long movieId) {
            /**
             * "content://com.example.jainsaab.movielib/reviews/135397"
             *  movieId = 135397
             */
            return CONTENT_URI.buildUpon().appendPath(String.valueOf(movieId)).build();
        }
    }

    public static final class TrailersEntry implements BaseColumns{

        static final String TABLE_NAME = "trailers";

        public static final String MOVIE_ID = "movie_id";

        public static final String TRAILER_NAME = "name";

        public static final String KEY = "key";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TRAILER).build();

        static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TRAILER;
        static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TRAILER;

        static String getMovieIdFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }

        public static Uri buildMovieTrailerUri(long movieId) {
            /**
             * "content://com.example.jainsaab.movielib/trailers/135397"
             *  movieId = 135397
             */
            return CONTENT_URI.buildUpon().appendPath(String.valueOf(movieId)).build();
        }
    }

    /* Inner class that defines the table contents of the movies table */
    public static final class MoviesEntry implements BaseColumns {

        static final String TABLE_NAME = "movies";

        public static final String COLUMN_MOVIE_ID = "movie_id";

        public static final String COLUMN_RELEASE_DATE = "release_date";

        public static final String COLUMN_USER_RATING = "user_rating";

        public static final String COLUMN_GENRE = "genre";

        public static final String COLUMN_PLOT = "plot";

        public static final String COLUMN_CBFC_RATING = "cbfc_rating";

        public static final String COLUMN_TITLE = "title";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

        static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;

        static Uri buildMoviesUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

}
