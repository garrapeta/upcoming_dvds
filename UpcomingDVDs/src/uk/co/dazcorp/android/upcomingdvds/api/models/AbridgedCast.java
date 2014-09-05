package uk.co.dazcorp.android.upcomingdvds.api.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AbridgedCast implements Serializable {

	private static final long serialVersionUID = -2279858458784812826L;

	public String name;
	public String id;
	public List<String> characters = new ArrayList<String>();

}
