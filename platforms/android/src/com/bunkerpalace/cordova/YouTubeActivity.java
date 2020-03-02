package com.bunkerpalace.cordova;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.android.youtube.player.YouTubePlayer.PlayerStateChangeListener;

public class YouTubeActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener,
        PlayerStateChangeListener, YouTubePlayer.PlaybackEventListener {

    private static final int RECOVERY_REQUEST = 1;
    private YouTubePlayerView youTubeView;
    private String videoId;
    private String apiKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        videoId = intent.getStringExtra("videoId");
        apiKey = intent.getStringExtra("YouTubeApiId");
        youTubeView = new YouTubePlayerView(this);
        youTubeView.initialize(apiKey, this);
        setContentView(youTubeView);

        YoutubeVideoPlayer youtubeVideoPlayer = new YoutubeVideoPlayer();
    }

    /**
     * Called when initialization of the player succeeds.
     */
    @Override
    public void onInitializationSuccess(Provider provider, YouTubePlayer player, boolean wasRestored) {
        if (!wasRestored) {
            player.loadVideo(videoId);
            player.setFullscreen(true);
            player.setPlayerStateChangeListener(this);
            player.setPlaybackEventListener(this);
        }
    }

    /**
     * Called when initialization of the player fails.
     */
    @Override
    public void onInitializationFailure(Provider provider, YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this, RECOVERY_REQUEST).show();
        } else {
            String error = String.format("Error initializing YouTube player", errorReason.toString());
            Toast.makeText(this, error, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Called when the video reaches its end.
     */
    @Override
    public void onVideoEnded() {
        setResult(RESULT_OK);
        finish();
    }

    /**
     * Called when an error occurs.
     */
    @Override
    public void onError(
            com.google.android.youtube.player.YouTubePlayer.ErrorReason reason) {
        updateLog("onError(): " + reason.toString());
        finish();
    }

    /**
     * Called when playback of an advertisement starts.
     */
    @Override
    public void onAdStarted() {
        CallbackManager.getInstance().publishEvents("onAdStarted");
    }

    /**
     * Called when a video has finished loading.
     */
    @Override
    public void onLoaded(String videoId) {
        CallbackManager.getInstance().publishEvents("onLoaded: " + videoId);
    }

    /**
     * Called when the player begins loading a video and is not ready to accept commands affecting playback (such as play() or pause()).
     */
    @Override
    public void onLoading() {
        CallbackManager.getInstance().publishEvents("onLoading");
    }

    /**
     * Called when playback of the video starts.
     */
    @Override
    public void onVideoStarted() {
        CallbackManager.getInstance().publishEvents("onVideoStarted");
    }

    private void updateLog(String text){
        Log.d("YouTubeActivity", text);
    };

    /**
     * Called when playback starts, either due to play() or user action.
     */
    @Override
    public void onPlaying() {
        updateLog("onPlaying");
        CallbackManager.getInstance().publishEvents("onPlaying");
    }

    /**
     * Called when playback is paused, either due to pause() or user action.
     */
    @Override
    public void onPaused() {
        updateLog("onPaused");
        CallbackManager.getInstance().publishEvents("onPaused");
    }

    /**
     * Called when playback stops for a reason other than being paused, such as the video ending or a playback error.
     */
    @Override
    public void onStopped() {
        updateLog("onStopped");
        CallbackManager.getInstance().publishEvents("onStopped");
    }

    /**
     * Called when buffering starts or ends.
     */
    @Override
    public void onBuffering(boolean isBuffering) {
        updateLog("onBuffering: " + isBuffering);
        CallbackManager.getInstance().publishEvents("onBuffering: " + isBuffering);
    }

    /**
     * Called when a jump in playback position occurs, either due to the user scrubbing or a seek method being called (e.g. seekToMillis(int)).
     */
    @Override
    public void onSeekTo(int newPositionMillis) {
        updateLog("onSeekTo: " + newPositionMillis);
        CallbackManager.getInstance().publishEvents("onSeekTo: " + newPositionMillis);
    }
}
