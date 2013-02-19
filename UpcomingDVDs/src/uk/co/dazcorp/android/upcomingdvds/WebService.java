
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
    private static final boolean LOGURI = true;
    public static final String TAG = "WebService";
    public static final String ERROR_MSG = "error";
    public static final String RESULT = "result";
    public static final String ACTION_RESP = "uk.co.dazcorp.android.upcomingdvds.SERVICE_COMPLETE";

    public WebService() {
        super("IntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // Not much to do here yet as we only handle one intent
        UrlGenerator gen = new UrlGenerator(UrlGenerator.UPCOMING_DVD_API, true);
        String locale = Locale.getDefault().getCountry();
        if (locale.equals("GB")) {
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

    private URI getURIfromString(String url) {
        URI uri = null;
        try {
            uri = new URI(url);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return uri;
    }

    private String doRequest(URI uri) {

        if (LOGURI) {
            Log.d(TAG, "*** URI = " + uri.toString() + " ***");
        }
        String result = null;
        try {
            BufferedReader in = null;
            AndroidHttpClient client = AndroidHttpClient.newInstance(null);
            HttpGet request = new HttpGet();
            request.setURI(uri);
            HttpResponse response = client.execute(request);
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
            client.close();

        } catch (Exception e) {
            result = ERROR_MSG;
            e.printStackTrace();
        }
        return result;
    }
}
