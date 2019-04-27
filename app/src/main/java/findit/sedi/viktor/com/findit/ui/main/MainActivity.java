package findit.sedi.viktor.com.findit.ui.main;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

import findit.sedi.viktor.com.findit.R;
import findit.sedi.viktor.com.findit.common.ManagersFactory;
import findit.sedi.viktor.com.findit.data.PlaceManager;
import findit.sedi.viktor.com.findit.ui.about_place.PlaceAboutActivity;
import findit.sedi.viktor.com.findit.ui.main.common.CommonMapManager;
import findit.sedi.viktor.com.findit.ui.main.fragment.GoogleMapFragment;
import findit.sedi.viktor.com.findit.ui.profile.ProfileActivity;
import findit.sedi.viktor.com.findit.ui.scanner_code.QRCodeCameraActivity;

import static findit.sedi.viktor.com.findit.ui.about_place.PlaceAboutActivity.KEY_PLACE_ID;

public class MainActivity extends AppCompatActivity implements LocationListener, NavigationView.OnNavigationItemSelectedListener {


    //Widgets
    private GoogleMap mMap;
    private Fragment mFragment;
    private FloatingActionButton mFloatingActionButton;

    //Values
    private FusedLocationProviderClient mFusedLocationClient;
    private final int DEFAULT_ZOOM = 15;
    private LocationResult mLocationResult;
    private LocationCallback mLocationCallback;
    private final double fixedDistance = 1000;
    private final int REQUEST_LOCATION_PERMISSION = 134;
    private static final String[] permissions = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
    private static LatLng sLatLng;
    private List<MarkerOptions> mMarkerOptions = new ArrayList<>();
    private GoogleMapFragment mGoogleMapFragment;
    private CommonMapManager mCommonMapManager;
    private PlaceManager mPlaceManager = ManagersFactory.getInstance().getPlaceManager();
    private FragmentManager mFragmentManager = getSupportFragmentManager();
    private EventsListener mEventsListener = new EventsListener() {

        @Override
        public void findMe() {

        }

        @Override
        public void showPassedPoints() {

        }

        @Override
        public void addedPoint() {

        }

        @Override
        public void showMenu() {

        }

        @Override
        public void showOptions() {

        }

        @Override
        public void mapReady() {
            if (sLatLng != null)
                updateMap(DEFAULT_ZOOM, "");
        }
    };


    //


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);




        mFloatingActionButton = findViewById(R.id.floating_action_button);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocation();
            }
        });


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mCommonMapManager = CommonMapManager.getInstance();
        mCommonMapManager.setServiceType(CommonMapManager.ServiceType.GOOGLE);
        mCommonMapManager.setContext(this);


        initLocationCallback();

        // Initialize FusedLocationClient
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


        // Показываем информацию, анимацию загрузки карты, пока карта гугл не загрузится

        if (mCommonMapManager.getServiceType().equals(CommonMapManager.ServiceType.GOOGLE)) {

            mGoogleMapFragment = GoogleMapFragment.getInstance();

            mCommonMapManager.addGoogleFragment(mGoogleMapFragment);

            mFragmentManager.beginTransaction()
                    .replace(R.id.map_fragment, mGoogleMapFragment)
                    .addToBackStack("null")
                    .commit();
            mGoogleMapFragment.setEventsListener(mEventsListener);

        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        getLocation();
    }


    private void initLocationCallback() {
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                //checkInternetConnection(locationResult);
                mLocationResult = locationResult;

                for (Location location : locationResult.getLocations()) {
                    // sLatLng = new LatLng(mLocationResult.getLastLocation().getLatitude(), mLocationResult.getLastLocation().getLatitude());
                    // Проверка расстояния между точками и соответтсующее оповешение в виде тоста
                    sLatLng = new LatLng(location.getLatitude(), location.getLongitude());

                    checkMapForPlaces();

                    updateMap(DEFAULT_ZOOM, "");
                    break;
                }


            }
        };
    }

    // Этот метод вытаскивает ID если место на карте существует, по которому запустим активность для показа подсказки о месте
    private void checkMapForPlaces() {
        long id = mPlaceManager.getIDsPlaceIfValid(sLatLng);

        if (id != 0) {
            // Запускаем активность и отправляем ID в неё
            Intent intent = new Intent(this, PlaceAboutActivity.class);
            intent.putExtra(KEY_PLACE_ID, id);
            startActivity(intent);
        }


    }


    private void initMapLongClick() {

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                // Сначала при каждом удалении удаляем предыдущую, метку
                // Ставим точку на карту


                updateMap(DEFAULT_ZOOM, "");
            }
        });

    }


    private void updateMap(float zoom, String tittle) {
        mCommonMapManager.zoomTo(sLatLng, DEFAULT_ZOOM);
        mCommonMapManager.addPoint(sLatLng, R.drawable.ic_location_24dp, null);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        // Check if location permissions are granted and if so enable the
        // location data layer.
        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSION:
                for (int result : grantResults)
                    if (result == PackageManager.PERMISSION_DENIED) {
                        Toast.makeText(this, getResources().getString(R.string.permition_requarement), Toast.LENGTH_SHORT);
                        return;
                    }
                break;
        }
        getLocation();
    }


    @TargetApi(23)
    private void getLocation() {

        for (String permition : permissions) {
            if (ContextCompat.checkSelfPermission(this, permition) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, permissions, REQUEST_LOCATION_PERMISSION);
                return;
            }

        }

        // Один раз получаем пестоположение
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            sLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                            updateMap(DEFAULT_ZOOM, "");
                        }
                    }
                });

        mFusedLocationClient.requestLocationUpdates(getLocationRequest(), mLocationCallback, null);
    }

    private LocationRequest getLocationRequest() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return locationRequest;
    }


    @Override
    public void onLocationChanged(Location location) {
        Toast.makeText(this, "Location changed", Toast.LENGTH_LONG).show();
        sLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        mCommonMapManager.zoomTo(sLatLng, DEFAULT_ZOOM);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        int id = menuItem.getItemId();

        if (id == R.id.nav_profile) {
            startActivity(new Intent(this, ProfileActivity.class));
            // Handle the camera action
        } /*else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }*/

        if (id == R.id.nav_scan_code) {
            startActivity(new Intent(this, QRCodeCameraActivity.class));
            // Handle the camera action
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return false;
    }


    public interface EventsListener {

        void findMe();

        // Показать зелёные точки на карте, где побывал пользователь
        void showPassedPoints();

        void addedPoint();

        void showMenu();

        void showOptions();

        void mapReady();

    }


    @Override
    protected void onStop() {
        super.onStop();
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }
}
