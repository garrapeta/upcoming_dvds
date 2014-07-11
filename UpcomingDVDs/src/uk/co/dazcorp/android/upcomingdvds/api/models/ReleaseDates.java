package uk.co.dazcorp.android.upcomingdvds.api.models;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ReleaseDates implements Serializable {

	private static final long serialVersionUID = 3011120982238997471L;

	public String theater;
	public String dvd;
	public Map<String, Object> additionalProperties = new HashMap<String, Object>();

}
