package ru.tomatapps.steps;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.widget.SimpleCursorAdapter;

import java.sql.Date;
import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 */
public class ListStatistics extends ListFragment {

    private SimpleCursorAdapter adapter;
    private LoaderStatistics loader;
    private Date[] dates = new Date[2];

    public static ListStatistics newInstance(Date[] dates) {
        ListStatistics fragment = new ListStatistics();
        Bundle args = new Bundle();
        args.putSerializable("begin", dates[0]);
        args.putSerializable("end", dates[1]);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        if (getArguments() != null) {
            dates[0] = (Date)getArguments().getSerializable("begin");
            dates[1] = (Date)getArguments().getSerializable("end");
        }
        String[] from = new String[]{StepsContract.COL_TRANSPORT, StepsContract.COL_QUANTITY, StepsContract.COL_EXPENSES};
        int[] to = new int[]{R.id.tvTransport, R.id.tvQuantity, R.id.tvExpenses};
        adapter = new SimpleCursorAdapter(getActivity(), R.layout.statisticslist_item, null, from, to, 0);
        setListAdapter(adapter);

        loader = new LoaderStatistics(getActivity(), adapter, null, dates );

    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().getSupportLoaderManager().initLoader(3, null, loader);
    }


    public void setTransport(ArrayList<String> transport){
        loader.setTransport(transport);
        getActivity().getSupportLoaderManager().getLoader(3).forceLoad();
    }

    public void setDates(Date[] dates) {
        loader.setDates(dates);
        getActivity().getSupportLoaderManager().getLoader(3).forceLoad();
    }
}
