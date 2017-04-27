package com.example.jainsaab.movielib.data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;

public class MoviesProvider extends ContentProvider {

    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MoviesDbHelper mOpenHelper;

    // /movie
    static final int MOVIES = 100;
    //    // /trailer
    static final int TRAILER = 200;
    //    // /trailer/*
    static final int TRAILER_WITH_MOVIE_ID = 201;

    // /review
    static final int REVIEW = 300;
    //    // /review/*
    static final int REVIEW_WITH_MOVIE_ID = 301;

    // /cast
    static final int CAST = 400;
    //    // /cast/*
    static final int CAST_WITH_MOVIE_ID = 401;

    private static final SQLiteQueryBuilder sTrailerByMovieIdQueryBuilder;
    private static final SQLiteQueryBuilder sReviewByMovieIdQueryBuilder;
    private static final SQLiteQueryBuilder sCastByMovieIdQueryBuilder;

    static {
        sTrailerByMovieIdQueryBuilder = new SQLiteQueryBuilder();
        sReviewByMovieIdQueryBuilder = new SQLiteQueryBuilder();
        sCastByMovieIdQueryBuilder = new SQLiteQueryBuilder();

        //This is an inner join which looks like
        //trailer INNER JOIN movie    ON trailer.movie_id    = movie.movie_id
        //review  INNER JOIN movie    ON review.movie_id    = movie.movie_id
        //cast  INNER JOIN movie    ON cast.movie_id    = movie.movie_id
        sTrailerByMovieIdQueryBuilder.setTables(
                MoviesContract.TrailersEntry.TABLE_NAME + " INNER JOIN " +
                        MoviesContract.MoviesEntry.TABLE_NAME +
                        " ON " + MoviesContract.TrailersEntry.TABLE_NAME +
                        "." + MoviesContract.TrailersEntry.MOVIE_ID +
                        " = " + MoviesContract.MoviesEntry.TABLE_NAME +
                        "." + MoviesContract.MoviesEntry.COLUMN_MOVIE_ID
        );

        sReviewByMovieIdQueryBuilder.setTables(
                MoviesContract.ReviewsEntry.TABLE_NAME + " INNER JOIN " +
                        MoviesContract.MoviesEntry.TABLE_NAME +
                        " ON " + MoviesContract.ReviewsEntry.TABLE_NAME +
                        "." + MoviesContract.ReviewsEntry.MOVIE_ID +
                        " = " + MoviesContract.MoviesEntry.TABLE_NAME +
                        "." + MoviesContract.MoviesEntry.COLUMN_MOVIE_ID
        );

        sCastByMovieIdQueryBuilder.setTables(
                MoviesContract.CastEntry.TABLE_NAME + " INNER JOIN " +
                        MoviesContract.MoviesEntry.TABLE_NAME +
                        " ON " + MoviesContract.CastEntry.TABLE_NAME +
                        "." + MoviesContract.CastEntry.MOVIE_ID +
                        " = " + MoviesContract.MoviesEntry.TABLE_NAME +
                        "." + MoviesContract.MoviesEntry.COLUMN_MOVIE_ID
        );

    }

    //movie.movie_id = ?
    private static final String sMovieIdSelection =
            MoviesContract.MoviesEntry.TABLE_NAME +
                    "." + MoviesContract.MoviesEntry.COLUMN_MOVIE_ID + " = ? ";

