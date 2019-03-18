//package com.kafka.player.core;
//
//import android.content.Context;
//import android.media.AudioAttributes;
//import android.media.AudioFocusRequest;
//import android.media.AudioManager;
//import android.net.Uri;
//import android.os.Build;
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.webkit.URLUtil;
//
//import com.google.android.exoplayer2.C;
//import com.google.android.exoplayer2.ExoPlaybackException;
//import com.google.android.exoplayer2.ExoPlayerFactory;
//import com.google.android.exoplayer2.ParserException;
//import com.google.android.exoplayer2.PlaybackParameters;
//import com.google.android.exoplayer2.Player;
//import com.google.android.exoplayer2.SimpleExoPlayer;
//import com.google.android.exoplayer2.Timeline;
//import com.google.android.exoplayer2.audio.AudioSink;
//import com.google.android.exoplayer2.mediacodec.MediaCodecRenderer;
//import com.google.android.exoplayer2.mediacodec.MediaCodecUtil;
//import com.google.android.exoplayer2.source.MediaSource;
//import com.google.android.exoplayer2.source.TrackGroupArray;
//import com.google.android.exoplayer2.source.UnrecognizedInputFormatException;
//import com.google.android.exoplayer2.source.hls.HlsMediaSource;
//import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
//import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
//import com.google.android.exoplayer2.upstream.DataSource;
//import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
//import com.google.android.exoplayer2.upstream.HttpDataSource;
//import com.google.android.exoplayer2.util.Util;
//
//import java.net.CookieHandler;
//import java.net.CookieManager;
//import java.net.CookiePolicy;
//
//import androidx.annotation.Nullable;
//import androidx.annotation.RequiresApi;
//import androidx.lifecycle.MutableLiveData;
//
//public class ExoPlayer extends IPlayer implements Player.EventListener, AudioManager
//        .OnAudioFocusChangeListener {
//    private static ExoPlayer mInstance;
//    private final AudioManager mAudioManager;
//    private Context mContext;
//    private SimpleExoPlayer mPlayer;
//    private boolean mIsPlayerReady;
//    protected boolean mIsOfflineSource;
//    private static final int BUFFER_POS_PERIOD = 1000;
//    private static final int CURRENT_POS_PERIOD = 1000;
//    private BufferDurationTimer mBufferDurationTimer;
//    private CurrentDurationTimer mCurrentDurationTimer;
//    private long mPrepareStartTime;
//    private long mPrepareTimeMillis;
//    private int currentIndex = -1;
//    private static final float MAX_VOLUME = 1.0f;
//    private static final float DUCK_VOLUME = 0.1f;
//    private boolean mAudioFocusInterrupted = false;
//    private boolean mAudioFocusGained;
//    private AudioFocusRequest mAudioFocusRequest;
//
//
//    private static CookieManager DEFAULT_COOKIE_MANAGER;
//
//
//    public static synchronized ExoPlayer getInstance(Context context) {
//        if (mInstance == null) {
//            DEFAULT_COOKIE_MANAGER = new CookieManager();
//            DEFAULT_COOKIE_MANAGER.setCookiePolicy(CookiePolicy.ACCEPT_ORIGINAL_SERVER);
//            mInstance = new ExoPlayer(context);
//        }
//        return mInstance;
//    }
//
//    private ExoPlayer(Context context) {
//        mContext = context;
//        mPlayer = ExoPlayerFactory.newSimpleInstance(context, new DefaultTrackSelector());
//        CookieHandler.setDefault(DEFAULT_COOKIE_MANAGER);
//        mAudioManager = (AudioManager) context.getApplicationContext().getSystemService(Context
//                .AUDIO_SERVICE);
//    }
//
//    private MediaSource buildMediaSOurces(Uri uri) {
//        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(mContext,
//                Util.getUserAgent(mContext, "yourApplicationName"));
//        MediaSource source = new HlsMediaSource.Factory(dataSourceFactory).setAllowChunklessPreparation(true)
//                .createMediaSource(uri);
//        return source;
//    }
//
//    @Override
//    public void onTimelineChanged(Timeline timeline, @Nullable Object manifest, int reason) {
//
//    }
//
//    @Override
//    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
//
//    }
//
//    @Override
//    public void onLoadingChanged(boolean isLoading) {
//
//    }
//
//    @Override
//    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
//        int playerState = IPlayer.STATE_UNKNOWN;
//        //Log.e("EXOPLAYER STATE", playbackState + " ////////////////////////////////////////////");
//        switch (playbackState) {
//            case Player.STATE_BUFFERING:
//                setDurationIfRequired();
//                playerState = STATE_BUFFERING;
//                break;
//            case Player.STATE_ENDED:
//                playerState = STATE_ENDED;
//                break;
//            case Player.STATE_READY:
//                setDurationIfRequired();
//                if (!mIsPlayerReady) {
//                    mIsPlayerReady = true;
//                    playerState = STATE_READY;
//                } else {
//                    playerState = playWhenReady ? STATE_PLAYING : STATE_PAUSED;
//                }
//                break;
//            case com.google.android.exoplayer2.Player.STATE_IDLE:
//                playerState = STATE_STOPPED;
//                break;
//        }
//
//        if (mPlayerState != playerState) {
//            updatePlayerState(playerState, null);
//        }
//    }
//
//    private void setDurationIfRequired() {
//        mDuration = (int) mPlayer.getDuration();
//        if (mDuration == POSITION_UNKNOWN && mPlayer.getDuration() != C.TIME_UNSET) {
//            mDuration = (int) mPlayer.getDuration();
//            if (mIsOfflineSource) {
//                updateBufferingProgress(mDuration);
//            }
//        }
//    }
//
//    private void updateBufferingProgress(int progress) {
//        mBufferedPosition = progress;
//
//        Bundle bundle = new Bundle();
//        bundle.putInt(IPlayer.EXTRA_BUFFERED_POSITION, mBufferedPosition);
//        notifyListeners(IPlayer.STATE_PROGRESS_UPDATE, bundle);
//    }
//
//    void notifyListeners(int playerState, Bundle extras) {
//        for (Listener listener : mListeners) {
//            listener.onPlayerStateChanged(this, playerState, extras);
//        }
//    }
//
//    void updatePlayerState(int playerState, Bundle extras) {
//        if (mBufferDurationTimer != null) {
//            mBufferDurationTimer.cancel();
//            mBufferDurationTimer = null;
//        }
//
//        if (mDuration != POSITION_UNKNOWN && (playerState == STATE_BUFFERING || playerState ==
//                STATE_PLAYING || playerState == STATE_PAUSED || playerState == STATE_READY ||
//                playerState == STATE_ENDED || playerState == STATE_SONG_COMPLETED)) {
//            mBufferDurationTimer = new BufferDurationTimer();
//        }
//
//        mPlayerState = playerState;
//        mCurrentPosition = getPlayerDuration();
//
//        if (mCurrentDurationTimer != null) {
//            mCurrentDurationTimer.cancel();
//            mCurrentDurationTimer = null;
//        }
//
//        if (playerState == IPlayer.STATE_READY || playerState == IPlayer.STATE_PLAYING ||
//                playerState == IPlayer.STATE_ENDED || playerState == IPlayer.STATE_SONG_COMPLETED) {
//            mCurrentDurationTimer = new CurrentDurationTimer(mCurrentPosition, mDuration);
//        }
//
//        if (playerState == STATE_PLAYING) {
//            // Try to request audio focus
//            canPlay();
//        }
//
//        if (playerState == STATE_PREPARING) {
//            startPrepare();
//        } else if (playerState == STATE_READY) {
//            finishPrepare();
//        }
//
//        notifyListeners(playerState, extras);
//    }
//
//    private void startPrepare() {
//        mPrepareStartTime = System.currentTimeMillis();
//    }
//
//    private void finishPrepare() {
//        mPrepareTimeMillis = System.currentTimeMillis() - mPrepareStartTime;
//        mPrepareStartTime = 0;
//    }
//
//    int getPlayerDuration() {
//        return (int) mPlayer.getCurrentPosition();
//    }
//
//    @Override
//    public void onRepeatModeChanged(int repeatMode) {
//
//    }
//
//    @Override
//    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {
//
//    }
//
//    @Override
//    public void onPlayerError(ExoPlaybackException error) {
//        pause();
//        stop();
//        int mErrorCode = IPlayer.ERROR_UNKNOWN;
//
//        if (error.type == ExoPlaybackException.TYPE_SOURCE) {
//            if ((error.getSourceException() instanceof UnrecognizedInputFormatException)) {
//                mErrorCode =  EXO_INPUT_FORMAT_EXCEPTION;
//            } else if (error.getSourceException() instanceof ParserException) {
//                mErrorCode = ERROR_PARSE_FAILED;
//            } else if (error.getSourceException() instanceof HttpDataSource.HttpDataSourceException) {
//                mErrorCode =  ERROR_NOT_CONNECTED;
//            } else {
//                mErrorCode = EXO_SOURCE_EXCEPTION;
//            }
//        } else if (error.type == ExoPlaybackException.TYPE_RENDERER) {
//            if (error.getRendererException() instanceof AudioSink.ConfigurationException) {
//                mErrorCode =  EXO_AUDIO_CONFIG_EXCEPTION;
//            } else if (error.getRendererException() instanceof AudioSink.InitializationException) {
//                mErrorCode =  EXO_AUDIO_INIT_EXCEPTION;
//            } else if (error.getRendererException() instanceof AudioSink.WriteException) {
//                mErrorCode = EXO_SINK_WRITE_EXCEPTION;
//            } else if (error.getRendererException() instanceof MediaCodecUtil.DecoderQueryException) {
//                mErrorCode = EXO_DECODE_QUERY_EXCEPTION;
//            } else if (error.getRendererException() instanceof MediaCodecRenderer.DecoderInitializationException) {
//                mErrorCode =  EXO_DECODE_INIT_EXCEPTION;
//            } else if (error.getRendererException() instanceof MetadataDecoderException) {
//                mErrorCode = EXO_META_DECODE_EXCEPTION;
//            } else {
//                mErrorCode = EXO_RENDERER_EXCEPTION;
//            }
//        } else if (error.type == ExoPlaybackException.TYPE_UNEXPECTED) {
//            mErrorCode = EXO_UNEXPECTED_EXCEPTION;
//        } else {
//            mErrorCode = DEFAULT_EXOPLAYER_ERROR;
//        }
//
//        Bundle bundle = new Bundle();
//        bundle.putInt(ERROR_CODE, mErrorCode);
//        updatePlayerState(IPlayer.STATE_ERROR, bundle);
//    }
//
//    @Override
//    public void onPositionDiscontinuity(int reason) {
//        int newIndex = mPlayer.getCurrentWindowIndex();
//        if (newIndex != currentIndex) {
//            // The index has changed; update the UI to show info for source at
//            // newIndex
//            currentIndex = newIndex;
//            setDurationIfRequired();
//
//            // LogUtils.errorLog(LOG_TAG, " onPositionDiscontinuity() | CurrentWindowIndex : " +
//            // exoPlayer.getCurrentWindowIndex() + " | PreviousWindowIndex : " + exoPlayer
//            // .getPreviousWindowIndex() + " | MAP : " + songIdToIndexMap + " |
//            // INDEX_TO_SONG_ID_ARRAY : " + indexToSongIdArray, new WynkExoPlayerException());
//            //TODO: removeMediaSource(exoPlayer.getPreviousWindowIndex());
//            currentIndex = mPlayer.getCurrentWindowIndex();
//
//            // LogUtils.verboseLog("hjhj", "ExoPlayer2:onPositionDiscontinuity | currentIndex=" +
//            // currentIndex + " | INDEX_TO_SONG_ID_ARRAY" + indexToSongIdArray);
//
//           /* TODO : Bundle bundle = new Bundle();
//            bundle.putString(ApiConstants.Player.CURRENT_SONG_ID, getCurrentSongId());*/
//            // updatePlayerState(STATE_SONG_COMPLETED, bundle);
//            updatePlayerState(STATE_PLAYING, null);
//        }
//    }
//
//    @Override
//    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
//
//    }
//
//    @Override
//    public void onSeekProcessed() {
//
//    }
//
//    @Override
//    public void prepare(Uri uri) {
//        mIsOfflineSource = !URLUtil.isNetworkUrl(uri.toString());
//        mPlayer.prepare(buildMediaSOurces(uri));
//        mPlayer.setPlayWhenReady(true);
//        mPlayer.removeListener(this);
//        mPlayer.addListener(this);
//        updatePlayerState(IPlayer.STATE_PREPARING, null);
//    }
//
//    @Override
//    public void playNext(Uri uri) {
//        if (uri != null && !TextUtils.isEmpty(uri.toString()))
//            prepare(uri);
//    }
//
//    @Override
//    public void pause() {
//        mPlayer.setPlayWhenReady(false);
//        updatePlayerState(STATE_PAUSED, null);
//    }
//
//
//    @Override
//    public void start() {
//        MutableLiveData<Boolean> value = FUManager.Companion.getInstance().isFULimitReached();
//        if (mPlayer == null || value.getValue()) {
//            //  LogUtils.errorLog(LOG_TAG, "ExoPlayer2:start() | exoplayer is null", new WynkExoPlayerException());
//            return;
//        }
//        currentIndex = mPlayer.getCurrentWindowIndex();
//        mPlayer.setPlayWhenReady(true);
//        updatePlayerState(STATE_PLAYING, null);
//    }
//
//    @Override
//    public void stop() {
//        mPlayer.stop();
//        mPlayer.removeListener(this);
//        mPlayer.setPlayWhenReady(false);
//        mIsPlayerReady = false;
//        //updatePlayerState(IPlayer.STATE_STOPPED, null);
//    }
//
//    @Override
//    public void seekTo(int position) {
//        mPlayer.seekTo(position);
//    }
//
//    @Override
//    public void setVolume(float left, float right) {
//        if (mPlayer != null) {
//            mPlayer.setVolume(Math.min(left, right));
//        }
//    }
//
//    @Override
//    public void release() {
//        if (mCurrentDurationTimer != null) {
//            mCurrentDurationTimer.cancel();
//            mCurrentDurationTimer = null;
//        }
//        mPlayerState = IPlayer.STATE_UNKNOWN;
//        mDuration = IPlayer.POSITION_UNKNOWN;
//        mBufferedPosition = IPlayer.POSITION_UNKNOWN;
//        mCurrentPosition = IPlayer.POSITION_UNKNOWN;
//        mAudioFocusInterrupted = false;
//        abandonAudioFocus();
//        if (mPlayer == null) return;
//        mPlayer.removeListener(this);
//        mPlayer.setPlayWhenReady(false);
//        mPlayer.release();
//        mPlayer = null;
//        mInstance = null;
//        mIsPlayerReady = false;
//    }
//
//    @Override
//    public int getAudioSessionId() {
//        return 0;
//    }
//
//    @Override
//    public void onAudioFocusChange(int focusChange) {
//        switch (focusChange) {
//            case AudioManager.AUDIOFOCUS_GAIN:
//                setVolume(MAX_VOLUME, MAX_VOLUME);
//                if (mAudioFocusInterrupted) {
//                    if (!isPlaying()) {
//                        start();
//                    }
//                }
//                mAudioFocusInterrupted = false;
//                mAudioFocusGained = true;
//                break;
//            case AudioManager.AUDIOFOCUS_LOSS:
//            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
//                if (isPlaying()) {
//                    mAudioFocusInterrupted = true;
//                    pause();
//                }
//                mAudioFocusGained = false;
//                break;
//            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
//                if (isPlaying()) {
//                    setVolume(DUCK_VOLUME, DUCK_VOLUME);
//                }
//                break;
//        }
//    }
//
//    private class BufferDurationTimer extends PreciseCountDownTimer {
//
//        private int lastUpdatedPosition = (int) -1;
//
//        public BufferDurationTimer() {
//            super(Long.MAX_VALUE, BUFFER_POS_PERIOD);
//            start();
//        }
//
//        @Override
//        public void onTick(long millisUntilFinished) {
//            if (mPlayer != null) {
//                int position = (int) mPlayer.getBufferedPosition();
//                if (position != -1 && position != lastUpdatedPosition) {
//                    lastUpdatedPosition = position;
//                    updateBufferingProgress(position);
//                }
//            }
//        }
//
//        @Override
//        public void onFinish() {
//        }
//    }
//
//
//    private class CurrentDurationTimer extends PreciseCountDownTimer {
//
//        public CurrentDurationTimer(int currentDuration, int duration) {
//            super(duration - currentDuration, CURRENT_POS_PERIOD);
//            this.start();
//        }
//
//        @Override
//        public void onTick(long millisUntilFinished) {
//            try {
//                mCurrentPosition = getPlayerDuration();
//
//                Bundle bundle = new Bundle();
//                bundle.putInt(IPlayer.EXTRA_CURRENT_POSITION, mCurrentPosition);
//                notifyListeners(IPlayer.STATE_PROGRESS_UPDATE, bundle);
//            } catch (IllegalStateException e) {
//                //LogUtils.errorLog(LOG_TAG, "Failed to get current position", e);
//            }
//        }
//
//        @Override
//        public void onFinish() {
//        }
//    }
//
//    public boolean isPlaying() {
//        return mPlayerState == STATE_READY || mPlayerState == STATE_PREPARING || mPlayerState ==
//                STATE_BUFFERING || mPlayerState == STATE_PLAYING;
//    }
//
//    private void abandonAudioFocus() {
//        mAudioManager.abandonAudioFocus(this);
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    private AudioFocusRequest getAudioFocusRequest() {
//        if (mAudioFocusRequest == null) {
//            AudioAttributes mAudioAttributes =
//                    new AudioAttributes.Builder()
//                            .setUsage(AudioAttributes.USAGE_MEDIA)
//                            .setFlags(AudioAttributes.FLAG_HW_AV_SYNC)
//                            .setLegacyStreamType(AudioManager.STREAM_MUSIC)
//                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
//                            .build();
//            mAudioFocusRequest = new AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
//                    .setAudioAttributes(mAudioAttributes)
//                    .setAcceptsDelayedFocusGain(false)
//                    .setOnAudioFocusChangeListener(this)
//                    .build();
//        }
//        return mAudioFocusRequest;
//    }
//
//    @Override
//    public boolean canPlay() {
//        return hasAudioFocus();
//    }
//
//    private boolean hasAudioFocus() {
//        if (mAudioManager == null) {
//            return false;
//        }
//        if (mAudioFocusGained) {
//            return true;
//        }
//        int result = 0;
//        result = getResult();
//        mAudioFocusGained = result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED;
//        return mAudioFocusGained;
//    }
//
//    private int getResult() {
//        int result;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            result = mAudioManager.requestAudioFocus(getAudioFocusRequest());
//        } else {
//            result = mAudioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC,
//                    AudioManager.AUDIOFOCUS_GAIN);
//        }
//        return result;
//    }
//
//}
