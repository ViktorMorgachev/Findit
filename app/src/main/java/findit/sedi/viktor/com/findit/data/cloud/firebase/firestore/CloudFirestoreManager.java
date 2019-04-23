package findit.sedi.viktor.com.findit.data.cloud.firebase.firestore;

import android.content.Context;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;


public class CloudFirestoreManager {
    private static final CloudFirestoreManager ourInstance = new CloudFirestoreManager();


    private FirebaseFirestore mFirebaseFirestore;
    private Map<String, Object> point = new HashMap<>();

    public static CloudFirestoreManager getInstance() {
        return ourInstance;
    }

    private CloudFirestoreManager() {
        mFirebaseFirestore = FirebaseFirestore.getInstance();
    }

    public void addPoint(Context context, LatLng latLng, String icon) {

        point.clear();
        point.put("Lat", latLng.latitude);
        point.put("Lon", latLng.longitude);
        point.put("Icon", icon);

        mFirebaseFirestore.collection("points")
                .add(point)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(context, "Point was added", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Point was not added", Toast.LENGTH_LONG).show();
                    }
                });


    }

}
