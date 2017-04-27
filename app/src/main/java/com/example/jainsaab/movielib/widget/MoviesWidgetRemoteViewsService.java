package com.example.jainsaab.movielib.widget;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.jainsaab.movielib.MoviesActivity;
import com.example.jainsaab.movielib.R;
import com.example.jainsaab.movielib.data.MoviesContract;

public class MoviesWidgetRemoteViewsService extends RemoteViewsService {

    private static final String[] MOVIE_COLUMNS = {
            MoviesContract.MoviesEntry.COLUMN_MOVIE_ID,
            MoviesContract.MoviesEntry.COLUMN_TITLE,
            MoviesContract.MoviesEntry.COLUMN_RELEASE_DATE,
            MoviesContract.MoviesEntry.COLUMN_CBFC_RATING
    };

    private static final int INDEX_MOVIE_ID = 0;
    private static final int INDEX_TITLE = 1;
    private static final int INDEX_RELEASE_DATE = 2;
    private static final int INDEX_CBFC_RATING = 3;

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {

        return new RemoteViewsFactory() {
            private Cursor cursor = null;

            @Override
            public void onCreate() {

            }

            @Override
            public void onDataSetChanged() {
                if (cursor != null) {
                    cursor.close();
                }
                final long identityToken = Binder.clearCallingIdentity();
                cursor = getContentResolver().query(MoviesContract.MoviesEntry.CONTENT_URI, MOVIE_COLUMNS, null, null, null);
                Binder.restoreCallingIdentity(identityToken);
            }

            @Override
            public void onDestroy() {
                if (cursor != null) {
                    cursor.close();
                    cursor = null;
                }
            }

            @Override
            public int getCount() {
                return cursor == null ? 0 : cursor.getCount();
            }

            @Override
            public RemoteViews getViewAt(int i) {
                if (i == AdapterView.INVALID_POSITION ||
                        cursor == null || !cursor.moveToPosition(i)) {
                    return null;
                }
                RemoteViews views = new RemoteViews(getPackageName(),
                        R.layout.movies_widget_list_item);

                int movieId = cursor.getInt(INDEX_MOVIE_ID);
                String movieTitle = cursor.getString(INDEX_TITLE);
                String releaseDate = cursor.getString(INDEX_RELEASE_DATE);
                String cbfcRating = cursor.getString(INDEX_CBFC_RATING);

                SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(MoviesWidgetRemoteViewsService.this);
                String posterStr = getPrefs.getString(movieId + "poster", null);
                byte[] posterByteArray = null;
                if(posterStr != null){
                    posterByteArray = Base64.decode(posterStr, Base64.DEFAULT);
                }
                views.setContentDescription(R.id.movie_image, getString(R.string.widget_movie_img_content_description));
                views.setTextViewText(R.id.movie_title, movieTitle);
                views.setTextViewText(R.id.subtitle, releaseDate);
                views.setTextViewText(R.id.cbfc_rating, cbfcRating);
                if(posterByteArray != null){
                    views.setImageViewBitmap(R.id.movie_image,BitmapFactory.decodeByteArray(posterByteArray, 0, posterByteArray.length));
                } else {
                    views.setImageViewResource(R.id.movie_image,R.drawable.ic_placeholder);
                }
                final Intent fillInIntent = new Intent(MoviesWidgetRemoteViewsService.this, MoviesActivity.class);
                views.setOnClickFillInIntent(R.id.widget_list_item, fillInIntent);
                return views;
            }

            @Override
            public RemoteViews getLoadingView() {
                return new RemoteViews(getPackageName(), R.layout.movies_widget_list_item);
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public long getItemId(int i) {
                if (cursor.moveToPosition(i))
                    return cursor.getLong(INDEX_MOVIE_ID);
                return i;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }
        };
    }
}
