package net.hobbitsoft.popularmovies.utils;

import android.arch.persistence.room.TypeConverter;

import java.util.ArrayList;
import java.util.Arrays;

/*
Class to specifically convert the Genre IDs to a String and vice versa
 */
public class ArrayConverter {
    //private static String TAG = ArrayConverter.class.getSimpleName();

    @TypeConverter
    public static String arrayToString(ArrayList<Integer> integers) {
        //Just return a list of comma separated numbers
        return Arrays.toString(integers.toArray())
                .replace("[", "").replace("]", "");
    }

    @TypeConverter
    public static ArrayList<Integer> stringToArray(String stringArray) {
        if (!stringArray.isEmpty()) {
            String[] stringIds = stringArray.split(",");
            ArrayList<Integer> intIds = new ArrayList<>();
            for (String id : stringIds) {
                intIds.add(Integer.parseInt(id.trim()));
            }
            return intIds;
        } else {
            return null;
        }
    }
}
