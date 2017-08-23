package azeddine.project.summer.dasBild.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

import azeddine.project.summer.dasBild.R;
import azeddine.project.summer.dasBild.adapters.CountriesListAdapter;
import azeddine.project.summer.dasBild.adapters.CountryAlbumAdapter;
import azeddine.project.summer.dasBild.fragments.CountriesListFragment;
import azeddine.project.summer.dasBild.fragments.CountryAlbumFragment;
import azeddine.project.summer.dasBild.objectsUtils.Country;
import azeddine.project.summer.dasBild.objectsUtils.KeysUtil;
import azeddine.project.summer.dasBild.objectsUtils.Photo;

public class MainActivity extends AppCompatActivity implements
        CountriesListAdapter.OnCountryItemClickedListener , CountriesListAdapter.OnCountryItemLongClickedListener,
        CountryAlbumAdapter.OnPhotoClickedListener, NavigationView.OnNavigationItemSelectedListener,TabLayout.OnTabSelectedListener{

    private static final String TAG = "MainActivity";
    public static final String DEFAULT_REGION_NAME = "Arab";
    public static final String DEFAULT_CATEGORY = "Landscapes";

    private  Toolbar mToolbar ;
    private  DrawerLayout mDrawer ;
    private  NavigationView mNavigationView;
    private  TabLayout mAlbumCategoriesTabLayout;

    private String currentCountryName;
    private String currentCategoryName = DEFAULT_CATEGORY;
    private String currentRegionName = DEFAULT_REGION_NAME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);
        mDrawer.setStatusBarBackground(R.color.colorPrimaryDark);
        toggle.syncState();

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);

        mAlbumCategoriesTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        ArrayList<String> albumCategories = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.albums_categories)) );
        for (String category:albumCategories){
            mAlbumCategoriesTabLayout.addTab(mAlbumCategoriesTabLayout.newTab().setText(category));
        }
        mAlbumCategoriesTabLayout.addOnTabSelectedListener(this);

        if(savedInstanceState !=null) {
            currentCategoryName = savedInstanceState.getString(KeysUtil.CATEGORY_NAME_KEY);
            currentCountryName = savedInstanceState.getString(KeysUtil.ALBUM_NAME_KEY);
            currentRegionName = savedInstanceState.getString(KeysUtil.REGION_NAME_KEY);

            mAlbumCategoriesTabLayout.getTabAt(albumCategories.indexOf(currentCategoryName)).select();
        }



    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed: ");
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FragmentManager fm = getSupportFragmentManager();

        if(fm.findFragmentByTag(CountriesListFragment.TAG) == null) startCountriesListFragment(DEFAULT_REGION_NAME);
        if(fm.findFragmentByTag(CountryAlbumFragment.TAG) == null) startAlbumFragment(DEFAULT_REGION_NAME,DEFAULT_CATEGORY);


    }

    @Override
    public void onCountryClicked(String countryName) {
        Log.d(TAG, "onCountryClicked: ");
        currentCountryName = countryName;
        startAlbumFragment(currentCountryName, currentCategoryName);
    }

    @Override
    public void onPhotoClicked(Photo photo,ImageView sharedImageView) {
        Log.d(TAG, "onPhotoClicked: ");

        Intent intent = new Intent(this,PhotoProfileActivity.class);
        intent.putExtra("Photo",photo);
        intent.putExtra("sharedPhoto", ViewCompat.getTransitionName(sharedImageView));

        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this,
                sharedImageView,
                ViewCompat.getTransitionName(sharedImageView));

        startActivity(intent,options.toBundle());
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        final int groupId= item.getGroupId();
        int id = item.getItemId();
        String selectedRegionTitle ;
        if(groupId == R.id.regions){
            switch (id){
                case R.id.arab_region:
                    selectedRegionTitle = getResources().getStringArray(R.array.regions_search_names)[0];
                    break;
                case R.id.europe_region:
                    selectedRegionTitle = getResources().getStringArray(R.array.regions_search_names)[1];
                    break;
                case R.id.africa_region:
                    selectedRegionTitle = getResources().getStringArray(R.array.regions_search_names)[2];
                    break;
                case R.id.asia_region:
                    selectedRegionTitle = getResources().getStringArray(R.array.regions_search_names)[3];
                    break;
                case R.id.america_region:
                    selectedRegionTitle = getResources().getStringArray(R.array.regions_search_names)[4];
                    break;
                case R.id.south_america_region:
                    selectedRegionTitle = getResources().getStringArray(R.array.regions_search_names)[5];
                    break;
                default:
                    selectedRegionTitle = getResources().getStringArray(R.array.regions_search_names)[0];
            }
            if(!selectedRegionTitle.equalsIgnoreCase(currentRegionName)){
                currentRegionName = selectedRegionTitle;
                currentCountryName = currentRegionName;
                startCountriesListFragment(selectedRegionTitle);
                startAlbumFragment(selectedRegionTitle,currentCategoryName);
            }
        }
        mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KeysUtil.CATEGORY_NAME_KEY, currentCategoryName);
        outState.putString(KeysUtil.ALBUM_NAME_KEY, currentCountryName);
        outState.putString(KeysUtil.REGION_NAME_KEY, currentRegionName);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        Log.d(TAG, "onTabSelected: ");
        currentCategoryName = tab.getText().toString();
        startAlbumFragment(currentCountryName, currentCategoryName);

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        Log.d(TAG, "onTabUnselected: ");
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        Log.d(TAG, "onTabReselected: ");
    }

    private void startAlbumFragment(String name,String category){
        Bundle args = new Bundle();
        args.putString(KeysUtil.ALBUM_NAME_KEY,name);
        args.putString(KeysUtil.CATEGORY_NAME_KEY,category);

        CountryAlbumFragment countryAlbumFragment= new CountryAlbumFragment();
        countryAlbumFragment.setArguments(args);


        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_country_album_container,countryAlbumFragment,CountryAlbumFragment.TAG)
                .commit();
    }

    private void startCountriesListFragment(String region){
        Bundle args = new Bundle();
        args.putString(KeysUtil.REGION_NAME_KEY,region);
        CountriesListFragment countriesListFragment = new CountriesListFragment();
        countriesListFragment.setArguments(args);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_countries_list_container,countriesListFragment,CountriesListFragment.TAG)
                .commit();
    }

    @Override
    public void onCountryLongClicked(Country country, ImageView sharedImageView) {
        Toast.makeText(this, "long clicked", Toast.LENGTH_SHORT).show();
    }
}
