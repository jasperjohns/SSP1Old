package com.example.jasperjohns.ssp1;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


/**
 * A placeholder fragment containing a simple view.
 */
public class playerActivityFragment extends Fragment {

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


    private ImageView mImgPlay;
    private ImageView mImgPrev;
    private ImageView mImgNext;

    private TextView mTxtSongCurrentDuration;
    private TextView mTxtSongTotalDuration;


    private ProgressBar mProgress;
    private int mProgressStatus = 0;
    private Utilities mUtils = new Utilities();

    ArrayList<TrackData> mArrayTracks;



    public playerActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        View rootView =
                inflater.inflate(R.layout.fragment_player, container, false);

        Bundle bundle = getActivity().getIntent().getExtras();

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
        }

        mListPosition= bundle.getInt(LIST_POSITION);




//        UpdateData(mArtistsPreviewURL);


        ((ActionBarActivity)getActivity()).getSupportActionBar().setSubtitle(bundle.getString(ARTIST_NAME).toString());

        SetUpUI(rootView);
        SetUpListeners();

        playSong(mListPosition);


        return rootView;
    }

    private void playSong(int indexValue) {
        Log.v(LOG_TAG, Integer.toString(indexValue));
        TrackData track = (TrackData) mArrayTracks.get(indexValue);
        UpdateData(track.getPreview_URL());

        if (indexValue >= (mArrayTracks.size()-1)){
            mImgNext.setEnabled(false);
        }

    }


    private void SetUpUI(View v){

        TextView textArtitst = (TextView) v.findViewById((R.id.textArtist));
        TextView textAlbum = (TextView) v.findViewById((R.id.textAlbum));
        TextView textTrack = (TextView) v.findViewById((R.id.textTrack));
        ImageView imgTrack = (ImageView) v.findViewById((R.id.imgTrack));

        mProgressBar = (SeekBar) v.findViewById((R.id.songProgressBar));
        mImgPlay = (ImageView) v.findViewById((R.id.imgPlay));
        mImgNext = (ImageView) v.findViewById(R.id.imgNext);
        mImgPrev = (ImageView) v.findViewById(R.id.imgPrev);

        mTxtSongCurrentDuration = ( TextView) v.findViewById(R.id.songCurrentDuration);
        mTxtSongTotalDuration  = ( TextView) v.findViewById(R.id.songTotalDuration);


        textArtitst.setText(mArtistName);
        textAlbum.setText(mArtistAlbum);
        textTrack.setText(mArtistTrack);

        Picasso.with(getActivity().getBaseContext()).load(mArtistTrackImage).resize(200, 200).centerCrop().into(imgTrack);


    }

    private void SetUpListeners () {

/*
        mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(final MediaPlayer mp, final int what,
                                   final int extra) {
                Log.e(LOG_TAG, "Error occurred while playing audio.");
                mMediaPlayer.stop();
                return false;
            }
        });
*/

        mImgPlay.setClickable(true);
        mImgPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMediaPlayer.isPlaying()) {
                    if (mMediaPlayer != null) {
                        mMediaPlayer.pause();
                        mImgPlay.setImageResource(android.R.drawable.ic_media_play);
                    }

                } else {
                    if (mMediaPlayer != null) {
                        mMediaPlayer.start();
                        mImgPlay.setImageResource(android.R.drawable.ic_media_pause);
                    }
                }
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
        new FetchDataTask().execute(artistsPreviewURL);
    }

    // HANDLERS - that manage the runnables
    private void updateProgressBar(){
        // set handler to run post-delayed
        mHandler.postDelayed(mUpdateTimeTask, 100);
    }


    // RUNNABLES - that do the stuff
    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            long totalDuration = mMediaPlayer.getDuration();
            long currentDuration = mMediaPlayer.getCurrentPosition();

            // Displaying Total Duration time
            mTxtSongCurrentDuration.setText("" + mUtils.milliSecondsToTimer(totalDuration));
            // Displaying time completed playing
            mTxtSongTotalDuration.setText("" + mUtils.milliSecondsToTimer(currentDuration));

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

        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState){
        super.onViewStateRestored(savedInstanceState);
        if(savedInstanceState != null) {
            mArrayTracks = savedInstanceState.getParcelableArrayList(ARTIST_TRACKS);
        }
    }

    // INNER CLASSES
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





}
