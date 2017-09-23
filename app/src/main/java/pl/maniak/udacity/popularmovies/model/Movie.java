package pl.maniak.udacity.popularmovies.model;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import pl.maniak.udacity.popularmovies.provider.MovieContract;

public class Movie implements Parcelable {

    private final int id;
    private final String title;

    @SerializedName("poster_path")
    private final String posterPath;

    private final String overview;

    @SerializedName("release_date")
    private final String date;

    @SerializedName("vote_average")
    private final float voteAverage;

    private Movie(Parcel in) {
        id = in.readInt();
        title = in.readString();
        posterPath = in.readString();
        overview = in.readString();
        date = in.readString();
        voteAverage = in.readFloat();
    }

    public Movie(Cursor cursor) {
        id = cursor.getInt(MovieContract.Number.MOVIE_ID);
        title = cursor.getString(MovieContract.Number.MOVIE_TITLE);
        posterPath = cursor.getString(MovieContract.Number.MOVIE_POSTER_PATH);
        overview = cursor.getString(MovieContract.Number.MOVIE_OVERVIEW);
        voteAverage = cursor.getFloat(MovieContract.Number.MOVIE_VOTE_AVERAGE);
        date = cursor.getString(MovieContract.Number.MOVIE_RELEASE_DATE);
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getOverview() {
        return overview;
    }

    public String getDate() {
        return date;
    }

    public float getVoteAverage() {
        return voteAverage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(posterPath);
        dest.writeString(overview);
        dest.writeString(date);
        dest.writeFloat(voteAverage);
    }

    public static final class Response {
        public int page;

        @SerializedName("total_pages")
        public int totalPages;

        @SerializedName("total_results")
        public int totalMovies;

        @SerializedName("results")
        public List<Movie> movies = new ArrayList<>();

    }

    @Override
    public String toString() {
        return title;
    }
}
