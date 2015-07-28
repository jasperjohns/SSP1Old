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
 * Created by asaldanha on 6/26/2015.
 */

public class ArtistDataAdapter extends ArrayAdapter <ArtistData>{

    private final String LOG_TAG = ArtistDataAdapter.class.getSimpleName();
    private Context context;
    private int resourceId;
    private List<ArtistData> artists;

    public ArtistDataAdapter(Context context, int resourceId, ArrayList<ArtistData> artists){
        // need to avoid "constructor" errors
        super(context, resourceId, artists);

        this.context = context;
        this.resourceId = resourceId;
        this.artists = artists;
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
            rowView = inflater.inflate(R.layout.list_view_item, parent, false );
            // get the resources of the list item view and assign it to the ViewHolder
            viewHolder.item_imageView = (ImageView) rowView.findViewById(R.id.item_ImageView);
            viewHolder.item_textView  = (TextView) rowView.findViewById(R.id.item_textView);
            // store it inside the layout
            rowView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder)rowView.getTag();
        }

        // Get the artist data at the specific position
        ArtistData artist =  artists.get(position);

        // Assign the data to the ViewHolder
        viewHolder.item_textView.setText(artist.getArtistName());


        if (artist.getArtistImage().length() > 0) {
            Picasso.with(context).load(artist.getArtistImage()).resize(200, 200).centerCrop().into(viewHolder.item_imageView);
        }


        return rowView;


    }

    static class ViewHolder {
        TextView item_textView;
        ImageView item_imageView;
    }

}