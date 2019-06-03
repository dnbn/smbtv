package com.smbtv.delegate.base;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.smbtv.R;
import com.smbtv.model.SMBShare;
import com.smbtv.model.SMBUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SMBUserDAO extends DAOBase {

    private static final String TAG = SMBUserDAO.class.getSimpleName();

    private static Map<String, String> queries = XMLQueryLoader.load(R.xml.db_query_users);

    public SMBUserDAO(Context context) {

        super(context);
    }

    public List<SMBUser> findAll() {

        Log.d(TAG, "findAll");

        List<SMBUser> users = new ArrayList<>();

        try (DBWrapper db = instanceDB()) {

            String query = queries.get("getAll_users");
            Cursor c = db.rawQuery(query);

            while (c.moveToNext()) {
                SMBUser user = new SMBUser();
                user.setId(c.getInt(0));
                user.setLogin(c.getString(1));
                user.setPassword(c.getString(2));
                users.add(user);
            }
        }

        return users;
    }

    public void insert(SMBUser user) {

        Log.d(TAG, "insert");

        try (DBWrapper db = instanceDB()) {

            String query = queries.get("insert_user");

            db.addBinding("login", user.getLogin());
            db.addBinding("password", user.getPassword());
            db.execSQL(query);
        }
    }

    public SMBUser findById(int idUser) {

        Log.d(TAG, "findById");

        SMBUser user = null;

        try (DBWrapper db = instanceDB()) {

            db.addBinding("id", Integer.toString(idUser));

            String query = queries.get("getById_user");
            Cursor c = db.rawQuery(query);

            if (c.moveToFirst()) {
                user = new SMBUser();
                user.setId(c.getInt(0));
                user.setLogin(c.getString(1));
                user.setPassword(c.getString(2));
            }
        }

        return user;
    }


    public List<SMBShare> findShares(SMBUser user) {

        Log.d(TAG, "findAll");

        List<SMBShare> shares = new ArrayList<>();

        try (DBWrapper db = instanceDB()) {

            db.addBinding("id", Integer.toString(user.getId()));

            String query = queries.get("get_user_shares");
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

    public void update(SMBUser user) {

        Log.d(TAG, "update");

        try (DBWrapper db = instanceDB()) {

            String query = queries.get("update_user");

            db.addBinding("id", Integer.toString(user.getId()));
            db.addBinding("login", user.getLogin());
            db.addBinding("password", user.getPassword());

            db.execSQL(query);
        }
    }

    public void delete(SMBUser user) {

        Log.d(TAG, "delete");

        try (DBWrapper db = instanceDB()) {

            String query = queries.get("delete_user");
            db.addBinding("id", Integer.toString(user.getId()));
            db.execSQL(query);
        }
    }
}
