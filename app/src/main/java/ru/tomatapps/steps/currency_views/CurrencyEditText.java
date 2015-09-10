package ru.tomatapps.steps.currency_views;

import android.content.Context;
import android.text.InputFilter;
import android.util.AttributeSet;
import android.widget.EditText;

import java.util.Locale;

/**
 * Created by LarryLurex on 29.08.2015.
 */
public class CurrencyEditText extends EditText{
    public CurrencyEditText(Context context) {
        super(context);
        configure();
    }

    public CurrencyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        configure();
    }

    public CurrencyEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        configure();
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

    private void configure(){
        CurrencyInputFilter filter = new CurrencyInputFilter();
        setFilters(new InputFilter[]{filter});
        setSelectAllOnFocus(true);
        setSingleLine();
        setText("0.00");
    }
}
