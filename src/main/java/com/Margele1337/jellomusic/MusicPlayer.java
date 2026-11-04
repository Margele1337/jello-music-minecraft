package com.margele1337.jellomusic;

import java.util.ArrayList;
import java.util.List;

public class MusicPlayer {

    private static List<Track> playlist = new ArrayList<>();

    public static void add(Track track) {
        playlist.add(track);
    }

    public static Track getFirst() {
        return playlist.get(0);
    }

    public static void logAll() {
        for (Track track : playlist) {
            JelloMusic.LOG.info(track.title() + " - " + track.seconds() + "s");
        }
    }
}
