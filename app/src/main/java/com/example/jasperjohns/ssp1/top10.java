package com.example.jasperjohns.ssp1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;


public class top10 extends ActionBarActivity  implements  top10Fragment.Callback{

    /*
    Call back for top10Fragment, when the track in the list is clicked on
     */
    @Override
    public  void  onItemSelected(int position, String spotifyID, ArrayList<TrackData> arrayTracks){


        TrackData track = arrayTracks.get(position);

        Bundle extras = new Bundle();

        extras.putString(top10Fragment.SPOTIFY_ID,spotifyID );
        extras.putString(top10Fragment.ARTIST_NAME, track.getArtists());
        extras.putString(top10Fragment.ARTIST_ALBUM, track.getArtistAlbum());
        extras.putString(top10Fragment.ARTIST_TRACK, track.getArtistTrack());
        extras.putString(top10Fragment.ARTIST_TRACK_IMAGE, track.getTrackImage());
        extras.putString(top10Fragment.ARTIST_TRACK_PREVIEW_URL, track.getPreview_URL());
        extras.putParcelableArrayList(top10Fragment.ARTIST_TRACKS, arrayTracks);
        extras.putInt(top10Fragment.LIST_POSITION, position);

        Intent player = new Intent(this, playerActivity.class);
        player.putExtras(extras);
        startActivity(player);



    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_top10);

        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString(top10Fragment.SPOTIFY_ID,
                    getIntent().getStringExtra(top10Fragment.SPOTIFY_ID));

            arguments.putString(top10Fragment.ARTIST_NAME,
                    getIntent().getStringExtra(top10Fragment.ARTIST_NAME));


            top10Fragment fragment = new top10Fragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_top10, menu);
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
