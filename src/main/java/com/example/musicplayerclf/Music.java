package com.example.musicplayerclf;

import android.graphics.Bitmap;
import android.icu.math.BigDecimal;

public class Music {
    // id
    private long id;
    // 歌名
    private String title;
    // 专辑
    private String album;
    // 歌手
    private String artist;
    // 时长
    private long duration;
    // 文件大小
    private long size;
    // 路径
    private String path;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }






}
