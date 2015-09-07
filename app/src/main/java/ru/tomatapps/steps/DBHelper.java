package ru.tomatapps.steps;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by LarryLurex on 25.08.2015.
 */
public class DBHelper extends SQLiteOpenHelper {
    public static final String T_SETTINGS = "settings";
    public static final String T_STATISTICS = "statistics";
    public static final String COL_ID = "_id";
    public static final String COL_TRANSPORT = "transport";
    public static final String COL_PRICE = "defaultPrice";
    public static final String COL_DEFAULT = "isDefault";
    public static final String COL_DATE = "date";
    public static final String COL_QUANTITY = "quantity";
    public static final String COL_EXPENSES = "expenses";

    public DBHelper(Context ctx){
        super(ctx, "stepsAppDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + T_SETTINGS + " ( " +
                    COL_ID + " integer primary key autoincrement, " +
                    COL_TRANSPORT  + " text," +
                    COL_PRICE + " real, " +
                    COL_DEFAULT + " integer );");
        db.execSQL("CREATE TABLE " + T_STATISTICS + " ( " +
                COL_ID + " integer primary key autoincrement, " +
                COL_TRANSPORT  + " text," +
                COL_PRICE + " real, " +
                COL_DATE + " integer NOT NULL DEFAULT (strftime('%s', 'now')));");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    //SETTINGS METHODS

    public Cursor getSettingsData(boolean defaultOnly){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor;
        String selection = null;
        String[] selectionArgs = null;
        String orderBY = COL_DEFAULT + " DESC, " + COL_ID + " ASC";
        if(defaultOnly) {
            selection = COL_DEFAULT + " = ?";
            selectionArgs = new String[]{"1"};
        }
        cursor = db.query(T_SETTINGS, null, selection, selectionArgs, null, null, orderBY);
        return cursor;
    }

    public boolean addSettingsItem(SettingsItem item){
        if(checkExist(item.transport))
            return false;
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_TRANSPORT, item.getTransport());
        cv.put(COL_PRICE, item.getPrice());
        cv.put(COL_DEFAULT, item.isDefault());
        db.insert(T_SETTINGS, null, cv);
        close();
        return true;
    }

    public void editSettingsItemIsDefault(boolean isDefault, Long id){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_DEFAULT, isDefault);
        String where = "_id = ?";
        String[] whereArgs = new String[]{id.toString()};
        db.update(T_SETTINGS, cv, where, whereArgs);
        close();
    }

    public void editSettingsItemPrice(Long id, Float newPrice) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_PRICE, newPrice.toString());
        String where = "_id = ?";
        String[] whereArgs = new String[]{id.toString()};
        db.update(T_SETTINGS, cv, where, whereArgs);
        close();
    }

    public void deleteSettingsItem(Long id) {
        SQLiteDatabase db = getWritableDatabase();
        String where = "_id = ?";
        String[] whereArgs = new String[]{id.toString()};
        db.delete(T_SETTINGS, where, whereArgs);
        close();
    }

    private boolean checkExist(String transport){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor;
        String selection = COL_TRANSPORT + " = ?";
        String[] selectionArgs = new String[]{transport};
        String limit = "1";
        cursor = db.query(T_SETTINGS, null, selection, selectionArgs, null, null, null, limit);
        int count = cursor.getCount();
        cursor.close();
        return count > 0;
    }

    //STATISTICS METHODS

    public Cursor getStatisticsListData(List<String> transport, Date[] dates) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor;
        String[] columns = new  String[]{"SUM(" +COL_ID +") AS " + COL_ID,COL_TRANSPORT, "Count("+ COL_TRANSPORT+") AS " + COL_QUANTITY, "SUM(" +COL_PRICE +") AS " + COL_EXPENSES};
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
            selection += COL_DATE + " between ? AND ? ";
            selectionArgs = new String[]{""+ dates[0].getTime() ,"" + dates[1].getTime()};
            GregorianCalendar c = new GregorianCalendar();
            c.setTime(dates[0]);
            Log.d("tag", "date0 " + c.get(Calendar.YEAR) +":"+ c.get(Calendar.MONTH) + ":" + c.get(Calendar.DAY_OF_MONTH));
            c.setTime(dates[1]);
            Log.d("tag", "date1 " + c.get(Calendar.YEAR) +":"+ c.get(Calendar.MONTH) + ":" + c.get(Calendar.DAY_OF_MONTH));
        }

        String groupBy = COL_TRANSPORT;
        String having = COL_QUANTITY + " > 0";

        cursor = db.query(T_STATISTICS, columns, selection, selectionArgs, groupBy, having, null);
