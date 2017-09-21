package pl.maniak.udacity.popularmovies.ui.movie;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import pl.maniak.udacity.popularmovies.R;
import pl.maniak.udacity.popularmovies.model.Movie;

public class MovieAdapter extends RecyclerView.Adapter<MovieHolder> {

    private final LayoutInflater inflater;
    private final List<Movie> movieList;

    private OnMovieItemClickedListener onClickListener;

    public MovieAdapter(Context context, List<Movie> movieList) {
        this.inflater = LayoutInflater.from(context);
        this.movieList = movieList;
    }

    @Override
    public MovieHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.movie_item, parent, false);
        MovieHolder viewHolder = new MovieHolder(view);
        viewHolder.setOnClickListener(onClickListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MovieHolder holder, int position) {
        Movie movie = movieList.get(position);
        holder.setItem(movie);
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public void updateMovie(List<Movie> list) {
        movieList.clear();
        movieList.addAll(list);
        notifyDataSetChanged();
    }

    public void setOnClickListener(OnMovieItemClickedListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnMovieItemClickedListener {
        void onMovieItemClicked(Movie movie);
    }
}
