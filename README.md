# Upcoming DVDs

Upcoming DVDs Retrieves new release dvds or movies from the Rotten Tomatoes web apis:

http://developer.rottentomatoes.com/docs/read/json/v10/Upcoming_DVDs
http://developer.rottentomatoes.com/docs/read/json/v10/Upcoming_Movies

### Parsing JSON


The application uses a simple JSONArrayAdapter to populate the initial list of results, this was primarily done as an experiment but i think it works rather well. The details screen is passed a POJO made by parsing a JSONObject from the Adapter.

### Libraries

I've used the [ImageLoader library](https://github.com/novoda/ImageLoader) from Novoda for fetching/displaying the Movie posters in the application. It's a very easy to use library and i'd highly recommend it, the project also uses the [RefreshActionItem-Native library](https://github.com/ManuelPeinado/RefreshActionItem-Native).

### Responsive layouts

The details screen shows how you should layout your content to match the screen. In landscape mode it displays the picture side by side with the information, making the most of the limited vertical space.

### Sharing

I wanted to take advantage of Androids great sharing functionality so i implemented the new ShareActionProvider it gives a really simple and user-friendly way of sharing content from your application.

### Play Store

I've now published the application to the Play Store. You can download it [here](https://play.google.com/store/apps/details?id=uk.co.dazcorp.android.upcomingdvds)

The code in this project is licensed under the Apache Software License 2.0.

Copyright (c) Daryl Gent
