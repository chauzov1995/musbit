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
    class_spis_vsego musik;


    public dial_perehod(game activity, int money, class_spis_vsego musik) {
        this.activity = activity;
        this.money = money;
        this.musik=musik;
        init();
    }

    private void init() {

        dialog = new Dialog(activity, R.style.CustomDialog);

        dialog.setContentView(R.layout.dial_perehod);
        dialog.setCancelable(false);




        dialog.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        perehod_btn = (Button) dialog.findViewById(R.id.perehod_btn);
        ((TextView) dialog.findViewById(R.id.textView6)).setText(musik.nazv);
        ((TextView) dialog.findViewById(R.id.textView10)).setText(musik.author);
        Button   button_apple = (Button) dialog.findViewById(R.id.button);
        Button   btn_share = (Button) dialog.findViewById(R.id.btn_share);



        btn_share.setOnClickListener(new View.OnClickListener() {
            public void onClick(View r) {

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Я угадал песню , в игре MusBi ссылка");
                sendIntent.setType("text/plain");
                activity.startActivity(Intent.createChooser(sendIntent,"Поделиться"));
            }
        });



        button_apple.setOnClickListener(new OnClickListener() {
            public void onClick(View r) {



            }
        });

        perehod_btn.setOnClickListener(new OnClickListener() {
            public void onClick(View r) {

                dialog.dismiss();
                activity.start_sled=true;


            }
        });
    }

    public void show() {
        dialog.show();
    }


}