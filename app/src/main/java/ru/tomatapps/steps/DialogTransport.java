package ru.tomatapps.steps;


import android.app.Activity;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class DialogTransport extends DialogFragment {

    private HashMap<String, Boolean> transport;
    private OnTransportListListener mListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().setTitle("Transport");
        View v = inflater.inflate(R.layout.dialog_transport, container, false);
        ListView lv = (ListView)v.findViewById(R.id.lvTransport);

        String[] t = new String[]{};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_multiple_choice, (transport.keySet()).toArray(t));
        lv.setAdapter(adapter);
        lv.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        int i = 0;
        for(Map.Entry<String, Boolean> checked: transport.entrySet()){
            lv.setItemChecked(i++, checked.getValue());
        }
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckedTextView ctv = (CheckedTextView)view;
                transport.put(ctv.getText().toString(), ctv.isChecked());
            }
        });

        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mListener = (OnTransportListListener)activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle != null)

                transport = (HashMap<String, Boolean>) bundle.getSerializable("map");

    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if(mListener != null)
            mListener.onTransportListChange();
    }

    public interface OnTransportListListener{
        void onTransportListChange();
    }

}
