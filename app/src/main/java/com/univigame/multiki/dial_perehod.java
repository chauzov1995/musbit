package com.univigame.multiki;


import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class dial_perehod {
    private EditText dialogEditBox;
    private TextView dialogInfo;
    private Button perehod_btn, button4;
    private Dialog dialog;
    private game activity;
    int money;


    public dial_perehod(game activity, int money) {
        this.activity = activity;
        this.money = money;
        init();
    }

    private void init() {

        dialog = new Dialog(activity, R.style.CustomDialog);

        dialog.setContentView(R.layout.dial_perehod);
        dialog.setCancelable(false);




        dialog.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        perehod_btn = (Button) dialog.findViewById(R.id.perehod_btn);



        perehod_btn.setOnClickListener(new OnClickListener() {
            public void onClick(View r) {

                dialog.dismiss();
                activity.start_sled=true;
               // activity.load_new_vopr(false);

            }
        });
    }

    public void show() {
        dialog.show();
    }


}