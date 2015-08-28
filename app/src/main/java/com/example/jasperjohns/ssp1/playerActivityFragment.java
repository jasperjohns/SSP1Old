package com.example.jasperjohns.ssp1;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


/**
 * A placeholder fragment containing a simple view.
 */
public class playerActivityFragment extends DialogFragment {

    private final String LOG_TAG = playerActivityFragment.class.getSimpleName();
    private final String MSG_NOITEMS = "Sorry, no items found";
    private final String ARTIST_NAME = "artistname";
    private final String ARTIST_ALBUM = "artistalbum";
    private final String ARTIST_TRACK = "artisttrack";
    private final String ARTIST_TRACKS = "artisttracks";
    private final String ARTIST_TRACK_PREVIEW_URL = "artisttrackpreviewURL";
    private final String ARTIST_TRACK_IMAGE = "artisttrackimage";
    private final String SPOTIFY_ID = "spotifyid";
    private final String LIST_POSITION ="listPosition";

    private String mSpotifyID;
    private String mArtistName;
    private String mArtistAlbum;
    private String mArtistTrack;
    private String mArtistTrackImage;
    private String mArtistsPreviewURL;
    private MediaPlayer mMediaPlayer = new MediaPlayer();
    private SeekBar mProgressBar;
    private Handler mHandler = new Handler();;
    private int mListPosition;
    private int mListSize =0 ;
    private boolean mIsPlaying = false;

    private int mCurrentDuration;
    private int mTotalDuration;

    //service
    private MusicService mMusicSrv;
    private Intent mPlayIntent;
    //binding
    private boolean mMusicBound=false;



    private ImageView mImgPlay;
    private ImageView mImgPrev;
    private ImageView mImgNext;
    private TextView mTextArtitst;
    private TextView mTextAlbum;
    private TextView mTextTrack;
    private ImageView mImgTrack;


    private TextView mTxtSongCurrentDuration;
    private TextView mTxtSongTotalDuration;


    private ProgressBar mProgress;
    private int mProgressStatus = 0;
    private Utilities mUtils = new Utilities();

    ArrayList<TrackData> mArrayTracks;

    private MusicServiceReceiver musicServiceReceiver;

    public playerActivityFragment() {
    }

    //start and bind the service when the activity starts
    @Override
    public void onStart() {
        musicServiceReceiver = new MusicServiceReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(constants.MY_ACTION);

        getActivity().registerReceiver(musicServiceReceiver, intentFilter);

        super.onStart();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        setRetainInstance(true);



    }

