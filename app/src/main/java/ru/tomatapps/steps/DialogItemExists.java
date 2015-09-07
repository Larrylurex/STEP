package ru.tomatapps.steps;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by LarryLurex on 29.08.2015.
 */
public class DialogItemExists extends DialogFragment implements View.OnClickListener  {
    private String Transport;
    private ItemExistDialogListener itemExistDialogListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Transport = getArguments().getString("transport");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getDialog().setTitle("Ooops..");
        View view =inflater.inflate(R.layout.confirm_dialog, container, false);
        ((TextView)view.findViewById(R.id.tvMessage)).setText(Transport + " " + getActivity().getString(R.string.existItemMessage));
        (view.findViewById(R.id.btnOk)).setOnClickListener(this);
        (view.findViewById(R.id.btnCancel)).setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnOk)
            itemExistDialogListener.onItemExists();
        dismiss();
    }

    public void setItemExistDialogListener(ItemExistDialogListener itemExistDialogListener) {
        this.itemExistDialogListener = itemExistDialogListener;
    }

    public interface ItemExistDialogListener{
        void onItemExists();
    }
}
