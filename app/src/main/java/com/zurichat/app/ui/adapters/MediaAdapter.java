package com.zurichat.app.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.zurichat.app.R;
import com.zurichat.app.models.Media;

import java.util.List;

public class MediaAdapter extends RecyclerView.Adapter<MediaAdapter.MediaViewHolder> {
    private List<Media> mediaList;

    @NonNull
    @Override
    public MediaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.media_item, parent, false);
        return new MediaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MediaViewHolder holder, int position) {
        Media media = mediaList.get(position);
        holder.imageView.setImageResource(media.getMedia_image());
    }

    @Override
    public int getItemCount() {
        return mediaList.size();
    }

    public void setMediaList(List<Media> mediaList) {
        this.mediaList = mediaList;
        notifyDataSetChanged();
    }

    class MediaViewHolder extends RecyclerView.ViewHolder{

        private ImageView imageView;

        public MediaViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.media_image);
        }
    }
}
