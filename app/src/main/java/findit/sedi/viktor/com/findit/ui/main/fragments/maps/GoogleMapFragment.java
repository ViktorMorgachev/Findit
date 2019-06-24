package findit.sedi.viktor.com.findit.ui.main.fragments.maps;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.List;

import findit.sedi.viktor.com.findit.R;
import findit.sedi.viktor.com.findit.common.ManagersFactory;
import findit.sedi.viktor.com.findit.common.Util;
import findit.sedi.viktor.com.findit.data_providers.data.QrPoint;
import findit.sedi.viktor.com.findit.presenter.interfaces.IAction;
import findit.sedi.viktor.com.findit.ui.main.interfaces.MapsFragmentListener;

import static findit.sedi.viktor.com.findit.interactors.KeyCommonSettings.KeysField.LOG_TAG;


public class GoogleMapFragment extends Fragment implements GoogleMap.OnCameraMoveListener, GoogleMap.OnMarkerClickListener {


    private MapsFragmentListener mCallBackClickListener;

    // Views
    private MapView mMapView;
    private static GoogleMap mMap;
    private IAction mIAction;

    // Logic
    private MarkerOptions mMarkerOptionsMe = new MarkerOptions();



    private List<MarkerOptions> mMarkerQrPoints = new ArrayList<>();

    private MarkerOptions mMarkerOptions;

    private static GoogleMapFragment instance = null;

    public static GoogleMapFragment getInstance() {
        if (instance == null) {
            instance = new GoogleMapFragment();
        }
        return instance;
    }

    public List<MarkerOptions> getMarkerQrPoints() {
        return mMarkerQrPoints;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.main_google_map, container, false);
        mMapView = (MapView) rootView.findViewById(R.id.map_view);
        mMapView.onCreate(savedInstanceState);

        MapsInitializer.initialize(getContext());


        Toast.makeText(getContext(), "GoogleMap was onCreateView", Toast.LENGTH_LONG).show();

