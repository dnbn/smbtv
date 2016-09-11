package com.smbtv.delegate.base;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.smbtv.R;
import com.smbtv.model.SMBShare;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SMBShareDAO extends DAOBase {

    private static final String TAG = SMBShareDAO.class.getSimpleName();

    private static Map<String, String> queries = XMLQueryLoader.load(R.xml.db_query_shares);

    public SMBShareDAO(Context context) {

        super(context);
    }


    public SMBShare findById(int idShare) {

        Log.d(TAG, "findById");

        SMBShare share = null;

        try (DBWrapper db = instanceDB()) {

            db.addBinding("id", Integer.toString(idShare));

            String query = queries.get("getById_share");
            Cursor c = db.rawQuery(query);

            if (c.moveToFirst()) {
                share = new SMBShare();
                share.setId(c.getInt(0));
                share.setPath(c.getString(1));
                share.setName(c.getString(2));
                share.setAccessMode(SMBShare.AccessMode.values()[c.getInt(3)]);
            }
        }

        return share;
    }

    public List<SMBShare> findAll() {

        Log.d(TAG, "findAll");

        List<SMBShare> shares = new ArrayList<>();

        try (DBWrapper db = instanceDB()) {

            String query = queries.get("getAll_shares");
            Cursor c = db.rawQuery(query);

            while (c.moveToNext()) {
                SMBShare share = new SMBShare();
                share.setId(c.getInt(0));
                share.setPath(c.getString(1));
                share.setName(c.getString(2));
                share.setAccessMode(SMBShare.AccessMode.values()[c.getInt(3)]);
                shares.add(share);
            }
        }

        return shares;
    }

    public void insert(SMBShare share) {

        Log.d(TAG, "insert");

        try (DBWrapper db = instanceDB()) {

            String query = queries.get("insert_share");

            db.addBinding("path", share.getPath());
            db.addBinding("name", share.getName());
            db.addBinding("access", share.getAccessMode().toStringValue());
            db.execSQL(query);
        }
    }

    public void delete(SMBShare share) {

        Log.d(TAG, "delete");

        try (DBWrapper db = instanceDB()) {

            String query = queries.get("delete_share");
            db.addBinding("id", Integer.toString(share.getId()));
            db.execSQL(query);
        }
    }

    public void update(SMBShare share) {

        Log.d(TAG, "update");

        try (DBWrapper db = instanceDB()) {

            String query = queries.get("update_share");

            db.addBinding("id", Integer.toString(share.getId()));
            db.addBinding("path", share.getPath());
            db.addBinding("name", share.getName());
            db.addBinding("access", share.getAccessMode().toStringValue());

            db.execSQL(query);
        }
    }
}
