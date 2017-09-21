package pl.maniak.udacity.popularmovies.api;

import pl.maniak.udacity.popularmovies.model.Movie;
import pl.maniak.udacity.popularmovies.model.Review;
import pl.maniak.udacity.popularmovies.model.Video;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

interface MovieInterface {

    @GET("/movie/popular")
    Movie.Response getMoviesPopular(@Query("page") int page);

    @GET("/movie/top_rated")
    Movie.Response getMoviesTopRated(@Query("page") int page);

    @GET("/movie/{id}/videos")
    Video.Response getVideos(@Path("id") int movieId);

    @GET("/movie/{id}/reviews")
    Review.Response getReviews(@Path("id") long movieId, @Query("page") int page);
}
