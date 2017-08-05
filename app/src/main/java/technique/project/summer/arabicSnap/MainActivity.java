package technique.project.summer.arabicSnap;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements CountriesListAdapter.OnCountryItemClickedListener {
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        Log.d(TAG, "onStart: ");
        FragmentManager fm = getSupportFragmentManager();
        super.onStart();

        if(fm.findFragmentByTag(CountriesListFragment.TAG) == null){
            fm.beginTransaction()
                    .add(R.id.fragment_countries_list_container,new CountriesListFragment(),CountriesListFragment.TAG)
                    .commit();
        }



        if(fm.findFragmentByTag(CountryAlbumFragment.TAG) == null){
            Bundle args = new Bundle();
            args.putString(CountryAlbumFragment.ALBUM_NAME_KEY,"Algeria");
            CountryAlbumFragment countryAlbumFragment= new CountryAlbumFragment();
            countryAlbumFragment.setArguments(args);
            fm.beginTransaction()
                    .add(R.id.fragment_country_album_container,countryAlbumFragment,CountryAlbumFragment.TAG)
                    .commit();
        }

    }

    @Override
    public void onCountryClicked(String countryName) {
        Log.d(TAG, "onCountryClicked: ");
        Bundle args = new Bundle();

        args.putString(CountryAlbumFragment.ALBUM_NAME_KEY,countryName);
        CountryAlbumFragment countryAlbumFragment= new CountryAlbumFragment();
        countryAlbumFragment.setArguments(args);


        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_country_album_container,countryAlbumFragment,CountryAlbumFragment.TAG)
                .commit();
    }
}
