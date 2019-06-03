package com.smbtv.delegate.base;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.smbtv.model.SMBShare;
import com.smbtv.model.SMBUser;

import org.apache.commons.lang3.text.StrSubstitutor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class DAOBase {

    protected final static int VERSION = 7;
    protected final static String NAME = "database.db";

    private SQLiteDatabase mDb = null;
    private DatabaseHandler mHandler = null;

    public DAOBase(Context pContext) {

        this.mHandler = new DatabaseHandler(pContext, NAME, null, VERSION);
    }

    public DBWrapper instanceDB() {

        mDb = mHandler.getWritableDatabase();
        return new DBWrapper(mDb);
    }

    protected class DBWrapper implements AutoCloseable {

        private SQLiteDatabase mDb;
        private Map<String, String> mBindings;

        public DBWrapper(SQLiteDatabase pDb) {

            this.mDb = pDb;
            mBindings = new HashMap<>();
        }

        @Override
        public void close() {

            mDb.close();
        }

        public void beginTransaction() {
            mDb.beginTransaction();
        }


        public void endTransaction() {

            mDb.endTransaction();
        }

        public boolean isOpen() {

            return mDb.isOpen();
        }

        public void addBinding(String name, String value) {

            mBindings.put(name, escapeSql(value));
        }

        public void execSQL(String sql) {

            mDb.execSQL(bindQuery(sql));
            mBindings.clear();
        }

        public Cursor rawQuery(String sql) {

            return mDb.rawQuery(bindQuery(sql), null);
        }

        private String escapeSql(String sql) {

            return sql.replaceAll("'", "''");
        }

        private String bindQuery(String sql) {

            return new StrSubstitutor(mBindings).replace(sql);
        }
    }
}
