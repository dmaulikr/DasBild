package technique.project.summer.arabicSnap;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by azeddine on 31/07/17.
 */
public class CountryAlbumAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    private Context mContext;
    private List<Photo> mPhotosList = new ArrayList<>();
    public OnLoadMoreListener loadMoreListener;
    private boolean loadingState = false;


    public CountryAlbumAdapter(Context context) {
        this.mContext = context ;
    }

    public CountryAlbumAdapter(Context mContext, List<Photo> photosList) {
        this.mContext = mContext;
        this.mPhotosList = photosList;
    }

    interface OnLoadMoreListener{
        public void onLoadMore();
    }

    @Override
    public int getItemViewType(int position) {
        return mPhotosList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view ;
        if(viewType == VIEW_TYPE_ITEM){
            view = LayoutInflater.from(mContext).inflate(R.layout.list_item_album,parent,false);
            return new PhotoViewHolder(view);
        }else{
            view = LayoutInflater.from(mContext).inflate(R.layout.list_item_load,parent,false);
            return  new LoadingProgressViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof PhotoViewHolder){
            Photo photo = getCountryByIndex(position);
            Glide.with(mContext)
                    .load(photo.getUrl())
                    .apply(new RequestOptions().placeholder(mContext.getResources().getDrawable(R.drawable.ic_image)))
                    .apply(new RequestOptions().fitCenter())
                    .into(( (PhotoViewHolder) holder).mPhoto);
        }else{
            loadMoreListener.onLoadMore();
        }


    }


    @Override
    public int getItemCount() {
        if (mPhotosList == null) return 0;
        else return mPhotosList.size();
    }

    private Photo getCountryByIndex(int index) {
        if (mPhotosList == null) return null;
        else return mPhotosList.get(index);
    }


    public void updatePhotosList(List<Photo> photosList) {
        for(int i=0;i<photosList.size();i++){
            mPhotosList.add(photosList.get(i));
        }
        notifyItemInserted(mPhotosList.size()-1);
    }
    public void setOnLoadMoreListener(OnLoadMoreListener listener){
        this.loadMoreListener = listener;
    }

    public boolean isLoading(){
        return loadingState;
    }
    public void setLoadingState(boolean loadingState) {
        this.loadingState = loadingState;
    }

    public static class PhotoViewHolder extends RecyclerView.ViewHolder{
        private ImageView mPhoto;
        public PhotoViewHolder(View itemView) {
            super(itemView);
            mPhoto  = itemView.findViewById(R.id.album_item);
        }
    }
    public static class LoadingProgressViewHolder extends  RecyclerView.ViewHolder{
        private ProgressBar mProgressBar;
        public LoadingProgressViewHolder(View itemView) {
            super(itemView);
            mProgressBar = itemView.findViewById(R.id.load_more_progress_bar);
        }
    }
}
