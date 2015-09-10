package ru.tomatapps.steps.database;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.sql.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by LarryLurex on 25.08.2015.
 */
public class ContentResolverHelper {

    private ContentResolver resolver;

    public ContentResolverHelper(Context ctx){
        resolver = ctx.getApplicationContext().getContentResolver();
    }
    //SETTINGS METHODS

    public Cursor getSettingsData(boolean defaultOnly){
        Cursor cursor;
        String selection = null;
        String[] selectionArgs = null;
        String orderBY = StepsContract.COL_DEFAULT + " DESC, " + StepsContract.COL_ID + " ASC";
        if(defaultOnly) {
            selection = StepsContract.COL_DEFAULT + " = ?";
            selectionArgs = new String[]{"1"};
        }
        cursor = resolver.query(StepsContract.SETTINGS_CONTENT_URI, null, selection, selectionArgs, orderBY);
        return cursor;
    }

    public boolean addSettingsItem(SettingsItem item){
        if(checkExist(item.transport))
            return false;
        ContentValues cv = new ContentValues();
        cv.put(StepsContract.COL_TRANSPORT, item.getTransport());
        cv.put(StepsContract.COL_PRICE, item.getPrice());
        cv.put(StepsContract.COL_DEFAULT, item.isDefault());
        resolver.insert(StepsContract.SETTINGS_CONTENT_URI, cv);

        return true;
    }

    public void editSettingsItemIsDefault(boolean isDefault, Long id){
        ContentValues cv = new ContentValues();
        cv.put(StepsContract.COL_DEFAULT, isDefault);
        String selection = "_id = ?";
        String[] selectionArgs = new String[]{id.toString()};
        resolver.update(StepsContract.SETTINGS_CONTENT_URI, cv, selection, selectionArgs);

    }

    public void editSettingsItemPrice(Long id, Float newPrice) {

        ContentValues cv = new ContentValues();
        cv.put(StepsContract.COL_PRICE, newPrice.toString());
        String selection = "_id = ?";
        String[] selectionArgs = new String[]{id.toString()};
        resolver.update(StepsContract.SETTINGS_CONTENT_URI, cv, selection, selectionArgs);

    }

    public void deleteSettingsItem(Long id) {
        String selection = "_id = ?";
        String[] selectionArgs = new String[]{id.toString()};
        resolver.delete(StepsContract.SETTINGS_CONTENT_URI, selection, selectionArgs);
    }

    private boolean checkExist(String transport){
        Cursor cursor;
        String selection = StepsContract.COL_TRANSPORT + " = ?";
        String[] selectionArgs = new String[]{transport};
        cursor = resolver.query(StepsContract.SETTINGS_LIMIT_CONTENT_URI, null, selection, selectionArgs, null);
        int count = cursor.getCount();
        cursor.close();
        return count > 0;
    }

    //STATISTICS METHODS

    public Cursor getStatisticsListData(List<String> transport, Date[] dates) {
        Cursor cursor;
        String[] columns = new  String[]{"SUM(" + StepsContract.COL_ID + ") AS " + StepsContract.COL_ID,
                StepsContract.COL_TRANSPORT,
                "Count("+ StepsContract.COL_TRANSPORT+") AS " + StepsContract.COL_QUANTITY,
                "SUM(" + StepsContract.COL_PRICE +") AS " + StepsContract.COL_EXPENSES};
        String selection = null;
        if(transport != null && !transport.isEmpty()){
            selection = createTransportSelection(transport);
        }
        String[] selectionArgs = null;
        if(dates != null){
            if(selection == null)
                selection = "";
            else
                selection += " AND ";
            selection += StepsContract.COL_DATE + " between ? AND ? ";
            selectionArgs = new String[]{""+ dates[0].getTime() ,"" + dates[1].getTime()};
        }

        cursor = resolver.query(StepsContract.STATISTICS_LIST_CONTENT_URI, columns, selection, selectionArgs, null);

        return cursor;
    }

    public Cursor getStatisticsChartData(List<String> transport, Date[] dates) {

        Cursor cursor;
        String[] columns = new  String[]{StepsContract.COL_TRANSPORT, StepsContract.COL_DATE,
                "Count("+ StepsContract.COL_TRANSPORT+") AS " + StepsContract.COL_QUANTITY};
        String selection = null;
        if(transport != null && !transport.isEmpty()){
            selection = createTransportSelection(transport);
        }
        String[] selectionArgs = null;
        if(dates != null){
            if(selection == null)
                selection = "";
            else
                selection += " AND ";
            selection += StepsContract.COL_DATE + " between ? AND ? ";
            selectionArgs = new String[]{""+ dates[0].getTime() ,"" + dates[1].getTime()};
        }
        String order = StepsContract.COL_DATE + " ASC ";
        cursor = resolver.query(StepsContract.STATISTICS_CHART_CONTENT_URI, columns, selection, selectionArgs, order);

        return cursor;
    }

