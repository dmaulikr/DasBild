package azeddine.project.summer.dasBild.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import azeddine.project.summer.dasBild.R;
import azeddine.project.summer.dasBild.objectsUtils.Country;

/**
 * Created by azeddine on 28/07/17.
 */

public class CountriesListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "CountriesListAdapter";

    private static final int VIEW_TYPE_FREE = 0;
    private static final int VIEW_TYPE_FOCUSED = 1;
    private static final int VIEW_TYPE_LOAD = 2;

    private  Context mContext;
    private Country mFocusedCountry;
    private ArrayList<Country> mCountries = new ArrayList<>();
    static private OnCountryItemClickedListener mOnCountryItemClickedListener;
    static private OnCountryItemLongClickedListener mOnCountryItemLongClickedListener;
    private boolean mRecyclerViewLoadingState;

    public String getFocusedCountryName() {
        return (mFocusedCountry != null)? mFocusedCountry.getName() : null;
    }

    public interface OnCountryItemClickedListener {
        void onCountryClicked(String countryName);
    }
    public interface OnCountryItemLongClickedListener{
        void onCountryLongClicked(Country country,ImageView sharedImageView);
    }

    public CountriesListAdapter(Context context) {
        mContext = context;
        if( context instanceof  OnCountryItemClickedListener){
            mOnCountryItemClickedListener = (OnCountryItemClickedListener) context;
        }
        if( context instanceof  OnCountryItemLongClickedListener){
            mOnCountryItemLongClickedListener = (OnCountryItemLongClickedListener) context;
        }
        setRecyclerViewLoadingState(true);
    }

    public void setOnCountryItemClickedListener(OnCountryItemClickedListener listener){
           mOnCountryItemClickedListener = listener;
    }
    public void setmOnCountryItemLongClickedListener(OnCountryItemLongClickedListener listener){
         mOnCountryItemLongClickedListener = listener;
    }

    public void setRecyclerViewLoadingState(boolean recyclerViewLoadingState) {
        mRecyclerViewLoadingState = recyclerViewLoadingState;
        if(mRecyclerViewLoadingState){
            mCountries.add(null);
            notifyItemInserted(mCountries.size()-1);
        }else{
            mCountries.remove(mCountries.size()-1);
            notifyItemRemoved(mCountries.size());
        }
    }
    @Override
    public int getItemViewType(int position) {
        Country country= mCountries.get(position);
      if(country == null){
          return VIEW_TYPE_LOAD;
      }else if(country.equals(mFocusedCountry)){
          return VIEW_TYPE_FOCUSED;
      }else {
          return VIEW_TYPE_FREE;
      }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType){
            case VIEW_TYPE_FOCUSED:
                view = LayoutInflater.from(mContext).inflate(R.layout.list_item_country_focused, parent, false);
                return new CountryViewHolder(view);
            case VIEW_TYPE_FREE:
                 view = LayoutInflater.from(mContext).inflate(R.layout.list_item_country, parent, false);
                return new CountryViewHolder(view);
            case VIEW_TYPE_LOAD:
                view = LayoutInflater.from(mContext).inflate(R.layout.list_item_load, parent, false);
                return new LoadingProgressViewHolder(view);
            default:
                return null;

        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof CountryViewHolder){

        Country country = getCountryByIndex(position);
        ((CountryViewHolder)holder).view.setTag(country);
        ViewCompat.setTransitionName(((CountryViewHolder) holder).mFlagImageView, country.getThreeAlphaCode());
        ((CountryViewHolder)holder).mNameTextView.setText(country.getThreeAlphaCode());
        Glide.with(mContext)
                .load(country.getThreeAlphaCode().equals("ALL")? R.mipmap.plant_earth: country.getFlagURL())
                .apply(new RequestOptions().optionalCircleCrop())
                .into(((CountryViewHolder)holder).mFlagImageView);
        }


    }

    @Override
    public int getItemCount() {
        return  mCountries.size();
    }

    private Country getCountryByIndex(int index) {
        return mCountries.get(index);
    }

    public void setCountriesList(ArrayList<Country> countriesList ,String focusedCountryName) {
        int position;
        setRecyclerViewLoadingState(false);
        if(countriesList != null){
            if(focusedCountryName != null)
                position = countriesList.indexOf(new Country(focusedCountryName));
            else
                position = 0;

            mFocusedCountry = countriesList.get(position);
            this.mCountries.addAll(countriesList);
            notifyDataSetChanged();
        }
    }


    public  class CountryViewHolder extends RecyclerView.ViewHolder implements RecyclerView.OnClickListener, RecyclerView.OnLongClickListener{
        private ImageView mFlagImageView;
        private TextView mNameTextView;
        private View view;

        public CountryViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
            mFlagImageView = itemView.findViewById(R.id.flag);
            mNameTextView = itemView.findViewById(R.id.country_name);
        }

        @SuppressLint("ResourceAsColor")
        @Override
        public void onClick(View view) {
            Log.d(TAG, "onClick: ");


            notifyItemChanged(mCountries.indexOf(mFocusedCountry));
            mFocusedCountry = ((Country)view.getTag());
            notifyItemChanged(mCountries.indexOf(mFocusedCountry));

            mOnCountryItemClickedListener.onCountryClicked(mFocusedCountry.getName());
        }


        @Override
        public boolean onLongClick(View view) {
           mOnCountryItemLongClickedListener.onCountryLongClicked(((Country)view.getTag()),mFlagImageView);
           return true;
        }
    }
    private static class LoadingProgressViewHolder extends  RecyclerView.ViewHolder{
        private ProgressBar mProgressBar;
        private LoadingProgressViewHolder(View itemView) {
            super(itemView);
            mProgressBar = itemView.findViewById(R.id.load_small_progress_bar);
        }
    }
}
