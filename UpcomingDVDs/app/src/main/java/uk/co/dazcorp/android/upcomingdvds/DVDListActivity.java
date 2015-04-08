package uk.co.dazcorp.android.upcomingdvds;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import uk.co.dazcorp.android.upcomingdvds.api.WebService;
import uk.co.dazcorp.android.upcomingdvds.api.models.Movie;

/**
 * An activity representing a list of DVDs. This activity has different presentations for handset
 * and tablet-size devices. On handsets, the activity presents a list of items, which when touched,
 * lead to a {@link DVDDetailActivity} representing item details. On tablets, the activity presents
 * the list of items and item details side-by-side using two vertical panes.
 * <p>
 * The activity makes heavy use of fragments. The list of items is a {@link DVDListFragment} and
 * the
 * item details (if present) is a {@link DVDDetailFragment}.
 * <p>
 * This activity also implements the required {@link DVDListFragment.Callbacks} interface to listen
 * for item selections.
 */
public class DVDListActivity extends ActionBarActivity implements DVDListFragment.Callbacks {

    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    public static void refreshData(Context context, int api) {

        Intent msgIntent = new Intent(context, WebService.class);
        if (api == DVDApplication.VIEW_DVD) {
            msgIntent.putExtra(WebService.TYPE, DVDApplication.VIEW_DVD);
        }

        if (api == DVDApplication.VIEW_MOVIES) {
            msgIntent.putExtra(WebService.TYPE, DVDApplication.VIEW_MOVIES);
        }

        context.startService(msgIntent);
    }

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet device.
     */
    private boolean mTwoPane;

    /**
     * Callback method from {@link DVDListFragment.Callbacks} indicating that the item with the
     * given ID was selected.
     */
    @Override
    public void onItemSelected(Movie item) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putSerializable(DVDDetailFragment.ARG_ITEM_ID, item);

            DVDDetailFragment fragment = new DVDDetailFragment();

            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.dvd_detail_container, fragment).commit();

        } else {
            // In single-pane mode, simply start the detail activity
            // for the selected item ID.
            Intent detailIntent = new Intent(this, DVDDetailActivity.class);
            detailIntent.putExtra(DVDDetailFragment.ARG_ITEM_ID, item);
            startActivity(detailIntent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dvd_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.my_drawer_layout);
        mDrawerLayout.setStatusBarBackgroundColor(
                getResources().getColor(R.color.material_blue_grey_900));

        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, 0, 0);
        mDrawerLayout.setDrawerListener(mToggle);
        mDrawerLayout.setStatusBarBackgroundColor(getResources().getColor(R.color.primary_dark));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // Set the adapter for the list view
        mDrawerList.setAdapter(new ArrayAdapter<DrawerItem>(this,
                R.layout.draweritem,
                new DrawerItem[]{new DrawerItem(R.string.movies, R.drawable.ic_action_ticket),
                        new DrawerItem(R.string.dvds, R.drawable.ic_action_record)}){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = getLayoutInflater().inflate(R.layout.draweritem, parent ,false);
                    viewHolder vh = new viewHolder();
                    vh.text = (TextView) convertView.findViewById(R.id.drawer_item_text);
                    vh.image = (ImageView) convertView.findViewById(R.id.drawer_item_icon);
                    convertView.setTag(vh);
                }
                DrawerItem item = getItem(position);
                ((viewHolder)convertView.getTag()).text.setText(item.mTitle);
                ((viewHolder)convertView.getTag()).image.setImageResource(item.mIcon);


                return convertView;
            }

            class viewHolder {
                public TextView text;
                public ImageView image;
            }
        });
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

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

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case R.id.about_menu:
                // Launch the about screen
                this.startActivity(new Intent(this, AboutActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            mDrawerList.setItemChecked(position, true);
            ((DVDListFragment) getSupportFragmentManager().findFragmentById(R.id.dvd_list))
                    .onNavigationItemSelected(position);
            getSupportActionBar().setTitle(getString(R.string.app_name) + " - " + ((TextView) view.findViewById(R.id.drawer_item_text)).getText().toString());
            mDrawerLayout.closeDrawer(mDrawerList);
        }
    }

    private class DrawerItem {

        public final int mTitle;
        public final int mIcon;

        public DrawerItem(int title, int icon) {
            mTitle = title;
            mIcon = icon;
        }
    }
}
