package pl.maniak.udacity.popularmovies.provider;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;

public class MovieContract {

    public static final String CONTENT_AUTHORITY = "pl.maniak.udacity.popularmovies.provider";
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String TABLE_NAME = "movie";
    public static final String PATH_MOVIE = "movie";
    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;
    public static final Uri CONTENT_URI =
            BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();

    public interface Columns {
        String ID = "id";
        String MOVIE_ID = "movie_id";
        String MOVIE_TITLE = "title";
        String MOVIE_POSTER_PATH = "poster_path";
        String MOVIE_OVERVIEW = "overview";
        String MOVIE_VOTE_AVERAGE = "vote_average";
        String MOVIE_RELEASE_DATE = "release_date";
    }

    public interface Number {
        int ID = 0;
        int MOVIE_ID = 1;
        int MOVIE_TITLE = 2;
        int MOVIE_POSTER_PATH = 3;
        int MOVIE_OVERVIEW = 4;
        int MOVIE_VOTE_AVERAGE = 5;
        int MOVIE_RELEASE_DATE = 6;
    }

    public static final String[] MOVIE_COLUMNS = {
            MovieContract.Columns.ID,
            MovieContract.Columns.MOVIE_ID,
            MovieContract.Columns.MOVIE_TITLE,
            MovieContract.Columns.MOVIE_POSTER_PATH,
            MovieContract.Columns.MOVIE_OVERVIEW,
            MovieContract.Columns.MOVIE_VOTE_AVERAGE,
            MovieContract.Columns.MOVIE_RELEASE_DATE
    };

    public static Uri buildMovieUri(long id) {
        return ContentUris.withAppendedId(CONTENT_URI, id);
    }
}
