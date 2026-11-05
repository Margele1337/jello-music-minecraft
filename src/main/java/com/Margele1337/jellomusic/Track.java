package com.margele1337.jellomusic;

public class Track {

    private final String title;
    private final String soundName;
    private final int seconds;

    public Track(String title, int seconds, String soundName) {
        this.title = title;
        this.seconds = seconds;
        this.soundName = soundName;
    }

    public String title() {
        return this.title;
    }

    public int seconds() {
        return this.seconds;
    }

    public String soundName() {
        return this.soundName;
    }
}
