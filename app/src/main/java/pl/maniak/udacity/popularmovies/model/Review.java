package pl.maniak.udacity.popularmovies.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Review {

    private String id;
    private String author;
    private String content;
    private String url;

    public String getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public String getUrl() {
        return url;
    }

    public static final class Response {

        public long id;
        public int page;
        @SerializedName("results")
        public List<Review> reviews;
        @SerializedName("total_pages")
        public int totalPages;
        @SerializedName("total_results")
        public int totalResults;
    }
}
