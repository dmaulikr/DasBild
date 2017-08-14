package azeddine.project.summer.dasBild.adapters;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import azeddine.project.summer.dasBild.R;
import azeddine.project.summer.dasBild.costumComponents.GridItem;
import azeddine.project.summer.dasBild.objectsUtils.Photo;

/**
 * Created by azeddine on 31/07/17.
 */
public class CountryAlbumAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "CountryAlbumAdapter";
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    private Context mContext;
    private ArrayList<Photo> photos = new ArrayList<>();
    private OnLoadMoreListener mLoadMoreListener;
    static private OnPhotoClickedListener mOnPhotoClickedListener;

    private RecyclerView mRecyclerView  ;
    private boolean mRecyclerViewLoadingState;


    public interface OnLoadMoreListener{
        void onLoadMore();
    }
    public  interface OnPhotoClickedListener {
        void onPhotoClicked(Photo photo);
    }

    public CountryAlbumAdapter(Context context,RecyclerView recyclerView) {
        this.mContext = context ;
        this.mRecyclerView = recyclerView ;
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManger = (LinearLayoutManager) recyclerView.getLayoutManager();
                int itemsNumber = layoutManger.getItemCount();
                int lastVisibleItemPosition = layoutManger.findLastCompletelyVisibleItemPosition();
                if(dy>0){
                        if(itemsNumber <= lastVisibleItemPosition+6 && !isLoading()){
                            mLoadMoreListener.onLoadMore();
                            setRecyclerViewLoadingState(true);
                            }
                }
            }
        });

        if(mRecyclerView.getLayoutManager() instanceof GridLayoutManager){
            ((GridLayoutManager) mRecyclerView.getLayoutManager()).setSpanSizeLookup(
                    new GridLayoutManager.SpanSizeLookup() {
                        @Override
                        public int getSpanSize(int position) {
                            switch (getItemViewType(position)){
                                case VIEW_TYPE_LOADING:
                                    return 3;
                                case VIEW_TYPE_ITEM:
                                    return 1;
                                default:
                                    return -1;
                            }
                        }
                    }
            );
        }
    }




    @Override
    public int getItemViewType(int position) {
        return photos.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
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

    public ArrayList<Photo> getPhotos() {
        return photos;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof PhotoViewHolder){
            Photo photo = photos.get(position);
            ((PhotoViewHolder) holder).mView.setTag(photo);
            Glide.with(mContext)
                    .load(photo.getCroppedPhotoUrl())
                    .apply(new RequestOptions().placeholder(mContext.getResources().getDrawable(R.drawable.ic_image)))
                    .apply(new RequestOptions().centerCrop())
                    .into(( (PhotoViewHolder) holder).mPhoto);
        }

    }

    @Override
    public int getItemCount() {
         return photos.size();
    }

    public void updatePhotos(ArrayList<Photo> photosList) {
        if(photosList != null){
            if(!photos.containsAll(photosList))  photos.addAll(photosList);
        }

    }
    public void notifyPhotosUpdates(){
        Log.d(TAG, "notifyPhotosUpdates: before");
        int i = getItemCount() - 1 ;
       // notifyItemInserted(photos.size()-1);
        notifyItemRangeInserted(i,photos.size()-1);
        Log.d(TAG, "notifyPhotosUpdates: after ");
    }

    public void setOnLoadMoreListener(OnLoadMoreListener listener){
        this.mLoadMoreListener = listener;
    }

    public void setOnPhotoClickedListener(OnPhotoClickedListener listener){
        mOnPhotoClickedListener = listener;
    }

    public boolean isLoading(){
        return mRecyclerViewLoadingState;
    }

    public void setRecyclerViewLoadingState(boolean recyclerViewLoadingState) {
        this.mRecyclerViewLoadingState = recyclerViewLoadingState;
        Log.d(TAG, "setRecyclerViewLoadingState: ");
        if(mRecyclerViewLoadingState){
                    photos.add(null);
                    notifyItemInserted(photos.size()-1);
        }else{
                    photos.remove(photos.size()-1);
                    notifyItemRemoved(photos.size());
                }
        }



    private static class PhotoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView mPhoto;
        private View mView;
        private PhotoViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mView = itemView;
            mPhoto  = itemView.findViewById(R.id.album_item);
        }
        @Override
        public void onClick(View view) {
            Log.d(TAG, "onClick: ");
            Photo photo = (Photo) view.getTag();
            mOnPhotoClickedListener.onPhotoClicked(photo);

        }
    }

    private static class LoadingProgressViewHolder extends  RecyclerView.ViewHolder{
        private ProgressBar mProgressBar;
        private LoadingProgressViewHolder(View itemView) {
            super(itemView);
            mProgressBar = itemView.findViewById(R.id.load_more_progress_bar);
        }
    }
}