package com.margele1337.jellomusic;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class MusicPlayer {

    private static List<Track> playlist = new ArrayList<>();
    private static int currentIndex = 0;

    public static void add(Track track) {
        playlist.add(track);
    }

    public static Track getFirst() {
        return playlist.get(0);
    }

    public static Track next() {
        currentIndex++;
        if (currentIndex >= playlist.size()) {
            return null;
        }
        return playlist.get(currentIndex);
    }

    public static void reset() {
        currentIndex = 0;
    }

    public static boolean isEmpty() {
        return playlist.isEmpty();
    }

    public static void loadPlaylist(File file) {
        playlist.clear();
        if (!file.exists()) {
            JelloMusic.LOG.warn("Playlist file " + file.getPath() + " does not exist.");
            return;
        }
        try {
            List<String> lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);//读取并格式化config/jellomusic/music.txt文件
            for (String line : lines) {
                if (line.isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length != 3) {
                    JelloMusic.LOG.warn("Invalid playlist entry: " + line);//警告无效的播放列表条目
                    continue;
                }
                String title = parts[0].trim();
                String soundName = parts[1].trim();
                int seconds = Integer.parseInt(parts[2].trim());
                playlist.add(new Track(title, seconds, soundName));
            }
        } catch (IOException e) {
            JelloMusic.LOG.warn("Error loading playlist file: ", e);//抛出播放列表文件时出错异常
        }
    }

    public static void logAll() {
        for (Track track : playlist) {
            JelloMusic.LOG.info(track.title() + " - " + track.seconds() + "s");//信息播放列表
        }
    }
}
