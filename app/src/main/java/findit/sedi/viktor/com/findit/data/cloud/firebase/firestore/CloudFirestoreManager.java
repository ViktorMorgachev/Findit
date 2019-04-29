package findit.sedi.viktor.com.findit.data.cloud.firebase.firestore;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

import findit.sedi.viktor.com.findit.App;
import findit.sedi.viktor.com.findit.common.ManagersFactory;
import findit.sedi.viktor.com.findit.data.Gender;
import findit.sedi.viktor.com.findit.data.Player;

import static findit.sedi.viktor.com.findit.interactors.KeyCommonSettings.KeysField.LOG_TAG;


public class CloudFirestoreManager {
    private static final CloudFirestoreManager ourInstance = new CloudFirestoreManager();
    private static final String KEY_POINTS_PATH = "points";
    private static final String KEY_USERS_INFO = "users";

    // Придётся ставить хак для работы с Enum
    private int genderType;
    private Gender mGender;
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

        mFirebaseFirestore.collection(KEY_USERS_INFO).get()
                .addOnFailureListener(e -> Log.w(LOG_TAG, "Error getting documents. Failure"))
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Обновляем значение по ID что эти точки уже нашли другие пользователи
                            // Карта при обновлени автоматически подхватит измененения
                            ManagersFactory.getInstance().getPlaceManager().markPlace(267354, 2);
                            Log.d(LOG_TAG, document.getId() + " => " + document.getData());
                        }
                    } else {
                        Log.w(LOG_TAG, "Error getting documents.", task.getException());
                    }
                });

    }

    public void getPlayers() {

        mFirebaseFirestore.collection(KEY_POINTS_PATH).get()
                .addOnFailureListener(e -> Log.w(LOG_TAG, "Error getting documents. Failure"))
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            if (document.getLong("gender") != null) {
                                long gender = document.getLong("gender");
                                if (gender == 1) {
                                    mGender = Gender.Male;
                                } else if (gender == 2) {
                                    mGender = Gender.Female;
                                } else mGender = Gender.None;
                            } else {
                                mGender = Gender.None;
                            }

                            if (document.getGeoPoint("position") != null &&
                                    document.getDouble("bonus") != null &&
                                    document.getLong("ID") != null) {
                                // Обновляем значение по ID что эти точки уже нашли другие пользователи
                                // Карта при обновлени автоматически подхватит измененения
                                ManagersFactory.getInstance().getPlayerManager().addPlayer(new Player(
                                        document.getGeoPoint("position"),
                                        document.getDouble("bonus"),
                                        document.getString("name"),
                                        mGender,
                                        document.getString("photoUrl"),
                                        document.getLong("ID")));
                            } else {
                                Log.e(LOG_TAG, "Field was empty " + document.getId() + " => " + document.getData());
                            }

                            Log.d(LOG_TAG, document.getId() + " => " + document.getData());
                        }
                    } else {
                        Log.w(LOG_TAG, "Error getting documents.", task.getException());
                    }
                });
    }
}
