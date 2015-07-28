package com.example.jasperjohns.ssp1;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {


    ListView listViewArtists;
    SearchView searchView;

    ArrayList<ArtistData> arrayArtists;
    ArtistDataAdapter adapter;
    String mLastSearch = null;

    private final String LOG_TAG = MainActivityFragment.class.getSimpleName();
    private final String ARTIST_TAG = "artists";
    private final String LISTPOSITION_TAG = "list_position";



    private final String ARTIST_NAME = "artistname";
    private final String ARTIST_ALBUM = "artistalbum";
    private final String ARTIST_TRACK = "artisttrack";
    private final String ARTIST_TRACK_PREVIEW_URL = "artisttrackpreviewURL";
    private final String ARTIST_TRACK_IMAGE = "artisttrackimage";
    private final String SPOTIFY_ID = "spotifyid";



    private final String MSG_NOITEMS = "Sorry, no items found";
    private boolean bReloadData = false;

    public MainActivityFragment() {
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);

        mLastSearch = loadLastSearch();
        if (!mLastSearch.equalsIgnoreCase("")){
            bReloadData = true;
            UpdateData(mLastSearch);
        }
    }



    @Override
    public void onStart() {
        super.onStart();

        mLastSearch = loadLastSearch();
        if (!mLastSearch.equalsIgnoreCase("")){
            bReloadData = true;
            UpdateData(mLastSearch);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        arrayArtists = new ArrayList<Artist>();

/*
        Artist picasso = new Artist();
        picasso.name="aasdasd";
        arrayArtists.add (picasso);
*/

        View v = inflater.inflate(R.layout.fragment_main, container, false);
        SetUpUI(v);
        SetUpListeners();


        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        Integer listPostion = listViewArtists.getFirstVisiblePosition();
        savedInstanceState.putParcelableArrayList(ARTIST_TAG, arrayArtists);
        savedInstanceState.putInt(LISTPOSITION_TAG, listPostion);

//        savedInstanceState.(ARTIST_TAG, );
        super.onSaveInstanceState(savedInstanceState);


    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState){
        super.onViewStateRestored(savedInstanceState);
        if(savedInstanceState != null) {
            arrayArtists = savedInstanceState.getParcelableArrayList(ARTIST_TAG);
            Integer listPostion =  savedInstanceState.getInt(LISTPOSITION_TAG);
            listViewArtists.setSelection(listPostion);

            adapter.clear();
            adapter.addAll(arrayArtists);
            adapter.notifyDataSetChanged();
        }


    }


    private void SetUpUI(View v){
        arrayArtists = new ArrayList<ArtistData>();

        listViewArtists = (ListView) v.findViewById(R.id.listView);
        adapter = new ArtistDataAdapter(getActivity(),0, arrayArtists);
        listViewArtists.setAdapter(adapter);

        searchView = (SearchView) v.findViewById(R.id.searchView);

    }

    private void SetUpListeners () {

        listViewArtists.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object listItem = listViewArtists.getItemAtPosition(position);
                //               Log.v(LOG_TAG, listItem.toString());
                ArtistData artist =  arrayArtists.get(position);


//                Intent top10Act = new Intent(getActivity(), top10.class).putExtra(Intent.EXTRA_TEXT, artist.getArtistId()) ;
                Intent top10Act = new Intent(getActivity(), top10.class);
                Bundle extras = new Bundle();
                extras.putString(SPOTIFY_ID,artist.getArtistId());
                extras.putString(ARTIST_NAME,artist.getArtistName());
                top10Act.putExtras(extras);

////                Toast.makeText(getActivity(), "selected Item Name is " + listItem.toString(), Toast.LENGTH_LONG).show();
                startActivity(top10Act);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {
//                Log.v(LOG_TAG, s);
                UpdateData(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });



    }


    private void UpdateData(String searchArtist){

        // get the previously saved one
        //      if (searchArtist == null) searchArtist="Beyonce";
        if (searchArtist == null) searchArtist="";
        new FetchDataTask().execute(searchArtist);

    }

    private void saveLastSearch(String searchString){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        prefs.edit().putString(getActivity().getString(R.string.pref_lastsearch_key), searchString).apply();
    }

    private String loadLastSearch(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String LastSearch = prefs.getString(getActivity().getString(R.string.pref_lastsearch_key), "");

        return LastSearch;
    }


    private boolean isValidUrl(String image){
        URL url;

        try{
            url = new URL(image);
        } catch(MalformedURLException e){
            return false;
        }

        try{
            url.toURI();
        } catch (URISyntaxException e){
            return false;
        }

        return true;
    }


    private class FetchDataTask extends AsyncTask <String, Integer, ArtistsPager>{
        protected ArtistsPager doInBackground(String... params) {

            String searchForArtist = params[0];
            if (searchForArtist !=null) {
                saveLastSearch(searchForArtist);
            }

            SpotifyApi api = new SpotifyApi();
            SpotifyService spotify = api.getService();
            ArtistsPager results = spotify.searchArtists(searchForArtist);

            return results;
        }

        @Override
        protected void onPostExecute(ArtistsPager artists)
        {
            super.onPostExecute(artists);

            if (artists != null) {
                if (artists.artists.items.size() > 0) {
                    adapter.clear();
                    arrayArtists = (convertData((ArrayList) artists.artists.items));
                    adapter.addAll(arrayArtists);
//                    adapter.addAll(convertData((ArrayList) artists.artists.items));
/*
                    for (kaaes.spotify.webapi.android.models.Artist item : artists.artists.items) {
                        adapter.add(item);
                    }
*/
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

        private ArrayList<ArtistData> convertData(ArrayList<Artist> artists){
            ArrayList returnArtists = new ArrayList<>();
            for(Artist artist: artists){
                //Log.v(LOG_TAG, artist.name);
                if(artist.images.size() > 0)
                    returnArtists.add(new ArtistData(artist.id, artist.name, artist.images.get(0).url));
                else
                    returnArtists.add(new ArtistData(artist.id, artist.name, ""));
            }
            return returnArtists;
        }


    }
}
