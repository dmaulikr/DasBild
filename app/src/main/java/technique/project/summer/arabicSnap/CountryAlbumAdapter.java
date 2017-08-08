package technique.project.summer.arabicSnap;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
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
    private OnLoadMoreListener mLoadMoreListener;
    static private OnPhotoClickedListener mOnPhotoClikedListener;
    private RecyclerView mRecyclerView ;
    private boolean mRecyclerViewLoadingState;


    interface OnLoadMoreListener{
        void onLoadMore();
    }
    interface OnPhotoClickedListener {
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
                        if(itemsNumber <= lastVisibleItemPosition+5 && !isLoading()){
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

    public CountryAlbumAdapter(Context mContext, List<Photo> photosList) {
        this.mContext = mContext;
        this.mPhotosList = photosList;
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
            ((PhotoViewHolder) holder).mView.setTag(photo);
            Glide.with(mContext)
                    .load(photo.getUrl())
                    .apply(new RequestOptions().placeholder(mContext.getResources().getDrawable(R.drawable.ic_image)))
                    .apply(new RequestOptions().fitCenter())
                    .into(( (PhotoViewHolder) holder).mPhoto);
        }else{
            mLoadMoreListener.onLoadMore();
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
        this.mLoadMoreListener = listener;
    }

    public void setOnPhotoClikedListener(OnPhotoClickedListener listener){
        this.mOnPhotoClikedListener  = listener;
    }

    public boolean isLoading(){
        return mRecyclerViewLoadingState;
    }

    public void setRecyclerViewLoadingState(boolean mRecyclerViewLoadingState) {
        this.mRecyclerViewLoadingState = mRecyclerViewLoadingState;
        if(mRecyclerViewLoadingState){
            mPhotosList.add(null);
            notifyItemInserted(mPhotosList.size()-1);
        }else{
            mPhotosList.remove(mPhotosList.size()-1);
            notifyItemRemoved(mPhotosList.size());
        }
    }

    public static class PhotoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView mPhoto;
        private View mView;
        public PhotoViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mPhoto  = itemView.findViewById(R.id.album_item);
        }
        @Override
        public void onClick(View view) {
            Photo photo = (Photo) view.getTag();
            mOnPhotoClikedListener.onPhotoClicked(photo);

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
