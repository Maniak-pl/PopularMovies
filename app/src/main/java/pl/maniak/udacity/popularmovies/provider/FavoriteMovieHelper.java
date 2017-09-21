package pl.maniak.udacity.popularmovies.provider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import pl.maniak.udacity.popularmovies.model.Movie;

public class FavoriteMovieHelper {

    public static void isFavorite(Context context, int id, FavoriteMovieListener listener) {
        new FavoriteMovieTask(context,listener).execute(id);
    }

    public static void changeFavorite(Context context, Movie movie, FavoriteMovieListener listener) {
        new ChangeFavoriteMovieTask(context, movie, listener).execute();
    }


   private static class FavoriteMovieTask extends AsyncTask<Integer, Void, Boolean> {

       private final Context context;
       private FavoriteMovieListener listener;

       public FavoriteMovieTask(Context context, FavoriteMovieListener listener) {
           this.context = context;
           this.listener = listener;
       }

       @Override
       protected Boolean doInBackground(Integer... params) {
           return isFavorite(context, params[0]);
       }

       @Override
       protected void onPostExecute(Boolean isFavorite) {
           listener.onTaskComplete(isFavorite);
           listener = null;
       }
   }
   private static class ChangeFavoriteMovieTask extends AsyncTask<Void, Void, Boolean> {

       private final Context context;
       private FavoriteMovieListener listener;
       private final Movie movie;

       public ChangeFavoriteMovieTask(Context context, Movie movie, FavoriteMovieListener listener) {
           this.context = context;
           this.listener = listener;
           this.movie = movie;
       }

       @Override
       protected Boolean doInBackground(Void... params) {
          boolean isFavorite = isFavorite(context, movie.getId());

           if(isFavorite) {
               deleteFromFavorites(context, movie.getId());
           } else {
               addToFavorites(context, movie);
           }
           return !isFavorite;
       }

       @Override
       protected void onPostExecute(Boolean isFavorite) {
           listener.onTaskComplete(isFavorite);
           listener = null;
       }
   }

    private static void addToFavorites(Context context, Movie movie) {
        ContentValues values = new ContentValues();

        values.put(MovieContract.Columns.MOVIE_ID, movie.getId());
        values.put(MovieContract.Columns.MOVIE_TITLE, movie.getTitle());
        values.put(MovieContract.Columns.MOVIE_POSTER_PATH, movie.getPosterPath());
        values.put(MovieContract.Columns.MOVIE_OVERVIEW, movie.getOverview());
        values.put(MovieContract.Columns.MOVIE_VOTE_AVERAGE, movie.getVoteAverage());
        values.put(MovieContract.Columns.MOVIE_RELEASE_DATE, movie.getDate());

        context.getContentResolver().insert(MovieContract.CONTENT_URI,
                values);
    }

    private static void deleteFromFavorites(Context context, int id) {
        context.getContentResolver().delete(
                MovieContract.CONTENT_URI,
                MovieContract.Columns.MOVIE_ID + " = ?",
                new String[]{Integer.toString(id)}
        );
    }

    private static boolean isFavorite(Context context, int id) {
        Cursor cursor = context.getContentResolver().query(
                MovieContract.CONTENT_URI,
                null,
                MovieContract.Columns.MOVIE_ID + " = ?",
                new String[] { Integer.toString(id) },
                null
        );
        int numRows = cursor.getCount();
        cursor.close();
        return numRows >= 1;
    }

    public interface FavoriteMovieListener {
        void onTaskComplete(boolean isFavorite);
    }
}
