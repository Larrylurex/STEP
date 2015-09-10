package ru.tomatapps.steps.others;

import android.graphics.Color;
import android.util.Log;

import java.util.Random;

/**
 * Created by LarryLurex (dmitry.borodin90@gmail.com) on 09.09.2015.
 */
public class ColorGenerator {
    private final double GOLDEN_RATIO = 0.618033988749895*360;
    private int pivotColor;
    public ColorGenerator(){
        pivotColor = new Random().nextInt(360);
    }
    public int next(){
        pivotColor += GOLDEN_RATIO;
        pivotColor %= 360;
        int color = Color.HSVToColor(new float[]{pivotColor, 0.95f, 0.95f});
        Log.d("myTag", "pivot= " + pivotColor + " color= " + color);
        return color;

    }
}
