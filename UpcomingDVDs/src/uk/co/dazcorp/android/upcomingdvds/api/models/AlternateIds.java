package uk.co.dazcorp.android.upcomingdvds.api.models;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class AlternateIds implements Serializable {

	private static final long serialVersionUID = 8425284730567507473L;

	public String imdb;
	public Map<String, Object> additionalProperties = new HashMap<String, Object>();

}
