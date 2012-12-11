
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

public class WebService extends IntentService {
    private static final boolean LOGURI = false;
    private static final String TAG = "WebService";
    private static final String ERROR_MSG = "error";

    public WebService() {
        super("IntentService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        // TODO Auto-generated method stub
        // Handle the call to process the request
        doRequest(null);

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
