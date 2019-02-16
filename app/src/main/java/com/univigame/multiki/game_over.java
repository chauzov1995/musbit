package com.univigame.multiki;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class game_over extends AppCompatActivity {


    private Button btn_vmenu, btn_restart, btn_share;
    private TextView total_score, tvplmoney;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);
        getSupportActionBar().hide();

        Intent intent = getIntent();
        int gameover_money = intent.getIntExtra("gameover_money",0);
        int gameover_schore = intent.getIntExtra("gameover_schore",0);


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
                onBackPressed();
            }
        });
        btn_share.setOnClickListener(new View.OnClickListener() {
            public void onClick(View r) {
                onBackPressed();
            }
        });

    }
}
