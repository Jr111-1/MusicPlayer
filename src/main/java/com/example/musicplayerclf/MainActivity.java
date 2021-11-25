package com.example.musicplayerclf;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuPopupHelper;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private int cMusicId = 0;
    int num = 0;

    private int currentTime;
    private boolean isSeekBarChanging;
    private PopupWindow popupWindow;



    private SeekBar seekBar;
    private Timer timer;



    int []song_id={R.raw.demo1, R.raw.demo2, R.raw.demo3, R.raw.demo4, R.raw.demo5};

    List<Integer> idList = new ArrayList<Integer>();

    List<String> list = new ArrayList<String>();
    List<String> myList = new ArrayList<String>();
    String[] song_name={"一个人去太古里","Ghost","船长", "We Will Rock You","space oddity"};

    private int modelorder = 0;


    String[]MySong_name={};


    private ImageButton play, next, back;
    private ListView musicListView;

    private Button localMusic, MyList, add, cancle, model, del, cancle2;
    private TextView songName, content,curTime, totalTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mediaPlayer=new MediaPlayer();

        play = findViewById(R.id.playMusic);
        next = findViewById(R.id.nextMusic);
        back = findViewById(R.id.backMusic);
        seekBar = findViewById(R.id.musicSeekBar);
        songName = findViewById(R.id.songName);

        musicListView = findViewById(R.id.musicList);
        content = findViewById(R.id.zhuangtai);
        curTime=findViewById(R.id.curTime);
        totalTime=findViewById(R.id.totalTime);

        for(int i = 0; i < song_id.length; i++){
            idList.add(song_id[i]);
        }


        //进度条
        seekBar.setOnSeekBarChangeListener(new MySeekBar());
        //初始化MediaPlayer
        initMediaPlayer(num);

        getMusicList();

        model = findViewById(R.id.model);
        model.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //创建mennu
                PopupMenu popupMenu = new PopupMenu(MainActivity.this,model);
                //加载菜单资源
                popupMenu.getMenuInflater().inflate(R.menu.menu1,popupMenu.getMenu());

                //菜单监听
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.solo:
                                model.setText("单曲循环");
                                modelorder = 1;
                                modelPlay();
                                break;
                            case R.id.order:
                                model.setText("顺序播放");
                                modelorder = 0;
                                modelPlay();
                                break;
                            case R.id.random:
                                model.setText("随机播放");
                                modelorder = 2;
                                modelPlay();
                                break;


                        }
                        return true;
                    }
                });

                try {
                    Field field = popupMenu.getClass().getDeclaredField("mm");
                    field.setAccessible(true);
                    MenuPopupHelper menuPopupHelper = (MenuPopupHelper) field.get(popupMenu);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    e.printStackTrace();
                }
                popupMenu.show();
            }
        });





        musicListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                //ArrayList<String> my = (ArrayList<String>) musicListView.getItemAtPosition(i);
                View popupWindow_view = getLayoutInflater().inflate(R.layout.updatepopupwindow, null,
                        false);
                popupWindow_view.setBackgroundResource(R.drawable.popwindow_shape);
                // 创建PopupWindow实例,200,LayoutParams.MATCH_PARENT分别是宽度和高度
                popupWindow = new PopupWindow(popupWindow_view, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
                popupWindow.setClippingEnabled(true);//
                popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.popwindow_shape));



                add = (Button) popupWindow_view.findViewById(R.id.bt_add);
                cancle = (Button) popupWindow_view.findViewById(R.id.bt_cancle);

                add.setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        myList.add((String) musicListView.getItemAtPosition(i));
                        popupWindow.dismiss();
                    }
                });

                cancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popupWindow.dismiss();
                    }
                });

                View rootview = LayoutInflater.from(MainActivity.this).inflate(R.layout.activity_main ,null);
                popupWindow.showAtLocation(rootview, Gravity.CENTER, 0, 0);


                return true;
            }
        });



        musicListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                cMusicId=i;
                mediaPlayer.stop();
                initMediaPlayer(cMusicId);
