package uk.co.dazcorp.android.upcomingdvds.api.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AbridgedCast implements Serializable {

	private static final long serialVersionUID = -2279858458784812826L;

	public String name;
	public String id;
	public List<String> characters = new ArrayList<String>();
	public Map<String, Object> additionalProperties = new HashMap<String, Object>();

}
