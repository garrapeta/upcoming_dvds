package uk.co.dazcorp.android.upcomingdvds.api.models;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Posters implements Serializable {

	private static final long serialVersionUID = 1565937050344664311L;

	public String thumbnail;
	public String profile;
	public String detailed;
	public String original;
	public Map<String, Object> additionalProperties = new HashMap<String, Object>();

}
