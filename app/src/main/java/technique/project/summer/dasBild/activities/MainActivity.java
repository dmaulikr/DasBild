package technique.project.summer.dasBild.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import technique.project.summer.dasBild.R;
import technique.project.summer.dasBild.adapters.CountriesListAdapter;
import technique.project.summer.dasBild.adapters.CountryAlbumAdapter;
import technique.project.summer.dasBild.fragments.CountriesListFragment;
import technique.project.summer.dasBild.fragments.CountryAlbumFragment;
import technique.project.summer.dasBild.objectsUtils.Photo;

public class MainActivity extends AppCompatActivity implements
        CountriesListAdapter.OnCountryItemClickedListener , CountryAlbumAdapter.OnPhotoClickedListener,
                                NavigationView.OnNavigationItemSelectedListener{

    private static final String TAG = "MainActivity";

    private  Toolbar mToolbar ;
    private  DrawerLayout mDrawer ;
    private  NavigationView navigationView;
    private  TabLayout mAlbumCategoriesTabLayout;
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

        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mAlbumCategoriesTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        String[] albumCategories = getResources().getStringArray(R.array.album_categories);
        for (String category:albumCategories){
            mAlbumCategoriesTabLayout.addTab(mAlbumCategoriesTabLayout.newTab().setText(category));
        }

    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onStart() {
        Log.d(TAG, "onStart: ");
        super.onStart();
        FragmentManager fm = getSupportFragmentManager();
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

    @Override
    public void onPhotoClicked(Photo photo) {
        Log.d(TAG, "onPhotoClicked: ");
        Intent intent = new Intent(MainActivity.this,PhotoProfileActivity.class);
        intent.putExtra("Photo",photo);
        startActivity(intent);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        mDrawer.closeDrawer(GravityCompat.START);
        return false;
    }
}
