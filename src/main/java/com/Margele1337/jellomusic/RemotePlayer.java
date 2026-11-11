package com.margele1337.jellomusic;

import java.io.BufferedInputStream;
import java.net.URL;
import javazoom.spi.mpeg.sampled.convert.MpegFormatConversionProvider;
import javazoom.spi.mpeg.sampled.file.MpegAudioFileReader;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.SourceDataLine;

public class RemotePlayer {

    private volatile boolean playing = false;
    private Thread thread;

    public boolean isPlaying() {
        return playing;
    }

    public void stop() {
        playing = false;
    }

    public void play(String url, float volume) {
        stop();
        thread = new Thread(() -> playLoop(url, volume), "jello-remote-player");
        thread.setDaemon(true);
        thread.start();
    }

    private void playLoop(String url, float volume) {
        playing = true;
        try {
            AudioInputStream raw = new MpegAudioFileReader()
                .getAudioInputStream(new BufferedInputStream(new URL(url).openStream()));
            AudioFormat target = new AudioFormat(
                AudioFormat.Encoding.PCM_SIGNED,
                raw.getFormat()
                    .getSampleRate(),
                16,
                raw.getFormat()
                    .getChannels(),
                raw.getFormat()
                    .getChannels() * 2,
                raw.getFormat()
                    .getSampleRate(),
                false);
            AudioInputStream in = new MpegFormatConversionProvider().getAudioInputStream(target, raw);
            if (in == null) {
                throw new IllegalArgumentException("MP3 to PCM decode failed");
            }
            SourceDataLine line = AudioSystem.getSourceDataLine(in.getFormat());
            line.open(in.getFormat());
            applyVolume(line, volume);
            line.start();

            byte[] buffer = new byte[4096];
            int read;
            while ((read = in.read(buffer)) != -1 && playing) {
                line.write(buffer, 0, read);
            }
            line.drain();
            line.close();
            in.close();
        } catch (Exception e) {
            JelloMusic.LOG.error("Remote playback failed: ", e);
        } finally {
            playing = false;
        }
    }

    private void applyVolume(SourceDataLine line, float volume) {
        try {
            FloatControl gain = (FloatControl) line.getControl(FloatControl.Type.MASTER_GAIN);
            float db = volume <= 0 ? -80f : 20f * (float) Math.log10(volume / 100f);
            gain.setValue(Math.max(gain.getMinimum(), Math.min(gain.getMaximum(), db)));
        } catch (Exception e) {
            JelloMusic.LOG.warn("Volume control not available for this line");
        }
    }
}
