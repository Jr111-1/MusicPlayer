package com.example.musicplayerclf;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.master.permissionhelper.PermissionHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity2 extends AppCompatActivity {

    private List<Music> musicList;


    private Button returnBtn;
    private ListView localList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        localList =findViewById(R.id.locatList);

        returnBtn = findViewById(R.id.returnBtn);
        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        getMusicInfo();

    }


    @SuppressLint("Range")
    private void getMusicInfo(){
        //TODO 创建一个集合存储读取的歌曲信息
        List<Music> musicList = new ArrayList<>();




        //TODO 读取数据库中歌曲信息
        Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        cursor.moveToFirst();

//        while (!cursor.isAfterLast()) {
//
//
//            Music music = new Music();
//
//            // id
//            music.setId(cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID)));
//            // 歌曲
//            music.setTitle(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));
//            // 歌手
//            music.setArtist(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)));
//            // 时长
//            music.setDuration(cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)));
//            // 文件大小
//            music.setSize(cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE)));
//            // 路径
//            music.setPath(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA)));
//
//            cursor.moveToNext();
//
//        }

        for(int i=0; i<cursor.getCount(); i++){
            Music music = new Music();

            // id
            music.setId(cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID)));
            // 歌曲
            music.setTitle(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));
            // 歌手
            music.setArtist(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)));
            // 时长
            music.setDuration(cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)));
            // 文件大小
            music.setSize(cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE)));
            // 路径
            music.setPath(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA)));

            // 将资源为音乐的媒体文件存储到集合中
            if(cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.IS_MUSIC)) != 0){
                musicList.add(music);
            }
        }

        cursor.close();

        ArrayAdapter<Music> listAdapter = new ArrayAdapter<Music>(this,android.R.layout.simple_list_item_1, musicList);
        localList.setAdapter(listAdapter);


    }








}