package findit.sedi.viktor.com.findit.common;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;

import androidx.annotation.DrawableRes;
import androidx.core.content.ContextCompat;
import findit.sedi.viktor.com.findit.R;

public class Util {
    private static final Util ourInstance = new Util();

    public static Util getInstance() {
        return ourInstance;
    }

    private Util() {
    }

    public BitmapDescriptor bitmapDescriptorFromVector(Context context, @DrawableRes int vectorDrawableResourceId) {
        Drawable background = ContextCompat.getDrawable(context, vectorDrawableResourceId);
        background.setBounds(0, 0, background.getIntrinsicWidth(), background.getIntrinsicHeight());
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorDrawableResourceId);
        vectorDrawable.setBounds(40, 20, vectorDrawable.getIntrinsicWidth() + 40, vectorDrawable.getIntrinsicHeight() + 20);
        Bitmap bitmap = Bitmap.createBitmap(background.getIntrinsicWidth(), background.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        background.draw(canvas);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    public double getDistance(LatLng pointFirst, LatLng pointSecond) {

        Location locationA = new Location("LocationA");
        Location locationB = new Location("LocationB");

        locationA.setLatitude(pointFirst.latitude);
        locationA.setLongitude(pointFirst.longitude);


        locationB.setLatitude(pointSecond.latitude);
        locationB.setLongitude(pointSecond.longitude);

        return locationA.distanceTo(locationB);
    }

}
