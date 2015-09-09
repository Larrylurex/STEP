package ru.tomatapps.steps;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.widget.ResourceCursorAdapter;

/**
 * Created by LarryLurex on 26.08.2015.
 */
public class LoaderSettings implements LoaderManager.LoaderCallbacks<Cursor> {

    private ResourceCursorAdapter adapter;
    private Context context;
    private boolean defaultOnly;
    private SettingsCursorLoader settingsCursorLoader;

    public LoaderSettings(Context ctx, ResourceCursorAdapter adapter, boolean defaultOnly){
        context = ctx;
        setAdapter(adapter);
        this.defaultOnly = defaultOnly;
    }

    public void setAdapter(ResourceCursorAdapter adapter){
        this.adapter = adapter;
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
        settingsCursorLoader = new SettingsCursorLoader(context);
        settingsCursorLoader.setDefaultOnly(defaultOnly);
        return settingsCursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        Log.d("myTag", "onFinished Callback " + this.hashCode() + " Loader " + cursorLoader.hashCode());
        adapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    static class SettingsCursorLoader extends CursorLoader {
        private ContentResolverHelper helper;

        public void setDefaultOnly(boolean defaultOnly) {
            this.defaultOnly = defaultOnly;
        }

        private boolean defaultOnly;
        public SettingsCursorLoader(Context context) {
            super(context);
            helper = new ContentResolverHelper(context);
        }

        @Override
        public Cursor loadInBackground() {
            return  helper.getSettingsData(defaultOnly);
        }
    }
}
