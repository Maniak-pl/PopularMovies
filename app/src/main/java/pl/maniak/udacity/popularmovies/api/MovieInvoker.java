package pl.maniak.udacity.popularmovies.api;

import com.squareup.okhttp.OkHttpClient;

import java.util.concurrent.TimeUnit;

import pl.maniak.udacity.popularmovies.BuildConfig;
import pl.maniak.udacity.popularmovies.Constants;
import pl.maniak.udacity.popularmovies.model.Movie;
import pl.maniak.udacity.popularmovies.model.Review;
import pl.maniak.udacity.popularmovies.model.Sort;
import pl.maniak.udacity.popularmovies.model.Video;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;

public class MovieInvoker {

    private static final int PAGE = 1;
    private static MovieInvoker instance;

    private MovieInterface movieInterface;

    private MovieInvoker() {
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setConnectTimeout(5000, TimeUnit.MILLISECONDS);

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setClient(new OkClient(okHttpClient))
                .setEndpoint(Constants.REST_URL)
                .setRequestInterceptor(new RequestInterceptor() {
                    @Override
                    public void intercept(RequestFacade request) {
                        request.addQueryParam("api_key", BuildConfig.TMDB_API_KEY);
                    }
                })
                .setLogLevel(BuildConfig.DEBUG == true ? RestAdapter.LogLevel.FULL : RestAdapter.LogLevel.NONE)
                .build();
        movieInterface = restAdapter.create(MovieInterface.class);
    }

    public static MovieInvoker getInstance() {
        if (instance == null) {
            instance = new MovieInvoker();
        }
        return instance;
    }

    public Movie.Response getMovies(Sort sort) {
        switch (sort) {
            case TOP_RATED:
                return movieInterface.getMoviesTopRated(PAGE);
            default:
                return movieInterface.getMoviesPopular(PAGE);
        }
    }

    public Video.Response getVideos(int movieId) {
        return movieInterface.getVideos(movieId);
    }
    public Review.Response getReviews(int movieId) {
        return movieInterface.getReviews(movieId, PAGE);
    }
}
