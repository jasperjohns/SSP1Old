package com.example.jasperjohns.ssp1;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;


/**
 * A placeholder fragment containing a simple view.
 */
public class top10Fragment extends android.support.v4.app.Fragment {

    private final String LOG_TAG = top10Fragment.class.getSimpleName();
    private final String MSG_NOITEMS = "Sorry, no items found";
    private final String LISTPOSITION_TAG = "list_position";

    public static final String ARTIST_NAME = "artistname";
    public static final String ARTIST_ALBUM = "artistalbum";
    public static final String ARTIST_TRACKS = "artisttracks";
    public static final String ARTIST_TRACK = "artisttrack";
    public static final String ARTIST_TRACK_PREVIEW_URL = "artisttrackpreviewURL";
    public static final String ARTIST_TRACK_IMAGE = "artisttrackimage";
    public static final String SPOTIFY_ID = "spotifyid";
    public static final String LIST_POSITION ="listPosition";

    TrackDataAdapter adapter;

    private List<TrackData> tracksData;
    ArrayList<TrackData> arrayTracks;
    ListView listViewTracks;

    private String mSpotifyID;
    private String mArtistName;


    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callback {
        /**
         * DetailFragmentCallback for when an item has been selected.
         */
        public void onItemSelected(int position , String spotifyID, ArrayList<TrackData> arrayTracks );
    }


    public top10Fragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =
                inflater.inflate(R.layout.fragment_top10, container, false);

        if (savedInstanceState !=null){
            mSpotifyID = savedInstanceState.getString(SPOTIFY_ID);
            mArtistName = savedInstanceState.getString(ARTIST_NAME);
        }
        else {
//            Bundle bundle = getActivity().getIntent().getExtras();

            Bundle bundle = getArguments();

            if (bundle !=null) {
                if (getArguments().containsKey(SPOTIFY_ID)) {
                    mSpotifyID = getArguments().getString(SPOTIFY_ID).toString();
                    //           Log.v(LOG_TAG, bundle.getString(Intent.EXTRA_TEXT).toString());
                    UpdateData(mSpotifyID);

                }

                if(bundle.getString(SPOTIFY_ID)!= null)
                {
                    mSpotifyID = bundle.getString(SPOTIFY_ID).toString();
                    //           Log.v(LOG_TAG, bundle.getString(Intent.EXTRA_TEXT).toString());
                    UpdateData(mSpotifyID);
                }

                if(bundle.getString(ARTIST_NAME)!= null)
                {
                    mArtistName = bundle.getString(ARTIST_NAME).toString();
                    ((ActionBarActivity)getActivity()).getSupportActionBar().setSubtitle(mArtistName);
                }
            }
        }

        SetUpUI(rootView);
        SetUpListeners();

        return rootView;

    }




    private void SetUpUI(View v){
        arrayTracks = new ArrayList<TrackData>();


        listViewTracks = (ListView) v.findViewById(R.id.listViewTracks);
        adapter = new TrackDataAdapter(getActivity(),0, arrayTracks);
        listViewTracks.setAdapter(adapter);
    }



    private void SetUpListeners () {
        listViewTracks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Activity activity = getActivity();
                Callback callback = (Callback)activity;
                callback.onItemSelected(position, mSpotifyID, arrayTracks);

//                ((Callback) getActivity()).onItemSelected(position, mSpotifyID, arrayTracks);


/*
                Object listItem = listViewTracks.getItemAtPosition(position);
                //               Log.v(LOG_TAG, listItem.toString());

                TrackData track = arrayTracks.get(position);






//                Intent top10Act = new Intent(getActivity(), top10.class).putExtra(Intent.EXTRA_TEXT, artist.getArtistId()) ;
                Intent playerAct = new Intent(getActivity(), playerActivity.class);
                Bundle extras = new Bundle();
                extras.putString(SPOTIFY_ID, mSpotifyID);
                extras.putString(ARTIST_NAME, track.getArtists());
                extras.putString(ARTIST_ALBUM, track.getArtistAlbum());
                extras.putString(ARTIST_TRACK, track.getArtistTrack());
                extras.putString(ARTIST_TRACK_IMAGE, track.getTrackImage());
                extras.putString(ARTIST_TRACK_PREVIEW_URL, track.getPreview_URL());
                extras.putParcelableArrayList(ARTIST_TRACKS, arrayTracks);
                extras.putInt(LIST_POSITION,position);
                playerAct.putExtras(extras);

////                Toast.makeText(getActivity(), "selected Item Name is " + listItem.toString(), Toast.LENGTH_LONG).show();
                startActivity(playerAct);
*/
            }
        });

    }

    private void UpdateData(String spotifyId){
        new FetchDataTask().execute(spotifyId);
    }


    // LIFE-CYCLE EVENTS
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        Integer listPostion =  listViewTracks.getFirstVisiblePosition();
        savedInstanceState.putInt(LISTPOSITION_TAG, listPostion);
        savedInstanceState.putString(SPOTIFY_ID, mSpotifyID);
        savedInstanceState.putString(ARTIST_NAME, mArtistName);
        savedInstanceState.putParcelableArrayList(ARTIST_TRACKS, arrayTracks);

        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState){
        super.onViewStateRestored(savedInstanceState);
        if(savedInstanceState != null) {

            mSpotifyID = savedInstanceState.getString(SPOTIFY_ID);
            mArtistName = savedInstanceState.getString(ARTIST_NAME);
            arrayTracks = savedInstanceState.getParcelableArrayList(ARTIST_TRACKS);
            Integer listPostion =  savedInstanceState.getInt(LISTPOSITION_TAG);
            listViewTracks.setSelection(listPostion);

            adapter.clear();
            adapter.addAll(arrayTracks);
            adapter.notifyDataSetChanged();
        }


    }


    // INNER CLASSES
    private class FetchDataTask extends AsyncTask<String, Integer, Tracks> {
        protected Tracks doInBackground(String... params) {

            String artistID = params[0];

            SpotifyApi api = new SpotifyApi();
            SpotifyService spotify = api.getService();
            Map<String, Object> spotifyOptions = new HashMap<>();
            spotifyOptions.put("country","US");

            Tracks results = spotify.getArtistTopTrack(artistID, spotifyOptions);

            return results;
        }

        @Override
        protected void onPostExecute(Tracks tracks)
        {
            super.onPostExecute(tracks);
            if (tracks != null) {
                if (tracks.tracks.size() > 0) {
                    adapter.clear();
                    adapter.addAll(convertData((ArrayList) tracks.tracks));

                    adapter.notifyDataSetChanged();
                }
                else {
                    adapter.clear();
                    //no artist found Toast
                    Toast.makeText(getActivity(), MSG_NOITEMS, Toast.LENGTH_LONG).show();
                }
            }
            else {
            }
        }

        private ArrayList<TrackData> convertData(ArrayList<Track> tracks){
            ArrayList returnTracks = new ArrayList<>();
            for(Track track: tracks){
                if(track.album.images.size() > 0)
                    returnTracks.add(new TrackData(mArtistName, track.id,track.album.name, track.name, track.album.images.get(0).url, track.preview_url));
                else
                    returnTracks.add(new TrackData(mArtistName, track.id,track.album.name, track.name, "",track.preview_url));
            }
            return returnTracks;
        }
    }
}
