package findit.sedi.viktor.com.findit.data_providers.cloud.firebase.firestore;

import android.util.Log;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

import findit.sedi.viktor.com.findit.App;
import findit.sedi.viktor.com.findit.common.ManagersFactory;
import findit.sedi.viktor.com.findit.data_providers.Gender;
import findit.sedi.viktor.com.findit.data_providers.data.Player;
import findit.sedi.viktor.com.findit.data_providers.data.User;

import static findit.sedi.viktor.com.findit.interactors.KeyCommonSettings.KeysField.LOG_TAG;


public class CloudFirestoreManager {
    private static final CloudFirestoreManager ourInstance = new CloudFirestoreManager();
    private static final String KEY_POINTS_PATH = "points";
    private static final String KEY_USERS_INFO = "users";
    private static final String KEY_PLAYER_PATH = "players";
    // Придётся ставить хак для работы с Enum
    private int genderType;
    private Gender mGender;
    private DocumentReference document;
    private FirebaseFirestore mFirebaseFirestore;
    private Map<String, Object> point = new HashMap<>();

    public static CloudFirestoreManager getInstance() {
        return ourInstance;
    }

    private CloudFirestoreManager() {
        mFirebaseFirestore = FirebaseFirestore.getInstance();
    }

    public void updatePoint(String icon, String ID) {

        point.clear();
        point.put("Icon", icon);
        point.put("ID", ID);

        document = mFirebaseFirestore.collection(KEY_POINTS_PATH).document(String.valueOf(ID));

        document.update("Icon", icon)
                .addOnSuccessListener(aVoid ->
                        Toast.makeText(App.instance, "Обновленно успешно", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e ->
                        Toast.makeText(App.instance, "Ошибка обновления", Toast.LENGTH_SHORT).show());


    }

    public void getPoint() {

        mFirebaseFirestore.collection(KEY_POINTS_PATH).get()
                .addOnFailureListener(e -> Log.w(LOG_TAG, "Error getting documents. Failure"))
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Обновляем значение по ID что эти точки уже нашли другие пользователи
                            // Карта при обновлени автоматически подхватит измененения

                            ManagersFactory.getInstance().getPlaceManager().markPlace(document.getLong("ID"), document.getLong("Icon"));
                            Log.d(LOG_TAG, document.getId() + " => " + document.getData());
                        }
                    } else {
                        Log.w(LOG_TAG, "Error getting documents.", task.getException());
                    }
                });

    }

    public void getPlayers() {

        mFirebaseFirestore.collection(KEY_PLAYER_PATH).get()
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

    public void updateUser(User user) {

        mFirebaseFirestore.collection(KEY_USERS_INFO).get()
                .addOnFailureListener(e -> Log.w(LOG_TAG, "Error getting documents. Failure"))
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Обновляем значение по ID что эти точки уже нашли другие пользователи

                            Log.d(LOG_TAG, document.getId() + " => " + document.getData());
                        }
                    } else {
                        Log.w(LOG_TAG, "Error getting documents.", task.getException());
                    }
                });

    }
}
