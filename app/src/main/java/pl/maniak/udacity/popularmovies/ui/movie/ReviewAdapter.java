package pl.maniak.udacity.popularmovies.ui.movie;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import pl.maniak.udacity.popularmovies.R;
import pl.maniak.udacity.popularmovies.model.Review;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewHolder> {

    private final LayoutInflater inflater;
    private List<Review> reviewList;

    public ReviewAdapter(Context context, List<Review> list) {
        inflater = LayoutInflater.from(context);
        reviewList = list;
    }

    @Override
    public ReviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.review_item, parent, false);
        ReviewHolder holder = new ReviewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ReviewHolder holder, int position) {
        Review review = reviewList.get(position);
        holder.setItem(review);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    public void updateReviews(List<Review> list) {
        reviewList.clear();
        reviewList.addAll(list);
        notifyDataSetChanged();
    }
}
