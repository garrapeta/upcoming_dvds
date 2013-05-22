
package uk.co.dazcorp.android.upcomingdvds;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

class UrlGenerator {
    public static String UPCOMING_DVD_API = "http://api.rottentomatoes.com/api/public/v1.0/lists/dvds/upcoming.json?";
    public static String UPCOMING_MOVIES_API = "http://api.rottentomatoes.com/api/public/v1.0/lists/movies/upcoming.json?";
    private static String API_KEY = "apikey";
    private static String mApiKey = "bye7be3mwyn6jwphp626jrf4";
    private static String TAG = "UrlGenerator";

    private String mCurrentURL;

    public UrlGenerator(String url, boolean isBase) {
        if (isBase) {
            setBaseUrlTo(url);
        } else {
            setCurrentURL(url);
        }
    }

    public boolean addValue(String value_type, String value) {
        if (value == null || value.equals("")) {
            return false;
        }

        Log.d(TAG, "URL - value type: " + value_type);
        value = value.trim();

        try {
            value = URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Should probably do something here
            e.printStackTrace();
        }
        mCurrentURL = mCurrentURL.concat("&" + value_type + "=" + value);

        return true;
    }

    public String getCurrentURL() {
        return mCurrentURL;
    }

    public void setBaseUrlTo(String baseUrl) {
        mCurrentURL = baseUrl;
        mCurrentURL = mCurrentURL.concat(API_KEY + "=" + mApiKey);
    }

    public void setCurrentURL(String current) {
        mCurrentURL = current;
    }

}
