package ru.tomatapps.steps;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListSettings extends Fragment implements MyCursorAdapter.CheckItemListener,
        DialogDeleteItem.DeleteItemDialogListener,
        DialogEditItem.EditItemDialogListener,
        DialogItemExists.ItemExistDialogListener{
    private DBHelper helper;
    private ListView list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings_list, container, false);

        helper = new DBHelper(getActivity());

        String[] from = {DBHelper.COL_TRANSPORT, DBHelper.COL_PRICE, DBHelper.COL_DEFAULT};
        int[] to = {R.id.tvTransport, R.id.tvPrice, R.id.cbIsDefault};

        MyCursorAdapter adapter = new MyCursorAdapter(getActivity(), R.layout.settingslist_item, null, from, to, 0);
        adapter.setMyCursorEventListener(this);

        list = (ListView)view.findViewById(R.id.settingsList);
        list.setAdapter(adapter);

        LoaderSettings loaderSettings = new LoaderSettings(getActivity(), adapter, helper, false);

        getActivity().getSupportLoaderManager().initLoader(1, null, loaderSettings);
        registerForContextMenu(list);
        return view;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
        Cursor cursor = (Cursor)list.getItemAtPosition(info.position);

        String transport = cursor.getString(cursor.getColumnIndex(DBHelper.COL_TRANSPORT));
        menu.setHeaderTitle(transport);
        menu.add(0, 0, 0,"Edit");
        menu.add(0, 1, 1, "Delete");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Cursor cursor = (Cursor)list.getItemAtPosition(info.position);
        long id = cursor.getLong(cursor.getColumnIndex(DBHelper.COL_ID));
        String transport = cursor.getString(cursor.getColumnIndex(DBHelper.COL_TRANSPORT));
        float price = cursor.getFloat(cursor.getColumnIndex(DBHelper.COL_PRICE));
        switch(item.getItemId()){
            case 0:
                showEditDialog(id, transport, price);
                break;
            case 1:
                showDeleteDialog(id, transport);
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onCheck(boolean isChecked, long id) {
        helper.editSettingsItemIsDefault(isChecked, id);
    }

    public void addNewItem(DBHelper.SettingsItem item) {
        if(!helper.addSettingsItem(item)){
            showItemExistDialog(item.getTransport());
        }
        getActivity().getSupportLoaderManager().getLoader(1).forceLoad();

    }

    private void showItemExistDialog(String transport) {
        Bundle args = new Bundle();
        args.putString("transport", transport);
        DialogItemExists fragment = new DialogItemExists();
        fragment.setArguments(args);
        fragment.setItemExistDialogListener(this);
        fragment.show(getActivity().getFragmentManager(), "existFragment");
    }

    public void showEditDialog(long id, String transport, float price){
        Bundle args = new Bundle();
        args.putString("transport", transport);
        args.putLong("id", id);
        args.putFloat("price", price);
        DialogEditItem fragment = new DialogEditItem();
        fragment.setArguments(args);
        fragment.setEditItemDialogListener(this);
        fragment.show(getActivity().getFragmentManager(), "editFragment");
    }

    public void showDeleteDialog(long id, String transport){
        Bundle args = new Bundle();
        args.putString("transport", transport);
        args.putLong("id", id);
        DialogDeleteItem fragment = new DialogDeleteItem();
        fragment.setArguments(args);
        fragment.setDeleteItemDialogListener(this);
        fragment.show(getActivity().getFragmentManager(), "deleteFragment");
    }

    @Override
    public void onDeleteItem(long id) {
        helper.deleteSettingsItem(id);
        getActivity().getSupportLoaderManager().getLoader(1).forceLoad();
    }

    @Override
    public void onEditItem(long id, float newPrice) {
        helper.editSettingsItemPrice(id, newPrice);
        getActivity().getSupportLoaderManager().getLoader(1).forceLoad();
    }

    @Override
    public void onItemExists() {
        ((ActivitySettings)getActivity()).onAddNewItemClick(null);
    }
}
