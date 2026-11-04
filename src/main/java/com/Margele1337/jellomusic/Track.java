package com.margele1337.jellomusic;

public class Track {

    public final String title;
    public final int seconds;

    public Track(String title, int seconds) {
        this.title = title;
        this.seconds = seconds;
    }

    public Track(String title) {
        this(title, 0);
    }

    public String title() {
        return this.title;
    }

    public int seconds() {
        return this.seconds;
    }
}
