package pl.maniak.udacity.popularmovies.ui.movie;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import pl.maniak.udacity.popularmovies.R;
import pl.maniak.udacity.popularmovies.model.Video;

public class VideoAdapter extends RecyclerView.Adapter<VideoHolder> {

    private final LayoutInflater inflater;
    private List<Video> videoList;

    private OnVideoItemClickedListener onClickListener;

    public VideoAdapter(Context context, List<Video> list) {
        inflater = LayoutInflater.from(context);
        videoList = list;
    }

    @Override
    public VideoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.video_item, parent, false);
        VideoHolder holder = new VideoHolder(view);
        holder.setOnClickListener(onClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(VideoHolder holder, int position) {
        Video video = videoList.get(position);
        holder.setItem(video);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public void updateVideo(List<Video> list) {
        videoList.clear();
        videoList.addAll(list);
        notifyDataSetChanged();
    }

    public void setOnClickListener(OnVideoItemClickedListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnVideoItemClickedListener {
        void onVideoItemClicked(Video video);
    }
}
