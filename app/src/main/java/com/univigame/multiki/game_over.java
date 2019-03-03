package com.univigame.multiki;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import android.widget.ShareActionProvider;
import android.widget.TextView;

public class game_over extends AppCompatActivity {


    private Button btn_vmenu, btn_restart, btn_share;
    private TextView total_score, tvplmoney;
    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);
        getSupportActionBar().hide();

        intent = getIntent();
        int gameover_money = intent.getIntExtra("gameover_money",0);
        int gameover_schore = intent.getIntExtra("gameover_schore",0);


        mDBHelper = new DatabaseHelper(this);
        mDb = mDBHelper.getWritableDatabase();

        btn_vmenu = (Button) findViewById(R.id.btn_vmenu);
        btn_restart = (Button) findViewById(R.id.btn_restart);
        btn_share = (Button) findViewById(R.id.btn_share);
        tvplmoney = (TextView) findViewById(R.id.tvplmoney);
        total_score = (TextView) findViewById(R.id.total_score);


        total_score.setText(gameover_schore+"");
        tvplmoney.setText("+ "+gameover_money+" (монет)");


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

                    Cursor cursor = mDb.rawQuery("SELECT energy FROM records ", null);
                    cursor.moveToFirst();

                    int energy = (cursor.getInt(cursor.getColumnIndex("energy")));
                    cursor.close();




                long unixTime = System.currentTimeMillis() / 1000L;
long tek_energy;

                if (unixTime >= energy)
                    tek_energy = 12;
                else
                    tek_energy = 11 - ((energy - unixTime) / 600);




                if(tek_energy>0){

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
                    CustomDialog_energy customDialog1 = new CustomDialog_energy((Activity)game_over.this, tek_energy);
                    customDialog1.show();
                }








            }
        });
        btn_share.setOnClickListener(new View.OnClickListener() {
            public void onClick(View r) {

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Приложение name, скачивай от сюда - ссылка");
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent,"Поделиться"));
            }
        });

    }

}
