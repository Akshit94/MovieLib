package com.example.jainsaab.movielib.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Manages a local database for movies data.
 */
class MoviesDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 5;

    private static final String DATABASE_NAME = "movielib.db";

    MoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_MOVIES_TABLE = "CREATE TABLE " + MoviesContract.MoviesEntry.TABLE_NAME + " (" +

                MoviesContract.MoviesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                MoviesContract.MoviesEntry.COLUMN_MOVIE_ID + " INTEGER UNIQUE NOT NULL, " +
                MoviesContract.MoviesEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                MoviesContract.MoviesEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                MoviesContract.MoviesEntry.COLUMN_USER_RATING + " TEXT NOT NULL, " +
                MoviesContract.MoviesEntry.COLUMN_GENRE + " TEXT NOT NULL, " +
                MoviesContract.MoviesEntry.COLUMN_CBFC_RATING + " TEXT NOT NULL, " +

                MoviesContract.MoviesEntry.COLUMN_PLOT + " TEXT NOT NULL" +
                ");";

        final String SQL_CREATE_REVIEW_TABLE = "CREATE TABLE " + MoviesContract.ReviewsEntry.TABLE_NAME + " (" +

                MoviesContract.ReviewsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MoviesContract.ReviewsEntry.MOVIE_ID + " INTEGER NOT NULL, " +
                MoviesContract.ReviewsEntry.AUTHOR_NAME + " TEXT NOT NULL, " +
                MoviesContract.ReviewsEntry.CONTENT + " TEXT NOT NULL, " +
                MoviesContract.ReviewsEntry.REVIEW_URL + " TEXT NOT NULL, " +

                " FOREIGN KEY (" + MoviesContract.ReviewsEntry.MOVIE_ID + ") REFERENCES " +
                MoviesContract.MoviesEntry.TABLE_NAME + " (" + MoviesContract.MoviesEntry.COLUMN_MOVIE_ID + ") );";

        final String SQL_CREATE_CAST_TABLE = "CREATE TABLE " + MoviesContract.CastEntry.TABLE_NAME + " (" +

                MoviesContract.CastEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MoviesContract.CastEntry.MOVIE_ID + " INTEGER NOT NULL, " +
                MoviesContract.CastEntry.CHARACTER_NAME + " TEXT NOT NULL, " +
                MoviesContract.CastEntry.ACTOR_NAME + " TEXT NOT NULL, " +

                " FOREIGN KEY (" + MoviesContract.CastEntry.MOVIE_ID + ") REFERENCES " +
                MoviesContract.MoviesEntry.TABLE_NAME + " (" + MoviesContract.MoviesEntry.COLUMN_MOVIE_ID + ") );";

        final String SQL_CREATE_TRAILER_TABLE = "CREATE TABLE " + MoviesContract.TrailersEntry.TABLE_NAME + " (" +

                MoviesContract.TrailersEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MoviesContract.TrailersEntry.MOVIE_ID + " INTEGER NOT NULL, " +
                MoviesContract.TrailersEntry.TRAILER_NAME + " TEXT NOT NULL, " +
                MoviesContract.TrailersEntry.KEY + " TEXT NOT NULL," +

                " FOREIGN KEY (" + MoviesContract.TrailersEntry.MOVIE_ID + ") REFERENCES " +
                MoviesContract.MoviesEntry.TABLE_NAME + " (" + MoviesContract.MoviesEntry.COLUMN_MOVIE_ID + ") );";

        sqLiteDatabase.execSQL(SQL_CREATE_MOVIES_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_REVIEW_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_CAST_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_TRAILER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // Note that this only fires if you change the version number for your database.
        // It does NOT depend on the version number for your application.
        // If you want to update the schema without wiping data, commenting out the next 2 lines
        // should be your top priority before modifying this method.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MoviesContract.MoviesEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MoviesContract.ReviewsEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MoviesContract.CastEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MoviesContract.TrailersEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
