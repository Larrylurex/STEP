package ru.tomatapps.steps;


import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import java.util.HashMap;

/**
 * Created by LarryLurex on 02.09.2015.
 */
public class LoaderTransport implements LoaderManager.LoaderCallbacks<Cursor> {
    private Context context;
    private HashMap<String, Boolean> transport;

    public LoaderTransport(Context context, HashMap<String, Boolean> transport ){
        this.context = context;
        this.transport = transport;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new TransportCursorLoader(context);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        transport.clear();
        data.moveToPosition(-1);
        while(data.moveToNext()){
            String t = data.getString(data.getColumnIndex(StepsContract.COL_TRANSPORT));
            transport.put(t, true);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    static class TransportCursorLoader extends CursorLoader{
        private ContentResolverHelper helper;

        public TransportCursorLoader(Context context) {
            super(context);
            helper = new ContentResolverHelper(context);
        }

        @Override
        public Cursor loadInBackground() {
            return helper.getTransportList();
        }


    }
}
