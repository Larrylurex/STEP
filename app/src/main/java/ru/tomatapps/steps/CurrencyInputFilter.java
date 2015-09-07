package ru.tomatapps.steps;

import android.text.InputFilter;
import android.text.Spanned;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by LarryLurex (dmitry.borodin90@gmail.com) on 29.08.2015.
 */
public class CurrencyInputFilter implements InputFilter {
    Pattern pattern = Pattern.compile("\\d+\\.?\\d{0,2}");

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        StringBuilder sb = new StringBuilder();
        sb.append(dest.subSequence(0, dstart));
        sb.append(source.subSequence(start,end));
        sb.append(dest.subSequence(dend, dest.length()));
        Matcher matcher = pattern.matcher(sb.toString());
        if(matcher.matches())
            return null;
        else
            return "";
    }
}
