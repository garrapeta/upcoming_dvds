package uk.co.dazcorp.android.upcomingdvds.api;

import android.app.IntentService;
import android.content.Intent;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;
import uk.co.dazcorp.android.upcomingdvds.DVDApplication;
import uk.co.dazcorp.android.upcomingdvds.api.models.Upcoming;

import java.util.Locale;

public class WebService extends IntentService {
	public static final String PACKAGE = "uk.co.dazcorp.android.upcomingdvds";
	public static final String ACTION_RESP = PACKAGE + ".SERVICE_COMPLETE";
	public static final String ERROR_MSG = "error";
	public static final String RESULT = "result";
	public static final String TAG = "WebService";
	public static final String TYPE = "type";
	public static final String PAGE = "page";
	public static final String PAGE_LIMIT = "page_limit";

	public static final int DEFAULT_PAGE = 1;
	public static final int DEFAULT_PAGE_LIMIT = 32;

	private static final String RT_API = "http://api.rottentomatoes.com/api/public/v1.0/";
	private static String API_KEY = "apikey";
	private static String mApiKey = "f7kctxkvak7p559bmzcxqhpu";

	private RottenTomartoes mRottenTomartoes;

	public WebService() {
		super("IntentService");
		RequestInterceptor requestInterceptor = new RequestInterceptor() {
			@Override
			public void intercept(RequestFacade request) {
				request.addQueryParam(API_KEY, mApiKey);
			}
		};

		Gson gson = new GsonBuilder().setFieldNamingPolicy(
				FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();

		RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(RT_API)
				.setConverter(new GsonConverter(gson)).setRequestInterceptor(requestInterceptor)
				.build();

		mRottenTomartoes = restAdapter.create(RottenTomartoes.class);

	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// Not much to do here yet as we only handle one intent
		int type = intent.getIntExtra(TYPE, -1);
		int page = intent.getIntExtra(PAGE, 1);
		int pageLimit = intent.getIntExtra(PAGE_LIMIT, 16);

		String locale = Locale.getDefault().getCountry();
		if (locale.equals("GB")) {
			// API doesn't work with GB
			locale = "uk";
		}

		Upcoming upcoming;

		switch (type) {
			case DVDApplication.VIEW_DVD:
				upcoming = mRottenTomartoes.upcomingDVDs(String.valueOf(pageLimit),
						String.valueOf(page), locale);
				break;
			case DVDApplication.VIEW_MOVIES:
				upcoming = mRottenTomartoes.upcomingMovies(String.valueOf(pageLimit),
						String.valueOf(page), locale);
				break;
			default:
				upcoming = mRottenTomartoes.upcomingDVDs(String.valueOf(pageLimit),
						String.valueOf(page), locale);
		}

		Intent broadcastIntent = new Intent();
		broadcastIntent.setAction(ACTION_RESP);
		broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
		broadcastIntent.putExtra(RESULT, upcoming);
		broadcastIntent.putExtra(TYPE, type);
		sendBroadcast(broadcastIntent);

	}
}
