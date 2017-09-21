package pl.maniak.udacity.popularmovies.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import pl.maniak.udacity.popularmovies.repository.DBHelper;

public class MovieProvider extends ContentProvider {

    private static final UriMatcher uriMatcher = buildUriMatcher();
    private DBHelper helper;

    private static final int MOVIE = 100;

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;
        matcher.addURI(authority, MovieContract.PATH_MOVIE, MOVIE);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        helper = new DBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor;
        switch (uriMatcher.match(uri)) {
            case MOVIE: {
                cursor = helper.getReadableDatabase().query(
                        MovieContract.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = uriMatcher.match(uri);

        switch (match) {
            case MOVIE:
                return MovieContract.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        final SQLiteDatabase db = helper.getWritableDatabase();
        Uri returnUri;

        switch (uriMatcher.match(uri)) {
            case MOVIE: {
                long id = db.insert(MovieContract.TABLE_NAME, null, values);
                if (id > 0) {
                    returnUri = MovieContract.buildMovieUri(id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
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

        final SQLiteDatabase db = helper.getWritableDatabase();
        int position;

        if (selection == null) {
            selection = "1";
        }
        switch (uriMatcher.match(uri)) {
            case MOVIE:
                position = db.delete(
                        MovieContract.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (position != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return position;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        final SQLiteDatabase db = helper.getWritableDatabase();
        int position;

        switch (uriMatcher.match(uri)) {
            case MOVIE:
                position = db.update(MovieContract.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (position != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return position;
    }
}
