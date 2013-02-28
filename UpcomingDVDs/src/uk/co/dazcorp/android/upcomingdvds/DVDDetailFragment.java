
package uk.co.dazcorp.android.upcomingdvds;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.novoda.imageloader.core.model.ImageTag;
import com.novoda.imageloader.core.model.ImageTagFactory;

import org.json.JSONException;
import org.json.JSONObject;

import uk.co.dazcorp.android.upcomingdvds.ApiDetails.Upcoming.Movies;

/**
 * A fragment representing a single DVD detail screen. This fragment is either
 * contained in a {@link DVDListActivity} in two-pane mode (on tablets) or a
 * {@link DVDDetailActivity} on handsets.
 */
public class DVDDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The content this fragment is presenting.
     */
    private Movies mMovie;

    private ImageTagFactory imageTagFactory;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public DVDDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            try {
                mMovie = new Movies(new JSONObject(getArguments().getString(ARG_ITEM_ID)));
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            imageTagFactory = new ImageTagFactory(this.getActivity(), R.drawable.ic_launcher);
            imageTagFactory.setErrorImageId(R.drawable.ic_launcher);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dvd_detail, container, false);
        if (mMovie != null) {
            ((TextView) rootView.findViewById(R.id.dvd_detail_title)).setText(mMovie.mTitle);
            ((TextView) rootView.findViewById(R.id.dvd_detail_year)).setText(mMovie.mYear);
            ((TextView) rootView.findViewById(R.id.dvd_detail_synopsis)).setText(mMovie.mSynopsis);
            ImageView v = (ImageView) rootView.findViewById(R.id.dvd_detail_poster);
            ImageTag tag = imageTagFactory.build(mMovie.mPosters.mOriginal);
            v.setTag(tag);
            DVDApplication.getImageManager().getLoader().load(v);

        }
        return rootView;
    }
}
