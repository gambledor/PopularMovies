package it.globrutto.popularmovies.utility;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by giuseppelobrutto on 28/01/17.
 */
public class Utility {

    private static final int SIZE = 180;

    private static final int MIN_COLUMNS_NUMBER = 2;

    /**
     * Calculate the number of columns to desplay in a grid layout
     *
     * @param context The Activity context
     * @return The number of columns
     */
    public static int calculateNumberOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth =  displayMetrics.widthPixels / displayMetrics.density;
        int columns = (int) (dpWidth / SIZE);

        return columns >= 2 ? columns : MIN_COLUMNS_NUMBER;
    }
}
