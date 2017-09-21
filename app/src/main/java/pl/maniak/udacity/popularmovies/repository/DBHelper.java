package pl.maniak.udacity.popularmovies.repository;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import pl.maniak.udacity.popularmovies.provider.MovieContract;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "movies.db";
    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + MovieContract.TABLE_NAME + " (" +
                MovieContract.Columns.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MovieContract.Columns.MOVIE_ID + " INTEGER NOT NULL, " +
                MovieContract.Columns.MOVIE_TITLE + " TEXT NOT NULL, " +
                MovieContract.Columns.MOVIE_POSTER_PATH + " TEXT, " +
                MovieContract.Columns.MOVIE_OVERVIEW + " TEXT, " +
                MovieContract.Columns.MOVIE_VOTE_AVERAGE + " REAL, " +
                MovieContract.Columns.MOVIE_RELEASE_DATE + " TEXT);";

        db.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.TABLE_NAME);
        onCreate(db);
    }
}
