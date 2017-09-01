package azeddine.project.summer.dasBild.fragments;

import android.content.res.Configuration;
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

import azeddine.project.summer.dasBild.R;
import azeddine.project.summer.dasBild.adapters.CountriesListAdapter;
import azeddine.project.summer.dasBild.loaders.CountriesListLoader;
import azeddine.project.summer.dasBild.objectsUtils.Country;
import azeddine.project.summer.dasBild.objectsUtils.KeysUtil;

/**
 * Created by azeddine on 28/07/17.
 */

public class CountriesListFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<Country>> {
    public static final String TAG = "CountriesListFragment";


    private RecyclerView mCountriesRecyclerView;
    private CountriesListAdapter mCountriesListAdapter;
    private String  mFocusedCountryName ;
    private String mRegionName;

    public CountriesListFragment() {}

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
        Log.d(TAG, "onViewCreated: ");
        int orientation = this.getResources().getConfiguration().orientation;
        mCountriesListAdapter = new CountriesListAdapter(getContext());
        mCountriesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),(orientation == Configuration.ORIENTATION_PORTRAIT)?0:1,false));
        mCountriesRecyclerView.setAdapter(mCountriesListAdapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onActivityCreated: ");
        super.onActivityCreated(savedInstanceState);
            if(savedInstanceState!= null) {
                mFocusedCountryName = savedInstanceState.getString(KeysUtil.ALBUM_NAME_KEY);
                Log.d(TAG, "there are items saved");
            }
            Log.d(TAG, "onActivityCreated: the focused country is "+mFocusedCountryName);
            mRegionName = getArguments().getString(KeysUtil.REGION_NAME_KEY);
            Bundle args = new Bundle();
            args.putString(KeysUtil.REGION_NAME_KEY, mRegionName);
            getLoaderManager().initLoader(KeysUtil.COUNTRIES_LIST_LOADER_ID,args,this);

    }

    @Override
    public Loader<ArrayList<Country>> onCreateLoader(int id, Bundle args) {
        Log.d(TAG, "onCreateLoader: ");
        switch (id){
            case KeysUtil.COUNTRIES_LIST_LOADER_ID:
                return new CountriesListLoader(getContext(),args.getString(KeysUtil.REGION_NAME_KEY));
            default:
                return null;
        }
    }
    @Override
    public void onLoadFinished(Loader<ArrayList<Country>> loader, ArrayList<Country> data) {
        Log.d(TAG, "onLoadFinished: ");
        if(loader.getId() == KeysUtil.COUNTRIES_LIST_LOADER_ID){
            Country regionCountries = new Country(mRegionName,"ALL","ALL",null);
            if(data != null) {
                if(!data.contains(regionCountries)) data.add(0,regionCountries);
                  Log.d(TAG, "onLoadFinished:  the focused country is "+mFocusedCountryName);
                  mCountriesListAdapter.setCountriesList(data,mFocusedCountryName);
            }
        }



    }
    @Override
    public void onLoaderReset(Loader<ArrayList<Country>> loader) {
        Log.d(TAG, "onLoaderReset: ");

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState: focused country "+mCountriesListAdapter.getFocusedCountryName());
        super.onSaveInstanceState(outState);
        outState.putString(KeysUtil.ALBUM_NAME_KEY,mCountriesListAdapter.getFocusedCountryName());
    }



}
