package findit.sedi.viktor.com.findit.data_providers.cloud.firebase.firestore;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import findit.sedi.viktor.com.findit.App;
import findit.sedi.viktor.com.findit.common.ManagersFactory;
import findit.sedi.viktor.com.findit.data_providers.Gender;
import findit.sedi.viktor.com.findit.data_providers.data.Player;
import findit.sedi.viktor.com.findit.data_providers.data.Team;
import findit.sedi.viktor.com.findit.data_providers.data.Tournament;
import findit.sedi.viktor.com.findit.data_providers.data.User;
import findit.sedi.viktor.com.findit.interactors.KeyCommonSettings;
import findit.sedi.viktor.com.findit.presenter.interfaces.IAction;
import findit.sedi.viktor.com.findit.presenter.otto.FinditBus;
import findit.sedi.viktor.com.findit.presenter.otto.events.UpdatePlayersLocations;
import findit.sedi.viktor.com.findit.presenter.otto.events.UpdateUsersEvent;

import static findit.sedi.viktor.com.findit.interactors.KeyCommonUserFields.KeysField.USER_ACCOUNT_TYPE;
import static findit.sedi.viktor.com.findit.interactors.KeyCommonUserFields.KeysField.USER_BONUS;
import static findit.sedi.viktor.com.findit.interactors.KeyCommonUserFields.KeysField.USER_EMAIL;
import static findit.sedi.viktor.com.findit.interactors.KeyCommonUserFields.KeysField.USER_GENDER;
import static findit.sedi.viktor.com.findit.interactors.KeyCommonUserFields.KeysField.USER_LOCATION;
import static findit.sedi.viktor.com.findit.interactors.KeyCommonUserFields.KeysField.USER_NAME;
import static findit.sedi.viktor.com.findit.interactors.KeyCommonUserFields.KeysField.USER_NET_STATUS;
import static findit.sedi.viktor.com.findit.interactors.KeyCommonUserFields.KeysField.USER_PASSWORD;
import static findit.sedi.viktor.com.findit.interactors.KeyCommonUserFields.KeysField.USER_PHONE;
import static findit.sedi.viktor.com.findit.interactors.KeyCommonUserFields.KeysField.USER_PHOTO;
import static findit.sedi.viktor.com.findit.interactors.KeyCommonUserFields.KeysField.USER_TEAM_ID;
import static findit.sedi.viktor.com.findit.interactors.KeyCommonUserFields.KeysField.USER_TOTAL_BONUS;
import static findit.sedi.viktor.com.findit.interactors.KeyCommonUserFields.KeysField.USER_TOURNAMENT_ID;
import static findit.sedi.viktor.com.findit.interactors.KeyCommonPath.KeysField.KEY_POINTS_PATH;
import static findit.sedi.viktor.com.findit.interactors.KeyCommonPath.KeysField.KEY_TEAMS_PATH;
import static findit.sedi.viktor.com.findit.interactors.KeyCommonPath.KeysField.KEY_TOURNAMENTS_PATH;
import static findit.sedi.viktor.com.findit.interactors.KeyCommonPath.KeysField.KEY_USERS_PATH;
import static findit.sedi.viktor.com.findit.interactors.KeyCommonSettings.KeysField.LOG_TAG;
import static findit.sedi.viktor.com.findit.interactors.KeyCommonTournamentsFields.KeysField.TOURNAMENTS_DATE_FROM;
import static findit.sedi.viktor.com.findit.interactors.KeyCommonTournamentsFields.KeysField.TOURNAMENTS_DATE_TO;
import static findit.sedi.viktor.com.findit.interactors.KeyCommonTournamentsFields.KeysField.TOURNAMENTS_DESCRIBE;
import static findit.sedi.viktor.com.findit.interactors.KeyCommonTournamentsFields.KeysField.TOURNAMENTS_DIFFICULTY;
import static findit.sedi.viktor.com.findit.interactors.KeyCommonTournamentsFields.KeysField.TOURNAMENTS_PLAYERS_IDS;
import static findit.sedi.viktor.com.findit.interactors.KeyCommonTournamentsFields.KeysField.TOURNAMENTS_TEAMS_IDS;
import static findit.sedi.viktor.com.findit.interactors.KeyCommonTournamentsFields.KeysField.TOURNAMENTS_TIPS;
import static findit.sedi.viktor.com.findit.interactors.KeyCommonTournamentsFields.KeysField.TOURNAMENTS_TOTAL_BONUSES;
import static findit.sedi.viktor.com.findit.interactors.KeyCommonTournamentsFields.KeysField.TOURNAMENTS_TYPE;


