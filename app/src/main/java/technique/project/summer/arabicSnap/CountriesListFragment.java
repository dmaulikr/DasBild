package technique.project.summer.arabicSnap;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by azeddine on 28/07/17.
 */

public class CountriesListFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<Country>> {
    public static final String TAG = "CountriesListFragment";
    public static final int COUNTRIES_LIST_LOADER = 0;

    private RecyclerView mCountriesRecyclerView;
    private CountriesListAdapter mCountriesListAdapter;


    public CountriesListFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        View view =  inflater.inflate(R.layout.fragment_countries_list,container,false);

        mCountriesRecyclerView =  view.findViewById(R.id.countries_list);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mCountriesListAdapter = new CountriesListAdapter(getContext());
        mCountriesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        mCountriesRecyclerView.setAdapter(mCountriesListAdapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onActivityCreated: ");
        super.onActivityCreated(savedInstanceState);
        if(ApiUtils.isOnline(getContext())){
            getLoaderManager().initLoader(COUNTRIES_LIST_LOADER,null,this);
        }else{
            mCountriesRecyclerView.setVisibility(View.GONE);
        }

    }

    @Override
    public Loader<ArrayList<Country>> onCreateLoader(int id, Bundle args) {
        Log.d(TAG, "onCreateLoader: ");
        switch (id){
            case COUNTRIES_LIST_LOADER:
                return new CountriesListLoader(getContext());
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Country>> loader, ArrayList<Country> data) {
        Log.d(TAG, "onLoadFinished: ");
        mCountriesListAdapter.setCountriesList(data);


    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Country>> loader) {
        Log.d(TAG, "onLoaderReset: ");

    }


}
