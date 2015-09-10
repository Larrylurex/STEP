package ru.tomatapps.steps.activities;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import ru.tomatapps.steps.fragments.ChartFragment;
import ru.tomatapps.steps.database.ContentResolverHelper;
import ru.tomatapps.steps.dialogs.DialogTransport;
import ru.tomatapps.steps.fragments.ListStatistics;
import ru.tomatapps.steps.loaders.LoaderTransport;
import ru.tomatapps.steps.R;
import ru.tomatapps.steps.others.TransportItem;


public class ActivityStatistics extends AppCompatActivity implements DialogTransport.OnTransportListListener {

    private  final int TRANSPORT_LOADER = 0;

    private Mode mode;
    private Date[] datesRange;
    private Date[] dates;
    private int monthOffset;
    private ArrayList<TransportItem> transport = new ArrayList<>();

    private ListStatistics listFragment;
    private final String LISTFRAGMENT_TAG = "ListFragment";
    private ChartFragment chartFragment;
    private final String CHARTFRAGMENT_TAG = "ChartFragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        LoaderTransport loaderTransport = new LoaderTransport(this, transport);
        loaderTransport.setListener(this);
        getSupportLoaderManager().initLoader(TRANSPORT_LOADER, null, loaderTransport);

        if(savedInstanceState == null) {
            getDatesRange();
            setMode(Mode.SINGLE_MONTH);

            listFragment = ListStatistics.newInstance(transport, dates);
            chartFragment = ChartFragment.newInstance(transport, dates);
            FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
            trans.replace(R.id.lfStatistics, listFragment, LISTFRAGMENT_TAG);
            trans.replace(R.id.chartContainer, chartFragment, CHARTFRAGMENT_TAG);
            trans.commit();
        }
        else{
            datesRange = (Date[])savedInstanceState.getSerializable("DatesRange");
            Mode m = (Mode)savedInstanceState.getSerializable("Mode");
            setMode(m);
            dates = (Date[])savedInstanceState.getSerializable("Dates");
            transport = (ArrayList<TransportItem>)savedInstanceState.getSerializable("tArray");
            setShiftBtnEnabled();
            listFragment = (ListStatistics)getSupportFragmentManager().findFragmentByTag(LISTFRAGMENT_TAG);
            chartFragment = (ChartFragment)getSupportFragmentManager().findFragmentByTag(CHARTFRAGMENT_TAG);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("Mode", mode);
        outState.putSerializable("Dates", dates);
        outState.putSerializable("DatesRange", datesRange);
        outState.putSerializable("tArray", transport);
    }

    public void onChooseTransport(View view){
        DialogFragment dialog = new DialogTransport();
        Bundle args = new Bundle();
        args.putSerializable("tArray", transport);
        dialog.setArguments(args);
        dialog.show(getSupportFragmentManager(), "dialogTransport");
    }


    @Override
    public void onTransportListChange() {

        listFragment.setTransport(transport);
        chartFragment.setTransport(transport);
    }

    public void onChangeMode(View view){
        switch(view.getId()){
            case R.id.btnZoomOut:
                setMode(mode.previous());
                break;
            case R.id.btnZoomIn:
                setMode(mode.next());
                break;
        }
        listFragment.setDates(dates);
        chartFragment.setDates(dates);
    }

    private void getDatesRange(){
        ContentResolverHelper helper = new ContentResolverHelper(this);
        datesRange = helper.getDatesRange();
    }

    public void onShiftChange(View view){
        GregorianCalendar c = new GregorianCalendar();
        int coef = 0;
        switch(view.getId()){
            case R.id.btnLeft:
                coef = -1;
                break;
            case R.id.btnRight:
                coef = 1;
                break;
        }
        c.setTime(dates[0]);
        c.add(Calendar.MONTH, coef * monthOffset);
        dates[0] = new Date(c.getTime().getTime());

        c.setTime(dates[1]);
        c.add(Calendar.MONTH, coef * monthOffset);
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        dates[1] = new Date(c.getTime().getTime());

        setShiftBtnEnabled();
        listFragment.setDates(dates);
        chartFragment.setDates(dates);
    }

    private void setMode(Mode m){
        mode = m;
        ImageButton btnZoomOut = (ImageButton)findViewById(R.id.btnZoomOut);
        btnZoomOut.setEnabled(!mode.isFirst());
        ImageButton btnZoomIn = (ImageButton)findViewById(R.id.btnZoomIn);
        btnZoomIn.setEnabled(!mode.isLast());
        setOffset();
        fromModeToDate();
        setShiftBtnEnabled();
    }

    private void setOffset() {
        switch(mode){
            case SINGLE_MONTH:
                monthOffset = 1;
                break;
            case QUARTER_YEAR:
                monthOffset = 3;
                break;
            case HALF_YEAR:
                monthOffset = 6;
                break;
            case YEAR:
                monthOffset = 12;
                break;
        }
    }

    private void fromModeToDate(){
        if(mode == Mode.ALL){
            dates = null;
            return;
        }

        dates = new Date[2];
        GregorianCalendar c = new GregorianCalendar();

        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        c.set(Calendar.HOUR_OF_DAY, c.getActualMaximum(Calendar.HOUR_OF_DAY));
        c.set(Calendar.MINUTE, c.getActualMaximum(Calendar.MINUTE));
        c.set(Calendar.SECOND, c.getActualMaximum(Calendar.SECOND));
        c.set(Calendar.MILLISECOND, c.getActualMaximum(Calendar.MILLISECOND));
        dates[1] = new Date(c.getTimeInMillis());

        c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        c.add(Calendar.MONTH, 1 - monthOffset);
        dates[0] = new Date(c.getTimeInMillis());
    }

    private void setShiftBtnEnabled(){
        ImageButton btnLeft = (ImageButton)findViewById(R.id.btnLeft);
        ImageButton btnRight = (ImageButton)findViewById(R.id.btnRight);

        btnLeft.setEnabled(dates != null && dates[0].getTime() > datesRange[0].getTime());
        btnRight.setEnabled(dates != null && dates[1].getTime() < datesRange[1].getTime());
    }

    public enum Mode{
        ALL, YEAR, HALF_YEAR, QUARTER_YEAR, SINGLE_MONTH;
        private static Mode[] modeArray = values();
        public Mode next(){
            int newOrdinal = ordinal();
            if(ordinal() < modeArray.length -1)
                newOrdinal = ordinal()+1;
            return modeArray[newOrdinal];
        }
        public Mode previous(){
            int newOrdinal = ordinal();
            if(ordinal() > 0)
                newOrdinal = ordinal()-1;
            return modeArray[newOrdinal];
        }
        public boolean isFirst(){
            return ordinal() == 0;
        }
        public boolean isLast(){
            return ordinal() == modeArray.length -1;
        }
    }
}
