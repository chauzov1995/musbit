package com.univigame.multiki;

import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;


public class nagrada_za_reklamu extends AppCompatActivity implements RewardedVideoAdListener {
    private RewardedVideoAd mRewardedVideoAd;
    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;
    int voznagr_str;
    int type_nagr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nagrada_za_reklamu);


        Intent intent = getIntent();
         type_nagr = intent.getIntExtra("type_nagr", 0);

        switch (type_nagr) {
            case 1:
                //100 монет
                voznagr_str = R.string.voznagr_rekl_100m;
                break;
            case 2:
                //150 монет
                voznagr_str = R.string.voznagr_rekl_150m;
                break;
            case 3:
                //2 энергии
                voznagr_str = R.string.voznagr_rekl_2energy;
                break;

        }

        mDBHelper = new DatabaseHelper(this);
        try {
            mDb = mDBHelper.getWritableDatabase();
        } catch (SQLException mSQLException) {
            throw mSQLException;
        }


        ProgressBar otv4 = (ProgressBar) findViewById(R.id.progressBar);
        otv4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View r) {
                if (mRewardedVideoAd.isLoaded()) {
                    mRewardedVideoAd.show();
                }
            }
        });


        MobileAds.initialize(this, getString(R.string.rekl_id_app));


        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.setRewardedVideoAdListener(this);
        loadRewardedVideoAd();
        Toast.makeText(this, "Ждите", Toast.LENGTH_LONG).show();

    }


    private void loadRewardedVideoAd() {
        Bundle extras = new Bundle();
        extras.putString("max_ad_content_rating", "G");

        mRewardedVideoAd.loadAd(getString(voznagr_str),
                new AdRequest.Builder().addNetworkExtrasBundle(AdMobAdapter.class, extras).build());
    }

    @Override
    public void onRewarded(RewardItem reward) {


        switch (type_nagr) {
            case 1:
                //100 монет
                mDb.execSQL("UPDATE `records` SET money=money+" + reward.getAmount());
                break;
            case 2:
                //150 монет
                mDb.execSQL("UPDATE `records` SET money=money+" + reward.getAmount());
                break;
            case 3:
                //2 энергии
                mDb.execSQL("UPDATE `records` SET money=energy+" + reward.getAmount());
                break;

        }

        onBackPressed();


        Toast.makeText(this, "Вам начислено " +
                reward.getAmount() + " " + reward.getType(), Toast.LENGTH_LONG).show();
        // Reward the user.

    }

    @Override
    public void onRewardedVideoAdLeftApplication() {
        //  Toast.makeText(this, "onRewardedVideoAdLeftApplication",
        //         Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdClosed() {
        //   Toast.makeText(this, "onRewardedVideoAdClosed", Toast.LENGTH_SHORT).show();
        //   loadRewardedVideoAd();
        onBackPressed();
    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int errorCode) {
        Toast.makeText(this, "Не удалось загрузить рекламу", Toast.LENGTH_LONG).show();
        //   mDb.execSQL("UPDATE `records` SET money=money+" + 50);
        onBackPressed();


        // Toast.makeText(this, "Вам начислено 50 монет", Toast.LENGTH_LONG);
    }

    @Override
    public void onRewardedVideoCompleted() {
        //  Toast.makeText(this, "загрузилось", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdLoaded() {
        //   Toast.makeText(this, "onRewardedVideoAdLoaded", Toast.LENGTH_SHORT).show();
        if (mRewardedVideoAd.isLoaded()) {
            mRewardedVideoAd.show();
        }
    }

    @Override
    public void onRewardedVideoAdOpened() {
        //   Toast.makeText(this, "onRewardedVideoAdOpened", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoStarted() {
        //   Toast.makeText(this, "onRewardedVideoStarted", Toast.LENGTH_SHORT).show();
    }


}
