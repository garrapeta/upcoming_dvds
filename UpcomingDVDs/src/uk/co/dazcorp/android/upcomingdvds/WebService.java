
package uk.co.dazcorp.android.upcomingdvds;

import android.app.IntentService;
import android.content.Intent;
import android.net.http.AndroidHttpClient;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Locale;

public class WebService extends IntentService {
    public static final String PACKAGE = "uk.co.dazcorp.android.upcomingdvds";
    public static final String ACTION_RESP = PACKAGE + ".SERVICE_COMPLETE";
    public static final String API = PACKAGE + ".API";
    public static final String ERROR_MSG = "error";
    public static final String RESULT = "result";
    public static final String TAG = "WebService";
    private static final boolean LOGURI = false;
    private AndroidHttpClient mClient;

    public WebService() {
        super("IntentService");
        mClient = AndroidHttpClient.newInstance(null);
    }

    private String doRequest(URI uri) {

        if (LOGURI) {
            Log.d(TAG, "*** URI = " + uri.toString() + " ***");
        }
        String result = null;
        try {
            BufferedReader in = null;
            if (mClient == null) {
                mClient = AndroidHttpClient.newInstance(null);
            }
            HttpGet request = new HttpGet();
            request.setURI(uri);
            HttpResponse response = mClient.execute(request);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                StringBuffer sb = new StringBuffer("");
                String line = "";
                String NL = System.getProperty("line.separator");
                while ((line = in.readLine()) != null) {
                    sb.append(line + NL);
                }
                in.close();
                String page = sb.toString();
                result = page;
            } else
                result = ERROR_MSG;
            mClient.close();

        } catch (Exception e) {
            result = ERROR_MSG;
            e.printStackTrace();
        } finally {
            // Close leaked client if we get an exception
            mClient.close();
        }
        return result;
    }

    private URI getURIfromString(String url) {
        URI uri = null;
        try {
            uri = new URI(url);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return uri;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // Not much to do here yet as we only handle one intent
        String api = intent.getStringExtra(API);
        UrlGenerator gen = new UrlGenerator(api, true);
        String locale = Locale.getDefault().getCountry();
        if (locale.equals("GB")) {
        	// API doesn't work with GB
            locale = "uk";
        }
        gen.addValue(ApiDetails.Upcoming.COUNTRY, locale);
        gen.addValue(ApiDetails.Upcoming.PAGE_LIMIT, Integer.toString(16));
        String result = doRequest(getURIfromString(gen.getCurrentURL()));
        result = result.trim();
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(ACTION_RESP);
        broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
        broadcastIntent.putExtra(RESULT, result);
        sendBroadcast(broadcastIntent);

    }
}
