package ru.tomatapps.steps;


import android.app.DialogFragment;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class DialogEditItem extends DialogFragment implements View.OnClickListener{

    private EditItemDialogListener editItemDialogListener;
    private long itemID;
    private String Transport;
    private Float price;
    private EditText etPrice;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        itemID = getArguments().getLong("id");
        Transport = getArguments().getString("transport");
        price = getArguments().getFloat("price");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getDialog().setTitle("Edit " + Transport);
        View view =inflater.inflate(R.layout.edit_item_dialog, container, false);
        ((TextView)view.findViewById(R.id.tvMessage)).setText(getString(R.string.Price));
        etPrice = ((EditText)view.findViewById(R.id.etPrice));
        etPrice.setText(price.toString());
        (view.findViewById(R.id.btnOk)).setOnClickListener(this);
        (view.findViewById(R.id.btnCancel)).setOnClickListener(this);
        return view;
    }

    public void onClick(View view){
        if(view.getId() == R.id.btnOk){
            float price = Float.parseFloat(etPrice.getText().toString());
            editItemDialogListener.onEditItem(itemID, price);
        }
        dismiss();
    }




    public void setEditItemDialogListener(EditItemDialogListener editItemDialogListener) {
        this.editItemDialogListener = editItemDialogListener;
    }



    public interface EditItemDialogListener{
        void onEditItem(long id, float newPrice);
    }
}
