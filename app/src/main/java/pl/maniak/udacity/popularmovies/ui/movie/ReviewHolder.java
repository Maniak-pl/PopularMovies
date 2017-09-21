package pl.maniak.udacity.popularmovies.ui.movie;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.maniak.udacity.popularmovies.R;
import pl.maniak.udacity.popularmovies.model.Review;

public class ReviewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.reviews_author)
    TextView authorReview;

    @BindView(R.id.reviews_content)
    TextView contentReview;


    public ReviewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void setItem(Review review) {
        authorReview.setText(review.getAuthor());
        contentReview.setText(review.getContent());
    }
}