package uk.co.dazcorp.android.upcomingdvds.api.models;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Ratings implements Serializable {

	private static final long serialVersionUID = -8065411969132533767L;

	public String criticsRating;
	public int criticsScore;
	public String audienceRating;
	public int audienceScore;
	public Map<String, Object> additionalProperties = new HashMap<String, Object>();

}
