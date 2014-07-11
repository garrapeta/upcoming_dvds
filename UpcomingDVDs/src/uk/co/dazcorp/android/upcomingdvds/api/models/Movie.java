package uk.co.dazcorp.android.upcomingdvds.api.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Movie implements Serializable {

	private static final long serialVersionUID = 3566189693264500078L;

	public String id;
	public String title;
	public String year;
	public String mpaaRating;
	public int runtime;
	public ReleaseDates releaseDates;
	public Ratings ratings;
	public String synopsis;
	public Posters posters;
	public List<AbridgedCast> abridgedCast = new ArrayList<AbridgedCast>();
	public AlternateIds alternateIds;
	public Links links;
	public Map<String, Object> additionalProperties = new HashMap<String, Object>();

}
