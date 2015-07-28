package com.example.jasperjohns.ssp1;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by asaldanha on 6/26/2015.
 */
public class ArtistData implements Parcelable {

    private String artistId;
    private String artistName;
    private String artistImage;


    public ArtistData(String artistId, String artistName, String artistImage){
        this.artistId = artistId;
        this.artistName = artistName;
        this.artistImage = artistImage;
    }

    public ArtistData(Parcel in){
        this.artistId = in.readString();
        this.artistName = in.readString();
        this.artistImage = in.readString();
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getArtistImage() {
        return artistImage;
    }

    public void setArtistImage(String artistImage) {
        this.artistImage = artistImage;
    }

    public String getArtistId() {

        return artistId;
    }

    public void setArtistId(String artistId) {
        this.artistId = artistId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(artistId);
        dest.writeString(artistName);
        dest.writeString(artistImage);
    }

    public static final Parcelable.Creator<ArtistData> CREATOR = new Parcelable.Creator<ArtistData>(){
        public ArtistData createFromParcel(Parcel in){
            return new ArtistData(in);
        }

        public ArtistData[] newArray(int size){
            return new ArtistData[size];
        }
    };

}