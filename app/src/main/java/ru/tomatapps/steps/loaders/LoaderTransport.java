package ru.tomatapps.steps.loaders;


import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import java.util.ArrayList;

import ru.tomatapps.steps.others.ColorGenerator;
import ru.tomatapps.steps.database.ContentResolverHelper;
import ru.tomatapps.steps.dialogs.DialogTransport;
import ru.tomatapps.steps.database.StepsContract;
import ru.tomatapps.steps.others.TransportItem;

/**
 * Created by LarryLurex on 02.09.2015.
 */
public class LoaderTransport implements LoaderManager.LoaderCallbacks<Cursor> {
    private Context context;
    private ArrayList<TransportItem> transport;

    public void setListener(DialogTransport.OnTransportListListener listener) {
        this.mListener = listener;
    }

    DialogTransport.OnTransportListListener mListener;

    public LoaderTransport(Context context, ArrayList<TransportItem> transport ){
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
        ColorGenerator colors = new ColorGenerator();
        data.moveToPosition(-1);
        while(data.moveToNext()){
            String t = data.getString(data.getColumnIndex(StepsContract.COL_TRANSPORT));
            transport.add(new TransportItem(t, colors.next(),true));
        }
        if(mListener != null)
            mListener.onTransportListChange();
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
