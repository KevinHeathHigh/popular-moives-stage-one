/*
 * Copyright (c) 2018.  HobbitSoft - Kevin Heath High
 */

package net.hobbitsoft.popularmoviesstageone.utils;

import android.content.res.Resources;

import net.hobbitsoft.popularmoviesstageone.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    //Constants to represent data patterns
    private static final String FULL_DATE_PATTERN = "yyyy-MM-dd";
    private static final String FULL_YEAR_PATTERN = "yyyy";

    /*
    TODO: Currently only supporting US English - At some point should implement phone based Locale
    Which also means getting Local based results from The Movie DB - if possible
     */
    public static String dateToYear(String releaseDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(FULL_DATE_PATTERN, Locale.US);
        SimpleDateFormat yearFormat = new SimpleDateFormat(FULL_YEAR_PATTERN, Locale.US);
        try {
            Date date = dateFormat.parse(releaseDate);
            return yearFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            //If something broke, then we will display "Unknown"
            return Resources.getSystem().getString(R.string.er_unknown_date);
        }
    }
}
