
package uk.co.dazcorp.android.upcomingdvds;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

public class ApiDetails {
    public static final class Upcoming {
        // API Input
        // default 16 - the amount of upcoming dvds to show per page
        public static String PAGE_LIMIT = "page_limit";
        // default 1 - the selected page of upcoming DVDs
        public static String PAGE = "page";
        // default US - the locale to provide data for
        public static String COUNTRY = "country";
        // END Input

        // OUTPUT
        public String mResultCount;
        public Movies mMovies[];
        public Links mLinks[];
        public String mLinkTemplate;
        // END OUTPUT

        // JSON NODES
        private static final String TAG_TOTAL = "total";
        private static final String TAG_MOVIES = "movies";
        private static final String TAG_LINKS = "links";
        private static final String TAG_LINK_TEMPLATE = "link_template";

        public Upcoming(String input_string) {
            JSONObject input;
            try {
                // Number of results in total
                input = new JSONObject(input_string);
                if (input.has(TAG_TOTAL))
                    mResultCount = input.getString(TAG_TOTAL);

                // Array of Movies
                if (input.has(TAG_MOVIES)) {
                    JSONArray movieTemp = input.getJSONArray(TAG_MOVIES);
                    mMovies = new Movies[movieTemp.length()];
                    for (int i = 0; i < movieTemp.length(); i++) {
                        mMovies[i] = new Movies(movieTemp.getJSONObject(i));
                    }
                }
                // Links to more results etc
                if (input.has(TAG_LINKS)) {
                    JSONArray linkTemp = input.getJSONArray(TAG_LINKS);
                    mLinks = new Links[linkTemp.length()];
                    for (int i = 0; i < linkTemp.length(); i++) {
                        mLinks[i] = new Links(linkTemp.getJSONObject(i));
                    }
                }
                // The link Template
                if (input.has(TAG_LINK_TEMPLATE))
                    mLinkTemplate = input.getString(TAG_LINK_TEMPLATE);

            } catch (JSONException e) {
                // TODO Something better than just catching the exception
                e.printStackTrace();
            }
        }

        public static final class Movies {
            // JSON NODES
            private static final String TAG_ID = "id";
            private static final String TAG_TITLE = "title";
            private static final String TAG_YEAR = "year";
            private static final String TAG_MPAA_RATING = "mpaa_rating";
            private static final String TAG_RUNTIME = "runtime";
            private static final String TAG_RELEASE_DATES = "release_dates";
            private static final String TAG_RATINGS = "ratings";
            private static final String TAG_SYNOPSIS = "synopsis";
            private static final String TAG_POSTERS = "posters";
            private static final String TAG_ABRIDGED_CAST = "abridged_cast";
            private static final String TAG_ALTERNATE_IDS = "alternate_ids";
            private static final String TAG_LINKS = "links";

            public Movies(JSONObject jsonObject) {
                try {
                    mId = jsonObject.getString(TAG_ID);
                    mTitle = jsonObject.getString(TAG_TITLE);
                    mYear = jsonObject.getString(TAG_YEAR);
                    mMPAARating = jsonObject.getString(TAG_MPAA_RATING);
                    mRuntime = jsonObject.getString(TAG_RUNTIME);
                    mSynopsis = jsonObject.getString(TAG_SYNOPSIS);
                    // TODO: The other parts of the Movie object
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    // handle this
                    e.printStackTrace();
                }

            }

            public String mId;
            public String mTitle;
            public String mYear;
            public String mMPAARating;
            public String mRuntime;
            public ReleaseDates mReleaseDates[];
            public Ratings mRatings[];
            public String mSynopsis;
            public Posters mPosters[];
            public Cast mCast[];
            public AlternateIds mAltIds[];
            public Links mLinks[];

            public static final class ReleaseDates {
                public String mTheater;
            }

            public static final class Ratings {
                public String mCriticsRating;
                public String mCriticsScore;
                public String mAudienceRating;
                public String mAudienceScore;
            }

            public static final class Posters {
                public String mThumbnail;
                public String mProfile;
                public String mDetailed;
                public String mOriginal;
            }

            public static final class Cast {
                public String mName;
                public String mId;
            }

            public static final class AlternateIds {
                public String mIMDB;
            }

            public static final class Links {
                public String mSelf;
                public String mAlternate;
                public String mCast;
                public String mClips;
                public String mReviews;
                public String mSimilar;
            }
        }

        public static final class Links {
            public Links(JSONObject jsonObject) {
                // TODO Auto-generated constructor stub
            }

            public String mSelf;
            public String mNext;
            public String mAlternate;
        }
    }
}