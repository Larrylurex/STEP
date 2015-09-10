package ru.tomatapps.steps.database;

import android.net.Uri;

/**
 * Created by LarryLurex (dmitry.borodin90@gmail.com) on 07.09.2015.
 */
public class StepsContract {
    public static final String T_SETTINGS = "settings";
    public static final String T_STATISTICS = "statistics";
    public static final String COL_ID = "_id";
    public static final String COL_TRANSPORT = "transport";
    public static final String COL_PRICE = "defaultPrice";
    public static final String COL_DEFAULT = "isDefault";
    public static final String COL_DATE = "date";
    public static final String COL_QUANTITY = "quantity";
    public static final String COL_EXPENSES = "expenses";


    public static final String AUTHORITY = "ru.tomatapps.steps";

    public static final String SETTINGS_PATH = T_SETTINGS;
    public static final int SETTINGS_CODE = 0;
    public static final String SETTINGS_PATH_LIMIT = T_SETTINGS + " limit";
    public static final int SETTINGS_LIMIT_CODE = 1;
    public static final Uri SETTINGS_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + SETTINGS_PATH);
    public static final Uri SETTINGS_LIMIT_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + SETTINGS_PATH_LIMIT);

    static final String SETTINGS_CONTENT_TYPE = "vnd.android.cursor.dir/vnd."
            + AUTHORITY + "." + SETTINGS_PATH;


    public static final String STATISTICS_PATH = T_STATISTICS;
    public static final int STATISTICS_CODE = 2;
    public static final String STATISTICS_LIST_PATH = T_STATISTICS + " list";
    public static final int STATISTICS_LIST_CODE = 3;
    public static final String STATISTICS_CHART_PATH = T_STATISTICS + " chart";
    public static final int STATISTICS_CHART_CODE = 4;
    public static final String STATISTICS_TRANSPORT_PATH = T_STATISTICS + " transport";
    public static final int STATISTICS_TRANSPORT_CODE = 5;
    public static final Uri STATISTICS_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + STATISTICS_PATH);
    public static final Uri STATISTICS_LIST_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + STATISTICS_LIST_PATH);
    public static final Uri STATISTICS_CHART_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + STATISTICS_CHART_PATH);
    public static final Uri STATISTICS_TRANSPORT_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + STATISTICS_TRANSPORT_PATH);

    static final String STATISTICS_CONTENT_TYPE = "vnd.android.cursor.dir/vnd."
            + AUTHORITY + "." + STATISTICS_PATH;

}