        if (mMapView != null) {

            mMapView.onCreate(null);  //Don't forget to call onCreate after get the mapView!


            mMapView.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    mMap = googleMap;
                    mMap.setMapStyle(
                            MapStyleOptions.loadRawResourceStyle(
                                    getContext(), R.raw.google_map_style));

                    mCallBackClickListener.mapReady();
                    // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LocationServices.me().getLocation().toLatLng(), MainActivity2.MAXIMUM_ZOOMLEVEL_GOOGLE));
                }
            });


        }

        return rootView;
    }


    @Override
    public void onCameraMove() {

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(LOG_TAG, "Google map  was resumed");
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();

        Log.d(LOG_TAG, "Google map  was paused");
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "Google map  was destroed");
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
        try {
            mCallBackClickListener = (MapsFragmentListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement MapsFragmentListener");
        }
    }


    public void deletePoints() {
        mMarkerQrPoints.clear();

    }


    public void updateMap() {

        if (mMap != null) {
            mMap.clear();

            for (int i = 0; i < mMarkerQrPoints.size(); i++) {
                mMap.addMarker(mMarkerQrPoints.get(i));
            }

            // Рисуем своё местоположени
            // if (mMarkerOptionsMe.getPosition() != null)
            mMap.addMarker(mMarkerOptionsMe);

            // Рисуем местоположение остальных игроков
          /*  if (ManagersFactory.getInstance().getPlayersManager().getPlayers().size() != 0)
                for (int i = 0; i < ManagersFactory.getInstance().getPlayersManager().getPlayers().size(); i++) {

                    GeoPoint geoPoint = ManagersFactory.getInstance().getPlayersManager().getPlayers().get(i).getGeopoint();
                    mMarkerOptions = new MarkerOptions();
                    mMarkerOptions.icon(Util.getInstance().bitmapDescriptorFromVector(getContext(), R.drawable.ic_person_pin_24dp));
                    mMarkerOptions.position(new LatLng(geoPoint.getLatitude(), geoPoint.getLongitude()));
                    mMap.addMarker(mMarkerOptions);

                }*/

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

    public void updateLocation(LatLng latLng, int drawable) {

        mMarkerOptionsMe.icon(Util.getInstance().bitmapDescriptorFromVector(getContext(), drawable));
        mMarkerOptionsMe.position(latLng);

        updateMap();

    }

    public void addPoint(LatLng latLng, int drawable, IAction action) {

        mIAction = action;

        mMarkerOptions = new MarkerOptions();
        mMarkerOptions.icon(Util.getInstance().bitmapDescriptorFromVector(getContext(), drawable));
        mMarkerOptions.position(latLng);
        mMarkerQrPoints.add(mMarkerOptions);


        updateMap();

    }

    public void addPointOnMap(LatLng latLong, String msg, int drawable) {

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.icon(BitmapDescriptorFactory.fromResource(drawable));
        markerOptions.position(latLong);
        mMarkerQrPoints.add(markerOptions);

        updateMap();

    }

  /*  public void deletePoint(LatLngpoint) {

        for (int i = 0; i < mMarkerQrPoints.size(); i++) {
            if (mMarkerQrPoints.get(i).getPosition().equals(point.getLatLong().toLatLng())) {
                mMarkerQrPoints.remove(mMarkerQrPoints.get(i));
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

        mMarkerQrPoints.clear();
        mIAction = null;

        updateMap();
    }

    public void initPoints(List<QrPoint> places) {


        for (int i = 0; i < places.size(); i++) {

            mMarkerOptions = new MarkerOptions();

            if (places.get(i).getMark().equalsIgnoreCase("none"))
                mMarkerOptions.icon(Util.getInstance().bitmapDescriptorFromVector(getContext(), R.drawable.ic_none_place_24dp));
            if (places.get(i).getMark().equalsIgnoreCase("fond")) {
                mMarkerOptions.icon(Util.getInstance().bitmapDescriptorFromVector(getContext(), R.drawable.ic_close_24dp));
            } else if (places.get(i).getMark().equalsIgnoreCase("discovered")) {
                switch ((int) places.get(i).getDifficulty()) {
                    case 1:
                        mMarkerOptions.icon(Util.getInstance().bitmapDescriptorFromVector(getContext(), R.drawable.ic_fonded_place_difficult_1_24dp));
                        break;
                    case 2:
                        mMarkerOptions.icon(Util.getInstance().bitmapDescriptorFromVector(getContext(), R.drawable.ic_fonded_place_difficult_2_24dp));
                        break;
                    case 3:
                        mMarkerOptions.icon(Util.getInstance().bitmapDescriptorFromVector(getContext(), R.drawable.ic_fonded_place_difficult_3_24dp));
                        break;
                }

            }

            mMarkerOptions.position(places.get(i).getLatLong());
            mMarkerOptions.title(String.valueOf(places.get(i).getID()));
            mMarkerQrPoints.add(mMarkerOptions);
        }


        updateMap();


    }

    // В надежде что  пустые icon можно инициализировать
    public void updatePoint(String id, String mark) {

        QrPoint qrPoint = ManagersFactory.getInstance().getQrPointManager().getQrPlaceByID(id);

        // Ищем точку у которой идентификатор необходимый
        for (int i = 0; i < mMarkerQrPoints.size(); i++) {
            if (mMarkerQrPoints.get(i).getTitle().equalsIgnoreCase(String.valueOf(id))) {
                if (mark.equalsIgnoreCase("fond")) {
                    mMarkerOptions.icon(Util.getInstance().bitmapDescriptorFromVector(getContext(), R.drawable.ic_close_24dp));
                } else if (mark.equalsIgnoreCase("discovered")) {
                    switch ((int) qrPoint.getDifficulty()) {
                        case 1:
                            mMarkerOptions.icon(Util.getInstance().bitmapDescriptorFromVector(getContext(), R.drawable.ic_fonded_place_difficult_1_24dp));
                            break;
                        case 2:
                            mMarkerOptions.icon(Util.getInstance().bitmapDescriptorFromVector(getContext(), R.drawable.ic_fonded_place_difficult_2_24dp));
                            break;
                        case 3:
                            mMarkerOptions.icon(Util.getInstance().bitmapDescriptorFromVector(getContext(), R.drawable.ic_fonded_place_difficult_3_24dp));
                            break;
                    }
                }
                return;
            }
        }

        updateMap();

    }

    public void clearQrPoints() {
        mMarkerQrPoints.clear();
    }
}
