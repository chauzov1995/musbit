package com.univigame.multiki;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class game_over extends AppCompatActivity {


    private Button btn_vk, btn_star, btn_menu, btn_restart, btn_fb;
    private TextView total_score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);


        btn_vk = (Button) findViewById(R.id.btn_vk);
        btn_fb = (Button) findViewById(R.id.btn_fb);
        btn_star = (Button) findViewById(R.id.btn_star);
        btn_menu = (Button) findViewById(R.id.btn_menu);
        btn_restart = (Button) findViewById(R.id.btn_restart);
        total_score = (TextView) findViewById(R.id.total_score);

        btn_menu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View r) {
 onBackPressed();
            }
        });
        btn_restart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View r) {
                onBackPressed();
            }
        });

    }
}
