package uk.co.dazcorp.android.upcomingdvds.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.novoda.imageloader.core.model.ImageTag;
import com.novoda.imageloader.core.model.ImageTagFactory;

import uk.co.dazcorp.android.upcomingdvds.DVDApplication;
import uk.co.dazcorp.android.upcomingdvds.R;
import uk.co.dazcorp.android.upcomingdvds.api.models.Movie;

import java.util.List;

public class MovieAdapter extends ArrayAdapter<Movie> {
	private final ImageTagFactory imageTagFactory;

	static class ViewHolder {
		public TextView title;
		public TextView year;
		public TextView releaseDateMovie;
		public TextView releaseDateDVD;
		public ImageView poster;
	}

	public MovieAdapter(Context context, int resource, List<Movie> objects) {
		super(context, resource, objects);

		imageTagFactory = ImageTagFactory.newInstance(getContext(), R.drawable.ic_action_reload);
		imageTagFactory.setAnimation(R.anim.fade_in);
		imageTagFactory.setErrorImageId(R.drawable.ic_launcher);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;

		if (rowView == null) {
			LayoutInflater inflater = LayoutInflater.from(getContext());
			rowView = inflater.inflate(R.layout.row_dvd_list_left, parent, false);

			ViewHolder viewHolder = new ViewHolder();
			viewHolder.title = (TextView) rowView.findViewById(R.id.list_item_title);
			viewHolder.year = (TextView) rowView.findViewById(R.id.list_item_year);
			viewHolder.releaseDateMovie = (TextView) rowView
					.findViewById(R.id.list_item_release_date_movie);
			viewHolder.releaseDateDVD = (TextView) rowView
					.findViewById(R.id.list_item_release_date_dvd);
			viewHolder.poster = (ImageView) rowView.findViewById(R.id.list_item_thumbnail);

			rowView.setTag(viewHolder);
		}

		ViewHolder holder = (ViewHolder) rowView.getTag();
		Movie m = getItem(position);
		if (m != null) {
			holder.title.setText(m.title);
			holder.year.setText(m.year);
			if (m.releaseDates != null) {
				holder.releaseDateMovie.setText(m.releaseDates.theater);
				holder.releaseDateDVD.setText(m.releaseDates.dvd);
			}

			ImageTag tag = imageTagFactory.build(m.posters.profile, getContext());
			holder.poster.setTag(tag);
			DVDApplication.getImageManager().getLoader().load(holder.poster);
		}

		return rowView;
	}
}
