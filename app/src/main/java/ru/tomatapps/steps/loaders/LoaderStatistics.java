package ru.tomatapps.steps.loaders;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.widget.SimpleCursorAdapter;

import java.sql.Date;
import java.util.ArrayList;

import ru.tomatapps.steps.database.ContentResolverHelper;

/**
 * Created by LarryLurex on 02.09.2015.
 */
public class LoaderStatistics implements LoaderManager.LoaderCallbacks<Cursor> {

    private Context ctx;
    private SimpleCursorAdapter adapter;
    private StatisticsListLoader loader;
    private ArrayList<String> transport = new ArrayList<>();
    private Date[] dates;

    public LoaderStatistics(Context ctx, SimpleCursorAdapter adapter, ArrayList<String> transport, Date[] dates) {
        this.ctx = ctx;
        this.adapter = adapter;

        this.transport = transport;
        this.dates = dates;
    }

    public void setTransport(ArrayList<String> transport) {
        this.transport = transport;
        if(loader != null)
            loader.setSelection(transport, dates);
    }

    public void setDates(Date[] dates){
        this.dates = dates;
        if(loader != null)
            loader.setSelection(transport, dates);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        loader = new StatisticsListLoader(ctx);
        loader.setSelection(transport, dates);
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }


    static class StatisticsListLoader extends CursorLoader{
        ContentResolverHelper helper;
        ArrayList<String> transport = new ArrayList<>();
        Date[] dates;

        public StatisticsListLoader(Context context) {
            super(context);
            this.helper = new ContentResolverHelper(context);
        }

        public void setSelection(ArrayList<String> transport, Date[] dates){
            this.transport = transport;
            this.dates = dates;
        }

        @Override
        public Cursor loadInBackground() {
            return helper.getStatisticsListData(transport, dates);
        }
    }

}
