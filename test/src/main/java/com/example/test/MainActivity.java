package com.example.test;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.VideoView;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {



    VideoView videoView;

    Button button;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        imageView = (ImageView)findViewById(R.id.imageView);
         button = (Button)findViewById(R.id.button);
        videoView = (VideoView)findViewById(R.id.videoView);


        button.setVisibility(View.GONE);



        String videoSource ="http://theserg43.beget.tech/2.mp4";
        videoView.setVideoURI(Uri.parse(videoSource));
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {

                button.setVisibility(View.VISIBLE);
            }
        });




        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View r) {
                videoView.start();
                new CountDownTimer(10000, 1000) {

                    //Здесь обновляем текст счетчика обратного отсчета с каждой секундой
                    public void onTick(long millisUntilFinished) {
                        Log.d("осталось ", millisUntilFinished/1000+"");
                    }
                    //Задаем действия после завершения отсчета (высвечиваем надпись "Бабах!"):
                    public void onFinish() {

                        imageView.setVisibility(View.GONE);



                        new CountDownTimer(8000, 1000) {

                            //Здесь обновляем текст счетчика обратного отсчета с каждой секундой
                            public void onTick(long millisUntilFinished) {
                                Log.d("осталось ", millisUntilFinished/1000+"");
                            }
                            //Задаем действия после завершения отсчета (высвечиваем надпись "Бабах!"):
                            public void onFinish() {

                                videoView.stopPlayback();
                            }
                        } .start();
                    }
                } .start();


            }
        });




    }





}
