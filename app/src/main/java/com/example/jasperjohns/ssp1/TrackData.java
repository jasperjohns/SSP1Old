package com.example.jasperjohns.ssp1;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by asaldanha on 6/29/2015.
 */
public class TrackData implements Parcelable {

    private String trackId;
    private String artists;
    private String artistAlbum;
    private String artistTrack;
    private String trackImage;
    private String trackImage640;
    private String trackImage300;
    private String trackImage64;
    private String preview_url;



    public TrackData(String artists, String trackId, String artistAlbum, String artistTrack, String trackImage, String preview_url){
        this.artists = artists;
        this.trackId = trackId;
        this.artistAlbum = artistAlbum;
        this.artistTrack = artistTrack;
        this.trackImage = trackImage;
        this.preview_url = preview_url;
    }

    public TrackData(Parcel in){
        this.artists = in.readString();
        this.trackId = in.readString();
        this.artistAlbum = in.readString();
        this.artistTrack = in.readString();
        this.trackImage = in.readString();
        this.preview_url = in.readString();
    }

    public String getArtists() {return artists;}

    public void setArtists(String artits) { this.artists = artists;}

    public String getArtistAlbum() {
        return artistAlbum;
    }

    public void setArtistAlbum(String artistName) {
        this.artistAlbum = artistName;
    }

    public String getArtistTrack() {
        return artistTrack;
    }

    public void setArtistTrack(String artistName) {
        this.artistTrack= artistName;
    }

    public String getTrackImage() {
        return trackImage;
    }

    public void setArtistTrackImage(String artistImage) {
        this.trackImage = artistImage;
    }

    public String getTrackId() {

        return trackId;
    }

    public void setTrackId(String trackId) {
        this.trackId = trackId;
    }

    public String getPreview_URL() {
        return preview_url;
    }

    public void setPreview_URL (String preview_url){ this.preview_url = preview_url;}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(artists);
        dest.writeString(trackId);
        dest.writeString(artistAlbum);
        dest.writeString(artistTrack);
        dest.writeString(trackImage);
        dest.writeString(preview_url);
    }

    public static final Parcelable.Creator<TrackData> CREATOR = new Parcelable.Creator<TrackData>(){
        public TrackData createFromParcel(Parcel in){
            return new TrackData(in);
        }

        public TrackData[] newArray(int size){
            return new TrackData[size];
        }
    };


}
