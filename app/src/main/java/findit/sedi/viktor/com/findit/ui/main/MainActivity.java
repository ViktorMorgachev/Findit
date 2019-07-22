package findit.sedi.viktor.com.findit.ui.main;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.squareup.otto.Subscribe;

import findit.sedi.viktor.com.findit.App;
import findit.sedi.viktor.com.findit.R;
import findit.sedi.viktor.com.findit.common.background_services.MyService;
import findit.sedi.viktor.com.findit.common.dialogs.DialogManager;
import findit.sedi.viktor.com.findit.common.interfaces.ILocationListener;
import findit.sedi.viktor.com.findit.data_providers.cloud.myserver.ServerManager;
import findit.sedi.viktor.com.findit.data_providers.data.User;
import findit.sedi.viktor.com.findit.presenter.otto.FinditBus;
import findit.sedi.viktor.com.findit.presenter.otto.events.UpdateAllQrPoints;
import findit.sedi.viktor.com.findit.presenter.otto.events.UpdatePlayersLocations;
import findit.sedi.viktor.com.findit.ui.about_application.AboutActivity;
import findit.sedi.viktor.com.findit.ui.find_tainik.NearbyTainikActivity;
import findit.sedi.viktor.com.findit.ui.main.common.CommonMapManager;
import findit.sedi.viktor.com.findit.ui.main.interfaces.MapsFragmentListener;
import findit.sedi.viktor.com.findit.ui.profile_info.ProfileInfoActivity;
import findit.sedi.viktor.com.findit.ui.rating.RatingActivity;
import findit.sedi.viktor.com.findit.ui.scanner_code.QRCodeCameraActivity;
import findit.sedi.viktor.com.findit.ui.tournament.TounamentActivity;
import ru.terrakok.cicerone.Navigator;
import ru.terrakok.cicerone.commands.Command;

import static findit.sedi.viktor.com.findit.interactors.KeyCommonSettings.KeysField.LOG_TAG;
import static findit.sedi.viktor.com.findit.interactors.KeyCommonUpdateUserRequests.KeysField.KEY_UPDATE_LOCATION;
import static findit.sedi.viktor.com.findit.interactors.KeyCommonUpdateUserRequests.KeysField.KEY_UPDATE_NET_STATUS;
import static findit.sedi.viktor.com.findit.ui.find_tainik.NearbyTainikActivity.POINT_ID;

/**
 * Главная активность которая будет управлять другими активностями возможно с помощью Cicerone
 */

public class MainActivity extends AppCompatActivity implements LocationListener, NavigationView.OnNavigationItemSelectedListener, MapsFragmentListener, ILocationListener {


    //Widgets
    private GoogleMap mMap;
    private FloatingActionButton mFloatingActionButtonLocation;
    private FloatingActionButton mFloatingActionButtonMenu;
    // TODO снова запускаем лишь тогда когда игра активна
    private TextView mNavTextViewName;
    private Context mContext;
    private DrawerLayout mDrawerLayout;
    private ImageView mImageViewIcon;
    private FragmentManager mFragmentManager = getSupportFragmentManager();


    //Values

    private final int DEFAULT_ZOOM = 15;

    private final int REQUEST_LOCATION_PERMISSION = 134;
    private static final String[] permissions = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
    public LatLng sLatLng;
    private CommonMapManager mCommonMapManager;


