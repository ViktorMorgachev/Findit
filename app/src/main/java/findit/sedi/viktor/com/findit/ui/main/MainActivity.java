package findit.sedi.viktor.com.findit.ui.main;

import android.Manifest;
import android.annotation.TargetApi;
import android.arch.lifecycle.Lifecycle;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import com.squareup.otto.Subscribe;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import findit.sedi.viktor.com.findit.R;
import findit.sedi.viktor.com.findit.common.ManagersFactory;
import findit.sedi.viktor.com.findit.common.Util;
import findit.sedi.viktor.com.findit.common.background_services.MyWorker;
import findit.sedi.viktor.com.findit.common.QrPointManager;
import findit.sedi.viktor.com.findit.data_providers.cloud.myserver.ServerManager;
import findit.sedi.viktor.com.findit.data_providers.data.QrPoint;
import findit.sedi.viktor.com.findit.data_providers.data.User;
import findit.sedi.viktor.com.findit.presenter.otto.FinditBus;
import findit.sedi.viktor.com.findit.presenter.otto.events.PlaceAboutEvent;
import findit.sedi.viktor.com.findit.presenter.otto.events.UpdateAllQrPoints;
import findit.sedi.viktor.com.findit.presenter.otto.events.UpdatePlayersLocations;
import findit.sedi.viktor.com.findit.ui.about_place.PlaceAboutActivity;
import findit.sedi.viktor.com.findit.ui.find_tainik.DiscoveredTainitDialogue;
import findit.sedi.viktor.com.findit.ui.main.common.CommonMapManager;
import findit.sedi.viktor.com.findit.ui.main.fragments.maps.GoogleMapFragment;
import findit.sedi.viktor.com.findit.ui.main.interfaces.MapsFragmentListener;
import findit.sedi.viktor.com.findit.ui.profile_info.ProfileInfoActivity;
import findit.sedi.viktor.com.findit.ui.rating.RatingActivity;
import findit.sedi.viktor.com.findit.ui.scanner_code.QRCodeCameraActivity;
import findit.sedi.viktor.com.findit.ui.tournament.TounamentActivity;

import static findit.sedi.viktor.com.findit.interactors.KeyCommonSettings.KeysField.LOG_TAG;
import static findit.sedi.viktor.com.findit.ui.about_place.PlaceAboutActivity.KEY_PLACE_ID;

/**
 * Главная активность которая будет управлять другими активностями возможно с помощью Cicerone
 */

