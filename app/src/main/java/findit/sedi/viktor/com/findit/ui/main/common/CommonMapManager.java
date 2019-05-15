package findit.sedi.viktor.com.findit.ui.main.common;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import findit.sedi.viktor.com.findit.data_providers.data.Place;
import findit.sedi.viktor.com.findit.presenter.interfaces.IAction;
import findit.sedi.viktor.com.findit.ui.main.fragments.maps.GoogleMapFragment;


public class CommonMapManager {
    private static final CommonMapManager ourInstance = new CommonMapManager();

    private Context mContext;
    private ServiceType mServiceType;
    private GoogleMapFragment mGoogleMapFragment;

    public static CommonMapManager getInstance() {
        return ourInstance;
    }

    private CommonMapManager() {
    }


    public void setContext(Context context) {
        if (mContext == null) ;
        mContext = context;
    }


    public void moveTo(LatLng position) {

        if (mServiceType == ServiceType.GOOGLE) {
            mGoogleMapFragment.moveTo(position);
        }

    }


    public void zoomTo(LatLng position, int zoom) {


        if (mServiceType == ServiceType.GOOGLE) {
            mGoogleMapFragment.zoomTo(position, zoom);
        }

    }


    public void invalidate() {

    }


    public void zoomIn() {

    }

    public void zoomOut() {

    }

   /* public void removePolyLine(Polyline object) {
        // mOsmMapController.remove(object);
    }*/


    public LatLng getMapCenter() {

        if (mServiceType == ServiceType.GOOGLE) {
            return mGoogleMapFragment.getMap().getCameraPosition().target;
        }

        return null;
    }


    public void addGoogleFragment(GoogleMapFragment googleMapFragment) {
        mGoogleMapFragment = googleMapFragment;
    }


    public void addPoint(LatLng latLong, int ic_map_1, IAction action) {


        if (mServiceType == ServiceType.GOOGLE) {
            mGoogleMapFragment.addPoint(latLong, ic_map_1, action);
        }

    }


    public void clearMap() {

        if (mServiceType == ServiceType.GOOGLE) {
            mGoogleMapFragment.clearMap();
        }
    }

    public void deletePoint(LatLng point) {


        if (mServiceType == ServiceType.GOOGLE) {
            //mGoogleMapFragment.deletePoint(point);
        }


    }

    public void deletePoints() {


        if (mServiceType == ServiceType.GOOGLE) {
            mGoogleMapFragment.deletePoints();
        }


    }

    public void updateMe(LatLng latLng, int ic_location_24dp) {

        if (mServiceType == ServiceType.GOOGLE) {
            mGoogleMapFragment.updateLocation(latLng, ic_location_24dp);
        }

    }

    public void initPoints(List<Place> places) {


        if (mServiceType == ServiceType.GOOGLE) {
            mGoogleMapFragment.initPoints(places);
        }

    }

    public void updatePoint(long id, long mark) {

        if (mServiceType == ServiceType.GOOGLE) {
            mGoogleMapFragment.updatePoint(id, mark);
        }

    }

    public void updatePlayers() {

        if (mServiceType == ServiceType.GOOGLE) {
            mGoogleMapFragment.updateMap();
        }

    }

    public enum ServiceType {
        YANDEX,
        GOOGLE,
        OSRM;
    }


    public void setServiceType(ServiceType serviceType) {
        mServiceType = serviceType;
    }

    public ServiceType getServiceType() {
        return mServiceType;
    }
}
