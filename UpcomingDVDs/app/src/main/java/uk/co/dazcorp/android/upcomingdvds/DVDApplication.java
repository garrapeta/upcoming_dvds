package uk.co.dazcorp.android.upcomingdvds;

import android.app.Application;

public class DVDApplication extends Application {

	public static final int VIEW_DVD = 1;
	public static final int VIEW_MOVIES = 0;

	public static final String CRITIC_ROTTEN = "ROTTEN";
	public static final String CRITIC_FRESH = "FRESH";
	public static final String CRITIC_CERTIFIED = "CERTIFIED FRESH";

	public static final String AUDIANCE_UPRIGHT = "UPRIGHT";
	public static final String AUDIANCE_SPILLED = "SPILLED";



	public DVDApplication() {
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}
}
