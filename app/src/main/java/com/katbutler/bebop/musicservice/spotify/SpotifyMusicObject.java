package com.katbutler.bebop.musicservice.spotify;

import com.katbutler.bebop.musicservice.RemoteMusicObject;
import com.katbutler.bebop.musicservice.RemoteMusicServiceType;
import com.katbutler.bebop.utils.BebopLog;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.PlayConfig;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerNotificationCallback;
import com.spotify.sdk.android.player.PlayerState;

/**
 * Created by kat on 2015-09-11.
 */
public class SpotifyMusicObject extends RemoteMusicObject<PlayConfig> implements PlayerNotificationCallback, ConnectionStateCallback {

    private static final String TAG = SpotifyMusicObject.class.getSimpleName();

    private static final String CLIENT_ID = "662b643f936a4275846788656a7fc1f5";
    private static final String REDIRECT_URI = "com-katbutler-bebop://callback";
    private static final String[] SPOTIFY_SCOPES = new String[]{
            "user-read-private",
            "user-library-read",
            "playlist-read-collaborative",
            "playlist-read-private",
            "streaming"};

    private Player mPlayer;

    @Override
    public PlayConfig getRemoteObject() {
        return PlayConfig.createFor(getKey());
    }

    @Override
    public RemoteMusicServiceType belongsToRemoteMusicService() {
        return RemoteMusicServiceType.SPOTIFY;
    }

    @Override
    public void onPlay(PlayConfig remoteObject) {

    }


    @Override
    public void onLoggedIn() {
        BebopLog.d(TAG, "User logged in");
    }

    @Override
    public void onLoggedOut() {
        BebopLog.d(TAG, "User logged out");
    }

    @Override
    public void onLoginFailed(Throwable error) {
        error.printStackTrace();
        BebopLog.d(TAG, "Login failed", error);
    }

    @Override
    public void onTemporaryError() {
        BebopLog.d(TAG, "Temporary error occurred");
    }

    @Override
    public void onConnectionMessage(String message) {
        BebopLog.d(TAG, "Received connection message: " + message);
    }

    @Override
    public void onPlaybackEvent(EventType eventType, PlayerState playerState) {
        BebopLog.d(TAG, "Playback event received: " + eventType.name());
    }

    @Override
    public void onPlaybackError(ErrorType errorType, String errorDetails) {
        BebopLog.d(TAG, "Playback error received: " + errorType.name());
    }
}