    private String createTransportSelection(List<String> transport){
        StringBuilder selection = new StringBuilder();

        selection.append(StepsContract.COL_TRANSPORT);
        selection.append(" In( ");
        for (String t : transport) {
            selection.append("'");
            selection.append(t);
            selection.append("',");
        }
        selection.deleteCharAt(selection.length() - 1);

        selection.append(") ");
        return selection.toString();
    }

    public Cursor getTransportList(){
        Cursor cursor;
        String[] columns = {StepsContract.COL_TRANSPORT};
        cursor = resolver.query(StepsContract.STATISTICS_TRANSPORT_CONTENT_URI, columns, null, null, null);
        return cursor;
    }

    public void truncateTable(Uri table){
        resolver.delete(table, null, null);
    }


    public void insertTestStatisticsData(){
        createStatisticsCV(2015, Calendar.JULY, 17, "subway", 31, 2);
        createStatisticsCV(2015, Calendar.JULY, 21, "subway", 31, 2);
        createStatisticsCV(2015, Calendar.JULY, 21, "bus", 28, 2);
        createStatisticsCV(2015, Calendar.JULY, 22, "subway", 31, 2);
        createStatisticsCV(2015, Calendar.JULY, 23, "subway", 31, 4);
        createStatisticsCV(2015, Calendar.JULY, 24, "subway", 31, 1);
        createStatisticsCV(2015, Calendar.JULY, 24, "bus", 28, 2);
        createStatisticsCV(2015, Calendar.JULY, 29, "tram", 28, 1);
        createStatisticsCV(2015, Calendar.JULY, 30, "subway", 31, 2);
        createStatisticsCV(2015, Calendar.JULY, 31, "tram", 28, 2);
        createStatisticsCV(2015, Calendar.AUGUST, 6, "bus", 28, 1);
        createStatisticsCV(2015, Calendar.AUGUST, 6, "subway", 31, 2);
        createStatisticsCV(2015, Calendar.AUGUST, 10, "subway", 31, 2);
        createStatisticsCV(2015, Calendar.AUGUST, 10, "bus", 28, 2);
        createStatisticsCV(2015, Calendar.AUGUST, 27, "subway", 31, 2);
        createStatisticsCV(2015, Calendar.SEPTEMBER, 1, "subway", 31, 4);
        createStatisticsCV(2015, Calendar.SEPTEMBER, 2, "bus", 31, 2);
        //showStatistics();
    }

    private void createStatisticsCV(int year, int month, int day, String transport, float price, int number){
        ContentValues cv = new ContentValues();
        GregorianCalendar c = new GregorianCalendar();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);
        cv.put(StepsContract.COL_TRANSPORT, transport);
        cv.put(StepsContract.COL_DATE, c.getTimeInMillis());
        cv.put(StepsContract.COL_PRICE, price);
        for(int i=0; i< number; i++)
            resolver.insert(StepsContract.STATISTICS_CONTENT_URI, cv);
    }

    public Date[] getDatesRange(){
        final String MIN = "min";
        final String MAX = "max";
        Date[] dates = new Date[2];
        String[] columns = new String[]{"MIN("+ StepsContract.COL_DATE+") AS " + MIN, "MAX("+ StepsContract.COL_DATE+") AS " + MAX};

        Cursor cursor = resolver.query(StepsContract.STATISTICS_CONTENT_URI, columns, null, null, null);
        cursor.moveToFirst();
        long minTime = cursor.getLong(cursor.getColumnIndex(MIN));
        long maxTime = cursor.getLong(cursor.getColumnIndex(MAX));
        dates[0] = (minTime == 0)? new Date(System.currentTimeMillis()) : new Date(minTime);
        dates[1] = (maxTime == 0)? new Date(System.currentTimeMillis()) : new Date(maxTime);
        cursor.close();

        return dates;
    }

    public static class SettingsItem{
        private String transport;
        private float price;
        private boolean isDefault;

        public SettingsItem(String transport, float price, boolean isDefault){
            this.transport = transport.toLowerCase();
            this.price = price;
            this.isDefault = isDefault;
        }


        public String getTransport() {
            return transport;
        }

        public float getPrice() {
            return price;
        }

        public boolean isDefault() {
            return isDefault;
        }

    }
}