    @Override
    public void onPause()
    {
        super.onPause();

        if (musicServiceReceiver != null){

            getActivity().unregisterReceiver(musicServiceReceiver);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView =
                inflater.inflate(R.layout.fragment_player, container, false);

        if (savedInstanceState !=null){
                mArrayTracks = savedInstanceState.getParcelableArrayList(ARTIST_TRACKS);

                mArtistName  =   savedInstanceState.getString(constants.ARTIST_NAME);
                mArtistAlbum =  savedInstanceState.getString(constants.ARTIST_ALBUM);
                mArtistTrack =  savedInstanceState.getString(constants.ARTIST_TRACK);
                mArtistTrackImage  = savedInstanceState.getString(constants.ARTIST_TRACK_IMAGE);

                mListPosition =            savedInstanceState.getInt(constants.LIST_POSITION);
                mCurrentDuration = savedInstanceState.getInt(constants.TRACK_CURRENT_DURATION);
                mTotalDuration = savedInstanceState.getInt(constants.TRACK_TOTAL_DURATION);

                mIsPlaying = savedInstanceState.getBoolean(constants.MEDIAPLAYER_PLAYING);
        }
        else {

//        Bundle bundle = getActivity().getIntent().getExtras();
            Bundle bundle = getArguments();

            if(bundle.getString(ARTIST_NAME)!= null)  {
                mArtistName = bundle.getString(ARTIST_NAME).toString();
            }
            if(bundle.getString(ARTIST_ALBUM)!= null)  {
                mArtistAlbum = bundle.getString(ARTIST_ALBUM).toString();
            }
            if(bundle.getString(ARTIST_TRACK)!= null)  {
                mArtistTrack = bundle.getString(ARTIST_TRACK).toString();
            }

            if(bundle.getString(ARTIST_TRACK_PREVIEW_URL)!= null)  {
                mArtistsPreviewURL = bundle.getString(ARTIST_TRACK_PREVIEW_URL).toString();
            }

            if(bundle.getString(ARTIST_TRACK_IMAGE)!= null) {
                mArtistTrackImage = bundle.getString(ARTIST_TRACK_IMAGE).toString();
            }

            if(bundle.getParcelableArrayList(ARTIST_TRACKS)!= null) {
                mArrayTracks = bundle.getParcelableArrayList(ARTIST_TRACKS);
                //Assign the size of the list
                mListSize = mArrayTracks.size();
            }

            mListPosition= bundle.getInt(LIST_POSITION);

            ((ActionBarActivity)getActivity()).getSupportActionBar().setSubtitle(bundle.getString(ARTIST_NAME).toString());
/*
            musicServiceReceiver = new MusicServiceReceiver();
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(constants.MY_ACTION);

            getActivity().registerReceiver(musicServiceReceiver, intentFilter);
*/
        }






//        UpdateData(mArtistsPreviewURL);



        SetUpUI(rootView);
        AssignUIValues();
        SetUpListeners();

        //Start music service
        Intent startIntent = new Intent(getActivity(), MusicService.class);
        startIntent.putParcelableArrayListExtra(ARTIST_TRACKS, mArrayTracks);
        startIntent.putExtra(constants.LIST_POSITION, mListPosition);


        if (savedInstanceState !=null){
/*
            startIntent.setAction(constants.ACTION.PLAY_ACTION);
            mImgPlay.setImageResource(android.R.drawable.ic_media_pause);
            startIntent.putExtra(constants.MEDIAPLAYER_CURRENT_DURATION, mCurrentDuration);
            Log.i(LOG_TAG, " onCreateView - mCurrentDuration:" + Integer.toString(mCurrentDuration));
*/

//            startIntent.setAction(constants.ACTION.STARTFOREGROUND_ACTION);
        }
        else {

            startIntent.setAction(constants.ACTION.STARTFOREGROUND_ACTION);
            getActivity().startService(startIntent);
        }



        return rootView;
    }




    /** The system calls this only when creating the layout in a dialog. */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // The only reason you might override this method when using onCreateView() is
        // to modify any dialog characteristics. For example, the dialog includes a
        // title by default, but your custom layout might not need it. So here you can
        // remove the dialog title, but you must call the superclass to get the Dialog.
        Dialog dialog = super.onCreateDialog(savedInstanceState);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    /**
     * react to the user tapping the back/up icon in the action bar
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                if (mMediaPlayer.isPlaying()) {
                    if (mMediaPlayer != null) {
                        mMediaPlayer.stop();
                        mMediaPlayer.release();
                    }

                } else {
                    if (mMediaPlayer != null) {
                        mMediaPlayer.release();
                    }
                }


                // this takes the user 'back', as if they pressed the left-facing triangle icon on the main android toolbar.
                // if this doesn't work as desired, another possibility is to call `finish()` here.

                getActivity().onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void playSong(int indexValue) {
        Log.v(LOG_TAG, Integer.toString(indexValue));
        TrackData track = (TrackData) mArrayTracks.get(indexValue);


        mArtistName = track.getArtists();
        mArtistAlbum = track.getArtistAlbum();
        mArtistTrack = track.getArtistTrack();
        mArtistsPreviewURL=track.getPreview_URL();
        mArtistTrackImage=track.getTrackImage();
        AssignUIValues();


        UpdateData(track.getPreview_URL());

        if (indexValue >= (mArrayTracks.size()-1)){
            mImgNext.setEnabled(false);
        }

    }

    //Assign global variables the element in the layout
    private void SetUpUI(View v){

        mTextArtitst = (TextView) v.findViewById((R.id.textArtist));
        mTextAlbum = (TextView) v.findViewById((R.id.textAlbum));
        mTextTrack = (TextView) v.findViewById((R.id.textTrack));
        mImgTrack = (ImageView) v.findViewById((R.id.imgTrack));

        mProgressBar = (SeekBar) v.findViewById((R.id.songProgressBar));
        mImgPlay = (ImageView) v.findViewById((R.id.imgPlayPause));
        mImgNext = (ImageView) v.findViewById(R.id.imgNext);
        mImgPrev = (ImageView) v.findViewById(R.id.imgPrev);

        mTxtSongCurrentDuration = ( TextView) v.findViewById(R.id.songCurrentDuration);
        mTxtSongTotalDuration  = ( TextView) v.findViewById(R.id.songTotalDuration);


    }

    // Assign the UI elements content
    private void AssignUIValues(){

        mTextArtitst.setText(mArtistName);
        mTextAlbum.setText(mArtistAlbum);
        mTextTrack.setText(mArtistTrack);

        // Reciver status from service
        if (mIsPlaying){
            mImgPlay.setImageResource(android.R.drawable.ic_media_pause);
        }
        else {
            mImgPlay.setImageResource(android.R.drawable.ic_media_play);
        }
        Picasso.with(getActivity().getBaseContext()).load(mArtistTrackImage).resize(200, 200).centerCrop().into(mImgTrack);

    }

    //Handles the click event Listeneres for the Next/Prev/Pause buttons in the player
    private void SetUpListeners () {


        mImgPlay.setClickable(true);
        mImgPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent startIntentPrev = new Intent(getActivity(), MusicService.class);
                startIntentPrev.putParcelableArrayListExtra(constants.ARTIST_TRACKS, mArrayTracks);
                startIntentPrev.putExtra(constants.LIST_POSITION, mListPosition);


                if (mIsPlaying) {
                    startIntentPrev.setAction(constants.ACTION.PAUSE_ACTION);
                    mImgPlay.setImageResource(android.R.drawable.ic_media_play);

                } else {
                    startIntentPrev.setAction(constants.ACTION.PLAY_ACTION);
                    mImgPlay.setImageResource(android.R.drawable.ic_media_pause);
                    startIntentPrev.putExtra(constants.MEDIAPLAYER_CURRENT_DURATION, mCurrentDuration);
                }

                getActivity().startService(startIntentPrev);

            }
        });

        // Next Button
        mImgNext.setClickable(true);
        mImgNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListPosition++;
                if (mListPosition > mListSize) {
                    mListPosition = 0;
                }

                Intent startIntentNext = new Intent(getActivity(), MusicService.class);
                startIntentNext.putParcelableArrayListExtra(constants.ARTIST_TRACKS, mArrayTracks);
                startIntentNext.setAction(constants.ACTION.NEXT_ACTION);
                startIntentNext.putExtra(constants.LIST_POSITION, mListPosition);

                getActivity().startService(startIntentNext);


            }
        });

        // Prev Button
        mImgPrev.setClickable(true);
        mImgPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //decrement and see if its avalid one
                mListPosition--;
                if (mListPosition < 0) {
                    mListPosition = mArrayTracks.size()-1;
                }
                Intent startIntentPrev = new Intent(getActivity(), MusicService.class);
                startIntentPrev.putParcelableArrayListExtra(constants.ARTIST_TRACKS, mArrayTracks);
                startIntentPrev.setAction(constants.ACTION.PREV_ACTION);
                startIntentPrev.putExtra(constants.LIST_POSITION, mListPosition);

                getActivity().startService(startIntentPrev);
            }
        });

        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {

                mProgressBar.setVisibility(ProgressBar.VISIBLE);
                mProgressBar.setProgress(0);
//                mProgressBar.setMax(mMediaPlayer.getDuration());
                mProgressBar.setMax((100));
                Log.v(LOG_TAG, Integer.toString(mMediaPlayer.getDuration()));
                // TODO Auto-generated method stub
                mMediaPlayer.start();
                updateProgressBar();
            }
        });


        mProgressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
//                scrubberTv.setText("" + i + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mHandler.removeCallbacks(mUpdateTimeTask);

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mHandler.removeCallbacks(mUpdateTimeTask);
                int totalDuration = mMediaPlayer.getDuration();
                int currentPosition = mUtils.progressToTimer(seekBar.getProgress(), totalDuration);

                // forward or backward to certain seconds
                mMediaPlayer.seekTo(currentPosition);

                // update timer progress again
                updateProgressBar();

            }
        });

    }

    private void UpdateData(String artistsPreviewURL){
//        new FetchDataTask().execute(artistsPreviewURL);
    }

    // HANDLERS - that manage the runnables
    private void updateProgressBar(){
        // set handler to run post-delayed
 //       mHandler.postDelayed(mUpdateTimeTask, 100);
    }


    // RUNNABLES - that do the stuff
    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            long totalDuration = mMediaPlayer.getDuration();
            long currentDuration = mMediaPlayer.getCurrentPosition();

            // Displaying Total Duration time
            mTxtSongCurrentDuration.setText("" + mUtils.milliSecondsToTimer(currentDuration));
            // Displaying time completed playing
            mTxtSongTotalDuration.setText("" + mUtils.milliSecondsToTimer(totalDuration));

            // Updating progress bar
            int progress = (int)(mUtils.getProgressPercentage(currentDuration, totalDuration));
            //Log.d("Progress", ""+progress);
            mProgressBar.setProgress(progress);
            // Running this thread after 100 milliseconds
            mHandler.postDelayed(this, 100);
        }
    };



    // Save/Restore State
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        savedInstanceState.putParcelableArrayList(ARTIST_TRACKS, mArrayTracks);
        savedInstanceState.putString(constants.ARTIST_NAME, mArtistName);
        savedInstanceState.putString(constants.ARTIST_ALBUM, mArtistAlbum);
        savedInstanceState.putString(constants.ARTIST_TRACK, mArtistTrack);
        savedInstanceState.putString(constants.ARTIST_TRACK_IMAGE, mArtistTrackImage);

        savedInstanceState.putInt(constants.LIST_POSITION, mListPosition);
        savedInstanceState.putInt(constants.TRACK_CURRENT_DURATION, mCurrentDuration);
        savedInstanceState.putInt(constants.TRACK_TOTAL_DURATION, mTotalDuration);

        savedInstanceState.putBoolean(constants.MEDIAPLAYER_PLAYING, mIsPlaying);

        super.onSaveInstanceState(savedInstanceState);

        Log.i(LOG_TAG, " onSaveInstanceState - mCurrentDuration:" + Integer.toString(mCurrentDuration));

    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState){
        super.onViewStateRestored(savedInstanceState);
        if(savedInstanceState != null) {
            mArrayTracks = savedInstanceState.getParcelableArrayList(ARTIST_TRACKS);

            mArtistName  =   savedInstanceState.getString(constants.ARTIST_NAME);
            mArtistAlbum =  savedInstanceState.getString(constants.ARTIST_ALBUM);
            mArtistTrack =  savedInstanceState.getString(constants.ARTIST_TRACK);
            mArtistTrackImage  = savedInstanceState.getString(constants.ARTIST_TRACK_IMAGE);

            mListPosition = savedInstanceState.getInt(constants.LIST_POSITION);
            mCurrentDuration = savedInstanceState.getInt(constants.TRACK_CURRENT_DURATION);
            mTotalDuration = savedInstanceState.getInt(constants.TRACK_TOTAL_DURATION);

            mIsPlaying = savedInstanceState.getBoolean(constants.MEDIAPLAYER_PLAYING);

            Log.i (LOG_TAG," onViewStateRestored - mCurrentDuration:"  + Integer.toString(mCurrentDuration));

            AssignUIValues();

        }
    }

    // INNER CLASSES
    // Receive and process the broadcast from the service
    public class MusicServiceReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            mCurrentDuration = intent.getIntExtra(constants.TRACK_CURRENT_DURATION, 0);
            mTotalDuration = intent.getIntExtra(constants.TRACK_TOTAL_DURATION, 0);

            mArtistName = intent.getStringExtra(constants.ARTIST_NAME);
            mArtistAlbum = intent.getStringExtra(constants.ARTIST_ALBUM);
            mArtistTrack = intent.getStringExtra(constants.ARTIST_TRACK);
            mArtistTrackImage = intent.getStringExtra(constants.ARTIST_TRACK_IMAGE);
            mListPosition = intent.getIntExtra(constants.LIST_POSITION, -1);
            mIsPlaying = intent.getBooleanExtra(constants.MEDIAPLAYER_PLAYING, false);

            if (mListPosition >= (mArrayTracks.size()-1)){
                mImgNext.setEnabled(false);
            }

            if (mListPosition <= 0){
                mImgPrev.setEnabled(false);
            }

            AssignUIValues();

            // Displaying Total Duration time
            mTxtSongCurrentDuration.setText("" + mUtils.milliSecondsToTimer(mCurrentDuration));
            // Displaying time completed playing
            mTxtSongTotalDuration.setText("" + mUtils.milliSecondsToTimer(mTotalDuration));

            // Updating progress bar
            int progress = (int)(mUtils.getProgressPercentage(mCurrentDuration, mTotalDuration));
            //Log.d("Progress", ""+progress);
            mProgressBar.setProgress(progress);
/*

            Toast.makeText(getActivity(),
                    "Triggered by Service!",
                    Toast.LENGTH_LONG).show();
*/

        }

    }





/*
    private class FetchDataTask extends AsyncTask<String, Integer, String> {
        protected String doInBackground(String... params) {

           String artistPreviewURL= params[0];

           mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);


            try {
                mMediaPlayer.setDataSource(artistPreviewURL);
 //               mMediaPlayer.create(this, R.raw.tutor);
 //               mMediaPlayer.setDisplay(holder);
                mMediaPlayer.prepareAsync();
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(LOG_TAG, "error:" + e.getMessage());
            }

            String results = null;

            return results;
        }

        @Override
        protected void onPostExecute(String preview_url)
        {
            super.onPostExecute(preview_url);
        }
    }
*/

}
