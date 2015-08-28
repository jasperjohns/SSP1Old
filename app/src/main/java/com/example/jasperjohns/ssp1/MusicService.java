package com.example.jasperjohns.ssp1;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MusicService extends Service
        implements  MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
    MediaPlayer.OnCompletionListener
    {
        //media player
        private MediaPlayer mPlayer;
        //song list
        ArrayList<TrackData> mArrayTracks;

        //current position
        private int songPosn;

        private String mSpotifyID;
        private String mArtistName;
        private String mArtistAlbum;
        private String mArtistTrack;
        private String mArtistTrackImage;
        private String mArtistsPreviewURL;

        private final String LOG_TAG = top10Fragment.class.getSimpleName();

        private ExecutorService executorService;

        private Handler mHandler = new Handler();;

        private static MusicService mInstance = null;

        private int mListPosition;

        public MusicService() {
        }

        public void onCreate(){
            //create the service
            super.onCreate();

            mInstance = this;

            executorService = Executors.newSingleThreadExecutor();

            //initialize position
            songPosn=0;
            //create player
            mPlayer = new MediaPlayer();
            //initialize
            initMusicPlayer();
        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {

            Runnable work2Run = new serviceRunnable(mInstance, intent, flags, startId);
            executorService.execute(work2Run);


            return START_STICKY;
        }


        public void initMusicPlayer(){
            //set player properties
            mPlayer.setWakeMode(getApplicationContext(),
                    PowerManager.PARTIAL_WAKE_LOCK);
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

            //set listeners
            mPlayer.setOnPreparedListener(this);
            mPlayer.setOnCompletionListener(this);
            mPlayer.setOnErrorListener(this);
        }




        //pass song list
        public void setList(ArrayList<TrackData> ArrayTracks){
            mArrayTracks=ArrayTracks;
        }

        //play a song
        public void playSong(){
            //play
            mPlayer.reset();
            //get song

            TrackData track = (TrackData) mArrayTracks.get(songPosn);
            mArtistName = track.getArtists();
            mArtistAlbum = track.getArtistAlbum();
            mArtistTrack = track.getArtistTrack();
            mArtistsPreviewURL=track.getPreview_URL();
            mArtistTrackImage=track.getTrackImage();


            try {
                mPlayer.setDataSource(mArtistsPreviewURL);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(LOG_TAG, "error:" + e.getMessage());
            }
            mPlayer.prepareAsync();
        }

        //set the song
        public void setSong(int songIndex){
            songPosn=songIndex;
        }

        @Override
        public void onCompletion(MediaPlayer mp) {
            // TODO Auto-generated method stub
        }

        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public void onPrepared(MediaPlayer mp) {
            //start playback
            mp.start();
            broadcastStatus();
        }

        //activity will bind to service
        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }


        class serviceRunnable implements Runnable {
            MusicService _service;
            Intent _intent;
            int _flags;
            int _startId;
            HandlerThread _handlerThread;
            Handler _handler;
            int _listPosition;

            public serviceRunnable(MusicService service,Intent intent, int flags, int startId){
                _service = service;
                _startId = startId;
                _intent = intent;
            }
            public void run() {

                setupHandler();
                mArrayTracks = _intent.getParcelableArrayListExtra(constants.ARTIST_TRACKS);
                mListPosition = _intent.getIntExtra(constants.LIST_POSITION, 0);

                if (_intent.getAction().equals(constants.ACTION.STARTFOREGROUND_ACTION)) {
                    Log.i(LOG_TAG, "Received Start Foreground _intent ");

                    setSong(mListPosition);
                    playSong();
                    //broadcastStatus();

                } else if (_intent.getAction().equals(constants.ACTION.PREV_ACTION)) {
                    Log.i(LOG_TAG, "Clicked Previous");
                    setSong(mListPosition);
                    playSong();
                    //broadcastStatus();

                } else if (_intent.getAction().equals(constants.ACTION.PLAY_ACTION)) {
                    int _currentDuration =  _intent.getIntExtra(constants.MEDIAPLAYER_CURRENT_DURATION, 0);
                    mPlayer.seekTo(_currentDuration);
                    mPlayer.start();

                    //broadcastStatus();


                    Log.i(LOG_TAG, "Clicked Play");
                } else if (_intent.getAction().equals(constants.ACTION.NEXT_ACTION)) {
                    Log.i(LOG_TAG, "Clicked Next");
                    setSong(mListPosition);
                    playSong();
                    //broadcastStatus();
                } else if (_intent.getAction().equals(constants.ACTION.PAUSE_ACTION)) {
                    Log.i(LOG_TAG, "Clicked Pause");
                    mPlayer.pause();
                    //broadcastStatus();

                } else if (_intent.getAction().equals(
                        constants.ACTION.STOPFOREGROUND_ACTION)) {
                    Log.i(LOG_TAG, "Received Stop Foreground _intent");

                    if (mPlayer.isPlaying()) {
                        if (mPlayer != null) {
                            mPlayer.stop();
                            mPlayer.release();
                        }

                    } else {
                        if (mPlayer != null) {
                            mPlayer.release();
                        }
                    }

                    stopForeground(true);
                    stopSelf();
                }

            }

            void setupHandler() {
                _handler = new Handler(_service.getMainLooper());

            }

        }

        // HANDLERS - that manage the runnables
        private void broadcastStatus (){
            // set handler to run post-delayed
            mHandler.postDelayed(mUpdateTimeTask, 100);
        }

        // RUNNABLES - that do the stuff
        private Runnable mUpdateTimeTask = new Runnable() {
            public void run() {
                int totalDuration = mPlayer.getDuration();
                int currentDuration = mPlayer.getCurrentPosition();
                boolean isPlaying = false;
                if (mPlayer.isPlaying()){

                    isPlaying= true;
                }


                Intent intent = new Intent();
                intent.setAction(constants.MY_ACTION);

                intent.putExtra(constants.TRACK_TOTAL_DURATION, totalDuration);
                intent.putExtra(constants.TRACK_CURRENT_DURATION, currentDuration);
                intent.putExtra(constants.ARTIST_NAME, mArtistName);
                intent.putExtra(constants.ARTIST_ALBUM, mArtistAlbum );
                intent.putExtra(constants.ARTIST_TRACK, mArtistTrack );
                intent.putExtra(constants.ARTIST_TRACK_IMAGE, mArtistTrackImage );
                intent.putExtra(constants.LIST_POSITION, mListPosition);
                intent.putExtra(constants.MEDIAPLAYER_PLAYING, isPlaying);


                        //send information back to the receivers
                 sendBroadcast(intent);

                // Running this thread after 100 milliseconds
                mHandler.postDelayed(this, 100);

 //               Log.i(LOG_TAG, " broadcastStatus - mCurrentDuration:" + Integer.toString(currentDuration));

            }
        };



    }
