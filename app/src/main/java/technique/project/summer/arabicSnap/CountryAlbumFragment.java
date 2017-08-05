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

public class CountryAlbumFragment extends Fragment implements LoaderManager.LoaderCallbacks<Object>{
    public static final String TAG = "CountryAlbumFragment";
    public static final int ALBUM_LOADER = 1;
    public static final String  ALBUM_NAME_KEY = "ALBUM_NAME_KEY";

    private RecyclerView mAlbumRecyclerView;
    private CountryAlbumAdapter mCountryAlbumAdapter;
    private String mAlbumName;
    private int albumPage = 1;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        View view = inflater.inflate(R.layout.fragment_counrty_album,container,false);
        mAlbumRecyclerView = view.findViewById(R.id.country_album);
        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated: ");
        super.onViewCreated(view, savedInstanceState);
        mCountryAlbumAdapter = new CountryAlbumAdapter(getContext());
        mCountryAlbumAdapter.setOnLoadMoreListener(new CountryAlbumAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if(mAlbumName != null){
                    albumPage++;
                    ((CountryAlbumLoader) getLoaderManager().getLoader(ALBUM_LOADER)).forceLoad(albumPage);
                }

            }
        });


        mAlbumRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                GridLayoutManager layoutManger = (GridLayoutManager) recyclerView.getLayoutManager();

                int itemsNumber = layoutManger.getItemCount();
                int lastVisibleItemPosition = layoutManger.findLastCompletelyVisibleItemPosition();

                if(dy>0){
                    Log.d(TAG, "onScrolled: itemsNumber="+itemsNumber+" and astVisibleItemPosition="+lastVisibleItemPosition);

                    if(itemsNumber <= lastVisibleItemPosition+5 && !mCountryAlbumAdapter.isLoading()){
                        mCountryAlbumAdapter.loadMoreListener.onLoadMore();
                        mCountryAlbumAdapter.setLoadingState(true);
                    }

                }

            }
        });
        mAlbumRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));
        mAlbumRecyclerView.setAdapter(mCountryAlbumAdapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onActivityCreated: ");
        super.onActivityCreated(savedInstanceState);
        mAlbumName = getArguments().getString(ALBUM_NAME_KEY);
        if(mAlbumName != null)  getLoaderManager().initLoader(ALBUM_LOADER,null,this);
    }

    @Override
    public Loader<Object> onCreateLoader(int id, Bundle args) {
        switch (id){
            case ALBUM_LOADER:
                return new CountryAlbumLoader(getContext(),mAlbumName);
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Object> loader, Object data) {
        if(loader.getId() == ALBUM_LOADER){
            final ArrayList<Photo> photos = (ArrayList<Photo>) data;
            Log.d(TAG, "onLoadFinished :"+photos.size());
            mAlbumRecyclerView.post(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "run: ");
                    mCountryAlbumAdapter.updatePhotosList(photos);
                    mCountryAlbumAdapter.setLoadingState(false);
                }
            });
        }
    }

    @Override
    public void onLoaderReset(Loader<Object> loader) {

    }


}
