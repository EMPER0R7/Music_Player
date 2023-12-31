package com.example.mediaplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    Button forward_btn,backward_btn,play_btn,stop_btn;
    TextView time_txt,title_txt;
    SeekBar seekBar;
    MediaPlayer mediaPlayer;
    Handler handler=new Handler();
    double startTime=0;
    double finalTime=0;
    int forwardTime=10000;
    int backwardTime=10000;
    static int oneTimeOnly=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        play_btn=findViewById(R.id.play);
        stop_btn=findViewById(R.id.pause);
        forward_btn=findViewById(R.id.forward);
        backward_btn=findViewById(R.id.rewind);

        time_txt=findViewById(R.id.time);
        title_txt=findViewById(R.id.song_title);

        seekBar=findViewById(R.id.seekBar);
        mediaPlayer=MediaPlayer.create(this,
                R.raw.love_again);
        title_txt.setText(getResources().getIdentifier(
                "love_again",
                "raw",
                getPackageName()
        ));
        seekBar.setClickable(false);

        play_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlayMusic();
            }
        });
        stop_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.pause();
            }
        });
        forward_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int temp=(int)startTime;
                if((temp+forwardTime)<=finalTime){
                    startTime=startTime+forwardTime;
                    mediaPlayer.seekTo((int)startTime);
                }else{
                    Toast.makeText(MainActivity.this, "Can't make jump", Toast.LENGTH_LONG).show();
                }
            }
        });
        backward_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int temp=(int)startTime;
                if((temp-backwardTime)>0){
                    startTime=startTime-backwardTime;
                    mediaPlayer.seekTo((int) startTime);
                }else {
                    Toast.makeText(MainActivity.this, "Cant go Back!", Toast.LENGTH_LONG).show();
                }
            }
        });

    }private void PlayMusic(){
        mediaPlayer.start();
        finalTime=mediaPlayer.getDuration();
        startTime= mediaPlayer.getCurrentPosition();

        if(oneTimeOnly==0){
            seekBar.setMax((int)finalTime);
            oneTimeOnly=1;
        }
        time_txt.setText(String.format(
                "%d min, %d sec ", TimeUnit.MILLISECONDS.toMinutes((long)finalTime),
                TimeUnit.MILLISECONDS.toSeconds((long)finalTime)-
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)finalTime))
        ));
        seekBar.setProgress((int)startTime);
        handler.postDelayed(UpdateSongTime,1000);
    }private Runnable UpdateSongTime =new Runnable() {
        @Override
        public void run() {
            startTime=mediaPlayer.getCurrentPosition();
            time_txt.setText(String.format(
                    " %d min, %d sec",TimeUnit.MILLISECONDS.toMinutes((long)startTime),
                    TimeUnit.MILLISECONDS.toSeconds((long)startTime)
                            -TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)startTime))
            ));
            seekBar.setProgress((int)startTime);
            handler.postDelayed(this,100);
        }
    };

}