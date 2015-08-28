package com.example.jasperjohns.ssp1;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;


public class playerActivity extends ActionBarActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();

            arguments.putString(top10Fragment.ARTIST_NAME,
                    getIntent().getStringExtra(top10Fragment.ARTIST_NAME));

            arguments.putString(top10Fragment.ARTIST_ALBUM,
                    getIntent().getStringExtra(top10Fragment.ARTIST_ALBUM));

            arguments.putString(top10Fragment.ARTIST_TRACK,
                    getIntent().getStringExtra(top10Fragment.ARTIST_TRACK));

            arguments.putString(top10Fragment.ARTIST_TRACK_PREVIEW_URL,
                    getIntent().getStringExtra(top10Fragment.ARTIST_TRACK_PREVIEW_URL));

            arguments.putString(top10Fragment.ARTIST_TRACK_IMAGE,
                    getIntent().getStringExtra(top10Fragment.ARTIST_TRACK_IMAGE));

            arguments.putParcelableArrayList(top10Fragment.ARTIST_TRACKS,
                    getIntent().getParcelableArrayListExtra(top10Fragment.ARTIST_TRACKS));


            arguments.putInt(top10Fragment.LIST_POSITION,
                    getIntent().getIntExtra(top10Fragment.LIST_POSITION, 0));



            playerActivityFragment fragment = new playerActivityFragment();
            fragment.setArguments(arguments);
            getFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_player, menu);
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

    @Override
    public void onBackPressed(){
        // do something here and don't write super.onBackPressed()
        super.onBackPressed();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch(keyCode){
            case KeyEvent.KEYCODE_BACK:
                // do something here
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
