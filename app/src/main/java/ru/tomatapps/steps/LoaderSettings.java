package ru.tomatapps.steps;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.widget.ResourceCursorAdapter;

/**
 * Created by LarryLurex on 26.08.2015.
 */
public class LoaderSettings implements LoaderManager.LoaderCallbacks<Cursor> {

    private ResourceCursorAdapter adapter;
    private Context context;
    private DBHelper helper;
    private boolean defaultOnly;
    private SettingsCursorLoader settingsCursorLoader;

    public LoaderSettings(Context ctx, ResourceCursorAdapter adapter, DBHelper helper, boolean defaultOnly){
        context = ctx;
        this.adapter = adapter;
        this.helper = helper;
        this.defaultOnly = defaultOnly;

    }

    public  void switchMoreLess(){
        setDefaultOnly(!defaultOnly);
    }

    public void setDefaultOnly(boolean defaultOnly) {
        this.defaultOnly = defaultOnly;
        if(settingsCursorLoader != null)
            settingsCursorLoader.setDefaultOnly(defaultOnly);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        settingsCursorLoader = new SettingsCursorLoader(context, helper);
        settingsCursorLoader.setDefaultOnly(defaultOnly);
        return settingsCursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        adapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    static class SettingsCursorLoader extends CursorLoader {
        private DBHelper helper;

        public void setDefaultOnly(boolean defaultOnly) {
            this.defaultOnly = defaultOnly;
        }

        private boolean defaultOnly;
        public SettingsCursorLoader(Context context, DBHelper helper) {
            super(context);
            this.helper = helper;
        }

        @Override
        public Cursor loadInBackground() {
            return  helper.getSettingsData(defaultOnly);
        }
    }
}
