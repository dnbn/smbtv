package com.smbtv.delegate.base;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.smbtv.R;
import com.smbtv.delegate.exception.SMBConfigException;
import com.smbtv.model.SMBConfig;

import java.util.Map;

public class SMBConfigDAO extends DAOBase {

    private static final String TAG = SMBConfigDAO.class.getSimpleName();

    private static Map<String, String> queries = XMLQueryLoader.load(R.xml.db_query_config);

    public SMBConfigDAO(Context pContext) {
        super(pContext);
    }

    public SMBConfig find() {

        Log.d(TAG, "find");

        SMBConfig config = null;

        try (DBWrapper db = instanceDB()) {

            String query = queries.get("get_config");
            Cursor c = db.rawQuery(query);

            if (c.moveToFirst()) {

                config = new SMBConfig();
                config.setHostname(c.getString(0));
                config.setSharename(c.getString(1));
                config.setDomain(c.getString(2));
                config.setServerPort(c.getInt(3));
                config.setSessionPort(c.getInt(4));
                config.setDatagramPort(c.getInt(5));
                config.setTcpIpSmbPort(c.getInt(6));

            } else {

                throw new SMBConfigException("Cannot load the actual configuration.");
            }
        }

        return config;
    }

    public void update(SMBConfig config) {


        Log.d(TAG, "update");

        try (DBWrapper db = instanceDB()) {

            String query = queries.get("update_config");

            db.addBinding("hostname", config.getHostname());
            db.addBinding("hostname", config.getHostname());
            db.addBinding("sharename", config.getSharename());
            db.addBinding("domain", config.getDomain());
            db.addBinding("serverport", Integer.toString(config.getServerPort()));
            db.addBinding("sessionport", Integer.toString(config.getSessionPort()));
            db.addBinding("datagramport", Integer.toString(config.getDatagramPort()));
            db.addBinding("smbport", Integer.toString(config.getTcpIpSmbPort()));

            db.execSQL(query);
        }
    }

}
