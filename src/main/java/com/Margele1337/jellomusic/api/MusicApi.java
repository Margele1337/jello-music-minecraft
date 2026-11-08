package com.margele1337.jellomusic.api;

import java.util.List;

import com.margele1337.jellomusic.Track;

public interface MusicApi {

    List<Track> search(String keyword);

    String getPlayUrl(String hash);
}
