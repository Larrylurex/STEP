package ru.tomatapps.steps;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

import java.util.Locale;

/**
 * Created by LarryLurex on 30.08.2015.
 */
public class CurrencyTextView extends TextView {
    public CurrencyTextView(Context context) {
        super(context);
        setSingleLine();
        setGravity(Gravity.RIGHT);
    }

    public CurrencyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setSingleLine();
        setGravity(Gravity.RIGHT);
    }

    public CurrencyTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setSingleLine();
        setGravity(Gravity.RIGHT);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        Double price = 0.0;
        try{
            price = Double.parseDouble(text.toString());
        }
        catch (NumberFormatException ex){}
        String formatted = String.format(Locale.US,"%.2f", price);
        super.setText(formatted, type);
    }

}
