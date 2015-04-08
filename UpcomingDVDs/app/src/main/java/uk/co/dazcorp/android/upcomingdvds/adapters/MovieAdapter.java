package uk.co.dazcorp.android.upcomingdvds.adapters;

import com.squareup.picasso.Picasso;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import uk.co.dazcorp.android.upcomingdvds.R;
import uk.co.dazcorp.android.upcomingdvds.api.models.Movie;

public class MovieAdapter extends ArrayAdapter<Movie> {

	static class ViewHolder {
		public TextView title;
		public TextView year;
		public TextView releaseDateMovie;
		public TextView releaseDateDVD;
		public ImageView poster;
	}

	public MovieAdapter(Context context, int resource, List<Movie> objects) {
		super(context, resource, objects);

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
		    Picasso.with(getContext()).load(m.posters.profile).into(holder.poster);
		}

		return rowView;
	}
}
