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
import pl.maniak.udacity.popularmovies.R;
import pl.maniak.udacity.popularmovies.model.Video;

public class VideoHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.video_image)
    ImageView videoImage;

    @BindView(R.id.video_name)
    TextView videoName;

    private final Context context;
    private Video video;

    VideoAdapter.OnVideoItemClickedListener onClickListener;

    public VideoHolder(View itemView) {
        super(itemView);
        context = itemView.getContext();
        ButterKnife.bind(this, itemView);
    }

    public void setItem(Video video) {
        this.video = video;
        String imageUrl = "http://img.youtube.com/vi/" + video.getKey() + "/0.jpg";
        Glide.with(context).load(imageUrl).into(videoImage);

        videoName.setText(video.getName());
    }

    public void setOnClickListener(VideoAdapter.OnVideoItemClickedListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @OnClick(R.id.video_item_root)
    void onItemClicked() {
        if(onClickListener != null) {
            onClickListener.onVideoItemClicked(video);
        }
    }
}