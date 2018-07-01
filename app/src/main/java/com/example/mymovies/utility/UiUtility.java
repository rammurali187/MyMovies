package com.example.mymovies.utility;


import android.text.TextUtils;
import android.util.Log;


import com.example.mymovies.model.Genre;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public final class UiUtility {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

    private UiUtility() {
        throw new AssertionError("No instances.");
    }

    public static String getDisplayReleaseDate(String releaseDate) {
        if (TextUtils.isEmpty(releaseDate)) return "";
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(DATE_FORMAT.parse(releaseDate));
            return calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH) + " " + calendar.get(Calendar.YEAR);
        } catch (ParseException e) {
            Log.e("Failed to parse date.",e.getMessage().toString());
            return "";
        }
    }

    public static List<Genre> genrelist() {
        List<Genre> lst = new ArrayList<Genre>();
        lst.add(new Genre(28,"Action"));
        lst.add(new Genre(12,"Adventure"));
        lst.add(new Genre(16,"Animation"));
        lst.add(new Genre(35,"Comedy"));
        lst.add(new Genre(80,"Crime"));
        lst.add(new Genre(99,"Documentary"));
        lst.add(new Genre(18,"Drama"));
        lst.add(new Genre(10751,"Family"));
        lst.add(new Genre(14,"Fantasy"));
        lst.add(new Genre(10765,"Foreign"));
        lst.add(new Genre(36,"History"));
        lst.add(new Genre(27,"Horror"));
        lst.add(new Genre(10402,"Music"));
        lst.add(new Genre(9648,"Mystery"));
        lst.add(new Genre(10749,"Romance"));
        lst.add(new Genre(878,"Science Fiction"));
        lst.add(new Genre(10770,"TV Movie"));
        lst.add(new Genre(53,"Thriller"));
        lst.add(new Genre(10752,"War"));
        lst.add(new Genre(37,"Western"));

        return  lst;
    }

}
