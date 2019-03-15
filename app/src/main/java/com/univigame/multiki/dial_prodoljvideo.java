package com.univigame.multiki;


import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import java.util.List;

public class dial_prodoljvideo implements RewardedVideoAdListener {

    private Button perehod_btn;
    private Dialog dialog;
    private game activity;
    int money;
    class_spis_vsego musik;
    CountDownTimer timer1;
    private RewardedVideoAd mRewardedVideoAd;

    public dial_prodoljvideo(game activity, int money, class_spis_vsego musik) {
        this.activity = activity;
        this.money = money;
        this.musik = musik;
        init();
    }


    private void init() {

        dialog = new Dialog(activity, R.style.CustomDialog);

        dialog.setContentView(R.layout.dial_prodoljvideo);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);


        // Use an activity context to get the rewarded video instance.
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(activity);
        mRewardedVideoAd.setRewardedVideoAdListener(dial_prodoljvideo.this);
        loadRewardedVideoAd();


        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                dialog.dismiss();
                timer1.cancel();
                activity.game_over();
            }
        });


        dialog.getWindow().setLayout(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        perehod_btn = (Button) dialog.findViewById(R.id.perehod_btn);
        ImageButton button4 = (ImageButton) dialog.findViewById( R.id.button4 );
        final TextView textView6 = (TextView) dialog.findViewById(R.id.textView6);

        button4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View r) {
                dialog.cancel();
            }
        });
        timer1 = new CountDownTimer(10000, 1000) {

            //Здесь обновляем текст счетчика обратного отсчета с каждой секундой
            public void onTick(long millisUntilFinished) {
                Log.d("осталось тимер1", millisUntilFinished / 1000 + "");
                textView6.setText((millisUntilFinished / 1000) + "");
            }

            //Задаем действия после завершения отсчета (высвечиваем надпись "Бабах!"):
            public void onFinish() {
                dialog.cancel();

            }
        }.start();


        perehod_btn.setOnClickListener(new OnClickListener() {
            public void onClick(View r) {
                timer1.cancel();
                dialog.dismiss();


                loadetsd = true;


            }
        });
    }

    boolean loadetsd = false;
    boolean prosmotrel = false;

    private void loadRewardedVideoAd() {
        mRewardedVideoAd.loadAd(activity.getString(R.string.voznagr_rekl_1zhisn),
                new AdRequest.Builder().build());
    }


    public void show() {
        dialog.show();
    }


    @Override
    public void onRewardedVideoAdLoaded() {

        if (mRewardedVideoAd.isLoaded() && loadetsd) {
            mRewardedVideoAd.show();
        }

    }

    @Override
    public void onRewardedVideoAdOpened() {

    }

    @Override
    public void onRewardedVideoStarted() {

    }

    @Override
    public void onRewardedVideoAdClosed() {
        if (prosmotrel) {
            activity.customDialog1 = new dial_perehod(activity, activity.money, musik);
            activity.customDialog1.show();
        } else {
            dialog.cancel();

        }
    }

    @Override
    public void onRewarded(RewardItem rewardItem) {

        //  dialog.dismiss();

        prosmotrel = true;
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {
        dialog.cancel();
    }

    @Override
    public void onRewardedVideoCompleted() {

    }
}