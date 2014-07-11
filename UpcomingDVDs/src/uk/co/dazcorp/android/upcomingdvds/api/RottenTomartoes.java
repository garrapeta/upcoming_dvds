package uk.co.dazcorp.android.upcomingdvds.api;

import retrofit.http.GET;
import retrofit.http.Query;
import uk.co.dazcorp.android.upcomingdvds.api.models.Upcoming;

public interface RottenTomartoes {

	@GET("/lists/movies/upcoming.json")
	Upcoming upcomingMovies(@Query("page_limit") String pageLimit, @Query("page") String page,
			@Query("country") String country);

	@GET("/lists/dvds/upcoming.json")
	Upcoming upcomingDVDs(@Query("page_limit") String pageLimit, @Query("page") String page,
			@Query("country") String country);

}
