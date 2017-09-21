package pl.maniak.udacity.popularmovies.model;

import java.io.Serializable;

public enum Sort implements Serializable {

    POPULAR("popular"),
    TOP_RATED("top_rated"),
    FAVORITE("favorite");

    private final String value;

    Sort(String value) {
        this.value = value;
    }

    @Override public String toString() {
        return value;
    }
}