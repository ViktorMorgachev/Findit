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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import findit.sedi.viktor.com.findit.R;
import findit.sedi.viktor.com.findit.common.ManagersFactory;
import findit.sedi.viktor.com.findit.common.background_services.MyWorker;
import findit.sedi.viktor.com.findit.common.PlaceManager;
import findit.sedi.viktor.com.findit.data_providers.cloud.myserver.ServerManager;
import findit.sedi.viktor.com.findit.ui.about_place.PlaceAboutActivity;
import findit.sedi.viktor.com.findit.ui.main.common.CommonMapManager;
import findit.sedi.viktor.com.findit.ui.main.fragment.GoogleMapFragment;
import findit.sedi.viktor.com.findit.ui.profile.ProfileActivity;
import findit.sedi.viktor.com.findit.ui.scanner_code.QRCodeCameraActivity;
import findit.sedi.viktor.com.findit.ui.tournament.TounamentActivity;

import static findit.sedi.viktor.com.findit.ui.about_place.PlaceAboutActivity.KEY_PLACE_ID;

/**
 * Главная активность которая будет управлять другими активностями возможно с помощью Cicerone
 */

public class MainActivity extends AppCompatActivity implements LocationListener, NavigationView.OnNavigationItemSelectedListener {


    //Widgets
    private GoogleMap mMap;
    private Fragment mFragment;
    private FloatingActionButton mFloatingActionButton;
    // TODO снова запускаем лишь тогда когда игра активна
    private PeriodicWorkRequest mPeriodicWorkRequest;
    private TextView mNavTextViewName;


    //Values
    private FusedLocationProviderClient mFusedLocationClient;
    private final int DEFAULT_ZOOM = 15;
    private LocationResult mLocationResult;
    private LocationCallback mLocationCallback;
    private final int REQUEST_LOCATION_PERMISSION = 134;
    private static final String[] permissions = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
    public static LatLng sLatLng;
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
            if (sLatLng != null) {
                updateMap(DEFAULT_ZOOM, "");

                // Изменяем координаты пользователя
                ManagersFactory.getInstance().getAccountManager().getUser().setGeopoint(sLatLng.latitude, sLatLng.longitude);
                // Отправляем на сервер
                ServerManager.getInstance().updateUserOnServer("location");
            }

            // И потом только по айди будем меняять состояние меток (показывать, скрывать. и.т.д)
            if (!ManagersFactory.getInstance().getPlaceManager().getPlaces().isEmpty())
                CommonMapManager.getInstance().initPoints(ManagersFactory.getInstance().getPlaceManager().getPlaces());
        }
    };


    //


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Востановить необходимые данные с сервера
        if (ManagersFactory.getInstance().getAccountManager().getUser() == null)
            restoreDataFromServer();


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        mPeriodicWorkRequest =
                new PeriodicWorkRequest.Builder(MyWorker.class, 5000, TimeUnit.SECONDS)
                        .addTag("periodic_work").build();

        mFloatingActionButton = findViewById(R.id.floating_action_button);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocation();
            }
        });


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        if (ManagersFactory.getInstance().getAccountManager().getUser() != null) {
            mNavTextViewName = navigationView.getHeaderView(0).findViewById(R.id.tv_profile_name);
            mNavTextViewName.setText(ManagersFactory.getInstance().getAccountManager().getUser().getName());
        }


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


        WorkManager.getInstance().enqueue(mPeriodicWorkRequest);

        // Тут получаем значение из процесса используя LiveData, и обновляем точки
        //WorkManager.getInstance().getWorkInfosForUniqueWorkLiveData();


    }

    private void restoreDataFromServer() {

        // Небольшой Хак, при востановлении работы, нужно проинициализировать пользователя, если он null

        ManagersFactory.getInstance().getAccountManager().updateUserByEmail(FirebaseAuth.getInstance().getCurrentUser().getEmail(), null);


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

                    updateMap(DEFAULT_ZOOM, "");

                    // Изменяем координаты пользователя

                    if (ManagersFactory.getInstance().getAccountManager().getUser() == null)
                        restoreDataFromServer();
                    else {

                        ManagersFactory.getInstance().getAccountManager().getUser().setGeopoint(sLatLng.latitude, sLatLng.longitude);
                        // Отправляем на сервер
                        ServerManager.getInstance().updateUserOnServer("location");

                    }

                    // checkMapForPlaces();


                    break;
                }


            }
        };
    }

    // Этот метод вытаскивает ID если место на карте существует, по которому запустим активность для показа подсказки о месте
    // Иначе обновим карту поставим дополнительные точки, (соответствующие метки)
    private void checkMapForPlaces() {


        String id = mPlaceManager.getValidIDsOfPlaced(sLatLng);

        if (id != null && !id.equalsIgnoreCase("")) {
            // Запускаем активность и отправляем ID в неё
            // Одновременно меняем статус на сервере что на это место набрели
            Intent intent = new Intent(this, PlaceAboutActivity.class);
            intent.putExtra(KEY_PLACE_ID, id);
            startActivity(intent);

            ServerManager.getInstance().sendCode(id, "detected");
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
        mCommonMapManager.updateMe(sLatLng, R.drawable.ic_location_24dp);
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
                            // Изменяем координаты пользователя
                            if (ManagersFactory.getInstance().getAccountManager().getUser() == null)
                                restoreDataFromServer();
                            else {
                                ManagersFactory.getInstance().getAccountManager().getUser().setGeopoint(sLatLng.latitude, sLatLng.longitude);
                                // Отправляем на сервер
                                ServerManager.getInstance().updateUserOnServer("location");
                            }


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
        } else if (id == R.id.nav_tournaments) {
            startActivity(new Intent(this, TounamentActivity.class));
        }
            /*else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }*/


        // TODO нудно будет доработать в будущем, если активной игры нет, то показывать
        //  уведомление, что активных игр нет пока и не запускать
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
        Toast.makeText(this, "Activity was stoped", Toast.LENGTH_LONG).show();
        WorkManager.getInstance().cancelAllWork();
        ServerManager.getInstance().changeUserNetStatus(false);
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }

    // Будем обновлять по ID только не обходимую точку
    private void updatePoint(long ID, long mark) {

        CommonMapManager.getInstance().updatePoint(ID, mark);

    }
}
