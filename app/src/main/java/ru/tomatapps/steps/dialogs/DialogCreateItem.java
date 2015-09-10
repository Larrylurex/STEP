package ru.tomatapps.steps.dialogs;


import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import ru.tomatapps.steps.database.ContentResolverHelper;
import ru.tomatapps.steps.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class DialogCreateItem extends DialogFragment implements View.OnClickListener{

    private EditText etTransport;
    private EditText etPrice;
    private CheckBox cbIsDefault;

    private CreateItemDialogListener createItemDialogListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().setTitle(R.string.createItemDialog);
        View view = inflater.inflate(R.layout.create_item_dialog, container, false);
        Button btnOk = (Button)view.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(this);
        Button btnCancel = (Button)view.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(this);
        etTransport = (EditText)view.findViewById(R.id.etTransport);
        etPrice = (EditText)view.findViewById(R.id.etPrice);
        cbIsDefault = (CheckBox)view.findViewById(R.id.cbIsDefault);
        return view;
    }


    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnOk){
            if(createItemDialogListener != null){
                float price;
                String strPrice = etPrice.getText().toString();

                try{
                    price = Float.parseFloat(strPrice);
                }
                catch(NumberFormatException ex){
                    price = 0.0f;
                }

                ContentResolverHelper.SettingsItem item = new ContentResolverHelper.SettingsItem(etTransport.getText().toString(), price, cbIsDefault.isChecked());
                createItemDialogListener.createItem(item);
            }
        }
         dismiss();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        createItemDialogListener = (CreateItemDialogListener)activity;
    }


    public interface CreateItemDialogListener{
        void  createItem(ContentResolverHelper.SettingsItem item);
    }

}
