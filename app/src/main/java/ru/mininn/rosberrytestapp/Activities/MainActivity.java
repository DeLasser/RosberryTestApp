package ru.mininn.rosberrytestapp.Activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import ru.mininn.rosberrytestapp.Fragments.WeatherListFragment;
import ru.mininn.rosberrytestapp.Fragments.WeatherSearchFragment;
import ru.mininn.rosberrytestapp.R;

/*
        Expected that
        BottomNavigationView should save its instance state and the selected tab should be restored
        but
        BottomNavigationView always selects the first tab after configuration change
        and so saving InstanceState is imposible.
        I'll try to fix it later if it's required to do.

        I should do costants for preferences keys, but it,s so little project.
 */

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {


    private BottomNavigationView mBottomNavigationView;
    private GoogleMap mGoogleMap;
    private Toolbar mToolbar;
    private MenuItem mSearchMenuItem;
    private SearchView mSearchView;
    private WeatherSearchFragment mWeatherSearchFragment;
    private SupportMapFragment mMapFragment;
    private FragmentTransaction mFragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getApplicationInfo().flags = ApplicationInfo.FLAG_DEBUGGABLE;

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mBottomNavigationView = (BottomNavigationView)
                findViewById(R.id.navigation);
        mBottomNavigationView.inflateMenu(R.menu.menu_bottom_navigation);

        mWeatherSearchFragment = new WeatherSearchFragment();
        replaceFragment(mWeatherSearchFragment, true);


        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_first_tab:
                        replaceFragment(mWeatherSearchFragment, false);
                        mSearchMenuItem.setVisible(true);
                        mToolbar.setTitle(R.string.search);
                        break;
                    case R.id.action_second_tab:
                        replaceFragment(new WeatherListFragment(), false);
                        mToolbar.setTitle(R.string.list);
                        mSearchMenuItem.setVisible(false);
                        break;
                    case R.id.action_third_tab:
                        mToolbar.setTitle(R.string.map);
                        initMapFragment();
                        break;
                }
                return true;
            }
        });


    }

    private void initMapFragment() {

        if (mMapFragment == null) {
            mMapFragment = new SupportMapFragment();
        }
        replaceFragment(mMapFragment, false);
        mSearchMenuItem.setVisible(false);
        mMapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
            return;
        }
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 0: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // mGoogleMap ask for this request...
                        return;
                    }
                    mGoogleMap.setMyLocationEnabled(true);
                    mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.premissions_denied), Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        mSearchMenuItem = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) mSearchMenuItem.getActionView();
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (isOnline(getApplicationContext())) {
                    mWeatherSearchFragment.startSearch(query);
                }else {
                    Toast.makeText(getApplicationContext(), R.string.no_internet,Toast.LENGTH_LONG).show();
                }
                mToolbar.collapseActionView();

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        MenuItem exitAction = menu.findItem(R.id.action_exit);
        exitAction.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                SharedPreferences prefs = getSharedPreferences("Prefs", MODE_PRIVATE);
                SharedPreferences.Editor prefEdit = prefs.edit();
                prefEdit.putString("CurrentUser",null);
                prefEdit.commit();
                Intent myIntent = new Intent(MainActivity.this,LoginActivity.class);
                MainActivity.this.startActivity(myIntent);
                MainActivity.this.finish();
                return false;
            }
        });

        return true;
    }

    public static boolean isOnline(Context context)
    {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting())
        {
            return true;
        }
        return false;
    }

    private void replaceFragment(Fragment fragment, boolean isNeedAddToBackStack) {
        mFragmentTransaction = getSupportFragmentManager().beginTransaction();
        mFragmentTransaction.replace(R.id.container, fragment);
        if (isNeedAddToBackStack) {
            mFragmentTransaction.addToBackStack(null);
        }
        mFragmentTransaction.commit();
    }



}
