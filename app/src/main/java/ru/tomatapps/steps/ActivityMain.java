package ru.tomatapps.steps;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class ActivityMain extends AppCompatActivity{
    private DBHelper helper;
    private LoaderSettings loaderSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        helper = new DBHelper(this);

        if(isFirstRun()){
            setDefaultSettings();
        }


        setTestStatisticsData(); // DELETE!!!!!!!!!!!!!!!!!


        String[] from = {DBHelper.COL_TRANSPORT, DBHelper.COL_PRICE};
        int[] to = {R.id.tvTransport, R.id.etPrice};
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.mainlist_item, null, from, to, 0);

        ListView list = (ListView)findViewById(R.id.mainList);
        View footer = getLayoutInflater().inflate(R.layout.mainlist_footer, null, false);
        list.addFooterView(footer);
        list.setFooterDividersEnabled(false);
        list.setAdapter(adapter);

        loaderSettings = new LoaderSettings(this, adapter, helper, true);
        getSupportLoaderManager().initLoader(0, null, loaderSettings);
    }

    @Override
    protected void onStart() {
        super.onStart();
        loaderSettings.setDefaultOnly(true);
        getSupportLoaderManager().getLoader(0).forceLoad();
    }

    private boolean isFirstRun(){
        SharedPreferences sPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        boolean isFirstRun = sPref.getBoolean("isFirstRun", true);
        if(isFirstRun){
            SharedPreferences.Editor editor = sPref.edit();
            editor.putBoolean("isFirstRun", false);
            editor.commit();
        }
        return isFirstRun;
    }

    private  void clearPrefs(){
        SharedPreferences sPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sPref.edit();
        editor.clear().commit();
    }

    private void setDefaultSettings(){
        helper.addSettingsItem(new DBHelper.SettingsItem("Автобус", 28.00f, true));
        helper.addSettingsItem(new DBHelper.SettingsItem("Метро", 31.00f, true));
        helper.addSettingsItem(new DBHelper.SettingsItem("Трамвай", 28.00f, true));
        helper.addSettingsItem(new DBHelper.SettingsItem("Такси", 150.00f, false));
    }

    private void setTestStatisticsData(){
        helper.truncateTable(DBHelper.T_STATISTICS);
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
