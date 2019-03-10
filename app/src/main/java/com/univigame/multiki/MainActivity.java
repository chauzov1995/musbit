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
import android.widget.Toast;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.games.Games;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PlayGamesAuthProvider;

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
    private static final int RC_SIGN_IN = 55;
    MainActivity tekactiviti;
    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;
    Button byn_start, byn_start2, btn_energ, btn_money, btn_videomodey;
    int money;
    long tek_energy;
    TextView textView9;
    private FirebaseAnalytics mFirebaseAnalytics;
    private FirebaseAuth mAuth;
    long energi_do12=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        tekactiviti = this;

        mAuth = FirebaseAuth.getInstance();

        Bundle bundle = new Bundle();



        mAuth = FirebaseAuth.getInstance();


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();

            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            String uid = user.getUid();
        }

        bundle.putString("max_ad_content_rating", "G");
        //инит рекл
        MobileAds.initialize(this, getString(R.string.rekl_id_app));


        byn_start = (Button) findViewById(R.id.byn_start);
        byn_start2 = (Button) findViewById(R.id.byn_start2);
        btn_energ = (Button) findViewById(R.id.btn_energ);
        btn_money = (Button) findViewById(R.id.btn_money);
        btn_videomodey = (Button) findViewById(R.id.btn_videomodey);
        textView9 = (TextView) findViewById(R.id.textView9);

        Button button6 = (Button) findViewById(R.id.button6);
        Button button7 = (Button) findViewById(R.id.button7);
        Button button8 = (Button) findViewById(R.id.button8);


        byn_start.setEnabled(false);


        button6.setOnClickListener(new View.OnClickListener() {
            public void onClick(View r) {
                //инста
                  openLink(MainActivity.this, "https://www.instagram.com/musbitgame/");
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
                startActivity(intent);
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
             //   showLeaderboard();
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


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
      //  updateUI(currentUser);
    }




// ...
// Initialize Firebase Auth


    private void showLeaderboard() {
// Initialize Firebase Auth



        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        Bundle bundle = new Bundle();
        bundle.putLong(FirebaseAnalytics.Param.SCORE, 200);
        bundle.putString("leaderboard_id", "CgkIpYam4KYPEAIQAQ");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.POST_SCORE, bundle);
        //startSignInIntent();

    }


    private void startSignInIntent() {
        GoogleSignInClient signInClient = GoogleSignIn.getClient(this,
                GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN);
        Intent intent = signInClient.getSignInIntent();
        startActivityForResult(intent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // The signed in account is stored in the result.
                GoogleSignInAccount signedInAccount = result.getSignInAccount();
            } else {
                String message = result.getStatus().getStatusMessage();
                if (message == null || message.isEmpty()) {
                    //message = getString(R.string.signin_other_error);
                }
                new AlertDialog.Builder(this).setMessage(message)
                        .setNeutralButton(android.R.string.ok, null).show();
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();



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
        energi_do12=energy - unixTime;

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
                    String ispoln = zakaz.getString("author");
                    String applemusikurl = zakaz.getString("url_applemus");

                    Log.d("ыйд", "INSERT INTO `musbit` ( `id`,`name`, `url`,`sort`, ispoln, applemusikurl)" +
                            " VALUES ('" + (i + 1) + "', '" + name + "', '" + imageurl + "', '" + (i + 1) + "', '"+ispoln+"', '"+applemusikurl+"' )");

                    mDb.execSQL("INSERT INTO `musbit` ( `id`,`name`, `url`,`sort`, ispoln, applemusikurl)" +
                            " VALUES ('" + (i + 1) + "', '" + name + "', '" + imageurl + "', '" + (i + 1) + "', '"+ispoln+"', '"+applemusikurl+"' )");

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
    private static final String INSTAGRAMM_APP_PACKAGE_ID = "com.facebook.katana";

    public static void openLink(Activity activity, String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        List<ResolveInfo> resInfo = activity.getPackageManager().queryIntentActivities(intent, 0);

        if (resInfo.isEmpty()) return;

        for (ResolveInfo info : resInfo) {
            if (info.activityInfo == null) continue;
            if (VK_APP_PACKAGE_ID.equals(info.activityInfo.packageName)
                    || FACEBOOK_APP_PACKAGE_ID.equals(info.activityInfo.packageName)
                    || FACEBOOK_APP_PACKAGE_ID.equals(info.activityInfo.packageName)
            ) {
                intent.setPackage(info.activityInfo.packageName);
                break;
            }
        }
        activity.startActivity(intent);
    }
}
