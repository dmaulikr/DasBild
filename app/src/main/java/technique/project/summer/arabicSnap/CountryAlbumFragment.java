package technique.project.summer.arabicSnap;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by azeddine on 31/07/17.
 */

public class CountryAlbumFragment extends Fragment implements LoaderManager.LoaderCallbacks<Object> {
    public static final String TAG = "CountryAlbumFragment";
    public static final String ALBUM_NAME_KEY = "ALBUM_NAME_KEY";
    public static final int ALBUM_LOADER = 1;


    private RecyclerView mAlbumRecyclerView;
    private CountryAlbumAdapter mCountryAlbumAdapter;
    private String mAlbumName;
    private int mCurrentAlbumPage = CountryAlbumLoader.DEFAULT_ALBUM_PAGE;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        View view = inflater.inflate(R.layout.fragment_counrty_album, container, false);

        mAlbumRecyclerView = view.findViewById(R.id.country_album);
        mAlbumRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));

        mCountryAlbumAdapter = new CountryAlbumAdapter(getContext(), mAlbumRecyclerView);
        mCountryAlbumAdapter.setOnPhotoClickedListener((CountryAlbumAdapter.OnPhotoClickedListener) getContext());
        mCountryAlbumAdapter.setOnLoadMoreListener(new CountryAlbumAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                mCurrentAlbumPage++;
                Log.d(TAG, "onLoadMore: loading the album page number " + mCurrentAlbumPage);
                ((CountryAlbumLoader) getLoaderManager().getLoader(ALBUM_LOADER)).forceLoad(mCurrentAlbumPage);
            }
        });

        if (savedInstanceState != null) {
            mCurrentAlbumPage = savedInstanceState.getInt("ALBUM_PAGE");
            Log.d(TAG, "onCreateView: getting the saved album number " + mCurrentAlbumPage);
            mCountryAlbumAdapter.updatePhotosList(((CountryAlbumLoader) getLoaderManager().getLoader(ALBUM_LOADER)).getSavedPhotos());
            mCountryAlbumAdapter.notifyAlbumUpdates();
        }

        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated: ");
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onActivityCreated: ");
        super.onActivityCreated(savedInstanceState);
        if (ApiUtils.isOnline(getContext())) {
            mAlbumRecyclerView.setAdapter(mCountryAlbumAdapter);

            mAlbumName = getArguments().getString(ALBUM_NAME_KEY);
            mCountryAlbumAdapter.setRecyclerViewLoadingState(true);
            getLoaderManager().initLoader(ALBUM_LOADER, null, CountryAlbumFragment.this);

        } else {
            // display empty album
        }
    }

    @Override
    public Loader<Object> onCreateLoader(int id, Bundle args) {
        Log.d(TAG, "onCreateLoader: ");
        switch (id) {
            case ALBUM_LOADER:
                return new CountryAlbumLoader(getContext(), mAlbumName);
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(final Loader<Object> loader, Object data) {
        Log.d(TAG, "onLoadFinished: ");
        if (loader.getId() == ALBUM_LOADER) {
                mCountryAlbumAdapter.setRecyclerViewLoadingState(false);
                if(data != null){
                    mCountryAlbumAdapter.updatePhotosList(((ArrayList<Photo>) data));
                    mAlbumRecyclerView.post(new Runnable() {
                        @Override
                        public void run() {
                            Log.d(TAG, "run: notify the data change");
                            mCountryAlbumAdapter.notifyAlbumUpdates();

                        }
                    });
                    }
            }

    }

    @Override
    public void onLoaderReset(Loader<Object> loader) {
        Log.d(TAG, "onLoaderReset: ");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState: saving the album page number");
        outState.putInt("ALBUM_PAGE", mCurrentAlbumPage);
        ((CountryAlbumLoader) getLoaderManager().getLoader(ALBUM_LOADER)).setSavedPhotos(mCountryAlbumAdapter.getPhotosList());
    }
}
