package com.sprobotics.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.devbrackets.android.exomedia.listener.OnBufferUpdateListener;
import com.devbrackets.android.exomedia.ui.widget.VideoView;
import com.orhanobut.logger.Logger;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.sprobotics.R;

import java.util.ArrayList;
import java.util.List;

public class VideoCarouselAdapter extends SliderViewAdapter<VideoCarouselAdapter.SliderAdapterVH> {

    private Context context;
    private List<String> mSliderItems = new ArrayList<>(); //Demo purpose. Implement your own model list

    public VideoCarouselAdapter(Context context) {
        this.context = context;
    }

    public void renewItems(List<String> sliderItems) { //apply your custom model
        this.mSliderItems = sliderItems;
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        this.mSliderItems.remove(position);
        notifyDataSetChanged();
    }

    public void addItem(String sliderItem) { //Apply your custom model class
        this.mSliderItems.add(sliderItem);
        notifyDataSetChanged();
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_video_carousel, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, int position) {
        //videoView.setVideoURI(Uri.parse(url));
        viewHolder.videoView.setOnPreparedListener(() -> {
            viewHolder.videoView.start();
        });
        viewHolder.videoView.setVideoURI(Uri.parse("url"));//url of video
    }

    @Override
    public int getCount() {
        return 0;
    }

    class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

        View itemView;
        VideoView videoView;
        TextView textViewVideoTitle, textViewCreator, textViewCat;

        public SliderAdapterVH(View itemView) {
            super(itemView);
            videoView = itemView.findViewById(R.id.video_view);
            textViewVideoTitle = itemView.findViewById(R.id.video_title);
            textViewCreator = itemView.findViewById(R.id.video_creator);
            textViewCat = itemView.findViewById(R.id.video_cat);
            this.itemView = itemView;
        }
    }
}
