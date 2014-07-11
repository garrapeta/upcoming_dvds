package uk.co.dazcorp.android.upcomingdvds;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ShareActionProvider;
import android.widget.TextView;

import com.novoda.imageloader.core.model.ImageTag;
import com.novoda.imageloader.core.model.ImageTagFactory;

import uk.co.dazcorp.android.upcomingdvds.api.models.Movie;

/**
 * A fragment representing a single DVD detail screen. This fragment is either contained in a {@link DVDListActivity} in two-pane mode (on tablets) or a {@link DVDDetailActivity} on handsets.
 */
public class DVDDetailFragment extends Fragment {
	/**
	 * The fragment argument representing the item ID that this fragment represents.
	 */
	public static final String ARG_ITEM_ID = "item_id";

	private ImageTagFactory imageTagFactory;
	/**
	 * The content this fragment is presenting.
	 */
	private Movie mMovie;

	private ShareActionProvider mShareActionProvider;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the fragment (e.g. upon screen orientation changes).
	 */
	public DVDDetailFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);

		if (getArguments().containsKey(ARG_ITEM_ID)) {
			mMovie = (Movie) getArguments().getSerializable(ARG_ITEM_ID);

			imageTagFactory = ImageTagFactory.newInstance(this.getActivity(),
					R.drawable.unloaded_photo);
			imageTagFactory.setSaveThumbnail(true);
			imageTagFactory.setAnimation(R.anim.fade_in);
			imageTagFactory.setErrorImageId(R.drawable.unloaded_photo);
		}
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// Inflate menu resource file.
		inflater.inflate(R.menu.details_menu, menu);
		// Locate MenuItem with ShareActionProvider
		MenuItem item = menu.findItem(R.id.menu_item_share);
		// Fetch and store ShareActionProvider
		mShareActionProvider = (ShareActionProvider) item.getActionProvider();
		setShareIntent();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_dvd_detail, container, false);
		if (mMovie != null) {

			((TextView) rootView.findViewById(R.id.dvd_detail_title)).setText(mMovie.title);
			((TextView) rootView.findViewById(R.id.dvd_detail_year)).setText(mMovie.year);
			((TextView) rootView.findViewById(R.id.dvd_detail_synopsis)).setText(mMovie.synopsis);
			((TextView) rootView.findViewById(R.id.dvd_detail_mpaa_rating))
					.setText(mMovie.mpaaRating);
			((TextView) rootView.findViewById(R.id.dvd_detail_runtime)).setText(mMovie.runtime
					+ " minutes");
			if (mMovie.releaseDates != null) {

				TextView dvd = new TextView(getActivity());
				dvd.setText(getString(R.string.release_type_dvd) + mMovie.releaseDates.dvd);
				TextView theater = new TextView(getActivity());
				theater.setText(getString(R.string.release_type_theater)
						+ mMovie.releaseDates.theater);

				((LinearLayout) rootView.findViewById(R.id.dvd_detail_release_dates_layout))
						.addView(theater);
				((LinearLayout) rootView.findViewById(R.id.dvd_detail_release_dates_layout))
						.addView(dvd);
			}

			if (mMovie.ratings != null) {

				if (mMovie.ratings.criticsScore != -1) {
					TextView critics = getTextView();

					String criticScore = mMovie.ratings.criticsScore + "%";
					critics.setText(criticScore);
					setIcon(critics, mMovie.ratings.criticsRating);

					((LinearLayout) rootView.findViewById(R.id.dvd_detail_rating_layout))
							.addView(critics);
				}

				if (mMovie.ratings.audienceScore != -1) {
					TextView audience = getTextView();
					String audianceScore = mMovie.ratings.audienceScore + "%";
					audience.setText(audianceScore);
					setIcon(audience, mMovie.ratings.audienceRating);

					((LinearLayout) rootView.findViewById(R.id.dvd_detail_rating_layout))
							.addView(audience);
				}

			}

			// Should move down the list of poster sizes if the largest doesn't
			// load
			ImageView v = (ImageView) rootView.findViewById(R.id.dvd_detail_poster);
			ImageTag tag = imageTagFactory.build(mMovie.posters.original, getActivity());
			v.setTag(tag);
			DVDApplication.getImageManager().getLoader().load(v);

		}
		return rootView;
	}

	private TextView getTextView() {
		TextView tv = new TextView(getActivity(), null,
				android.R.style.TextAppearance_DeviceDefault_Large);
		tv.setGravity(Gravity.CENTER);

		return tv;

	}

	private void setIcon(TextView tv, String type) {
		if (type != null && !type.isEmpty()) {
			type = type.toUpperCase();

			if (type.equals(DVDApplication.CRITIC_CERTIFIED)) {
				tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_certified_fresh, 0, 0, 0);
			}
			if (type.equals(DVDApplication.CRITIC_FRESH)) {
				tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_fresh, 0, 0, 0);
			}
			if (type.equals(DVDApplication.CRITIC_ROTTEN)) {
				tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_rotten, 0, 0, 0);
			}

			if (type.equals(DVDApplication.AUDIANCE_SPILLED)) {
				tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_spilt, 0, 0, 0);
			}
			if (type.equals(DVDApplication.AUDIANCE_UPRIGHT)) {
				tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_popcorn, 0, 0, 0);

			}

		}

	}

	@Override
	public void onResume() {
		super.onResume();
		setShareIntent();
	}

	// Call to update the share intent
	private void setShareIntent() {
		if (mShareActionProvider != null && mMovie != null) {
			Intent shareIntent = new Intent(Intent.ACTION_SEND);
			shareIntent.setType("*/*");
			shareIntent.putExtra(Intent.EXTRA_TEXT, mMovie.title + " - " + mMovie.year + " "
					+ mMovie.links.alternate + " "
					+ getResources().getString(R.string.via_upcoming));

			mShareActionProvider.setShareIntent(shareIntent);
		}
	}
}
