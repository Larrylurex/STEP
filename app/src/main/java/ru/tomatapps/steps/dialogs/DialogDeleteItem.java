package ru.tomatapps.steps.dialogs;


import android.app.DialogFragment;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ru.tomatapps.steps.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class DialogDeleteItem extends DialogFragment implements View.OnClickListener {

    private DeleteItemDialogListener deleteItemDialogListener;
    private long itemID;
    private String Transport;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        itemID = getArguments().getLong("id");
        Transport = getArguments().getString("transport");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getDialog().setTitle("Delete");
        View view =inflater.inflate(R.layout.confirm_dialog, container, false);
        ((TextView)view.findViewById(R.id.tvMessage)).setText(Transport + " " + getActivity().getString(R.string.deleteItemMessage));
        (view.findViewById(R.id.btnOk)).setOnClickListener(this);
        (view.findViewById(R.id.btnCancel)).setOnClickListener(this);
        return view;
    }

    public void onClick(View view){
        if(view.getId() == R.id.btnOk)
            deleteItemDialogListener.onDeleteItem(itemID);
        dismiss();
    }

    public void setDeleteItemDialogListener(DeleteItemDialogListener deleteItemDialogListener) {
        this.deleteItemDialogListener = deleteItemDialogListener;
    }

    public interface DeleteItemDialogListener{
        void onDeleteItem(long id);
    }


}
