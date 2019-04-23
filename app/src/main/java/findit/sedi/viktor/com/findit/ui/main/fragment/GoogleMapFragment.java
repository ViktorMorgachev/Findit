package findit.sedi.viktor.com.findit.ui.main.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import findit.sedi.viktor.com.findit.R;
import findit.sedi.viktor.com.findit.common.Util;
import findit.sedi.viktor.com.findit.presenter.interfaces.IAction;
import findit.sedi.viktor.com.findit.ui.main.MainActivity;


public class GoogleMapFragment extends Fragment implements GoogleMap.OnCameraMoveListener, GoogleMap.OnMarkerClickListener {

    private MainActivity.EventsListener mCallBackListener;


    // Views
    private MapView mMapView;
    private static GoogleMap mMap;
    private IAction mIAction;

    // Logic
    private List<MarkerOptions> mMarkerPoints = new ArrayList<>();


    private static GoogleMapFragment instance = null;

    public static GoogleMapFragment getInstance() {
        if (instance == null) {
            instance = new GoogleMapFragment();
        }
        return instance;
    }


    public void setEventsListener(MainActivity.EventsListener eventsListener) {
        mCallBackListener = eventsListener;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.main_google_map, container, false);
        mMapView = (MapView) rootView.findViewById(R.id.map_view);
        mMapView.onCreate(savedInstanceState);


        if (mMapView != null) {

            mMapView.onCreate(null);  //Don't forget to call onCreate after get the mapView!


            mMapView.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    mMap = googleMap;
                    mCallBackListener.mapReady();
                    // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LocationService.me().getLocation().toLatLng(), MainActivity2.MAXIMUM_ZOOMLEVEL_GOOGLE));
                }
            });

            MapsInitializer.initialize(getContext());
        } else {

        }

        return rootView;
    }


    @Override
    public void onCameraMove() {

    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
       /* try {
            mCallBackListener = (OnMapEventsListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnMapEventsListener");
        }*/
    }


    public void deletePoints() {
        mMarkerPoints.clear();

    }


    public void updateMap() {

        if (mMap != null) {
            mMap.clear();

            // Рисуем точки
            for (int i = 0; i < mMarkerPoints.size(); i++) {
                mMap.addMarker(mMarkerPoints.get(i));
            }

        }
    }

    public void moveTo(LatLng latLng) {
        if (mMap != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        }
    }


    public void zoomTo(LatLng position, int zoom) {
        if (mMap != null)
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, zoom));
    }


    public void removeMapTouchListener() {
        throw new UnsupportedOperationException();
    }


    /*public LatLng getMapCenter() {
        return ;
    }*/

    public GoogleMap getMap() {
        return mMap;
    }

    public void addPoint(LatLng latLng, int drawable, IAction action) {

        mIAction = action;

        mMarkerPoints.clear();

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.icon(Util.getInstance().bitmapDescriptorFromVector(getContext(), drawable));
        markerOptions.position(latLng);
        mMarkerPoints.add(markerOptions);


        updateMap();

    }

    public void addPointOnMap(LatLng latLong, String msg, int drawable) {

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.icon(BitmapDescriptorFactory.fromResource(drawable));
        markerOptions.position(latLong);
        mMarkerPoints.add(markerOptions);

        updateMap();

    }

  /*  public void deletePoint(LatLngpoint) {

        for (int i = 0; i < mMarkerPoints.size(); i++) {
            if (mMarkerPoints.get(i).getPosition().equals(point.getLatLong().toLatLng())) {
                mMarkerPoints.remove(mMarkerPoints.get(i));
                return;
            }
        }

        updateMap();
    }*/

    @Override
    public boolean onMarkerClick(Marker marker) {

        return true;
    }

    public void clearMap() {

        mMarkerPoints.clear();
        mIAction = null;

        updateMap();
    }

}
