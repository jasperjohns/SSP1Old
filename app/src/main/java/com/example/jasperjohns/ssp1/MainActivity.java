package com.example.jasperjohns.ssp1;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity  implements  MainActivityFragment.Callback, top10Fragment.Callback {

    private static boolean mTwoPane;
    private static final String DETAILFRAGMENT_TAG = "DFTAG";
    private static final String PLAYER_TAG = "PFTAG";
    public final String ARTIST_NAME = "artistname";
    public final String ARTIST_ALBUM = "artistalbum";
    private final String ARTIST_TRACKS = "artisttracks";
    private final String ARTIST_TRACK = "artisttrack";
    public final String ARTIST_TRACK_PREVIEW_URL = "artisttrackpreviewURL";
    public final String ARTIST_TRACK_IMAGE = "artisttrackimage";
    public final String SPOTIFY_ID = "spotifyid";
    private final String LIST_POSITION ="listPosition";




    /*
    Call back for MainActivityFragment, when the artist in the list is clicked on
     */

    @Override
    public void onItemSelected(String artistID, String artistName) {

        if (mTwoPane){
            Bundle extras = new Bundle();

            extras.putString(SPOTIFY_ID,artistID);
            extras.putString(ARTIST_NAME, artistName);

            top10Fragment fragment = new top10Fragment();
            fragment.setArguments(extras);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment, DETAILFRAGMENT_TAG)
                    .commit();
        }
        else {

            Intent top10Act = new Intent(this, top10.class);
            Bundle extras = new Bundle();
            extras.putString(SPOTIFY_ID, artistID);
            extras.putString(ARTIST_NAME,artistName);
            top10Act.putExtras(extras);

            startActivity(top10Act);

        }


    }

    /*
    Call back for top10Fragment, when the track in the list is clicked on
     */
    @Override
    public  void  onItemSelected(int position, String spotifyID, ArrayList<TrackData> arrayTracks){
        TrackData track = arrayTracks.get(position);

        Bundle extras = new Bundle();

        extras.putString(SPOTIFY_ID,spotifyID );
        extras.putString(ARTIST_NAME, track.getArtists());
        extras.putString(ARTIST_ALBUM, track.getArtistAlbum());
        extras.putString(ARTIST_TRACK, track.getArtistTrack());
        extras.putString(ARTIST_TRACK_IMAGE, track.getTrackImage());
        extras.putString(ARTIST_TRACK_PREVIEW_URL, track.getPreview_URL());
        extras.putParcelableArrayList(ARTIST_TRACKS, arrayTracks);
        extras.putInt(LIST_POSITION, position);

        if (mTwoPane){

            playerActivityFragment player = new playerActivityFragment();
            player.setArguments(extras);

            getFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, player, PLAYER_TAG)
                    .commit();
        }
        else {
            Intent player = new Intent(this, playerActivity.class);
            player.putExtras(extras);
            startActivity(player);
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Is this a two-pane/tablet layout or not
        if (findViewById(R.id.fragment_container) != null) {
            mTwoPane = true;
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new top10Fragment(), DETAILFRAGMENT_TAG)
                        .commit();
            }
        } else {
            mTwoPane = false;

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
