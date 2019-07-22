package findit.sedi.viktor.com.findit.common;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import findit.sedi.viktor.com.findit.App;
import findit.sedi.viktor.com.findit.R;
import findit.sedi.viktor.com.findit.common.dialogs.DialogManager;
import findit.sedi.viktor.com.findit.common.interfaces.ILocationListener;
import findit.sedi.viktor.com.findit.presenter.interfaces.IAction;


public class LocationManager {
    private static final LocationManager ourInstance = new LocationManager();

    public static LocationManager getInstance() {
        return ourInstance;
    }


    private LatLng mLatLng;
    private List<ILocationListener> mListeners = new ArrayList<>();
    private FusedLocationProviderClient mFusedLocationClient;
    private LatLng mLastLocation;
    private Context mContext;
    private LocationResult mLocationResult;
    private LocationCallback mLocationCallback;

    private LocationManager() {
        initLocationCallback();
    }


    public void subscribe(ILocationListener listener) {
        if (!mListeners.contains(listener))
            mListeners.add(listener);
    }

    public void unsubscribe(ILocationListener listener) {
        mListeners.remove(listener);
    }

    public void updateLocation() {

        for (ILocationListener listener : mListeners) {
            listener.updateLocation(mLatLng);
        }

    }

    public LatLng getLatLng() {
        return mLatLng;
    }

    public LatLng getLocation(IAction action) {


        if (ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            App.instance.getDialogManager().setContext(getContext());
            App.instance.getDialogManager().showDialog(getContext().getResources().getString(R.string.location_permission_denied_please_grant),
                    null, null, null, null, null, true, false);

            mLatLng = new LatLng(0, 0);

            return mLatLng;
        } else {
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(location -> {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            mLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                        }

                        if (action != null) {
                            action.action();
                        }

                    });


            mFusedLocationClient.requestLocationUpdates(getLocationRequest(), mLocationCallback, null);

            return mLatLng;
        }

    }

    public Context getContext() {
        return mContext;
    }

    public void setContext(Context context) {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context.getApplicationContext());
        mContext = context;
    }


    private void initLocationCallback() {

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {

                mLocationResult = locationResult;


                for (Location location : locationResult.getLocations()) {
                    mLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                    updateLocation();
                    mLastLocation = mLatLng;
                    break;
                }

            }
        };

    }

    private LocationRequest getLocationRequest() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(8000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return locationRequest;
    }


}
