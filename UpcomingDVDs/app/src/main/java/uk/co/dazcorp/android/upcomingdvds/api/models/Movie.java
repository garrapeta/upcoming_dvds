package uk.co.dazcorp.android.upcomingdvds.api.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Movie implements Serializable {

	private static final long serialVersionUID = 3566189693264500078L;

	public String id;
	public String title;
	public String year;
	public String mpaaRating;
	public String runtime;
	public ReleaseDates releaseDates;
	public Ratings ratings;
	public String synopsis;
	public Posters posters;
	public List<AbridgedCast> abridgedCast = new ArrayList<AbridgedCast>();
	public AlternateIds alternateIds;
	public Links links;

}
