package com.univigame.multiki;

import android.app.Dialog;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appodeal.ads.Appodeal;
import com.appodeal.ads.RewardedVideoCallbacks;


public class CustomDialog_money {
    private EditText dialogEditBox;
    private TextView dialogInfo;
    private Button dialogButton;
    private Dialog dialog;
    private MainActivity activity;
    int money;
    ImageButton button4;
   // private RewardedAd rewardedAd;

    public CustomDialog_money(MainActivity activity, int money) {
        this.activity = activity;
        this.money = money;
        init();
    }

    private void init() {

        dialog = new Dialog(activity, R.style.CustomDialog);

        dialog.setContentView(R.layout.dialog_money);
        dialog.setCancelable(false);


        dialog.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        Button textView7 = (Button) dialog.findViewById(R.id.btn_money);
        button4 = (ImageButton) dialog.findViewById(R.id.button4);
        dialogButton = (Button) dialog.findViewById(R.id.dialog_button);



        DatabaseHelper   mDBHelper = new DatabaseHelper(activity);
        SQLiteDatabase mDb = mDBHelper.getWritableDatabase();



        textView7.setText("Монеты "+money);
        button4.setOnClickListener(new OnClickListener() {
            public void onClick(View r) {
                dialog.dismiss();
            }
        });

        dialogButton.setOnClickListener(new OnClickListener() {
            public void onClick(View r) {
           //     dialog.dismiss();
                if(Appodeal.isLoaded(Appodeal.REWARDED_VIDEO)) {


                Appodeal.setRewardedVideoCallbacks(new RewardedVideoCallbacks() {
                    @Override
                    public void onRewardedVideoLoaded(boolean isPrecache) {



                        Log.d("Appodeal", "onRewardedVideoLoaded");
                    }
                    @Override
                    public void onRewardedVideoFailedToLoad() {
                        Log.d("Appodeal", "onRewardedVideoFailedToLoad");
                    }
                    @Override
                    public void onRewardedVideoShown() {
                        Log.d("Appodeal", "onRewardedVideoShown");
                    }
                    @Override
                    public void onRewardedVideoFinished(double amount, String name) {
                        Log.d("Appodeal", "onRewardedVideoFinished");

                        mDb.execSQL("UPDATE `records` SET money=money+100" );

                        Toast.makeText(activity, "Вам начислено 100 монет", Toast.LENGTH_LONG).show();


                    }
                    @Override
                    public void onRewardedVideoClosed(boolean finished) {
                        Log.d("Appodeal", "onRewardedVideoClosed");
                        dialog.dismiss();
                    }
                    @Override
                    public void onRewardedVideoExpired() {
                        Log.d("Appodeal", "onRewardedVideoExpired");
                    }
                });



                Appodeal.show(activity, Appodeal.REWARDED_VIDEO);

            }else{
                Toast.makeText(activity, "Реклама не готова, нажмите позже", Toast.LENGTH_LONG).show();
            }
/*

                if (rewardedAd.isLoaded()) {
                    Activity activityContext = activity;
                    RewardedAdCallback adCallback = new RewardedAdCallback() {
                        public void onRewardedAdOpened() {
                            // Ad opened.
                        }

                        public void onRewardedAdClosed() {
                            // Ad closed.

                        }

                        public void onUserEarnedReward(@NonNull RewardItem reward) {
                            // User earned reward.
                            mDb.execSQL("UPDATE `records` SET money=money+" + reward.getAmount());

                            Toast.makeText(activity, "Вам начислено " +
                                    reward.getAmount() + " " + reward.getType(), Toast.LENGTH_LONG).show();
                        }

                        public void onRewardedAdFailedToShow(int errorCode) {
                            // Ad failed to display
                        }
                    };
                    rewardedAd.show(activityContext, adCallback);
                } else {
                    Log.d("TAG", "The rewarded ad wasn't loaded yet.");
                }
*/

            }
        });

/*

        rewardedAd = new RewardedAd(activity,
                activity.getString(R.string.voznagr_rekl_100m));

        RewardedAdLoadCallback adLoadCallback = new RewardedAdLoadCallback() {
            @Override
            public void onRewardedAdLoaded() {


                    dialogButton.setVisibility(View.VISIBLE);
                    YoYo.with(Techniques.FlipInX).playOn(dialogButton);

                // Ad successfully loaded.
            }

            @Override
            public void onRewardedAdFailedToLoad(int errorCode) {
                // Ad failed to load.
            }
        };
        rewardedAd.loadAd(new AdRequest.Builder().build(), adLoadCallback);

*/

    }

    public void show() {
        dialog.show();
    }


}