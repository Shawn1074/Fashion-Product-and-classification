package com.example.mainproject;

import android.app.Application;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;

public class myviewholder extends RecyclerView.ViewHolder {
    private PlayerView playerView;
    private SimpleExoPlayer simpleExoPlayer;
    private TextView vtitleView;
    private ProgressBar recycleProgress;

    public myviewholder(@NonNull View itemView) {
        super(itemView);
        vtitleView = itemView.findViewById(R.id.vtitle);
        playerView = itemView.findViewById(R.id.player_view);
        recycleProgress = itemView.findViewById(R.id.recycleProgressId);
    }

    void prepareExoPlayer(Application application, String videoTitle, String videoUrl) {
        try {
            vtitleView.setText(videoTitle);
            DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter.Builder(application).build();
            simpleExoPlayer = new SimpleExoPlayer.Builder(application).build();
            playerView.setPlayer(simpleExoPlayer);

            DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(application,
                    "exoplayer_video", bandwidthMeter);
            MediaSource mediaSource = new ProgressiveMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(MediaItem.fromUri(Uri.parse(videoUrl)));

            simpleExoPlayer.prepare(mediaSource);
            simpleExoPlayer.setPlayWhenReady(false);
        } catch (Exception ex) {
            Log.d("ExoPlayer Crashed", ex.getMessage());
        }
    }
}

