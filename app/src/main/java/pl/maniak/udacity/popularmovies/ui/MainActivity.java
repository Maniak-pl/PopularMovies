package pl.maniak.udacity.popularmovies.ui;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.maniak.udacity.popularmovies.Constants;
import pl.maniak.udacity.popularmovies.R;
import pl.maniak.udacity.popularmovies.api.MovieInvoker;
import pl.maniak.udacity.popularmovies.model.Movie;
import pl.maniak.udacity.popularmovies.model.Sort;
import pl.maniak.udacity.popularmovies.provider.MovieContract;
import pl.maniak.udacity.popularmovies.ui.movie.MovieAdapter;

public class MainActivity extends AppCompatActivity implements MovieAdapter.OnMovieItemClickedListener {

    RecyclerView recyclerView;
    private MovieAdapter adapter;

    private Sort sort = Sort.POPULAR;

    private ArrayList<Movie> list;
    private Parcelable state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            sort = (Sort) savedInstanceState.getSerializable(Constants.BUNDLE_SORT);
            state = savedInstanceState.getParcelable(Constants.BUNDLE_KEY_STATE);
            list = savedInstanceState.getParcelableArrayList(Constants.BUNDLE_KEY_MOVIE);
        }

        initRecycler();

        if (list == null) {
            getMovies(sort);
        } else {
            updateMovies(list);
            if (state != null) {
                recyclerView.getLayoutManager().onRestoreInstanceState(state);
                log("Restored layout manager");

            }
        }
        log("MainActivity.onCreate() called with: movies = [" + list + "] state = [" + state + "]");

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(Constants.BUNDLE_KEY_MOVIE, list);
        Parcelable listState = recyclerView.getLayoutManager().onSaveInstanceState();
        outState.putParcelable(Constants.BUNDLE_KEY_STATE, listState);
        outState.putSerializable(Constants.BUNDLE_SORT, sort);
        super.onSaveInstanceState(outState);
        log("MainActivity.onSaveInstanceState() called with: movies = [" + list + "]");
    }

    void getMovies(Sort sort) {
        this.sort = sort;
        if (isOnline()) {
            if (sort == Sort.FAVORITE) {
                new FavoriteTask(this).execute();
            } else {
                new MoviesTask().execute(sort);
            }
        } else {
            Toast.makeText(this, getString(R.string.toast_connection_error), Toast.LENGTH_LONG).show();
        }
    }

    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private void initRecycler() {
        recyclerView = (RecyclerView) findViewById(R.id.movies_recycle_view);
        adapter = new MovieAdapter(this);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, numberOfColumns());
        recyclerView.setLayoutManager(layoutManager);

        adapter.setOnClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    private int numberOfColumns() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        // You can change this divider to adjust the size of the poster
        int widthDivider = 400;
        int width = displayMetrics.widthPixels;
        int nColumns = width / widthDivider;
        if (nColumns < 2) return 2;
        if (nColumns > 4) return 4;
        return nColumns;
    }

    @Override
    public void onMovieItemClicked(Movie movie) {
        showDetails(movie);
    }

    private void showDetails(Movie movie) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(Constants.BUNDLE_KEY_MOVIE, movie);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sort_highest_rated:
                getMovies(Sort.TOP_RATED);
                break;
            case R.id.action_sort_most_popular:
                getMovies(Sort.POPULAR);
                break;
            case R.id.action_sort_favorite:
                getMovies(Sort.FAVORITE);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private class MoviesTask extends AsyncTask<Sort, Void, List<Movie>> {

        @Override
        protected List<Movie> doInBackground(Sort... params) {
            return MovieInvoker.getInstance().getMovies(params[0]).movies;
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            log("MoviesTask.onPostExecute() called with: movies = [" + movies + "]");
            updateMovies(movies);
        }
    }

    private class FavoriteTask extends AsyncTask<Void, Void, List<Movie>> {

        private final Context context;

        FavoriteTask(Context context) {
            this.context = context;
        }

        @Override
        protected List<Movie> doInBackground(Void... params) {
            Cursor cursor = context.getContentResolver().query(
                    MovieContract.CONTENT_URI,
                    MovieContract.MOVIE_COLUMNS,
                    null,
                    null,
                    null
            );
            return getFavoriteMoviesDataFromCursor(cursor);
        }

        private List<Movie> getFavoriteMoviesDataFromCursor(Cursor cursor) {
            List<Movie> results = new ArrayList<>();
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    Movie movie = new Movie(cursor);
                    results.add(movie);
                } while (cursor.moveToNext());
                cursor.close();
            }
            return results;
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            log("onPostExecute() called with: movies = [" + movies + "]");
            updateMovies(movies);
        }
    }

    private void updateMovies(List<Movie> movies) {
        if (movies != null) {
            list = (ArrayList<Movie>) movies;
            if (adapter != null) {
                adapter.updateMovie(list);

            }
        }
    }

    private static void log(String message) {
        Log.e("Test", message);
    }
}
