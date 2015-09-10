package ru.tomatapps.steps.adapters;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

import ru.tomatapps.steps.R;

/**
 * Created by LarryLurex on 26.08.2015.
 */
public class MyCursorAdapter extends ResourceCursorAdapter{

    private int layoutId;
    private LayoutInflater mInflater;
    private String[] mFrom;
    private int[] mTo;

    public void setMyCursorEventListener(CheckItemListener checkItemListener) {
        this.checkItemListener = checkItemListener;
    }

    private CheckItemListener checkItemListener;

    public MyCursorAdapter(Context context, int layout, Cursor c,String[] from, int[] to, int flags) {
        super(context, layout, c, flags);
        this.mFrom = from;
        this.mTo = to;
        layoutId = layout;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        final int count = mTo.length;
        final String[] from = mFrom;
        final int[] to = mTo;

        for (int i = 0; i < count; i++) {
            final View v = view.findViewById(to[i]);
            if (v != null) {
                int iFrom = cursor.getColumnIndex(from[i]);

                int isChecked = cursor.getInt(iFrom);
                if(v instanceof Checkable){
                    setViewCheckable((Checkable) v, isChecked);
                    return;
                }


                String text = cursor.getString(iFrom);
                if (text == null) {
                    text = "";
                }

                if (v instanceof TextView) {
                    setViewText((TextView) v, text);
                } else if (v instanceof ImageView) {
                    setViewImage((ImageView) v, text);
                } else {
                    throw new IllegalStateException(v.getClass().getName() + " is not a " +
                            " view that can be bounds by this SimpleCursorAdapter");
                }
            }

        }
    }

    public void setViewText(TextView v, String text) {
        text = text.substring(0,1).toUpperCase() + text.substring(1, text.length()).toLowerCase();
        v.setText(text);
    }


    public void setViewImage(ImageView v, String value) {
        try {
            v.setImageResource(Integer.parseInt(value));
        } catch (NumberFormatException nfe) {
            v.setImageURI(Uri.parse(value));
        }
    }

    public void setViewCheckable(Checkable v, int isChecked) {
        v.setChecked(isChecked == 1);
    }


    @Override
    public Cursor swapCursor(Cursor c) {
        return super.swapCursor(c);
    }

    @Override
    public View newView(Context context, final Cursor cursor, ViewGroup parent) {
        if(checkItemListener == null)
            return super.newView(context, cursor, parent);

        View view = mInflater.inflate(layoutId, parent, false);
        view.setLongClickable(true);

        final CheckBox checkBox = (CheckBox)view.findViewById(R.id.cbIsDefault);
        checkBox.setOnClickListener(new View.OnClickListener() {
            final int position = cursor.getPosition();
            @Override
            public void onClick(View v) {
                checkItemListener.onCheck(checkBox.isChecked(), getItemId(position));
            }
        });

        return view;
    }

    public interface CheckItemListener{
        void onCheck(boolean isChecked, long id);
    }

}
