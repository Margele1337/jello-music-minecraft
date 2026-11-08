package com.margele1337.jellomusic;

public class Track {

    private final String title;
    private final String soundName;
    private final int seconds;
    private final String hash;

    public Track(String title, int seconds, String soundName) {
        this(title, seconds, soundName, null);
    }

    public Track(String title, int seconds, String soundName, String hash) {
        this.title = title;
        this.seconds = seconds;
        this.soundName = soundName;
        this.hash = hash;
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

    public String hash() {
        return this.hash;
    }
}
