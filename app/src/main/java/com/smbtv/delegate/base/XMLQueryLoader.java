package com.smbtv.delegate.base;


import android.content.res.Resources;
import android.util.Log;

import com.smbtv.delegate.ApplicationDelegate;
import com.smbtv.delegate.exception.LoadQueryFromXMLException;

import org.xmlpull.v1.XmlPullParser;

import java.util.HashMap;
import java.util.Map;

public class XMLQueryLoader {

    private static final String TAG = XMLQueryLoader.class.getSimpleName();
    private static final String ROOT = "queries";

    public static Map<String, String> load(int resourceFile) {

        Map<String, String> queries = new HashMap<>();

        Resources res = ApplicationDelegate.getContext().getResources();
        XmlPullParser xpp = res.getXml(resourceFile);

        try {
            while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {
                if (xpp.getEventType() == XmlPullParser.START_TAG) {
                    if (!xpp.getName().equals(ROOT)) {
                        String key = xpp.getAttributeValue(0);
                        String value = xpp.nextText();
                        queries.put(key, value);
                    }
                }
                xpp.next();
            }
        } catch (Exception e) {

            Log.e(TAG, Log.getStackTraceString(e));
            throw new LoadQueryFromXMLException(e);
        }

        return queries;
    }
}