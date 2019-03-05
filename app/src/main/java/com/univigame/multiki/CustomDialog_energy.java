package com.univigame.multiki;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class CustomDialog_energy {
    private EditText dialogEditBox;
    private TextView dialogInfo;
    private Button dialogButton;
    ImageButton button4;
    private Dialog dialog;
    private Activity activity;
    long tek_energy;
    Timer timer;
    TextView timer_view;
    Button  imageView5;
    int  sekdoplus1=10;


    public CustomDialog_energy(Activity activity, long tek_energy  ){
        this.activity = activity;
        this.tek_energy=tek_energy;

        init();
    }

    private void init(){

        dialog = new Dialog(activity, R.style.CustomDialog);
       // dialog.setTitle("Подсказки");
        dialog.setContentView(R.layout.dialog_energy);
        dialog.setCancelable(false);


        dialog.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        imageView5= (Button) dialog.findViewById( R.id.imageView5 );
                button4 = (ImageButton) dialog.findViewById( R.id.button4 );
        dialogButton = (Button) dialog.findViewById( R.id.dialog_button );
         timer_view = (TextView) dialog.findViewById( R.id.textView8 );


        imageView5.setText("Энергия "+tek_energy+" / 12");





        DatabaseHelper   mDBHelper = new DatabaseHelper(activity);
        SQLiteDatabase   mDb = mDBHelper.getWritableDatabase();
        Cursor cursor = mDb.rawQuery("SELECT * FROM records ", null);
        cursor.moveToFirst();
        int energy = (cursor.getInt(cursor.getColumnIndex("energy")));
        long unixTime = System.currentTimeMillis() / 1000L;
        sekdoplus1=(int)((energy - unixTime)%600);
        cursor.close();




        timer = new Timer();
        timer.schedule(new MyTimerTask(), 0, 1000);










        button4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View r) {
                dialog.dismiss();
            }
        });

        dialogButton.setOnClickListener(new OnClickListener() {
            public void onClick(View r) {
                dialog.dismiss();
                Intent intent = new Intent(activity, nagrada_za_reklamu.class);
                intent.putExtra("type_nagr", 3);
                activity.startActivity(intent);



            }
        });
    }

    public void show(){
        dialog.show();
    }


    class MyTimerTask extends TimerTask {

        @Override
        public void run() {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    if(tek_energy>=12){
                        timer.cancel();
                        imageView5.setText("Энергия 12 / 12");
                        timer_view.setText("");
                        return;
                    }

                    int minut=sekdoplus1/60;
                    int secund=sekdoplus1%60;
                    String secund_formatted = String.format("%02d", secund);
                    timer_view.setText("+1 через "+ minut+":"+secund_formatted);


                    if(sekdoplus1==0){
                        tek_energy++;
                        imageView5.setText("Энергия "+tek_energy+" / 12");
                        sekdoplus1=600;

                    }



                    sekdoplus1--;
                }
            });
        }
    }






}