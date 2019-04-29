package findit.sedi.viktor.com.findit.data.cloud.firebase.firestore;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

import findit.sedi.viktor.com.findit.App;
import findit.sedi.viktor.com.findit.common.ManagersFactory;
import findit.sedi.viktor.com.findit.data.PlaceManager;

import static findit.sedi.viktor.com.findit.interactors.KeyCommonSettings.KeysField.LOG_TAG;


public class CloudFirestoreManager {
    private static final CloudFirestoreManager ourInstance = new CloudFirestoreManager();
    private static final String KEY_POINTS_PATH = "points";


    private FirebaseFirestore mFirebaseFirestore;
    private Map<String, Object> point = new HashMap<>();

    public static CloudFirestoreManager getInstance() {
        return ourInstance;
    }

    private CloudFirestoreManager() {
        mFirebaseFirestore = FirebaseFirestore.getInstance();
    }

    public void addPoint(LatLng latLng, String icon, long ID) {

        point.clear();
        point.put("Point", latLng);
        point.put("Icon", icon);
        point.put("ID", ID);

        mFirebaseFirestore.collection(KEY_POINTS_PATH)
                .add(point)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(App.instance, "Point was added", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(App.instance, "Point was not added", Toast.LENGTH_LONG).show();
                    }
                });


    }

    public void getPoint() {

        mFirebaseFirestore.collection(KEY_POINTS_PATH).get()
                .addOnFailureListener(e -> Log.w(LOG_TAG, "Error getting documents. Failure"))
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Обновляем значение (маркируем id - шники позиций и обновляем карту)
                            ManagersFactory.getInstance().getPlaceManager().markPlace(267354, 2);
                            Log.d(LOG_TAG, document.getId() + " => " + document.getData());
                        }
                    } else {
                        Log.w(LOG_TAG, "Error getting documents.", task.getException());
                    }
                });

    }

}
