package com.samuel.mp3player;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;

import java.io.File;

/**
 * Created by samuel on 06/08/15.
 */
public class Mp3Service extends Service {
    String Default;
    File musicPlaying;
    File nextMusic;
    MediaPlayer mediaPLayer;
    IBinder binder = new MyBinder();
    String nameSong;

    public Mp3Service() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Default = "http://mobi.randomsort.net/wp-content/uploads/2015/07/filetoplay.mp3";
        mediaPLayer = MediaPlayer.create(getApplicationContext(), Uri.parse(Default));
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public void play() {
        mediaPLayer.start();//useful method
    }

    public void stop() {
        mediaPLayer.seekTo(0);
        mediaPLayer.pause();
    }

    public void pause() {
        mediaPLayer.pause();//useful method
    }

    public File getMusicPlaying() {
        return musicPlaying;
    }

    public void setMusicPlaying(File musicPlaying) {
        this.musicPlaying = musicPlaying;
    }

    public File getNextMusic() {
        return nextMusic;
    }

    public void setNextMusic(File nextMusic) {
        this.nextMusic = nextMusic;
    }

    public MediaPlayer getMediaPLayer() {
        return mediaPLayer;
    }
    public String getNameSong() {
        return nameSong;
    }

    public void setNameSong(String nameSong) {
        this.nameSong = nameSong;
    }

    public void changeMusic(String path, String nameSong) {
        mediaPLayer.stop();
        mediaPLayer.seekTo(0);
        this.nameSong = nameSong;
        mediaPLayer = MediaPlayer.create(getApplicationContext(), Uri.parse(path));
        mediaPLayer.start();
    }

    public class MyBinder extends Binder {
        public Mp3Service getMyService() {
            return Mp3Service.this;
        }
    }
}