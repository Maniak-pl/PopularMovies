package pl.maniak.udacity.popularmovies.ui;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.maniak.udacity.popularmovies.Constants;
import pl.maniak.udacity.popularmovies.R;
import pl.maniak.udacity.popularmovies.api.MovieInvoker;
import pl.maniak.udacity.popularmovies.model.Movie;
import pl.maniak.udacity.popularmovies.model.Review;
import pl.maniak.udacity.popularmovies.model.Video;
import pl.maniak.udacity.popularmovies.provider.FavoriteMovieHelper;
import pl.maniak.udacity.popularmovies.ui.movie.ReviewAdapter;
import pl.maniak.udacity.popularmovies.ui.movie.VideoAdapter;

public class DetailActivity extends AppCompatActivity implements VideoAdapter.OnVideoItemClickedListener {

    @BindView(R.id.detail_movie_title)
    TextView titleTv;

    @BindView(R.id.detail_movie_image)
    ImageView posterIv;

    @BindView(R.id.detail_movie_release_date)
    TextView releaseDateTv;

    @BindView(R.id.detail_movie_vote_average)
    TextView voteAverageTv;

    @BindView(R.id.detail_movie_overview)
    TextView overview;

    @BindView(R.id.detail_video_layout)
    LinearLayout videoLayout;

    @BindView(R.id.detail_video_recycler)
    RecyclerView videoRecycleView;

    @BindView(R.id.detail_review_layout)
    LinearLayout reviewLayout;

    @BindView(R.id.detail_review_recycler)
    RecyclerView reviewRecycleView;

    private Movie movie;
    private VideoAdapter videoAdapter;
    private ReviewAdapter reviewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        movie = getIntent().getParcelableExtra(Constants.BUNDLE_KEY_MOVIE);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        showDetail();
        initRecycler();
    }

    private void showDetail() {
        titleTv.setText(movie.getTitle());
        Glide.with(this)
                .load(Constants.IMAGE_URL + movie.getPosterPath())
                .fitCenter()
                .into(posterIv);
        String releaseDate = getString(R.string.detail_release_date_key) + movie.getDate();
        String voteAverage = getString(R.string.detail_average_rating_key) + movie.getVoteAverage();
        releaseDateTv.setText(releaseDate);
        voteAverageTv.setText(voteAverage);
        overview.setText(movie.getOverview());
    }

    private void initRecycler() {
        RecyclerView.LayoutManager layoutManagerVideo = new LinearLayoutManager(this);
        videoAdapter = new VideoAdapter(this, new ArrayList<Video>());
        videoAdapter.setOnClickListener(this);
        videoRecycleView.setAdapter(videoAdapter);
        videoRecycleView.setLayoutManager(layoutManagerVideo);

        RecyclerView.LayoutManager layoutManagerReview = new LinearLayoutManager(this);
        reviewAdapter = new ReviewAdapter(this, new ArrayList<Review>());
        reviewRecycleView.setAdapter(reviewAdapter);
        reviewRecycleView.setLayoutManager(layoutManagerReview);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(movie !=null && isOnline()) {
            new VideoTask().execute(movie.getId());
            new ReviewTask().execute(movie.getId());
        }
    }

    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(movie != null) {
            getMenuInflater().inflate(R.menu.detail_menu, menu);
            final MenuItem favorite = menu.findItem(R.id.action_favorite);
            FavoriteMovieHelper.isFavorite(this, movie.getId(), new FavoriteMovieHelper.FavoriteMovieListener() {
                @Override
                public void onTaskComplete(boolean isFavorite) {
                    favorite.setIcon(isFavorite ? R.drawable.ic_favorite : R.drawable.ic_favorite_border);
                }
            });
            return true;
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_favorite:
                FavoriteMovieHelper.changeFavorite(this, movie, new FavoriteMovieHelper.FavoriteMovieListener() {
                    @Override
                    public void onTaskComplete(boolean isFavorite) {
                        if(isFavorite) {
                            item.setIcon(R.drawable.ic_favorite);
                            Toast.makeText(DetailActivity.this, R.string.detail_message_add_favorite, Toast.LENGTH_SHORT).show();
                        } else {
                            item.setIcon(R.drawable.ic_favorite_border);
                            Toast.makeText(DetailActivity.this, R.string.detail_message_remove_favorite, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onVideoItemClicked(Video video) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("http://www.youtube.com/watch?v=" + video.getKey()));
        startActivity(intent);
    }

    private class VideoTask extends AsyncTask<Integer, Video, List<Video>>  {
        @Override
        protected List<Video> doInBackground(Integer... params) {
            if (params.length == 0) {
                return null;
            }

           return MovieInvoker.getInstance().getVideos(params[0]).videos;
        }

        @Override
        protected void onPostExecute(List<Video> videos) {
            if(videos !=null) {
                if(videos.size() >0) {
                    videoLayout.setVisibility(View.VISIBLE);
                    if(videoAdapter != null) {
                        videoAdapter.updateVideo(videos);
                    }
                }
            }
        }
    }

    private class ReviewTask extends AsyncTask<Integer, Video, List<Review>>  {
        @Override
        protected List<Review> doInBackground(Integer... params) {
            if (params.length == 0) {
                return null;
            }

            return MovieInvoker.getInstance().getReviews(params[0]).reviews;
        }

        @Override
        protected void onPostExecute(List<Review> reviews) {
            if(reviews !=null) {
                if(reviews.size() >0) {
                    reviewLayout.setVisibility(View.VISIBLE);
                    if(reviewAdapter != null) {
                        reviewAdapter.updateReviews(reviews);
                    }
                }
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