    private Navigator mNavigator = new Navigator() {
        @Override
        public void applyCommands(Command[] commands) {

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        mContext = this;

        setContentView(R.layout.activity_main);

        FinditBus.getInstance().register(this);


        mFloatingActionButtonLocation = findViewById(R.id.floating_location_button);
        mFloatingActionButtonMenu = findViewById(R.id.floating_menu_button);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mFloatingActionButtonLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocation();
            }
        });

        mFloatingActionButtonMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                } else mDrawerLayout.openDrawer(Gravity.LEFT);
            }
        });


        DialogManager.getInstance().setContext(this);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        mNavTextViewName = navigationView.getHeaderView(0).findViewById(R.id.tv_profile_name);
        mImageViewIcon = navigationView.getHeaderView(0).findViewById(R.id.profile_icon);


        mCommonMapManager = CommonMapManager.getInstance();
        mCommonMapManager.setServiceType(CommonMapManager.ServiceType.GOOGLE);
        mCommonMapManager.setContext(this);
        mCommonMapManager.initMap();

        logUser();


        // Тут получаем значение из процесса используя LiveData, и обновляем точки
        // Показываем информацию, анимацию загрузки карты, пока карта гугл не загрузится


    }

    private void logUser() {

        User user = App.instance.getAccountManager().getUser();
        Crashlytics.setUserIdentifier(user.getID());
        Crashlytics.setUserEmail(user.getEmail());
        Crashlytics.setUserName(user.getName());

    }


    private void showMap() {


        if (mCommonMapManager.getServiceType() == CommonMapManager.ServiceType.GOOGLE) {
            if (mFragmentManager.findFragmentById(R.id.map_fragment) != null) {
                mFragmentManager.beginTransaction()
                        .replace(R.id.map_fragment, mCommonMapManager.getGoogleMap())
                        .commit();
            } else {
                mFragmentManager.beginTransaction()
                        .add(R.id.map_fragment, mCommonMapManager.getGoogleMap())
                        .commit();
            }
        }

        App.instance.getLocationManager().unsubscribe(this);
        startService(new Intent(MainActivity.this, MyService.class));

    }


    @Override
    protected void onResume() {
        super.onResume();

        App.instance.getLocationManager().subscribe(this);

        getLocation();


        User user = App.instance.getAccountManager().getUser();

        Glide.with(this)
                .load(user.getPhotoUrl())
                .error(getResources().getDrawable(R.drawable.ic_account_circle_24dp))
                .apply(RequestOptions.circleCropTransform()).into(mImageViewIcon);

        if (App.instance.getAccountManager().getUser() != null) {
            mNavTextViewName.setText(App.instance.getAccountManager().getUser().getName());
        }

        showMap();

        App.instance.getNavigationHolder().setNavigator(mNavigator);

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

    }


    @TargetApi(23)
    private void getLocation() {

        for (String permition : permissions) {
            if (ContextCompat.checkSelfPermission(this, permition) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, permissions, REQUEST_LOCATION_PERMISSION);
                return;
            }

        }

        App.instance.getLocationManager().getLocation(() -> {
            sLatLng = App.instance.getLocationManager().getLatLng();
            if (CommonMapManager.getInstance().getGoogleMap() != null && CommonMapManager.getInstance().getGoogleMap().getLifecycle().getCurrentState() == Lifecycle.State.RESUMED)
                updateMap(DEFAULT_ZOOM, "");

            // Изменяем координаты пользователя
            App.instance.getAccountManager().getUser().setGeopoint(sLatLng.latitude, sLatLng.longitude);
            // Отправляем на сервер

            ServerManager.getInstance().updateUserOnServer(KEY_UPDATE_LOCATION);
        });


    }


    @Override
    public void onLocationChanged(Location location) {
        //   Toast.makeText(this, "Location changed", Toast.LENGTH_LONG).show();
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
        } else if (id == R.id.nav_bonus_code) {
            showBonusDialog();
        } else if (id == R.id.nav_about) {
            startActivity(new Intent(this, AboutActivity.class));
        } else if (id == R.id.nav_exit) {
            stopService(new Intent(this, MyService.class));
            App.instance.getAccountManager().getUser().setNetStatus(false);
            ServerManager.getInstance().updateUserOnServer(KEY_UPDATE_NET_STATUS);
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain);
            finish();
        }
            /*else if (id == R.id.nav_gallery) {

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

    private void showBonusDialog() {

        App.instance.getDialogManager().showBonusDialog();

    }

    @Subscribe
    public void updatePlayerLocation(UpdatePlayersLocations updatePlayersLocations) {

        if (mCommonMapManager.getGoogleMap().getLifecycle().getCurrentState() == Lifecycle.State.RESUMED) {
            mCommonMapManager.updatePlayers();
            // Toast.makeText(this, "Players locations updated", Toast.LENGTH_SHORT).show();
        }

    }


    @Subscribe
    public void updateQrPointsOnMap(UpdateAllQrPoints updateAllQrPoints) {

        if (mCommonMapManager.getServiceType() == CommonMapManager.ServiceType.GOOGLE) {
            if (mCommonMapManager.getGoogleMap().getLifecycle().getCurrentState() == Lifecycle.State.RESUMED) {

                //  Toast.makeText(this, "QrPoints updated on map", Toast.LENGTH_SHORT).show();
                if (sLatLng != null)
                    mCommonMapManager.initPoints(App.instance.getQrPointManager().getQrPlaces());
            }
        }

    }


    @Override
    protected void onPause() {
        super.onPause();
        App.instance.getNavigationHolder().removeNavigator();
    }

    @Override
    protected void onStop() {
        super.onStop();
        App.instance.getLocationManager().unsubscribe(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        FinditBus.getInstance().unregister(this);


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


            App.instance.getAccountManager().getUser().setGeopoint(sLatLng.latitude, sLatLng.longitude);
            // Отправляем на сервер
            ServerManager.getInstance().updateUserOnServer(KEY_UPDATE_LOCATION);


        }

        //    CommonMapManager.getInstance().initPoints(ManagersFactory.getInstance().getQrPointManager().getQrPlaces());

    }

    @Override
    public void QrPointClicked(String QrPointID) {


        Intent intent = new Intent(this, NearbyTainikActivity.class);
        intent.putExtra(POINT_ID, QrPointID);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    @Override
    public void updateLocation(LatLng latLng) {

        Log.i(LOG_TAG, "MainActivity Locations was updated");

        sLatLng = latLng;

        if (App.instance.getAccountManager().getUser() != null) {

            App.instance.getAccountManager().getUser().setGeopoint(sLatLng.latitude, sLatLng.longitude);

            // Отправляем на сервер если расстояние изменилось более чем на 50м

            ServerManager.getInstance().updateUserOnServer(KEY_UPDATE_LOCATION);

            // Если находится на переднем плане фрагмент
            if (mCommonMapManager.getServiceType() == CommonMapManager.ServiceType.GOOGLE) {
                if (App.instance.getAccountManager().getUser() != null &&
                        App.instance.getAccountManager().getUser().getTournamentID() != null &&
                        !App.instance.getAccountManager().getUser().getTournamentID().equalsIgnoreCase(""))

                    if (mCommonMapManager.getGoogleMap().getLifecycle().getCurrentState() == Lifecycle.State.RESUMED && mCommonMapManager.getGoogleMap().getMarkerQrPoints().size() > 0) {
                        String nearbyQrPointID = App.instance.getQrPointManager().getNearbyOfQrPlaced(sLatLng);
                        App.instance.getQrPointManager().checkNearbyQrPlaces(mContext, nearbyQrPointID, latLng, () -> updatePoint(nearbyQrPointID, "discovered"));
                    }

            }

        }

    }
}
