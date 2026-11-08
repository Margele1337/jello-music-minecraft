package com.margele1337.jellomusic.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.margele1337.jellomusic.JelloMusic;
import com.margele1337.jellomusic.Track;

public class KuGouApi implements MusicApi {

    private final String baseUrl;
    private final String cookie;

    public KuGouApi(String baseUrl, String cookie) {
        this.baseUrl = baseUrl;
        this.cookie = cookie;
    }

    @Override
    public List<Track> search(String keyword) {
        List<Track> result = new ArrayList<>();
        try {
            String encoded = URLEncoder.encode(keyword, "UTF-8");
            String body = httpGet(baseUrl + "/search?keywords=" + encoded);
            JsonObject root = new JsonParser().parse(body)
                .getAsJsonObject();
            JsonArray lists = root.getAsJsonObject("data")
                .getAsJsonArray("lists");
            for (JsonElement element : lists) {
                JsonObject item = element.getAsJsonObject();
                String fileName = item.get("FileName")
                    .getAsString();
                int seconds = item.get("Duration")
                    .getAsInt();
                String hash = item.get("FileHash")
                    .getAsString();
                result.add(new Track(fileName, seconds, null, hash));
            }
        } catch (IOException e) {
            JelloMusic.LOG.error("KuGou search failed: ", e);
        }
        return result;
    }

    @Override
    public String getPlayUrl(String hash) {
        try {
            String body = httpGet(baseUrl + "/song/url?hash=" + hash + "&album_id=0");
            JsonObject root = new JsonParser().parse(body)
                .getAsJsonObject();
            int status = root.get("status")
                .getAsInt();

            if (status == 1) {
                JsonArray urls = root.getAsJsonArray("url");
                return urls.size() > 0 ? urls.get(0)
                    .getAsString() : null;
            }
            if (root.has("errcode") && root.get("errcode")
                .getAsInt() == 20028) {
                JelloMusic.LOG.warn(
                    "KuGou requires browser verification: please complete the slider verification at localhost:3000/verifySlide.html and retry");
                return null;
            }
            JelloMusic.LOG.warn("Cannot get play URL (possibly requires VIP or purchase)");
            return null;
        } catch (IOException e) {
            JelloMusic.LOG.error("KuGou getPlayUrl failed: ", e);
            return null;
        }
    }

    private String httpGet(String url) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setConnectTimeout(5000);
        conn.setReadTimeout(10000);
        if (!cookie.isEmpty()) {
            conn.setRequestProperty("Cookie", cookie);
        }
        try (BufferedReader reader = new BufferedReader(
            new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } finally {
            conn.disconnect();
        }
    }
}
