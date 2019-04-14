package com.univigame.multiki;


import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

public class dial_prodoljvideo {

    private Button perehod_btn;
    private Dialog dialog;
    private game activity;
    int money;
    class_spis_vsego musik;
    CountDownTimer timer1;

    boolean loadetsd = false;
    boolean prosmotrel = false;


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
        dialog.setOnCancelListener(dialogInterface -> close_Ad_fail());
        dialog.getWindow().setLayout(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        perehod_btn = (Button) dialog.findViewById(R.id.perehod_btn);
        ImageButton button4 = (ImageButton) dialog.findViewById(R.id.button4);
        final TextView textView6 = (TextView) dialog.findViewById(R.id.textView6);

        button4.setOnClickListener(r -> {
                    close_Ad_fail();
                }
        );
        perehod_btn.setOnClickListener(r -> {
            if (timer1 != null) timer1.cancel();

            RewardedAdCallback adCallback = new RewardedAdCallback() {
                public void onRewardedAdOpened() {
                    // Ad opened.
                }

                public void onRewardedAdClosed() {
                    // Ad closed.
                    if (prosmotrel)//единственный успешный вход
                    {
                        dialog.dismiss();
                        activity.prodolj_dialog(musik);

                        activity.rewardedAd = new RewardedAd(activity,
                                activity.getString(R.string.vozn_game_over));

                        RewardedAdLoadCallback adLoadCallback = new RewardedAdLoadCallback() {
                            @Override
                            public void onRewardedAdLoaded() {
                                // Ad successfully loaded.
                            }

                            @Override
                            public void onRewardedAdFailedToLoad(int errorCode) {
                                // Ad failed to load.
                            }
                        };
                        activity.rewardedAd.loadAd(new AdRequest.Builder().build(), adLoadCallback);
                    } else {
                        close_Ad_fail();
                    }
                }

                public void onUserEarnedReward(@NonNull RewardItem reward) {
                    // User earned reward.
                    prosmotrel = true;
                }

                public void onRewardedAdFailedToShow(int errorCode) {
                    // Ad failed to display
                    close_Ad_fail();
                }
            };
            activity.rewardedAd.show(activity, adCallback);


        });


        timer1 = new CountDownTimer(10000, 1000) {

            //Здесь обновляем текст счетчика обратного отсчета с каждой секундой
            public void onTick(long millisUntilFinished) {
                Log.d("осталось тимер1", millisUntilFinished / 1000 + "");
                textView6.setText((millisUntilFinished / 1000) + "");
            }

            //Задаем действия после завершения отсчета (высвечиваем надпись "Бабах!"):
            public void onFinish() {

                close_Ad_fail();

            }
        }.start();


    }


    private void close_Ad_fail() {


        if (timer1 != null) timer1.cancel();
        dialog.dismiss();
        activity.game_over();

    }

    void show() {
        dialog.show();
    }


}