public class CloudFirestoreManager {


    private static final CloudFirestoreManager ourInstance = new CloudFirestoreManager();


    // Вынесем его так же как сделали в Tournament
    // Придётся ставить хак для работы с Enum
    private int genderType;
    private static Handler mHandler;
    private Gender mGender;

    private DocumentReference document;
    private FirebaseFirestore mFirebaseFirestore;
    private Context mContext = App.instance.getBaseContext();
    private Map<String, Object> data = new HashMap<>();

    public static CloudFirestoreManager getInstance() {
        return ourInstance;
    }

    private CloudFirestoreManager() {
        mFirebaseFirestore = FirebaseFirestore.getInstance();
    }


    // Нужно будет доработать конструкцию, заменив строки на Enum Или KeyCommonPath
    public void updateUser(String tag) {


        // Логика такова, работа во втором потоке, он запускает другие потоки,
        // Сам засыпает на 1 секунду, если обновления успешны у всех трёх потоков, то останавливаем сами себя и отплавляем событие на обновление
        // Данных
        if (tag.equalsIgnoreCase("profile")) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {


                    final int[] succesResult = {0};

                    document = mFirebaseFirestore.collection(KEY_USERS_PATH).document(ManagersFactory.getInstance().getAccountManager().getUser().getID());

                    document.update(USER_NAME, ManagersFactory.getInstance().getAccountManager().getUser().getName())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    succesResult[0]++;
                                    Log.d(LOG_TAG, task + " => " + task.getResult());
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    e.printStackTrace();
                                }
                            });

                    document.update(USER_PHONE, ManagersFactory.getInstance().getAccountManager().getUser().getPhone())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    succesResult[0]++;
                                    Log.d(LOG_TAG, task + " => " + task.getResult());
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    e.printStackTrace();
                                }
                            });

                    document.update(USER_GENDER, ManagersFactory.getInstance().getAccountManager().getUser().getGender())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    succesResult[0]++;
                                    Log.d(LOG_TAG, task + " => " + task.getResult());
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    e.printStackTrace();
                                }
                            });

                    // Пока значение не увеличили на 3 и поток не прерван
                    while (succesResult[0] < 3 && !Thread.currentThread().isInterrupted()) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    ManagersFactory.getInstance().getAccountManager().updateUserByEmail(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail(), null);

                }
            });
            thread.start();

        }

        if (tag.equalsIgnoreCase("location")) {


            document = mFirebaseFirestore.collection(KEY_USERS_PATH).document(ManagersFactory.getInstance().getAccountManager().getUser().getID());

            document.update(USER_LOCATION, ManagersFactory.getInstance().getAccountManager().getUser().getGeopoint())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Log.d(LOG_TAG, task + " => " + task.getResult());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            e.printStackTrace();
                        }
                    });
        }

        if (tag.equalsIgnoreCase("net_status")) {


            document = mFirebaseFirestore.collection(KEY_USERS_PATH).document(ManagersFactory.getInstance().getAccountManager().getUser().getID());

            document.update(USER_NET_STATUS, true)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Log.d(LOG_TAG, task + " => " + task.getResult());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            e.printStackTrace();
                        }
                    });
        }


    }


    public void changeUserNetStatus(boolean status) {

        document = mFirebaseFirestore.collection(KEY_USERS_PATH).document(ManagersFactory.getInstance().getAccountManager().getUser().getID());
        document.update(USER_NET_STATUS, status);

    }


    // Вытаскиваем все команды для начала
    // После уже просто будем инициализировать по идентификаторам списко в обьект Tournament
    public void getTeams() {

        FirebaseFirestore.getInstance().collection(KEY_TEAMS_PATH).get()
                .addOnFailureListener(e -> Log.w(LOG_TAG, "Error getting documents. Failure"))
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful() && task.getResult() != null) {
                        for (QueryDocumentSnapshot document : task.getResult()) {

                            if (document.exists()) {

                                ManagersFactory.getInstance().getTeamManager().addTeam(
                                        new Team(document.getString("TournamentID"),
                                                (List<String>) document.get("PlayersIDs"),
                                                document.getId(),
                                                document.getString("Name")));
                                Log.d(LOG_TAG, document.getId() + " => " + document.getData());
                            }
                        }
                    } else {
                        Log.w(LOG_TAG, "Error getting documents.", task.getException());
                    }
                });

    }


    public void getTournaments() {

        mFirebaseFirestore.collection(KEY_TOURNAMENTS_PATH).get()
                .addOnFailureListener(e -> Log.w(LOG_TAG, "Error getting documents. Failure"))
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful() && task.getResult() != null) {
                        for (QueryDocumentSnapshot document : task.getResult()) {

                            if (document.exists()) {


                                ManagersFactory.getInstance().getTournamentManager().addTournament(
                                        new Tournament(document.getTimestamp(TOURNAMENTS_DATE_FROM),
                                                document.getTimestamp(TOURNAMENTS_DATE_TO),
                                                document.getString(TOURNAMENTS_DESCRIBE),
                                                (ArrayList<String>) document.get(TOURNAMENTS_TIPS),
                                                document.getLong(TOURNAMENTS_TOTAL_BONUSES),
                                                Tournament.convertIntToTournamentType(document.getLong(TOURNAMENTS_TYPE)),
                                                document.getLong(TOURNAMENTS_DIFFICULTY),
                                                (ArrayList<String>) document.get(TOURNAMENTS_PLAYERS_IDS),
                                                document.getId(),
                                                (ArrayList<String>) document.get(TOURNAMENTS_TEAMS_IDS)
                                        )
                                );

                                Log.d(LOG_TAG, document.getId() + " => " + document.getData());
                            }
                        }
                    } else {
                        Log.w(LOG_TAG, "Error getting documents.", task.getException());
                    }
                });
    }


    public void createUser(String email, String password, IAction IAction) {


        // Create a new user with a first and last name
        // Инициализируем остальные значения по умолчанию
        Map<String, Object> user = new HashMap<>();
        user.put(USER_EMAIL, email);
        user.put(USER_PASSWORD, password);
        user.put(USER_NET_STATUS, true);
        user.put(USER_GENDER, 0);
        user.put(USER_BONUS, 0);
        user.put(USER_PHOTO, "");
        user.put(USER_LOCATION, new GeoPoint(0, 0));
        user.put(USER_ACCOUNT_TYPE, "Free");
        user.put(USER_NAME, "");
        user.put(USER_PHONE, "");
        user.put(USER_TEAM_ID, "");
        user.put(USER_TOTAL_BONUS, 0);
        user.put(USER_TOURNAMENT_ID, "");


        // Add a new document with a generated ID
        mFirebaseFirestore.collection(KEY_USERS_PATH)
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        ManagersFactory.getInstance().getAccountManager().updateUserByEmail(email, null);
                        if (IAction != null) {
                            IAction.action();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(KeyCommonSettings.KeysField.LOG_TAG, "Error adding document", e);
                        // Тут нужно будет удалить пользователя с БД и перезарегестрировать
                    }
                });


    }


    public void initUser(String email, IAction IAction) {


        // В этом  методе получаем список элементов, и инициализируем только тот, который на м нужен
        FirebaseFirestore.getInstance().collection(KEY_USERS_PATH).get()
                .addOnFailureListener(e -> Log.w(LOG_TAG, "Error getting documents. Failure"))
                .addOnCompleteListener(task -> {

                    Log.w(LOG_TAG, "Task: " + task.getResult() + "\nTask Exception: " + task.getException() + "\nTask isSuccessful " + task.isSuccessful());

                    if (task.isSuccessful() && task.getResult() != null) {
                        for (QueryDocumentSnapshot document : task.getResult()) {

                            Log.d(LOG_TAG, document.getId() + " => " + document.getData());

                            // Если email не равен тому который нам нужен, то пропускаем, иначе инициализируем и стопаем
                            if (!document.getString(USER_EMAIL).equalsIgnoreCase(email)) {

                                continue;
                            } else {


                                ManagersFactory.getInstance().getAccountManager().initUser(new User(document.getString(USER_PHONE),
                                        document.getString(USER_NAME),
                                        document.getId(),
                                        document.getString(USER_EMAIL),
                                        document.getLong(USER_BONUS) == null ? 0 : document.getLong(USER_BONUS),
                                        document.getString(USER_PHOTO),
                                        document.getString(USER_PASSWORD),
                                        document.getLong(USER_GENDER) == null ? 0 : document.getLong(USER_GENDER),
                                        document.getString(USER_TOURNAMENT_ID),
                                        document.getString(USER_TEAM_ID),
                                        document.getLong(USER_TOTAL_BONUS) == null ? 0 : document.getLong(USER_TOTAL_BONUS)
                                ));


                                if (IAction != null)
                                    IAction.action();

                                // По логике в этом методе пользователь запускает устройсво
                                changeUserNetStatus(true);

                                FinditBus.getInstance().post(new UpdateUsersEvent());

                                break;
                            }

                        }
                    } else {
                        Log.w(KeyCommonSettings.KeysField.LOG_TAG, "User error initialisation");
                        Log.w(LOG_TAG, "Error getting documents.", task.getException());
                    }
                });


    }


    public void getPlayers() {

        mFirebaseFirestore.collection(KEY_USERS_PATH).get()
                .addOnFailureListener(e -> Log.w(LOG_TAG, "Error getting documents. Failure"))
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        for (QueryDocumentSnapshot document : task.getResult()) {


                            GeoPoint geoPoint = document.getGeoPoint(USER_LOCATION);

                            // Ставим ограничение, если ID равен нашему аккаунту, то игнорим
                            if (document.getId().equalsIgnoreCase(ManagersFactory.getInstance().getAccountManager().getUser().getID()))
                                continue;

                            // Обновляем значение по ID что эти точки уже нашли другие пользователи
                            // Карта при обновлени автоматически подхватит измененения
                            ManagersFactory.getInstance().getPlayersManager().addPlayer(new Player(
                                    document.getLong(USER_BONUS),
                                    document.getString(USER_NAME),
                                    document.getString(USER_PHOTO),
                                    document.getId(),
                                    document.getBoolean(USER_NET_STATUS),
                                    document.getString(USER_TOURNAMENT_ID),
                                    document.getString(USER_TEAM_ID),
                                    document.getLong(USER_TOTAL_BONUS),
                                    geoPoint.getLatitude(),
                                    geoPoint.getLongitude(),
                                    document.getLong(USER_GENDER)));

                            Log.d(LOG_TAG, document.getId() + " => " + document.getData());
                        }

                        FinditBus.getInstance().post(new UpdatePlayersLocations());

                    } else {
                        Log.w(LOG_TAG, "Error getting documents.", task.getException());
                    }
                });
    }


    public void updatePoint(String icon, String ID) {


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
}