    private Cursor getReviewByMovieId(Uri uri, String[] projection, String sortOrder) {
        String movieId = MoviesContract.ReviewsEntry.getMovieIdFromUri(uri);

        String[] selectionArgs = new String[]{movieId};
        String selection = sMovieIdSelection;

        return sReviewByMovieIdQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    private Cursor getCastByMovieId(Uri uri, String[] projection, String sortOrder) {
        String movieId = MoviesContract.CastEntry.getMovieIdFromUri(uri);

        String[] selectionArgs = new String[]{movieId};
        String selection = sMovieIdSelection;

        return sCastByMovieIdQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    private Cursor getTrailerByMovieId(Uri uri, String[] projection, String sortOrder) {
        String movieId = MoviesContract.TrailersEntry.getMovieIdFromUri(uri);

        String[] selectionArgs = new String[]{movieId};
        String selection = sMovieIdSelection;

        return sTrailerByMovieIdQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }


    static UriMatcher buildUriMatcher() {
        // 1) The code passed into the constructor represents the code to return for the root
        // URI.  It's common to use NO_MATCH as the code for this case. Add the constructor below.
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);


        // 2) Use the addURI function to match each of the types.  Use the constants from
        // MoviesContract to help define the types to the UriMatcher.
        uriMatcher.addURI(MoviesContract.CONTENT_AUTHORITY, MoviesContract.PATH_MOVIES, MOVIES);
        uriMatcher.addURI(MoviesContract.CONTENT_AUTHORITY, MoviesContract.PATH_TRAILER, TRAILER);
        uriMatcher.addURI(MoviesContract.CONTENT_AUTHORITY, MoviesContract.PATH_TRAILER + "/#", TRAILER_WITH_MOVIE_ID);
        uriMatcher.addURI(MoviesContract.CONTENT_AUTHORITY, MoviesContract.PATH_REVIEW, REVIEW);
        uriMatcher.addURI(MoviesContract.CONTENT_AUTHORITY, MoviesContract.PATH_REVIEW + "/#", REVIEW_WITH_MOVIE_ID);
        uriMatcher.addURI(MoviesContract.CONTENT_AUTHORITY, MoviesContract.PATH_CAST, CAST);
        uriMatcher.addURI(MoviesContract.CONTENT_AUTHORITY, MoviesContract.PATH_CAST + "/#", CAST_WITH_MOVIE_ID);


        // 3) Return the new matcher!
        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new MoviesDbHelper(getContext());
        return true;
    }

    @Override
    public String getType(@NonNull Uri uri) {

        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case MOVIES:
                return MoviesContract.MoviesEntry.CONTENT_TYPE;

            case TRAILER:
                return MoviesContract.TrailersEntry.CONTENT_TYPE;

            case TRAILER_WITH_MOVIE_ID:
                if(getTrailerByMovieId(uri, null, null).getCount() > 1)
                    return MoviesContract.TrailersEntry.CONTENT_TYPE;
                else
                    return MoviesContract.TrailersEntry.CONTENT_ITEM_TYPE;

            case REVIEW:
                return MoviesContract.ReviewsEntry.CONTENT_TYPE;

            case REVIEW_WITH_MOVIE_ID:
                if(getReviewByMovieId(uri,null,null).getCount() > 1)
                    return MoviesContract.ReviewsEntry.CONTENT_TYPE;
                else
                    return MoviesContract.ReviewsEntry.CONTENT_ITEM_TYPE;

            case CAST:
                return MoviesContract.CastEntry.CONTENT_TYPE;

            case CAST_WITH_MOVIE_ID:
                if(getCastByMovieId(uri,null,null).getCount() > 1)
                    return MoviesContract.CastEntry.CONTENT_TYPE;
                else
                    return MoviesContract.CastEntry.CONTENT_ITEM_TYPE;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }


    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        // Here's the switch statement that, given a URI, will determine what kind of request it is,
        // and query the database accordingly.
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            // "movies"
            case MOVIES: {
                retCursor = mOpenHelper.getReadableDatabase().query(MoviesContract.MoviesEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }
//             "trailer"
            case TRAILER:
                retCursor = mOpenHelper.getReadableDatabase().query(MoviesContract.TrailersEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case TRAILER_WITH_MOVIE_ID: {
                retCursor = getTrailerByMovieId(uri, projection, sortOrder);
                break;
            }
//             "review"
            case REVIEW:
                retCursor = mOpenHelper.getReadableDatabase().query(MoviesContract.ReviewsEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            case REVIEW_WITH_MOVIE_ID: {
                retCursor = getReviewByMovieId(uri, projection, sortOrder);
                break;
            }
//            "cast"
            case CAST:
                retCursor = mOpenHelper.getReadableDatabase().query(MoviesContract.CastEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            case CAST_WITH_MOVIE_ID: {
                retCursor = getCastByMovieId(uri, projection, sortOrder);
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case MOVIES: {
                long _id = db.insert(MoviesContract.MoviesEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = MoviesContract.MoviesEntry.buildMoviesUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case TRAILER: {
                long _id = db.insert(MoviesContract.TrailersEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = MoviesContract.TrailersEntry.buildMovieTrailerUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case REVIEW: {
                long _id = db.insert(MoviesContract.ReviewsEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = MoviesContract.ReviewsEntry.buildMovieReviewsUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case CAST: {
                long _id = db.insert(MoviesContract.CastEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = MoviesContract.CastEntry.buildMovieCastUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        // Student: Start by getting a writable database
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        // Student: Use the uriMatcher to match the MOVIES URI's we are going to
        // handle.  If it doesn't match these, throw an UnsupportedOperationException.
        final int match = sUriMatcher.match(uri);

        // Student: A null value deletes all rows.  In my implementation of this, I only notified
        // the uri listeners (using the content resolver) if the rowsDeleted != 0 or the selection
        // is null.
        // Oh, and you should notify the listeners here.
        int rowsDeleted;
        if (null == selection) selection = "1";
        switch (match) {
            case MOVIES: {
                rowsDeleted = db.delete(MoviesContract.MoviesEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            case TRAILER: {
                rowsDeleted = db.delete(
                        MoviesContract.TrailersEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            case REVIEW: {
                rowsDeleted = db.delete(
                        MoviesContract.ReviewsEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            case CAST: {
                rowsDeleted = db.delete(
                        MoviesContract.CastEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Student: return the actual rows deleted
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(
            @NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        // Student: Start by getting a writable database
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        final int match = sUriMatcher.match(uri);

        int rowsUpdated;
        if (null == selection) selection = "1";

        switch (match) {
            case MOVIES:
                rowsUpdated = db.update(MoviesContract.MoviesEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case TRAILER:
                rowsUpdated = db.update(MoviesContract.TrailersEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case REVIEW:
                rowsUpdated = db.update(MoviesContract.ReviewsEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case CAST:
                rowsUpdated = db.update(MoviesContract.CastEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Student: return the actual rows updated
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MOVIES:
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(MoviesContract.MoviesEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            case TRAILER:
                db.beginTransaction();
                returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(MoviesContract.TrailersEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            case REVIEW:
                db.beginTransaction();
                returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(MoviesContract.ReviewsEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            case CAST:
                db.beginTransaction();
                returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(MoviesContract.CastEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }

    // You do not need to call this method. This is a method specifically to assist the testing
    // framework in running smoothly. You can read more at:
    // http://developer.android.com/reference/android/content/ContentProvider.html#shutdown()
    @Override
    @TargetApi(11)
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }
}
