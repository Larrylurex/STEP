package ru.tomatapps.steps;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ValueFormatter;

import java.sql.Date;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChartFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private ChartLoader loader;
    private LineChart chart;
    private Date[] dates = new Date[2];
    ArrayList<String> transport;

    public static ChartFragment newInstance(Date[] dates) {
        ChartFragment fragment = new ChartFragment();
        Bundle args = new Bundle();
        args.putSerializable("begin", dates[0]);
        args.putSerializable("end", dates[1]);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chart, container, false);

        if (getArguments() != null) {
            dates[0] = (Date)getArguments().getSerializable("begin");
            dates[1] = (Date)getArguments().getSerializable("end");
        }
        chart = (LineChart)v.findViewById(R.id.chart);
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        YAxis yAxisRight = chart.getAxisRight();
        yAxisRight.setEnabled(false);
        getActivity().getSupportLoaderManager().initLoader(5, null, this);
        return v;
    }

    public void setTransport(ArrayList<String> transport){
        this.transport = transport;
        loader.setSelection(transport, dates);
        getActivity().getSupportLoaderManager().getLoader(5).forceLoad();
    }
    public void setDates(Date[] dates){
        this.dates = dates;
        loader.setSelection(transport, dates);
        getActivity().getSupportLoaderManager().getLoader(5).forceLoad();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        loader = new ChartLoader(getActivity(),dates);
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        final long MILISEC_IN_DAY = 24*60*60*1000;
        float fMax = 0;

        ArrayList<String> xVals = new ArrayList<>();
        for(long i = dates[0].getTime(); i <= dates[1].getTime(); i+=MILISEC_IN_DAY){
            SimpleDateFormat sdf = new SimpleDateFormat("d MMM");
            xVals.add(sdf.format(new Date(i)));
        }
        HashMap<String, int[]> map = new HashMap<>();
        cursor.moveToPosition(-1);

        while(cursor.moveToNext()){
            String t = cursor.getString(cursor.getColumnIndex(DBHelper.COL_TRANSPORT));
            if(!map.containsKey(t)){
                int[] y = new int[xVals.size()];
                map.put(t, y);
            }
            int[] y = map.get(t);
            long curDate = cursor.getLong(cursor.getColumnIndex(DBHelper.COL_DATE));
            int index = (int)((curDate - dates[0].getTime())/MILISEC_IN_DAY);
            y[index] = cursor.getInt(cursor.getColumnIndex(DBHelper.COL_QUANTITY));
            fMax = (y[index] > fMax) ? y[index] : fMax;
            map.put(t, y);
        }
        ArrayList<LineDataSet> sets = new ArrayList<>();
        Set<String> keys = map.keySet();
        for(String key: keys){
            ArrayList<Entry> entries = new ArrayList<>();
            int[] array = map.get(key);
            for(int i = 0 ; i < xVals.size(); i++ ){
                Entry e = new Entry(array[i],i);
                entries.add(e);
            }
            LineDataSet dataSet = new LineDataSet(entries, key);
            dataSet.setDrawCircles(false);
            sets.add(dataSet);
        }
        LineData lineData = new LineData(xVals, sets);
        lineData.setValueFormatter(new RemoveZeroFormatter());
        int max = (int)fMax;
        YAxis Left = chart.getAxisLeft();
        Left.setAxisMaxValue(max+1);
        Left.setLabelCount(max+2, true);

        chart.setData(lineData);
        chart.invalidate();

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
    public class RemoveZeroFormatter implements ValueFormatter {

        private DecimalFormat mFormat;

        @Override
        public String getFormattedValue(float value) {
            if(value == 0)
                return ""; // append a dollar-sign
            else
                return Integer.toString((int) value);
        }
    }
}
