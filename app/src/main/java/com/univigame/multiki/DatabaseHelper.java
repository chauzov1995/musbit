package com.univigame.multiki;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;


public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;

    public DatabaseHelper(Context context) {
        // конструктор суперкласса
        super(context, "myDB", null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {




        db.execSQL(" CREATE TABLE `records` (\n" +
                "  `energy` int NOT NULL DEFAULT '0',\n" +
                "  `score` int NOT NULL DEFAULT '0',\n" +
                "  `money` int NOT NULL DEFAULT '0',\n" +
                "  `level` int NOT NULL DEFAULT '0'\n" +


                ");"

        );

        db.execSQL(" CREATE TABLE `musbit` (\n" +
                "  `id` int NOT NULL DEFAULT 0,\n" +
                "  `name` varchar NOT NULL DEFAULT '',\n" +
                "  `ispoln` varchar NOT NULL DEFAULT '',\n" +
                "  `applemusikurl` varchar NOT NULL DEFAULT '',\n" +
                "  `url` varchar NOT NULL DEFAULT '',\n" +
                "  `sort` int NOT NULL DEFAULT 0,\n" +
                "  `podtverjd` int NOT NULL DEFAULT 0\n" +
                ");"

        );

        db.execSQL(" CREATE TABLE `musbit1` (\n" +
                "  `id` int NOT NULL DEFAULT 0,\n" +
                "  `name` varchar NOT NULL DEFAULT '',\n" +
                "  `ispoln` varchar NOT NULL DEFAULT '',\n" +
                "  `applemusikurl` varchar NOT NULL DEFAULT '',\n" +
                "  `url` varchar NOT NULL DEFAULT '',\n" +
                "  `sort` int NOT NULL DEFAULT 0,\n" +
                "  `podtverjd` int NOT NULL DEFAULT 0\n" +
                ");"
        );

        Calendar calendar = Calendar.getInstance();
        long startTime = calendar.getTimeInMillis()/1000;
      //  Log.d("asdad", "asdasdasd");
        db.execSQL("INSERT INTO `records`" +
                "( `money`) VALUES" +
                " (100)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {



        switch (oldVersion) {
            case 1:
                db.execSQL("ALTER TABLE musbit ADD COLUMN `podtverjd` int NOT NULL DEFAULT 0");
                db.execSQL(" CREATE TABLE `musbit1` (\n" +
                        "  `id` int NOT NULL DEFAULT 0,\n" +
                        "  `name` varchar NOT NULL DEFAULT '',\n" +
                        "  `ispoln` varchar NOT NULL DEFAULT '',\n" +
                        "  `applemusikurl` varchar NOT NULL DEFAULT '',\n" +
                        "  `url` varchar NOT NULL DEFAULT '',\n" +
                        "  `sort` int NOT NULL DEFAULT 0,\n" +
                        "  `podtverjd` int NOT NULL DEFAULT 0\n" +
                        ");"
                );

        }
    }
}