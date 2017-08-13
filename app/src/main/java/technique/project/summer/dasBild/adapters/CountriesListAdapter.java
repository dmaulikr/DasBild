package technique.project.summer.dasBild.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import technique.project.summer.dasBild.R;
import technique.project.summer.dasBild.objectsUtils.Country;

/**
 * Created by azeddine on 28/07/17.
 */

public class CountriesListAdapter extends RecyclerView.Adapter<CountriesListAdapter.CountryViewHolder> {
    private static final String TAG = "CountriesListAdapter";
    private Context mContext;
    private List<Country> mCountriesList;
    static private OnCountryItemClickedListener mCallback;



    public interface OnCountryItemClickedListener {
        void onCountryClicked(String countryName);
    }

    public CountriesListAdapter(Context context, ArrayList<Country> countriesList) {
        this.mContext = context;
        this.mCountriesList = countriesList;
        if( context instanceof  OnCountryItemClickedListener){
            mCallback = (OnCountryItemClickedListener) context;
        }

    }
    public CountriesListAdapter(Context context) {
        this.mContext = context;
        if( context instanceof  OnCountryItemClickedListener){
            mCallback = (OnCountryItemClickedListener) context;
        }
    }

    @Override
    public CountryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_country, parent, false);
        return new CountryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CountryViewHolder holder, int position) {
        Country country = getCountryByIndex(position);
        holder.view.setTag(country);
        holder.mNameTextView.setText(country.getThreeAlphaCode());
        Glide.with(mContext)
                .load(country.getFlagURL())
                .apply(new RequestOptions().optionalCircleCrop())
                .into(holder.mFlagImageView);


    }

    @Override
    public int getItemCount() {
        if (mCountriesList == null) return 0;
        else return mCountriesList.size();
    }


    private Country getCountryByIndex(int index) {
        if (mCountriesList == null) return null;
        else return mCountriesList.get(index);
    }

    public void setCountriesList(List<Country> mCountriesList) {
        this.mCountriesList = mCountriesList;
        notifyDataSetChanged();
    }

    public static class CountryViewHolder extends RecyclerView.ViewHolder
                    implements RecyclerView.OnClickListener{
        private ImageView mFlagImageView;
        private TextView mNameTextView;
        private View view;

        public CountryViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            view.setOnClickListener(this);
            mFlagImageView = itemView.findViewById(R.id.flag);
            mNameTextView = itemView.findViewById(R.id.country_name);
        }

        @Override
        public void onClick(View view) {
            Log.d(TAG, "onClick: ");
            String name = ((Country)view.getTag()).getName();
            mCallback.onCountryClicked(name);
        }
    }
}