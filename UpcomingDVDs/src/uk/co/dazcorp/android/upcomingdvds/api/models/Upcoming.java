package uk.co.dazcorp.android.upcomingdvds.api.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Upcoming implements Serializable {

	private static final long serialVersionUID = 790677597651853699L;

	public int total;
	public List<Movie> movies = new ArrayList<Movie>();
	public Links_ links;
	public String linkTemplate;
	public Map<String, Object> additionalProperties = new HashMap<String, Object>();

}
