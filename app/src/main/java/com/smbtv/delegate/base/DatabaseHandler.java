package com.smbtv.delegate.base;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.smbtv.R;

import java.util.Map;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final String TAG = DatabaseHandler.class.getSimpleName();

    private static final Map<String, String> strucQueries = XMLQueryLoader.load(R.xml.db_structure);
    private static final Map<String, String> configQueries = XMLQueryLoader.load(R.xml.db_query_config);

    public DatabaseHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {

        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        Log.d(TAG, "onCreate");

        db.execSQL(strucQueries.get("create_config"));
        db.execSQL(configQueries.get("init_config"));
        db.execSQL(strucQueries.get("create_shares"));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        Log.d(TAG, "onUpgrade");

        db.execSQL(strucQueries.get("drop_config"));
        db.execSQL(strucQueries.get("drop_shares"));
        onCreate(db);
    }

}