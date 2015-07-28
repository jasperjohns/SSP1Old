package com.example.jasperjohns.ssp1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asaldanha on 6/29/2015.
 */
public class TrackDataAdapter extends ArrayAdapter<TrackData> {

    private final String LOG_TAG = TrackDataAdapter.class.getSimpleName();
    private Context context;
    private int resourceId;
    private List<TrackData> tracks;

    public TrackDataAdapter(Context context, int resourceId, ArrayList<TrackData> tracks){
        // need to avoid "constructor" errors
        super(context, resourceId, tracks);

        this.context = context;
        this.resourceId = resourceId;
        this.tracks = tracks;
    }


    @Override
    public View getView (int position, View convertView, ViewGroup parent){
        View rowView = convertView;

        // Create an instance of a ViewHolder
        ViewHolder viewHolder = new ViewHolder();
        if (rowView == null){
            // Get the inflater - either via this.inflater or via the Context
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            // Inflate the list item layout
            rowView = inflater.inflate(R.layout.list_view_item_top10_track, parent, false );
            // get the resources of the list item view and assign it to the ViewHolder
            viewHolder.item_imageView = (ImageView) rowView.findViewById(R.id.item_ImageView);
            viewHolder.item_textView_albumn  = (TextView) rowView.findViewById(R.id.item_textView_albumn);
            viewHolder.item_textView_track  = (TextView) rowView.findViewById(R.id.item_textView_track);
            // store it inside the layout
            rowView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder)rowView.getTag();
        }

        // Get the track data at the specific position
        TrackData track =  tracks.get(position);

        // Assign the data to the ViewHolder
        viewHolder.item_textView_albumn.setText(track.getArtistAlbum());
        viewHolder.item_textView_track.setText(track.getArtistTrack());


        if (track.getTrackImage().length() > 0) {
            Picasso.with(context).load(track.getTrackImage()).resize(200, 200).centerCrop().into(viewHolder.item_imageView);
        }


        return rowView;


    }

    static class ViewHolder {
        ImageView item_imageView;
        TextView item_textView_albumn;
        TextView item_textView_track;

    }




}
