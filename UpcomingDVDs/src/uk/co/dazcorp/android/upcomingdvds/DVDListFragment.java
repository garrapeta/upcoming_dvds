package uk.co.dazcorp.android.upcomingdvds;

import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.manuelpeinado.refreshactionitem.ProgressIndicatorType;
import com.manuelpeinado.refreshactionitem.RefreshActionItem;
import com.manuelpeinado.refreshactionitem.RefreshActionItem.RefreshActionListener;

import uk.co.dazcorp.android.upcomingdvds.adapters.MovieAdapter;
import uk.co.dazcorp.android.upcomingdvds.api.WebService;
import uk.co.dazcorp.android.upcomingdvds.api.models.Movie;
import uk.co.dazcorp.android.upcomingdvds.api.models.Upcoming;

import java.util.ArrayList;
import java.util.List;

/**
 * A list fragment representing a list of DVDs. This fragment also supports tablet devices by allowing list items to be given an 'activated' state upon selection. This helps indicate which item is currently being viewed in a {@link DVDDetailFragment}.
 * <p>
 * Activities containing this fragment MUST implement the {@link Callbacks} interface.
 */
public class DVDListFragment extends ListFragment implements RefreshActionListener,
		OnNavigationListener, OnRefreshListener {

	/**
	 * A callback interface that all activities containing this fragment must implement. This mechanism allows activities to be notified of item selections.
	 */
	public interface Callbacks {
		/**
		 * Callback for when an item has been selected.
		 */
		public void onItemSelected(Movie item);
	}

	public static final String ERROR = "error";

	public class ResponseReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			boolean error = intent.getExtras().getBoolean(ERROR);
			if (error) {
				Toast.makeText(getActivity(), getString(R.string.something_went_wrong),
						Toast.LENGTH_LONG).show();
				mRefreshActionItem.showProgress(false);
				mSwipeLayout.setRefreshing(false);
				return;
			}
			int type = intent.getExtras().getInt(WebService.TYPE);
			Upcoming upcoming = (Upcoming) intent.getExtras().getSerializable(WebService.RESULT);

			mMovies = upcoming.movies;
			mAdapter.clear();
			mAdapter.addAll(mMovies);

			Gson gson = new Gson();
			// Save some data
			SharedPreferences.Editor ed = mPrefs.edit();
			if (type == DVDApplication.VIEW_DVD) {
				ed.putString(DVD_DATA, gson.toJson(upcoming));
			}
			if (type == DVDApplication.VIEW_MOVIES) {
				ed.putString(MOVIES_DATA, gson.toJson(upcoming));
			}

			ed.putLong(MOVIES_TIME, System.currentTimeMillis());
			ed.commit();
			mRefreshActionItem.showProgress(false);
			mSwipeLayout.setRefreshing(false);
		}

	}

	private static final Long HOUR = (long) 3600000;

	private static final String MOVIES_DATA = "movies_data";
	private static final String DVD_DATA = "dvd_data";

	private static final String PREFS = "dvdlistprefs";

	// The type of content we are currently looking at
	public static final String VIEW_TYPE = "view_type";
	public int CURRENT_VIEW = 0;

	/**
	 * A dummy implementation of the {@link Callbacks} interface that does nothing. Used only when this fragment is not attached to an activity.
	 */
	private static Callbacks sDummyCallbacks = new Callbacks() {
		@Override
		public void onItemSelected(Movie item) {
		}
	};

	/**
	 * The serialization (saved instance state) Bundle key representing the activated item position. Only used on tablets.
	 */
	private static final String STATE_ACTIVATED_POSITION = "activated_position";
	/**
	 * The current activated item position. Only used on tablets.
	 */
	private int mActivatedPosition = ListView.INVALID_POSITION;
	private MovieAdapter mAdapter;

	/**
	 * The fragment's current callback object, which is notified of list item clicks.
	 */
	private Callbacks mCallbacks = sDummyCallbacks;

	private List<Movie> mMovies = new ArrayList<Movie>();
	private final String MOVIES_TIME = "movies_timestamp";

	private SharedPreferences mPrefs;

	private ResponseReceiver mReceiver;

	private RefreshActionItem mRefreshActionItem;

	private SwipeRefreshLayout mSwipeLayout;

	private boolean isFromCode;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the fragment (e.g. upon screen orientation changes).
	 */
	public DVDListFragment() {
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// Activities containing this fragment must implement its callbacks.
		if (!(activity instanceof Callbacks)) {
			throw new IllegalStateException("Activity must implement fragment's callbacks.");
		}

		mCallbacks = (Callbacks) activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mPrefs = getActivity().getSharedPreferences(PREFS, Context.MODE_PRIVATE);
		String movies = mPrefs.getString(MOVIES_DATA, null);
		String dvds = mPrefs.getString(DVD_DATA, null);
		Long savedAt = mPrefs.getLong(MOVIES_TIME, 0);
		CURRENT_VIEW = savedInstanceState != null ? savedInstanceState.getInt(VIEW_TYPE) : 0;
		switch (CURRENT_VIEW) {
			case DVDApplication.VIEW_DVD:
				if (dvds != null) {
					// Have saved dvds, use them
					Gson gson = new Gson();
					Upcoming upcoming = gson.fromJson(dvds, Upcoming.class);
					mMovies = upcoming.movies;

				} else {
					refreshData();
				}
				break;

			case DVDApplication.VIEW_MOVIES:
				if (movies != null) {
					// Have saved movies, use them
					Gson gson = new Gson();
					Upcoming upcoming = gson.fromJson(movies, Upcoming.class);
					mMovies = upcoming.movies;

				} else {
					refreshData();
				}
				break;

		}

		if (System.currentTimeMillis() > savedAt + HOUR) {
			refreshData();
		}

		mReceiver = new ResponseReceiver();
		mAdapter = new MovieAdapter(getActivity(), 0, mMovies);
		this.setListAdapter(mAdapter);

		Context context = this.getActivity().getActionBar().getThemedContext();
		ArrayAdapter<CharSequence> list = ArrayAdapter.createFromResource(context, R.array.menu,
				android.R.layout.simple_list_item_1);
		list.setDropDownViewResource(android.R.layout.simple_list_item_1);
		this.getActivity().getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		isFromCode = true;
		getActivity().getActionBar().setSelectedNavigationItem(CURRENT_VIEW);
		isFromCode = false;
		this.getActivity().getActionBar().setListNavigationCallbacks(list, this);

		setHasOptionsMenu(true);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
		menuInflater.inflate(R.menu.list_menu, menu);
		MenuItem item = menu.findItem(R.id.list_refresh);
		mRefreshActionItem = (RefreshActionItem) item.getActionView();
		mRefreshActionItem.setMenuItem(item);
		mRefreshActionItem.setProgressIndicatorType(ProgressIndicatorType.INDETERMINATE);
		mRefreshActionItem.setRefreshActionListener(this);

	}

	@Override
	public void onRefreshButtonClick(RefreshActionItem sender) {
		refreshData();

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);

		View v = inflater.inflate(R.layout.fragment_dvd_list, container, false);
		mSwipeLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_container);
		mSwipeLayout.setOnRefreshListener(this);
		mSwipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
				android.R.color.holo_green_light, android.R.color.holo_orange_light,
				android.R.color.holo_red_light);

		return v;
	}

	@Override
	public void onDetach() {
		super.onDetach();

		// Reset the active callbacks interface to the dummy implementation.
		mCallbacks = sDummyCallbacks;
	}

	@Override
	public void onListItemClick(ListView listView, View view, int position, long id) {
		super.onListItemClick(listView, view, position, id);

		// Notify the active callbacks interface (the activity, if the
		// fragment is attached to one) that an item has been selected.
		mCallbacks.onItemSelected(mAdapter.getItem(position));
	}

	@Override
	public void onPause() {
		super.onPause();

		this.getActivity().unregisterReceiver(mReceiver);
	}

	@Override
	public void onResume() {
		super.onResume();
		IntentFilter filter = new IntentFilter(WebService.ACTION_RESP);
		filter.addCategory(Intent.CATEGORY_DEFAULT);
		this.getActivity().registerReceiver(mReceiver, filter);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (mActivatedPosition != AdapterView.INVALID_POSITION) {
			// Serialize and persist the activated item position.
			outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
		}
		outState.putInt(VIEW_TYPE, CURRENT_VIEW);

	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		// Restore the previously serialized activated item position.
		if (savedInstanceState != null && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
			setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
		}
	}

	/**
	 * Turns on activate-on-click mode. When this mode is on, list items will be given the 'activated' state when touched.
	 */
	public void setActivateOnItemClick(boolean activateOnItemClick) {
		// When setting CHOICE_MODE_SINGLE, ListView will automatically
		// give items the 'activated' state when touched.
		getListView()
				.setChoiceMode(
						activateOnItemClick ? AbsListView.CHOICE_MODE_SINGLE
								: AbsListView.CHOICE_MODE_NONE);
	}

	private void setActivatedPosition(int position) {
		if (position == AdapterView.INVALID_POSITION) {
			getListView().setItemChecked(mActivatedPosition, false);
		} else {
			getListView().setItemChecked(position, true);
		}

		mActivatedPosition = position;
	}

	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {

		if (CURRENT_VIEW != itemPosition && !isFromCode) {
			CURRENT_VIEW = itemPosition;
			refreshData();
		}

		return true;
	}

	private void refreshData() {
		if (mRefreshActionItem != null) {
			mRefreshActionItem.showProgress(true);
		}

		DVDListActivity.refreshData(this.getActivity(), CURRENT_VIEW);
	}

	@Override
	public void onRefresh() {
		refreshData();

	}
}
