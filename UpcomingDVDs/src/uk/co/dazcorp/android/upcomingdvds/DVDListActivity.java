
package uk.co.dazcorp.android.upcomingdvds;

import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import org.json.JSONObject;

/**
 * An activity representing a list of DVDs. This activity has different
 * presentations for handset and tablet-size devices. On handsets, the activity
 * presents a list of items, which when touched, lead to a
 * {@link DVDDetailActivity} representing item details. On tablets, the activity
 * presents the list of items and item details side-by-side using two vertical
 * panes.
 * <p>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link DVDListFragment} and the item details (if present) is a
 * {@link DVDDetailFragment}.
 * <p>
 * This activity also implements the required {@link DVDListFragment.Callbacks}
 * interface to listen for item selections.
 */
public class DVDListActivity extends FragmentActivity implements DVDListFragment.Callbacks,
        OnNavigationListener {

    public static void refreshData(Context context, int api) {
        Intent msgIntent = new Intent(context, WebService.class);
        if (api == DVDApplication.VIEW_DVD)
            msgIntent.putExtra(WebService.API, UrlGenerator.UPCOMING_DVD_API);
        if (api == DVDApplication.VIEW_MOVIES)
            msgIntent.putExtra(WebService.API, UrlGenerator.UPCOMING_MOVIES_API);
        context.startService(msgIntent);
    }

    // The type of content we are currently looking at
    public int CURRENT_VIEW = 0;

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.list_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Callback method from {@link DVDListFragment.Callbacks} indicating that
     * the item with the given ID was selected.
     */
    @Override
    public void onItemSelected(JSONObject item) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString(DVDDetailFragment.ARG_ITEM_ID, item.toString());
            DVDDetailFragment fragment = new DVDDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.dvd_detail_container, fragment).commit();

        } else {
            // In single-pane mode, simply start the detail activity
            // for the selected item ID.
            Intent detailIntent = new Intent(this, DVDDetailActivity.class);
            detailIntent.putExtra(DVDDetailFragment.ARG_ITEM_ID, item.toString());
            startActivity(detailIntent);
        }
    }

    @Override
    public boolean onNavigationItemSelected(int itemPosition, long itemId) {
        if (CURRENT_VIEW != itemPosition) {
            CURRENT_VIEW = itemPosition;
            refreshData(this, CURRENT_VIEW);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int featureId = item.getItemId();
        switch (featureId) {
            case (R.id.list_refresh):
                refreshData(this, CURRENT_VIEW);
                return true;
            default:
                return super.onMenuItemSelected(featureId, item);
        }

    }

    public void refreshData(Context context) {
        refreshData(context, CURRENT_VIEW);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dvd_list);

        if (findViewById(R.id.dvd_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-large and
            // res/values-sw600dp). If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;

            // In two-pane mode, list items should be given the
            // 'activated' state when touched.
            ((DVDListFragment) getSupportFragmentManager().findFragmentById(R.id.dvd_list))
                    .setActivateOnItemClick(true);
        }
        Context context = this.getActionBar().getThemedContext();
        ArrayAdapter<CharSequence> list = ArrayAdapter.createFromResource(context, R.array.menu,
                android.R.layout.simple_list_item_1);
        list.setDropDownViewResource(android.R.layout.simple_list_item_1);
        getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        getActionBar().setListNavigationCallbacks(list, this);

    }

}
