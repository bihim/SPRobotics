package com.sproboticworks.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import com.sproboticworks.R;

public class VideoActivity extends AppCompatActivity {

    String url;
    VideoView simpleVideoView;
    ProgressBar progress;
    ImageView iv_play;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        init();
        playVideo();

    }

    private void init()
    {
        url = getIntent().getStringExtra("videourl");

        simpleVideoView = findViewById(R.id.simpleVideoView);
        progress = findViewById(R.id.progress);
        iv_play = findViewById(R.id.iv_play);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void playVideo(){
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(simpleVideoView);
        simpleVideoView.setMediaController(mediaController);

        simpleVideoView.setVideoURI(Uri.parse(url));

        simpleVideoView.setOnPreparedListener(mediaPlayer -> {
            progress.setVisibility(View.GONE);
            simpleVideoView.start();

        });

        simpleVideoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mediaPlayer, int i, int i1) {
                switch (i) {
                    case MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START: {
                        progress.setVisibility(View.GONE);
                        return true;
                    }
                    case MediaPlayer.MEDIA_INFO_BUFFERING_START: {
                        progress.setVisibility(View.VISIBLE);
                        return true;
                    }
                    case MediaPlayer.MEDIA_INFO_BUFFERING_END: {
                        progress.setVisibility(View.GONE);
                        return true;
                    }
                }

                return false;
            }
        });

    }
}
