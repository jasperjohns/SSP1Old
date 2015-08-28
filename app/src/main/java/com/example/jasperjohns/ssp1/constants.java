package com.example.jasperjohns.ssp1;

/**
 * Created by asaldanha on 8/4/2015.
 */
public class constants {


    public interface ACTION {
        public static String MAIN_ACTION = "com.truiton.foregroundservice.action.main";
        public static String PREV_ACTION = "com.truiton.foregroundservice.action.prev";
        public static String PLAY_ACTION = "com.truiton.foregroundservice.action.play";
        public static String PAUSE_ACTION = "com.truiton.foregroundservice.action.pause";
        public static String NEXT_ACTION = "com.truiton.foregroundservice.action.next";
        public static String STARTFOREGROUND_ACTION = "com.truiton.foregroundservice.action.startforeground";
        public static String STOPFOREGROUND_ACTION = "com.truiton.foregroundservice.action.stopforeground";

    }

    public interface NOTIFICATION_ID {
        public static int FOREGROUND_SERVICE = 101;
    }
    public static String SONG_LIST ="songlist";
    public static String TRACK_TOTAL_DURATION = "TRACK_TOTAL_DURATION";
    public static String TRACK_CURRENT_DURATION = "TRACK_CURRENT_DURATION";
    final static String MY_ACTION = "MY_ACTION";

    public static String ARTIST_NAME = "artistname";
    public static String ARTIST_ALBUM = "artistalbum";
     public static String ARTIST_TRACK = "artisttrack";
     public static String ARTIST_TRACKS = "artisttracks";
    public static String ARTIST_TRACK_PREVIEW_URL = "artisttrackpreviewURL";
     public static String ARTIST_TRACK_IMAGE = "artisttrackimage";
     public static String SPOTIFY_ID = "spotifyid";
     public static String LIST_POSITION ="listPosition";
    public static String MEDIAPLAYER_PLAYING ="mediaplayer_playing";
    public static String MEDIAPLAYER_CURRENT_DURATION ="mediaplayer_currentduration";

}
