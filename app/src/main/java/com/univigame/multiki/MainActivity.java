package com.univigame.multiki;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.games.Games;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    MainActivity tekactiviti;
    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;
    Button byn_start, byn_start2, btn_energ, btn_money, btn_videomodey;
    int money;
    long tek_energy;
    TextView textView9;
    private FirebaseAnalytics mFirebaseAnalytics;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        tekactiviti = this;

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);


        Bundle extras = new Bundle();
        extras.putString("max_ad_content_rating", "G");
        //инит рекл
        MobileAds.initialize(this, getString(R.string.rekl_id_app));


        byn_start = (Button) findViewById(R.id.byn_start);
        byn_start2 = (Button) findViewById(R.id.byn_start2);
        btn_energ = (Button) findViewById(R.id.btn_energ);
        btn_money = (Button) findViewById(R.id.btn_money);
        btn_videomodey = (Button) findViewById(R.id.btn_videomodey);
        textView9 = (TextView) findViewById(R.id.textView9);

        Button  button6 = (Button) findViewById(R.id.button6);
        Button   button7 = (Button) findViewById(R.id.button7);
        Button  button8 = (Button) findViewById(R.id.button8);


        byn_start.setEnabled(false);


        button6.setOnClickListener(new View.OnClickListener() {
            public void onClick(View r) {
                //инста
              //  openLink(this, "http://vk.com/id1");
            }
        });
        button7.setOnClickListener(new View.OnClickListener() {
            public void onClick(View r) {
                //вк
                openLink(MainActivity.this, "https://vk.com/musbitgame");
            }
        });
        button8.setOnClickListener(new View.OnClickListener() {
            public void onClick(View r) {
                //facebook
                openLink(MainActivity.this, "https://www.facebook.com/MusBit-угадай-музыку-по-биту-1116667585187816");
            }
        });

        byn_start.setOnClickListener(new View.OnClickListener() {
            public void onClick(View r) {
                if (tek_energy > 0) {

//вычтем энергию за игру
                    Cursor cursor = mDb.rawQuery("SELECT energy FROM records ", null);
                    cursor.moveToFirst();

                    int energy = (cursor.getInt(cursor.getColumnIndex("energy")));
                    cursor.close();
                    long unixTime = System.currentTimeMillis() / 1000L;
                    if (energy >= unixTime)
                        mDb.execSQL("UPDATE `records` SET energy=energy+" + 600);
                    else
                        mDb.execSQL("UPDATE `records` SET energy=" + (unixTime + 600));
                    //Log.d("asdasd",energy+" "+unixTime);
//вычтем энергию за игру

                    Intent intent = new Intent(tekactiviti, game.class);
                    startActivity(intent);
                } else {
                    CustomDialog_energy customDialog1 = new CustomDialog_energy(tekactiviti, tek_energy);
                    customDialog1.show();
                }
            }
        });
        btn_energ.setOnClickListener(new View.OnClickListener() {
            public void onClick(View r) {
                CustomDialog_energy customDialog1 = new CustomDialog_energy(tekactiviti, tek_energy);
                customDialog1.show();
            }
        });
        btn_money.setOnClickListener(new View.OnClickListener() {
            public void onClick(View r) {
                CustomDialog_money customDialog1 = new CustomDialog_money(tekactiviti, money);
                customDialog1.show();
            }
        });
        btn_videomodey.setOnClickListener(new View.OnClickListener() {
            public void onClick(View r) {
                Intent intent = new Intent(MainActivity.this, nagrada_za_reklamu.class);
                intent.putExtra("type_nagr", 1);
                MainActivity.this.startActivity(intent);
            }
        });


        byn_start2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View r) {
                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }
        });

        textView9.setOnClickListener(new View.OnClickListener() {
            public void onClick(View r) {
                showLeaderboard();
            }
        });


        mDBHelper = new DatabaseHelper(this);
        mDb = mDBHelper.getWritableDatabase();


        new load_spis_valut().execute();
        ;//загрузим песни с сайта


        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

        }
    }


    private static final int RC_LEADERBOARD_UI = 9004;

    private void showLeaderboard() {
        Games.getLeaderboardsClient(this, GoogleSignIn.getLastSignedInAccount(this))
                .getLeaderboardIntent(getString(R.string.leaderboard_id))
                .addOnSuccessListener(new OnSuccessListener<Intent>() {
                    @Override
                    public void onSuccess(Intent intent) {
                        startActivityForResult(intent, RC_LEADERBOARD_UI);
                    }
                });
    }


    private void signInSilently() {
        GoogleSignInClient signInClient = GoogleSignIn.getClient(this,
                GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN);
        signInClient.silentSignIn().addOnCompleteListener(this,
                new OnCompleteListener<GoogleSignInAccount>() {
                    @Override
                    public void onComplete(@NonNull Task<GoogleSignInAccount> task) {
                        if (task.isSuccessful()) {
                            // The signed in account is stored in the task's result.
                            GoogleSignInAccount signedInAccount = task.getResult();
                        } else {
                            // Player will need to sign-in explicitly using via UI
                            Log.d("pfkegf", "asdasd");
                        }
                    }
                });
    }


    @Override
    protected void onResume() {
        super.onResume();

        signInSilently();

        TextView textView10 = (TextView) findViewById(R.id.textView9);
        Cursor cursor = mDb.rawQuery("SELECT * FROM records ", null);
        cursor.moveToFirst();


        int energy = (cursor.getInt(cursor.getColumnIndex("energy")));
        int score = (cursor.getInt(cursor.getColumnIndex("score")));
        money = (cursor.getInt(cursor.getColumnIndex("money")));
        long unixTime = System.currentTimeMillis() / 1000L;


        if (unixTime >= energy)
            tek_energy = 12;
        else
            tek_energy = 11 - ((energy - unixTime) / 600);


        textView10.setText(score + "");
        btn_energ.setText("Энергия " + tek_energy + "/12");
        btn_money.setText("Монеты " + money + "");

    }


    private class load_spis_valut extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... path) {
            BufferedReader reader = null;
            String content;
            try {

                URL url = new URL("https://teplogico.ru/musbit");
                HttpURLConnection c = (HttpURLConnection) url.openConnection();
                c.setRequestMethod("GET");
                c.setReadTimeout(10000);
                c.connect();

                reader = new BufferedReader(new InputStreamReader(c.getInputStream()));
                StringBuilder buf = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    buf.append(line + "\n");
                }
                // return (buf.toString());


                Log.d("json", buf.toString());

                JSONArray jsonArray = new JSONArray(buf.toString());

                if (jsonArray.length() > 0) mDb.execSQL("DELETE FROM `musbit` ");

                Cursor curs = mDb.rawQuery("SELECT id FROM `musbit` ", null);


                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject zakaz = jsonArray.getJSONObject(i);
                    Log.d("json", "" + zakaz.getString("name"));
                    String name = zakaz.getString("name");
                    String imageurl = zakaz.getString("url");

                    Log.d("ыйд", "INSERT INTO `musbit` ( `id`,`name`, `url`,`sort`)" +
                            " VALUES ('" + (i + 1) + "', '" + name + "', '" + imageurl + "', '" + (i + 1) + "')");

                    mDb.execSQL("INSERT INTO `musbit` ( `id`,`name`, `url`,`sort`)" +
                            " VALUES ('" + (i + 1) + "', '" + name + "', '" + imageurl + "', '" + (i + 1) + "')");

                }


                content = "";
            } catch (Exception e) {
                content = e.getMessage();

            }

            return content;
        }


        @Override
        protected void onPostExecute(String content) {

            byn_start.setEnabled(true);
        }


    }


    private static final String VK_APP_PACKAGE_ID = "com.vkontakte.android";
    private static final String FACEBOOK_APP_PACKAGE_ID = "com.facebook.katana";

    public static void openLink(Activity activity, String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        List<ResolveInfo> resInfo = activity.getPackageManager().queryIntentActivities(intent, 0);

        if (resInfo.isEmpty()) return;

        for (ResolveInfo info : resInfo) {
            if (info.activityInfo == null) continue;
            if (VK_APP_PACKAGE_ID.equals(info.activityInfo.packageName)
                    || FACEBOOK_APP_PACKAGE_ID.equals(info.activityInfo.packageName)
            ) {
                intent.setPackage(info.activityInfo.packageName);
                break;
            }
        }
        activity.startActivity(intent);
    }
}