//                mediaPlayer.start();//播放
                modelPlay();
                content.setText("播放中");
                //Toast.makeText(MainActivity.this,word,Toast.LENGTH_LONG).show();

            }
        });


        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(content.getText().toString().equals("暂停")){

                    mediaPlayer.start();
//                    modelPlay();
                    content.setText("播放中");
                }
                else{
                    mediaPlayer.pause();
                    content.setText("暂停");
                }

            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                if(num == song_id.length-1)
                    num = 0;
                else
                    num ++;
                initMediaPlayer(num);
//                mediaPlayer.start();

                content.setText("播放中");

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                timer.cancel();
                timer=null;
                if(num == 0)
                    num = song_id.length-1;
                else
                    num--;
                initMediaPlayer(num);
                mediaPlayer.start();
                content.setText("播放中");

            }
        });

        //本地
        localMusic = findViewById(R.id.localM);
        localMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                list.clear();

                getMusicList();
            }
        });
        //歌单
        MyList = findViewById(R.id.MyList);
        MyList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                getMyList();

            }
        });



    }

    //初始化MediaPlayer
    private void initMediaPlayer(int i){
        songName.setText(song_name[i]);
        if(mediaPlayer != null){
            mediaPlayer.stop();
        }

        mediaPlayer = MediaPlayer.create(this,song_id[i]);
        seekBar.setMax(mediaPlayer.getDuration());
        currentTime=0;

        //令时间轴滚动
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(!isSeekBarChanging&&mediaPlayer.isPlaying()){//如果进度条未改变，并且当前正在播放
                    //tv1.append(""+mediaPlayer.getCurrentPosition());
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    //lrcShow(currentTime);

                }
            }
        },0,1000);

    }

    private void modelPlay(){

            //列表循环
        if(modelorder == 0){
            if (num < idList.size() ) {
                for (int i = num-1; i < idList.size(); i++) {
                    num ++;
                    mediaPlayer.reset();
                    mediaPlayer = MediaPlayer.create(MainActivity.this, idList.get(num-1));
                    songName.setText(song_name[num-1]);
                    mediaPlayer.start();
                    seekBar.setMax(mediaPlayer.getDuration());
                    break;
                }
            } else if (num == idList.size()){
                num = 0;
                mediaPlayer.reset();
                mediaPlayer = MediaPlayer.create(MainActivity.this, idList.get(0));
                songName.setText(song_name[0]);
                mediaPlayer.start();
                seekBar.setMax(mediaPlayer.getDuration());
            }

            //单曲循环
        }else if(modelorder == 1){

            boolean reverse = mediaPlayer.isLooping();
            mediaPlayer.setLooping(!reverse);

            if (!reverse) {
                mediaPlayer.start();

            } else {
                mediaPlayer.stop();
            }

        }else if(modelorder == 2){


            int i = new Random().nextInt(idList.size());
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            } else {
                mediaPlayer.reset();
                initMediaPlayer(i);
                mediaPlayer.start();
            }

        }

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){
            public void onCompletion(MediaPlayer mp) {
                modelPlay();

            }
        });
        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            public boolean onError(MediaPlayer mp, int what, int extra) {
                return true;
            }
        });



    }








    //获取mp3文件列表
    public void getMusicList() {

        for(int i = 0; i< song_name.length; i++){
            list.add(song_name[i]);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1,list);
        ListView lv_1 = findViewById(R.id.musicList);
        lv_1.setAdapter(adapter);
        musicListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                //ArrayList<String> my = (ArrayList<String>) musicListView.getItemAtPosition(i);
                View popupWindow_view = getLayoutInflater().inflate(R.layout.updatepopupwindow, null,
                        false);
                popupWindow_view.setBackgroundResource(R.drawable.popwindow_shape);
                // 创建PopupWindow实例,200,LayoutParams.MATCH_PARENT分别是宽度和高度
                popupWindow = new PopupWindow(popupWindow_view, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
                popupWindow.setClippingEnabled(true);//
                popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.popwindow_shape));



                add = (Button) popupWindow_view.findViewById(R.id.bt_add);
                cancle = (Button) popupWindow_view.findViewById(R.id.bt_cancle);

                add.setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        myList.add((String) musicListView.getItemAtPosition(i));
                        popupWindow.dismiss();
                    }
                });

                cancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popupWindow.dismiss();
                    }
                });

                View rootview = LayoutInflater.from(MainActivity.this).inflate(R.layout.activity_main ,null);
                popupWindow.showAtLocation(rootview, Gravity.CENTER, 0, 0);


                return true;
            }
        });

        musicListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                cMusicId=i;
                mediaPlayer.stop();
                initMediaPlayer(cMusicId);
                mediaPlayer.start();//播放
                content.setText("播放中");
                //Toast.makeText(MainActivity.this,word,Toast.LENGTH_LONG).show();

            }
        });

    }
    //歌单列表
    public void getMyList(){

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1,myList);
        ListView lv_1 = findViewById(R.id.musicList);
        lv_1.setAdapter(adapter);

        musicListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                //ArrayList<String> my = (ArrayList<String>) musicListView.getItemAtPosition(i);
                View popupWindow_view = getLayoutInflater().inflate(R.layout.deletpop_window, null,
                        false);
                popupWindow_view.setBackgroundResource(R.drawable.popwindow_shape);
                // 创建PopupWindow实例,200,LayoutParams.MATCH_PARENT分别是宽度和高度
                popupWindow = new PopupWindow(popupWindow_view, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
                popupWindow.setClippingEnabled(true);//
                popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.popwindow_shape));



                del = (Button) popupWindow_view.findViewById(R.id.bt_del);
                cancle2 = (Button) popupWindow_view.findViewById(R.id.bt_cancle2);

                del.setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        myList.remove((String) musicListView.getItemAtPosition(i));
                        getMyList();
                        popupWindow.dismiss();
                    }
                });

                cancle2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popupWindow.dismiss();
                    }
                });

                View rootview = LayoutInflater.from(MainActivity.this).inflate(R.layout.activity_main ,null);
                popupWindow.showAtLocation(rootview, Gravity.CENTER, 0, 0);


                return true;
            }
        });



    }



    //进度条
    public class MySeekBar implements SeekBar.OnSeekBarChangeListener {

        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            int total = mediaPlayer.getDuration() / 1000;//获取音乐总时长
            int curl = mediaPlayer.getCurrentPosition() / 1000;//获取当前播放的位置
            curTime.setText(calculateTime(curl));//开始时间
            totalTime.setText(calculateTime(total));//总时长
        }
        /*滚动时,应当暂停后台定时器*/
        public void onStartTrackingTouch(SeekBar seekBar) {
            isSeekBarChanging = true;
        }
        /*滑动结束后，重新设置值*/
        public void onStopTrackingTouch(SeekBar seekBar) {
            isSeekBarChanging = false;
            mediaPlayer.seekTo(seekBar.getProgress());
            curTime.setText(calculateTime(mediaPlayer.getCurrentPosition() / 1000));

        }
    }

    //计算时间
    public String calculateTime(int time){
        int minute;
        int second;
        if(time > 60){
            minute = time / 60;
            second = time % 60;
            //判断秒
            if(second >= 0 && second < 10){
                return "0"+minute+":"+"0"+second;
            }else {
                return "0"+minute+":"+second;
            }
        }else if(time < 60){
            second = time;
            if(second >= 0 && second < 10){
                return "00:"+"0"+second;
            }else {
                return "00:"+ second;
            }
        }else{
            return "01:00";
        }
    }


    @Override
    protected void onDestroy(){
        super.onDestroy();
        if (mediaPlayer!=null)
        {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }

}