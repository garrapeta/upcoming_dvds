
package uk.co.dazcorp.android.upcomingdvds;

import android.app.Application;

import com.novoda.imageloader.core.ImageManager;
import com.novoda.imageloader.core.LoaderSettings;
import com.novoda.imageloader.core.LoaderSettings.SettingsBuilder;
import com.novoda.imageloader.core.cache.LruBitmapCache;

public class DVDApplication extends Application {

    public static final int VIEW_DVD = 1;
    public static final int VIEW_MOVIES = 0;
    private static ImageManager imageManager;

    public static final ImageManager getImageManager() {
        return imageManager;
    }

    public DVDApplication() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LoaderSettings settings = new SettingsBuilder().withDisconnectOnEveryCall(true)
                .withCacheManager(new LruBitmapCache(this)).build(this);
        imageManager = new ImageManager(this, settings);
    }
}
