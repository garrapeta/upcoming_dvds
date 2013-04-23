
package uk.co.dazcorp.android.upcomingdvds;

import android.app.Application;

import com.novoda.imageloader.core.ImageManager;
import com.novoda.imageloader.core.LoaderSettings;
import com.novoda.imageloader.core.LoaderSettings.SettingsBuilder;

public class DVDApplication extends Application {

    private static ImageManager imageManager;
    public static final int VIEW_MOVIES = 0;
    public static final int VIEW_DVD = 1;

    public DVDApplication() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LoaderSettings settings = new SettingsBuilder().withDisconnectOnEveryCall(true).build(this);
        imageManager = new ImageManager(this, settings);
    }

    public static final ImageManager getImageManager() {
        return imageManager;
    }
}
