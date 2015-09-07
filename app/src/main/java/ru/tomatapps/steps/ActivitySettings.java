package ru.tomatapps.steps;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;


public class ActivitySettings extends AppCompatActivity implements DialogCreateItem.CreateItemDialogListener{
    ListSettings listFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        listFragment = new ListSettings();
        FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
        trans.replace(R.id.settingsListContainer, listFragment);
        trans.commit();
    }

    public void onAddNewItemClick(View view){
        DialogCreateItem dialog = new DialogCreateItem();
        dialog.show(getSupportFragmentManager(), "createDialog");
    }

    @Override
    public void createItem(DBHelper.SettingsItem item) {
        Log.d("tag", "Activity createItem");
        listFragment.addNewItem(item);
    }
}