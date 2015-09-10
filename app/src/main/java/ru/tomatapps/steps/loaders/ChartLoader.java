package ru.tomatapps.steps.loaders;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;

import java.sql.Date;
import java.util.ArrayList;

import ru.tomatapps.steps.database.ContentResolverHelper;

/**
 * Created by LarryLurex on 04.09.2015.
 */
public class ChartLoader extends CursorLoader {

    ContentResolverHelper helper;
    private Date[] dates;
    ArrayList<String> transport = new ArrayList<>();


    public ChartLoader(Context context, Date[] dates) {
        super(context);
        helper = new ContentResolverHelper(context);
        setSelection(null, dates);
    }

    public void setSelection(ArrayList<String> transport, Date[] dates){
        this.transport = transport;
        this.dates = dates;
    }

    @Override
    public Cursor loadInBackground() {
        return helper.getStatisticsChartData(transport, dates);
    }
}