//        Log.d("tag", "Columns " + columns);
//        Log.d("tag", "selection " + selection);
//        Log.d("tag", "args " + selectionArgs);
//        Log.d("tag", "group " + groupBy);
//        Log.d("tag", "having " + having);
//        Log.d("tag", new Integer(cursor.getCount()).toString());
        return cursor;
    }

    public Cursor getStatisticsChartData(List<String> transport, Date[] dates) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor;
        String[] columns = new  String[]{COL_TRANSPORT, COL_DATE, "Count("+ COL_TRANSPORT+") AS " + COL_QUANTITY};
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
            selection += COL_DATE + " between ? AND ? ";
            selectionArgs = new String[]{""+ dates[0].getTime() ,"" + dates[1].getTime()};
            GregorianCalendar c = new GregorianCalendar();
            c.setTime(dates[0]);
            Log.d("tag", "date0 " + c.get(Calendar.YEAR) +":"+ c.get(Calendar.MONTH) + ":" + c.get(Calendar.DAY_OF_MONTH));
            c.setTime(dates[1]);
            Log.d("tag", "date1 " + c.get(Calendar.YEAR) +":"+ c.get(Calendar.MONTH) + ":" + c.get(Calendar.DAY_OF_MONTH));
        }

        String groupBy = COL_TRANSPORT + " , " + COL_DATE;


        cursor = db.query(T_STATISTICS, columns, selection, selectionArgs, groupBy, null, null);
//        Log.d("tag", "Columns " + columns);
//        Log.d("tag", "selection " + selection);
//        Log.d("tag", "args " + selectionArgs);
//        Log.d("tag", "group " + groupBy);
//        Log.d("tag", "having " + having);
//        Log.d("tag", new Integer(cursor.getCount()).toString());
        return cursor;
    }

    private String createTransportSelection(List<String> transport){
        StringBuilder selection = new StringBuilder();

        selection.append(COL_TRANSPORT);
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
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor;
        String[] columns = {"distinct " + COL_TRANSPORT};
        cursor = db.query(T_STATISTICS, columns, null, null, null, null, null);
        return cursor;
    }

    public void truncateTable(String table){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(table, null, null);
    }
    public void insertTestStatisticsData(){
        SQLiteDatabase db = getWritableDatabase();
        createStatisticsCV(db, 2015, Calendar.JULY, 17, "subway", 31, 2);
        createStatisticsCV(db, 2015, Calendar.JULY, 21, "subway", 31, 2);
        createStatisticsCV(db, 2015, Calendar.JULY, 21, "bus", 28, 2);
        createStatisticsCV(db, 2015, Calendar.JULY, 22, "subway", 31, 2);
        createStatisticsCV(db, 2015, Calendar.JULY, 23, "subway", 31, 4);
        createStatisticsCV(db, 2015, Calendar.JULY, 24, "subway", 31, 1);
        createStatisticsCV(db, 2015, Calendar.JULY, 24, "bus", 28, 2);
        createStatisticsCV(db, 2015, Calendar.JULY, 29, "tram", 28, 1);
        createStatisticsCV(db, 2015, Calendar.JULY, 30, "subway", 31, 2);
        createStatisticsCV(db, 2015, Calendar.JULY, 31, "tram", 28, 2);
        createStatisticsCV(db, 2015, Calendar.AUGUST, 6, "bus", 28, 1);
        createStatisticsCV(db, 2015, Calendar.AUGUST, 6, "subway", 31, 2);
        createStatisticsCV(db, 2015, Calendar.AUGUST, 10, "subway", 31, 2);
        createStatisticsCV(db, 2015, Calendar.AUGUST, 10, "bus", 28, 2);
        createStatisticsCV(db, 2015, Calendar.AUGUST, 27, "subway", 31, 2);
        createStatisticsCV(db, 2015, Calendar.SEPTEMBER, 1, "subway", 31, 4);
        createStatisticsCV(db, 2015, Calendar.SEPTEMBER, 2, "bus", 31, 2);
        close();
        //showStatistics();
    }

    private void createStatisticsCV(SQLiteDatabase db,int year, int month, int day, String transport, float price, int number){
        ContentValues cv = new ContentValues();
        GregorianCalendar c = new GregorianCalendar();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);
        cv.put(COL_TRANSPORT, transport);
        cv.put(COL_DATE, c.getTimeInMillis());
        cv.put(COL_PRICE, price);
        for(int i=0; i< number; i++)
            db.insert(T_STATISTICS, null, cv);
    }

//    public void showStatistics(){
//        SQLiteDatabase db = getReadableDatabase();
//        Cursor cursor = db.query(T_STATISTICS, null, null, null,null,null, null);
//        while(cursor.moveToNext()){
//            GregorianCalendar c = new GregorianCalendar();
//            c.setTime(new Date(cursor.getLong(cursor.getColumnIndex(COL_DATE))));
//            Log.d("tag", "date0 " + c.get(Calendar.YEAR) +":"+ c.get(Calendar.MONTH) + ":" + c.get(Calendar.DAY_OF_MONTH));
//        }
//    }

    public Date[] getDatesRange(){
        final String MIN = "min";
        final String MAX = "max";
        Date[] dates = new Date[2];
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(T_STATISTICS, new String[]{"MIN("+ COL_DATE+") AS " + MIN, "MAX("+ COL_DATE+") AS " + MAX}, null, null, null, null, null);
        cursor.moveToFirst();
        long minTime = cursor.getLong(cursor.getColumnIndex(MIN));
        long maxTime = cursor.getLong(cursor.getColumnIndex(MAX));
        dates[0] = (minTime == 0)? new Date(System.currentTimeMillis()) : new Date(minTime);
        dates[1] = (maxTime == 0)? new Date(System.currentTimeMillis()) : new Date(maxTime);
        cursor.close();
        return dates;
    }

    static class SettingsItem{
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
