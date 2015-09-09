package ru.tomatapps.steps;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import static ru.tomatapps.steps.StepsContract.*;

/**
 * Created by LarryLurex (dmitry.borodin90@gmail.com) on 07.09.2015.
 */
public class StepsContentProvider extends ContentProvider {
    private DbHelper helper;
    private UriMatcher uriMatcher;


    @Override
    public boolean onCreate() {
        helper = new DbHelper(getContext());
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, SETTINGS_PATH, SETTINGS_CODE);
        uriMatcher.addURI(AUTHORITY, SETTINGS_PATH_LIMIT, SETTINGS_LIMIT_CODE);
        uriMatcher.addURI(AUTHORITY, STATISTICS_PATH, STATISTICS_CODE);
        uriMatcher.addURI(AUTHORITY, STATISTICS_LIST_PATH, STATISTICS_LIST_CODE);
        uriMatcher.addURI(AUTHORITY, STATISTICS_CHART_PATH, STATISTICS_CHART_CODE);
        uriMatcher.addURI(AUTHORITY, STATISTICS_TRANSPORT_PATH, STATISTICS_TRANSPORT_CODE);
        return false;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)){
            case SETTINGS_CODE:
            case SETTINGS_LIMIT_CODE:
                return SETTINGS_CONTENT_TYPE;
            case STATISTICS_CODE:
            case STATISTICS_CHART_CODE:
            case STATISTICS_LIST_CODE:
            case STATISTICS_TRANSPORT_CODE:
                return STATISTICS_CONTENT_TYPE;
        }
        return null;
    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        String table;
        String groupBy = null;
        String limit = null;
        switch (uriMatcher.match(uri)){
            case SETTINGS_CODE:
                table = T_SETTINGS;
                break;
            case SETTINGS_LIMIT_CODE:
                table = T_SETTINGS;
                limit = "1";
                break;
            case STATISTICS_CODE:
                table = T_STATISTICS;
                break;
            case STATISTICS_CHART_CODE:
                table = T_STATISTICS;
                groupBy = COL_TRANSPORT + " , " + COL_DATE;
                break;
            case STATISTICS_LIST_CODE:
                table = T_STATISTICS;
                groupBy = COL_TRANSPORT;
                break;
            case STATISTICS_TRANSPORT_CODE:
                table = T_STATISTICS;
                projection[0] = "DISTINCT " + projection[0];
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }

        SQLiteDatabase db = helper.getReadableDatabase();
        return db.query(table, projection, selection, selectionArgs, groupBy, null, sortOrder, limit);
    }


    @Override
    public Uri insert(Uri uri, ContentValues values) {
        String table;
        switch (uriMatcher.match(uri)){
            case SETTINGS_CODE:
                table = T_SETTINGS;
                break;
            case STATISTICS_CODE:
                table = T_STATISTICS;
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        SQLiteDatabase db = helper.getWritableDatabase();
        long inserted = db.insert(table, null, values);
        return ContentUris.withAppendedId(uri, inserted);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        String table;
        switch (uriMatcher.match(uri)){
            case SETTINGS_CODE:
                table = T_SETTINGS;
                break;
            case STATISTICS_CODE:
                table = T_STATISTICS;
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        SQLiteDatabase db = helper.getWritableDatabase();

        return db.delete(table, selection, selectionArgs);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        String table;
        switch (uriMatcher.match(uri)){
            case SETTINGS_CODE:
                table = T_SETTINGS;
                break;
            case STATISTICS_CODE:
                table = T_STATISTICS;
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        SQLiteDatabase db = helper.getWritableDatabase();

        return db.update(table, values, selection, selectionArgs);
    }


    private class DbHelper extends SQLiteOpenHelper {
        public DbHelper(Context ctx){
            super(ctx, "stepsAppDB", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + T_SETTINGS + " ( " +
                    COL_ID + " integer primary key autoincrement, " +
                    COL_TRANSPORT + " text," +
                    COL_PRICE + " real, " +
                    COL_DEFAULT + " integer );");
            db.execSQL("CREATE TABLE " + T_STATISTICS + " ( " +
                    COL_ID + " integer primary key autoincrement, " +
                    COL_TRANSPORT + " text," +
                    COL_PRICE + " real, " +
                    COL_DATE + " integer NOT NULL DEFAULT (strftime('%s', 'now')));");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
