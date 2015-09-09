package ru.tomatapps.steps;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class ActivityMain extends AppCompatActivity{
    private LoaderSettings loaderSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("myTag", "Activity onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(isFirstRun()){
            setDefaultSettings();
        }

        String[] from = {StepsContract.COL_TRANSPORT, StepsContract.COL_PRICE};
        int[] to = {R.id.tvTransport, R.id.etPrice};
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.mainlist_item, null, from, to, 0);

        ListView list = (ListView)findViewById(R.id.mainList);
        View footer = getLayoutInflater().inflate(R.layout.mainlist_footer, null, false);
        list.addFooterView(footer);
        list.setFooterDividersEnabled(false);
        list.setAdapter(adapter);

        loaderSettings = (LoaderSettings) getLastCustomNonConfigurationInstance();
        if(loaderSettings ==null )
            loaderSettings = new LoaderSettings(this, adapter, true);
        else
            loaderSettings.setAdapter(adapter);

        getSupportLoaderManager().initLoader(0, null, loaderSettings);
        Log.d("myTag", "Loader " + getSupportLoaderManager().getLoader(0).hashCode());

    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return loaderSettings;
    }


    private boolean isFirstRun(){
        SharedPreferences sPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        boolean isFirstRun = sPref.getBoolean("isFirstRun", true);
        if(isFirstRun){
            SharedPreferences.Editor editor = sPref.edit();
            editor.putBoolean("isFirstRun", false);
            editor.apply();
        }
        return isFirstRun;
    }

    private  void clearPrefs(){
        SharedPreferences sPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sPref.edit();
        editor.clear().apply();
    }

    private void setDefaultSettings(){
        ContentResolverHelper helper = new ContentResolverHelper(this);
        helper.addSettingsItem(new ContentResolverHelper.SettingsItem("Автобус", 28.00f, true));
        helper.addSettingsItem(new ContentResolverHelper.SettingsItem("Метро", 31.00f, true));
        helper.addSettingsItem(new ContentResolverHelper.SettingsItem("Трамвай", 28.00f, true));
        helper.addSettingsItem(new ContentResolverHelper.SettingsItem("Такси", 150.00f, false));
    }

    private void setTestStatisticsData(){
        ContentResolverHelper helper = new ContentResolverHelper(this);
        helper.truncateTable(StepsContract.STATISTICS_CONTENT_URI);
        helper.insertTestStatisticsData();
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.btnMore:
                loaderSettings.switchMoreLess();
                getSupportLoaderManager().getLoader(0).forceLoad();
                break;
            case R.id.btnSettings:
                Intent intent = new Intent(this, ActivitySettings.class);
                startActivity(intent);
                break;
            case R.id.btnStatistics:
                intent = new Intent(this, ActivityStatistics.class);
                startActivity(intent);
                break;
        }
    }


}
