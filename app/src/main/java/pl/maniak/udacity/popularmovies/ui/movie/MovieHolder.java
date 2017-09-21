package pl.maniak.udacity.popularmovies.ui.movie;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.maniak.udacity.popularmovies.Constants;
import pl.maniak.udacity.popularmovies.R;
import pl.maniak.udacity.popularmovies.model.Movie;

class MovieHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.movie_item_image)
    ImageView movieImage;

    @BindView(R.id.movie_item_title)
    TextView movieTitle;

    private final Context context;
    private Movie movie;

    private MovieAdapter.OnMovieItemClickedListener onClickListener;

    public MovieHolder(View itemView) {
        super(itemView);
        context = itemView.getContext();
        ButterKnife.bind(this, itemView);
    }

    public void setItem(Movie movie) {
        this.movie = movie;
        Glide.with(context)
                .load(Constants.IMAGE_URL + movie.getPosterPath())
                .fitCenter()
                .into(movieImage);
        movieTitle.setText(movie.getTitle());
    }

    public void setOnClickListener(MovieAdapter.OnMovieItemClickedListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @OnClick(R.id.movie_item_root)
    void onItemClicked() {
        if (onClickListener != null) {
            onClickListener.onMovieItemClicked(movie);
        }
    }
}

