//package com.kafka.player.service;
//
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.media.AudioManager;
//import android.os.Handler;
//import android.os.Looper;
//import android.os.Message;
//import android.os.PowerManager;
//import android.view.KeyEvent;
//
//import com.kafka.player.model.PlayerCommand;
//
///**
// * Declared in the AndroidManifest to receive intents. Used to control headset
// * playback. <br/>
// * Single press: pause/resume <br/>
// * Double press: next track <br/>
// * Triple press: previous track <br/>
// */
//public class PlayerServiceIntentReceiver extends BroadcastReceiver {
//
//    private static final String LOG_TAG                  = "PLAYER_SERVICE_INTENT_RECEIVER";
//
//    private static final Handler HANDLER                  = new Handler(Looper.getMainLooper(), new HandlerCallback());
//
//    private static final int                   MSG_HEADSET_DOUBLE_CLICK = 1;
//
//    private static final long                  WAKELOCK_TIMEOUT         = 10 * 1000;
//
//    private static final int                   DOUBLE_CLICK_DELAY       = 800;
//
//    private static PowerManager.WakeLock       sWakeLock                = null;
//
//    private static int                         sClickCounter            = 0;
//
//    private static long                        sLastClickTime           = 0;
//
//    private static PlayerServiceIntentReceiver receiver;
//
//    public static void init(Context context) {
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction("android.media.AUDIO_BECOMING_NOISY");
//        intentFilter.addAction("android.intent.action.MEDIA_BUTTON");
//
//        receiver = new PlayerServiceIntentReceiver();
//        context.registerReceiver(receiver, intentFilter);
//    }
//
//    @Override
//    public void onReceive(Context context, Intent intent) {
//        boolean used = processMediaButtonIntent(context, intent);
//        if (used && isOrderedBroadcast()) {
//            abortBroadcast();
//        }
//    }
//
//    private static void startService(Context context, PlayerCommand command) {
//        PlayerServiceBridge instance = PlayerServiceBridge.Companion.getInstance();
//        if (instance.currentSong() != null) {
//            if(command == PlayerCommand.TOGGLE) {
//                instance.togglePlayback();
//            } else if(command == PlayerCommand.PLAY && instance.currentSong() != null) {
//                instance.playSong(instance.currentSong());
//            } else if(command == PlayerCommand.NEXT) {
//                instance.playNextSong();
//            } else if(command == PlayerCommand.PREV) {
//                instance.playPrevSong();
//            } else if(command == PlayerCommand.STOP) {
//                instance.stop();
//            } else if(command == PlayerCommand.PAUSE) {
//                instance.pause();
//            }
//        }
//    }
//
//    private static void acquireWakeLockAndSendMessage(Context context, Message msg, long delay) {
//        HANDLER.sendMessageDelayed(msg, delay);
//    }
//
//    private static void releaseWakeLockIfHandlerIdle() {
//
//    }
//
//    public static boolean processMediaButtonIntent(Context context, Intent intent) {
////        LogUtils.debugLog(LOG_TAG, "Received intent: " + intent);
//        final String intentAction = intent.getAction();
//
//        if (AudioManager.ACTION_AUDIO_BECOMING_NOISY.equals(intentAction)) {
//            // Headphones disconnected or audio route changed
//
//            if (PlayerServiceBridge.Companion.getInstance().isPlaying()) {
//                PlayerServiceBridge.Companion.getInstance().pause();
//            }
////            if (isTelephonyInterrupted()) {
////                setTelephonyInterrupted(false);
////            }
//            return true;
//        }
//
//        if (AudioManager.ACTION_HEADSET_PLUG.equals(intentAction)) {
//            int state = intent.getIntExtra("state", 1);
////            if (state == 1 && isTelephonyInterrupted()) {
////                setTelephonyInterrupted(false);
////            }
//            return true;
//        }
//
//        if (Intent.ACTION_MEDIA_BUTTON.equals(intentAction)) {
//            final KeyEvent event = intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
//            if (event == null) {
//                return false;
//            }
//
//            final int keyCode = event.getKeyCode();
//            final int action = event.getAction();
//            final long eventTime = event.getEventTime();
//
//            PlayerCommand command = null;
//            switch (keyCode) {
//            case KeyEvent.KEYCODE_HEADSETHOOK:
//            case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
//                command = PlayerCommand.TOGGLE;
//                break;
//            case KeyEvent.KEYCODE_MEDIA_PLAY:
//                if (!PlayerServiceBridge.Companion.getInstance().isPlaying()) {
//                    command = PlayerCommand.TOGGLE;
//                } else {
//                    command = PlayerCommand.PLAY;
//                }
//                break;
//            case KeyEvent.KEYCODE_MEDIA_PAUSE:
//                command = PlayerCommand.PAUSE;
//                break;
//            case KeyEvent.KEYCODE_MEDIA_STOP:
//                command = PlayerCommand.STOP;
//                break;
//            case KeyEvent.KEYCODE_MEDIA_NEXT:
//                command = PlayerCommand.NEXT;
//                break;
//            case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
//                command = PlayerCommand.PREV;
//                break;
//            }
//
//            if (command == null) {
//                return false;
//            }
//
//            if (action == KeyEvent.ACTION_DOWN) {
//                if (event.getRepeatCount() == 0) {
//                    // Only consider the first event in a sequence, not the
//                    // repeat events, so that we don't trigger in cases
//                    // where the first event went to a different app (e.g.
//                    // when the user ends a phone call by long pressing the
//                    // headset button)
//                    if (keyCode == KeyEvent.KEYCODE_HEADSETHOOK) {
//                        if (eventTime - sLastClickTime >= DOUBLE_CLICK_DELAY) {
//                            sClickCounter = 0;
//                        }
//
//                        sClickCounter++;
////                        LogUtils.debugLog(LOG_TAG, "Got headset click, count = " + sClickCounter);
//                        HANDLER.removeMessages(MSG_HEADSET_DOUBLE_CLICK);
//                        Message msg = HANDLER.obtainMessage(MSG_HEADSET_DOUBLE_CLICK, sClickCounter, 0, context);
//                        long delay = 0;
//                        if (sClickCounter > 1 && sClickCounter < 3) {
//                            delay = DOUBLE_CLICK_DELAY;
//                        }
//                        if (sClickCounter >= 3) {
//                            sClickCounter = 0;
//                        }
//                        sLastClickTime = eventTime;
//                        acquireWakeLockAndSendMessage(context, msg, delay);
//                    } else {
//                        // https://fabric.io/bharti-softbank2/android/apps/com.bsbportal.music/issues/56da983fffcdc042508f59e4?time=last-seven-days
//                        if (context != null)
//                            startService(context, command);
//                    }
//                }
//            }
//            releaseWakeLockIfHandlerIdle();
//            return true;
//        }
//
//        return false;
//    }
//
//    private static class HandlerCallback implements Handler.Callback {
//
//        @Override
//        public boolean handleMessage(Message msg) {
//            switch (msg.what) {
//            case MSG_HEADSET_DOUBLE_CLICK:
//                int clickCount = msg.arg1;
//                PlayerCommand command = null;
//
////                LogUtils.debugLog(LOG_TAG, "Handling headset click, count = " + clickCount);
//                switch (clickCount) {
//                case 1:
//                    command = PlayerCommand.TOGGLE;
//                    break;
//                case 2:
//                    command = PlayerCommand.NEXT;
//                    break;
//                case 3:
//                    command = PlayerCommand.PREV;
//                    break;
//                }
//
//                if (command != null) {
//                    final Context context = (Context) msg.obj;
//                    startService(context, command);
//                }
//                break;
//            }
//            releaseWakeLockIfHandlerIdle();
//            return true;
//        }
//    }
//
//    public static void unregister(Context context) {
//        if (receiver == null)
//            return;
//        context.unregisterReceiver(receiver);
//        receiver = null;
//    }
//}
