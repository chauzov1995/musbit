package com.univigame.multiki;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import android.widget.ShareActionProvider;
import android.widget.TextView;

import com.facebook.ads.*;

public class game_over extends AppCompatActivity {


    private Button btn_vmenu, btn_restart, btn_share;
    private TextView total_score, tvplmoney;
    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;
    Intent intent;
    int gameover_schore;
    private final String TAG = game_over.class.getSimpleName();
    private RewardedVideoAd rewardedVideoAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);


        intent = getIntent();
        int gameover_money = intent.getIntExtra("gameover_money", 0);
        gameover_schore = intent.getIntExtra("gameover_schore", 0);


        mDBHelper = new DatabaseHelper(this);
        mDb = mDBHelper.getWritableDatabase();

        btn_vmenu = (Button) findViewById(R.id.btn_vmenu);
        btn_restart = (Button) findViewById(R.id.btn_restart);
        btn_share = (Button) findViewById(R.id.btn_share);
        tvplmoney = (TextView) findViewById(R.id.tvplmoney);
        total_score = (TextView) findViewById(R.id.total_score);


        total_score.setText(gameover_schore + "");
        tvplmoney.setText("+ " + gameover_money);


        btn_vmenu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View r) {
                onBackPressed();
            }
        });
        btn_restart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View r) {

                //  onBackPressed();


                onBackPressed();


//вычтем энергию за игру

                long tek_energy;

                Cursor cursor = mDb.rawQuery("SELECT * FROM records ", null);
                cursor.moveToFirst();


                int energy = (cursor.getInt(cursor.getColumnIndex("energy")));
                int score = (cursor.getInt(cursor.getColumnIndex("score")));

                long unixTime = System.currentTimeMillis() / 1000L;


                if (unixTime >= energy)
                    tek_energy = 12;
                else
                    tek_energy = 11 - ((energy - unixTime) / 600);


                if (tek_energy > 0) {

                    if (energy >= unixTime)
                        mDb.execSQL("UPDATE `records` SET energy=energy+" + 600);
                    else
                        mDb.execSQL("UPDATE `records` SET energy=" + (unixTime + 600));
                    //Log.d("asdasd",energy+" "+unixTime);
//вычтем энергию за игру
                    //  game_over.this.dismiss();
                    onBackPressed();
                    Intent intent = new Intent(game_over.this, game.class);
                    startActivity(intent);
                } else {
                    CustomDialog_energy customDialog1 = new CustomDialog_energy((Activity) game_over.this, tek_energy);
                    customDialog1.show();
                }


            }
        });
        btn_share.setOnClickListener(new View.OnClickListener() {
            public void onClick(View r) {

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Я набрал " + gameover_schore + " очков в игре MusBit - угадай музыку по биту, скачай и побей мой рекорд - https://play.google.com/store/apps/details?id=com.univigame.musbit");
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, "Поделиться"));
            }
        });


        AdSettings.setIsChildDirected(true);//для детей
        rewardedVideoAd = new RewardedVideoAd(this, "338186373483051_338187103482978");
        rewardedVideoAd.setAdListener(new RewardedVideoAdListener() {
            @Override
            public void onError(Ad ad, AdError error) {
                // Rewarded video ad failed to load
                Log.e(TAG, "Rewarded video ad failed to load: " + error.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Rewarded video ad is loaded and ready to be displayed
                Log.d(TAG, "Rewarded video ad is loaded and ready to be displayed!");
                rewardedVideoAd.show();
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Rewarded video ad clicked
                Log.d(TAG, "Rewarded video ad clicked!");
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Rewarded Video ad impression - the event will fire when the
                // video starts playing
                Log.d(TAG, "Rewarded video ad impression logged!");
            }

            @Override
            public void onRewardedVideoCompleted() {
                // Rewarded Video View Complete - the video has been played to the end.
                // You can use this event to initialize your reward
                Log.d(TAG, "Rewarded video completed!");

                // Call method to give reward
                // giveReward();
            }

            @Override
            public void onRewardedVideoClosed() {
                // The Rewarded Video ad was closed - this can occur during the video
                // by closing the app, or closing the end card.
                Log.d(TAG, "Rewarded video ad closed!");
            }
        });
        rewardedVideoAd.loadAd();


    }

}