public class MainActivity extends AppCompatActivity implements LocationListener, NavigationView.OnNavigationItemSelectedListener, MapsFragmentListener {


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
    private LatLng mLastLocation;
    private QrPointManager mQrPointManager = ManagersFactory.getInstance().getQrPointManager();
    private FragmentManager mFragmentManager = getSupportFragmentManager();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);

        Toast.makeText(this, "Activity was Created,  Player: " + ManagersFactory.getInstance().getAccountManager().getUser().getName(), Toast.LENGTH_LONG).show();


        FinditBus.getInstance().register(this);

        setContentView(R.layout.activity_main);

        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        mPeriodicWorkRequest =
                new PeriodicWorkRequest.Builder(MyWorker.class, 6000, TimeUnit.SECONDS)
                        .addTag("periodic_work").setConstraints(constraints).build();

        WorkManager.getInstance().enqueue(mPeriodicWorkRequest);

        mFloatingActionButton = findViewById(R.id.floating_action_button);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocation();
            }
        });


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        mNavTextViewName = navigationView.getHeaderView(0).findViewById(R.id.tv_profile_name);


        mCommonMapManager = CommonMapManager.getInstance();
        mCommonMapManager.setServiceType(CommonMapManager.ServiceType.GOOGLE);
        mCommonMapManager.setContext(this);


        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        initLocationCallback();

        getLocation();
        // Тут получаем значение из процесса используя LiveData, и обновляем точки
        //WorkManager.getInstance().getWorkInfosForUniqueWorkLiveData();
        // Показываем информацию, анимацию загрузки карты, пока карта гугл не загрузится
        if (mCommonMapManager.getServiceType().equals(CommonMapManager.ServiceType.GOOGLE)) {

            mGoogleMapFragment = GoogleMapFragment.getInstance();

            mCommonMapManager.addGoogleFragment(mGoogleMapFragment);

            mFragmentManager.beginTransaction()
                    .replace(R.id.map_fragment, mGoogleMapFragment)
                    .addToBackStack("null")
                    .commit();

        }

    }


    @Override
    protected void onResume() {
        super.onResume();



        // Initialize FusedLocationClient

        if (ManagersFactory.getInstance().getAccountManager().getUser() != null) {
            mNavTextViewName.setText(ManagersFactory.getInstance().getAccountManager().getUser().getName());
        }

        Toast.makeText(this, "Activity was Resumed", Toast.LENGTH_LONG).show();


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

                    //  updateMap(DEFAULT_ZOOM, "");

                    // Изменяем координаты пользователя

                    ManagersFactory.getInstance().getAccountManager().getUser().setGeopoint(sLatLng.latitude, sLatLng.longitude);

                    // Отправляем на сервер если расстояние изменилось более чем на 50м
                    if (mLastLocation != null && Util.getInstance().getDistance(sLatLng, mLastLocation) > 50)
                        ServerManager.getInstance().updateUserOnServer("location");


                    // checkMapForPlaces();


                    break;
                }

            }
        };
    }


    // Сюда попадают тольеко те указатели, которые относятся к нашему турниру
    @Subscribe
    public void chechNearbyQrPlace(PlaceAboutEvent placeAboutEvent) {


        if (getLifecycle().getCurrentState() != Lifecycle.State.DESTROYED) {


            QrPoint qrPoint = ManagersFactory.getInstance().getQrPointManager().getQrPlaceByID(placeAboutEvent.getID());
            User user = ManagersFactory.getInstance().getAccountManager().getUser();
            boolean discovered = false; // Открывали ли ранее
            boolean fond = false; // Находили ли ранее?


            Toast.makeText(getApplicationContext(), "Вы рядом с возможно новым тайником", Toast.LENGTH_LONG).show();

            // Если тайник новый и мы его не обнаруживали и не находили,  то показывает диалоговое окно

            if (qrPoint.getMark().equalsIgnoreCase("none")) {

                // Запускаем активность и отправляем ID в неё
                Intent intent = new Intent(this, PlaceAboutActivity.class);
                intent.putExtra(KEY_PLACE_ID, qrPoint.getID());
                startActivity(intent);

                // Помечаем у себя в списке QrPoints что она detecteds
                ManagersFactory.getInstance().getQrPointManager().getQrPlaceByID(placeAboutEvent.getID()).setMark("detected");

                // Одновременно меняем статус на сервере что на это место набрели
                ServerManager.getInstance().sendCode(qrPoint.getID(), "detected");

                // Синхронизация с сервером
                ServerManager.getInstance().updateUserOnServer("fonded_qrpoint");

                // Обновление информации на карте
                updatePoint(qrPoint.getID(), "detected");


            } else { // Иначе его обнаруживали, так как статус не none
                // Проверям обнаруживали ли мы ранее это точку?
                for (int i = 0; i < user.getDiscoveredQrPointIDs().size(); i++) {
                    if (user.getDiscoveredQrPointIDs().get(i).equalsIgnoreCase(qrPoint.getID())) {
                        discovered = true;
                        Toast.makeText(getApplicationContext(), "Вы его обнануживали ранее, можете нажать на вопрос для подсказки", Toast.LENGTH_LONG).show();

                        break;
                    }
                }

                if (!discovered) {
                    for (int i = 0; i < user.getFondedQrPointsIDs().size(); i++) {
                        if (user.getFondedQrPointsIDs().get(i).equalsIgnoreCase(qrPoint.getID())) {
                            fond = true;
                            Toast.makeText(getApplicationContext(), "Вы его находили ранее", Toast.LENGTH_LONG).show();
                            break;
                        }
                    }
                }

                // Значит набрели на знак вопроса, который кто-то уже его обнаруживал
                if (!fond && !discovered) {


                    // Запускаем активность и отправляем ID в неё
                   /* Intent intent = new Intent(this, PlaceAboutActivity.class);
                    intent.putExtra(KEY_PLACE_ID, qrPoint.getID());
                    startActivity(intent);*/
                    startActivity(new Intent(this, DiscoveredTainitDialogue.class));

                    Toast.makeText(getApplicationContext(), "Кто-то обнаружил тайник, попробуйте его найти", Toast.LENGTH_LONG).show();

                    // Сохраняем у себя и на сервере информацию о найденных точках
                    ManagersFactory.getInstance().getAccountManager().getUser().getFondedQrPointsIDs().add(qrPoint.getID());

                    ServerManager.getInstance().updateUserOnServer("fonded_qrpoint");

                }

            }

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
                            ManagersFactory.getInstance().getAccountManager().getUser().setGeopoint(sLatLng.latitude, sLatLng.longitude);
                            // Отправляем на сервер

                            ServerManager.getInstance().updateUserOnServer("location");

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
            startActivity(new Intent(this, ProfileInfoActivity.class));
            // Handle the camera action
        } else if (id == R.id.nav_tournaments) {
            startActivity(new Intent(this, TounamentActivity.class));
        } else if (id == R.id.nav_rating) {
            startActivity(new Intent(this, RatingActivity.class));
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

    @Subscribe
    public void updatePlayerLocation(UpdatePlayersLocations updatePlayersLocations) {

        if (this.getLifecycle().getCurrentState() != Lifecycle.State.DESTROYED) {
            mCommonMapManager.updatePlayers();
            Toast.makeText(this, "Players locations updated", Toast.LENGTH_SHORT).show();
        }

    }

    @Subscribe
    public void updateQrPointsOnMap(UpdateAllQrPoints updateAllQrPoints) {

        if (this.getLifecycle().getCurrentState() != Lifecycle.State.DESTROYED) {

            Toast.makeText(this, "QrPoints updated", Toast.LENGTH_SHORT).show();
            mCommonMapManager.initPoints(ManagersFactory.getInstance().getQrPointManager().getQrPlaces());
        }

    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Activity was destroed", Toast.LENGTH_LONG).show();
        WorkManager.getInstance().cancelAllWork();
        FinditBus.getInstance().unregister(this);
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }

    // Будем обновлять по ID только не обходимую точку
    private void updatePoint(String ID, String mark) {

        CommonMapManager.getInstance().updatePoint(ID, mark);

    }

    @Override
    public void mapReady() {

        if (sLatLng != null) {
            updateMap(DEFAULT_ZOOM, "");

            // Изменяем координаты пользователя
            ManagersFactory.getInstance().getAccountManager().getUser().setGeopoint(sLatLng.latitude, sLatLng.longitude);
            // Отправляем на сервер
            ServerManager.getInstance().updateUserOnServer("location");
            ServerManager.getInstance().updateUserOnServer("net_status");
        }

        // И потом только по айди будем меняять состояние меток (показывать, скрывать. и.т.д)
        if (!ManagersFactory.getInstance().getQrPointManager().getQrPlaces().isEmpty()) {

        }
        ;
        //    CommonMapManager.getInstance().initPoints(ManagersFactory.getInstance().getQrPointManager().getQrPlaces());

    }
}
