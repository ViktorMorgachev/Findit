package findit.sedi.viktor.com.findit.data_providers.cloud.firebase.firestore;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import findit.sedi.viktor.com.findit.App;
import findit.sedi.viktor.com.findit.R;
import findit.sedi.viktor.com.findit.data_providers.cloud.myserver.ServerManager;
import findit.sedi.viktor.com.findit.data_providers.data.Player;
import findit.sedi.viktor.com.findit.data_providers.data.QrPoint;
import findit.sedi.viktor.com.findit.data_providers.data.Team;
import findit.sedi.viktor.com.findit.data_providers.data.Tournament;
import findit.sedi.viktor.com.findit.data_providers.data.User;
import findit.sedi.viktor.com.findit.interactors.KeyCommonSettings;
import findit.sedi.viktor.com.findit.interactors.KeyCommonUpdateUserRequests;
import findit.sedi.viktor.com.findit.presenter.IActionHelper;
import findit.sedi.viktor.com.findit.presenter.interfaces.IAction;
import findit.sedi.viktor.com.findit.presenter.otto.FinditBus;
import findit.sedi.viktor.com.findit.presenter.otto.events.UpdateAllQrPoints;
import findit.sedi.viktor.com.findit.presenter.otto.events.UpdateTournamentsEvent;
import io.reactivex.observers.DisposableObserver;

import static findit.sedi.viktor.com.findit.interactors.KeyCommonBonusFields.KeysField.KEY_BONUS_CODE_GIFT_BONUS;
import static findit.sedi.viktor.com.findit.interactors.KeyCommonPath.KeysField.KEY_BONUS_CODE_PATH;
import static findit.sedi.viktor.com.findit.interactors.KeyCommonPath.KeysField.KEY_QRPOINTS_PATH;
import static findit.sedi.viktor.com.findit.interactors.KeyCommonPath.KeysField.KEY_TEAMS_PATH;
import static findit.sedi.viktor.com.findit.interactors.KeyCommonPath.KeysField.KEY_TOURNAMENTS_PATH;
import static findit.sedi.viktor.com.findit.interactors.KeyCommonPath.KeysField.KEY_USERS_PATH;
import static findit.sedi.viktor.com.findit.interactors.KeyCommonQrPointsFields.KeysField.QRPOINT_BONUS;
import static findit.sedi.viktor.com.findit.interactors.KeyCommonQrPointsFields.KeysField.QRPOINT_DIFFICULTY;
import static findit.sedi.viktor.com.findit.interactors.KeyCommonQrPointsFields.KeysField.QRPOINT_DISTANCE;
import static findit.sedi.viktor.com.findit.interactors.KeyCommonQrPointsFields.KeysField.QRPOINT_IS_MAIN;
import static findit.sedi.viktor.com.findit.interactors.KeyCommonQrPointsFields.KeysField.QRPOINT_LOCATION;
import static findit.sedi.viktor.com.findit.interactors.KeyCommonQrPointsFields.KeysField.QRPOINT_MARK;
import static findit.sedi.viktor.com.findit.interactors.KeyCommonQrPointsFields.KeysField.QRPOINT_QUESTS;
import static findit.sedi.viktor.com.findit.interactors.KeyCommonQrPointsFields.KeysField.QRPOINT_QUEST_BONUS;
import static findit.sedi.viktor.com.findit.interactors.KeyCommonQrPointsFields.KeysField.QRPOINT_TIP_FOR_CURRENT;
import static findit.sedi.viktor.com.findit.interactors.KeyCommonQrPointsFields.KeysField.QRPOINT_TIP_FOR_NEXT;
import static findit.sedi.viktor.com.findit.interactors.KeyCommonQrPointsFields.KeysField.QRPOINT_TIP_PHOTO;
import static findit.sedi.viktor.com.findit.interactors.KeyCommonQrPointsFields.KeysField.QRPOINT_TOURNAMENT_ID;
import static findit.sedi.viktor.com.findit.interactors.KeyCommonQrPointsFields.KeysField.QRPOINT_TYPE;
import static findit.sedi.viktor.com.findit.interactors.KeyCommonSettings.KeysField.LOG_TAG;
import static findit.sedi.viktor.com.findit.interactors.KeyCommonTeamsFields.KeysField.KEY_TEAM_BONUS;
import static findit.sedi.viktor.com.findit.interactors.KeyCommonTeamsFields.KeysField.KEY_TEAM_NAME;
import static findit.sedi.viktor.com.findit.interactors.KeyCommonTeamsFields.KeysField.KEY_TEAM_PLAYER_IDS;
import static findit.sedi.viktor.com.findit.interactors.KeyCommonTeamsFields.KeysField.KEY_TEAM_TOURNAMENT_ID;
import static findit.sedi.viktor.com.findit.interactors.KeyCommonTournamentsFields.KeysField.TOURNAMENTS_DATE_FROM;
import static findit.sedi.viktor.com.findit.interactors.KeyCommonTournamentsFields.KeysField.TOURNAMENTS_DATE_TO;
import static findit.sedi.viktor.com.findit.interactors.KeyCommonTournamentsFields.KeysField.TOURNAMENTS_DESCRIBE;
import static findit.sedi.viktor.com.findit.interactors.KeyCommonTournamentsFields.KeysField.TOURNAMENTS_DIFFICULTY;
import static findit.sedi.viktor.com.findit.interactors.KeyCommonTournamentsFields.KeysField.TOURNAMENTS_PLAYERS_IDS;
import static findit.sedi.viktor.com.findit.interactors.KeyCommonTournamentsFields.KeysField.TOURNAMENTS_TEAMS_IDS;
import static findit.sedi.viktor.com.findit.interactors.KeyCommonTournamentsFields.KeysField.TOURNAMENTS_TIPS;
import static findit.sedi.viktor.com.findit.interactors.KeyCommonTournamentsFields.KeysField.TOURNAMENTS_TOTAL_BONUSES;
import static findit.sedi.viktor.com.findit.interactors.KeyCommonTournamentsFields.KeysField.TOURNAMENTS_TYPE;
import static findit.sedi.viktor.com.findit.interactors.KeyCommonUpdateUserRequests.KeysField.KEY_UPDATE_BONUS;
import static findit.sedi.viktor.com.findit.interactors.KeyCommonUpdateUserRequests.KeysField.KEY_UPDATE_DISCOVERED_QR_POINTS;
import static findit.sedi.viktor.com.findit.interactors.KeyCommonUpdateUserRequests.KeysField.KEY_UPDATE_FONDED_QR_POINTS;
import static findit.sedi.viktor.com.findit.interactors.KeyCommonUpdateUserRequests.KeysField.KEY_UPDATE_LOCATION;
import static findit.sedi.viktor.com.findit.interactors.KeyCommonUpdateUserRequests.KeysField.KEY_UPDATE_NET_STATUS;
import static findit.sedi.viktor.com.findit.interactors.KeyCommonUpdateUserRequests.KeysField.KEY_UPDATE_PROFILE;
import static findit.sedi.viktor.com.findit.interactors.KeyCommonUpdateUserRequests.KeysField.KEY_UPDATE_TOURNAMENT;
import static findit.sedi.viktor.com.findit.interactors.KeyCommonUserFields.KeysField.USER_ACCOUNT_TYPE;
import static findit.sedi.viktor.com.findit.interactors.KeyCommonUserFields.KeysField.USER_BONUS;
import static findit.sedi.viktor.com.findit.interactors.KeyCommonUserFields.KeysField.USER_DISCOVERED_QR_POINTS;
import static findit.sedi.viktor.com.findit.interactors.KeyCommonUserFields.KeysField.USER_EMAIL;
import static findit.sedi.viktor.com.findit.interactors.KeyCommonUserFields.KeysField.USER_FONDED_QR_POINTS;
import static findit.sedi.viktor.com.findit.interactors.KeyCommonUserFields.KeysField.USER_GENDER;
import static findit.sedi.viktor.com.findit.interactors.KeyCommonUserFields.KeysField.USER_LOCATION;
import static findit.sedi.viktor.com.findit.interactors.KeyCommonUserFields.KeysField.USER_NAME;
import static findit.sedi.viktor.com.findit.interactors.KeyCommonUserFields.KeysField.USER_NET_STATUS;
import static findit.sedi.viktor.com.findit.interactors.KeyCommonUserFields.KeysField.USER_PASSWORD;
import static findit.sedi.viktor.com.findit.interactors.KeyCommonUserFields.KeysField.USER_PHONE;
import static findit.sedi.viktor.com.findit.interactors.KeyCommonUserFields.KeysField.USER_PHOTO;
import static findit.sedi.viktor.com.findit.interactors.KeyCommonUserFields.KeysField.USER_SUM_OF_DISCOVERED_POINTS;
import static findit.sedi.viktor.com.findit.interactors.KeyCommonUserFields.KeysField.USER_SUM_OF_FONDED_POINTS;
import static findit.sedi.viktor.com.findit.interactors.KeyCommonUserFields.KeysField.USER_TEAM_ID;
import static findit.sedi.viktor.com.findit.interactors.KeyCommonUserFields.KeysField.USER_TOTAL_BONUS;
import static findit.sedi.viktor.com.findit.interactors.KeyCommonUserFields.KeysField.USER_TOURNAMENT_ID;


public class CloudFirestoreManager {


    private static final CloudFirestoreManager ourInstance = new CloudFirestoreManager();


    // Вынесем его так же как сделали в Tournament
    // Придётся ставить хак для работы с Enum

    private DocumentReference document;
    private FirebaseFirestore mFirebaseFirestore;
    private Context mContext = App.instance.getBaseContext();

    public static CloudFirestoreManager getInstance() {
        return ourInstance;
    }

    private CloudFirestoreManager() {
        mFirebaseFirestore = FirebaseFirestore.getInstance();
    }


    // Нужно будет доработать конструкцию, заменив строки на Enum Или KeyCommonPath
    public void updateUser(String tag) {

        if (App.instance.getAccountManager().getUser() == null)
            return;


        Log.d(LOG_TAG, "User " + App.instance.getAccountManager().getUser().getID());

        document = mFirebaseFirestore.collection(KEY_USERS_PATH).document(App.instance.getAccountManager().getUser().getID());
        // Логика такова, работа во втором потоке, он запускает другие потоки,
        // Сам засыпает на 1 секунду, если обновления успешны у всех трёх потоков, то останавливаем сами себя и отплавляем событие на обновление
        // Данных
        if (tag.equalsIgnoreCase(KEY_UPDATE_PROFILE)) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {


                    final int[] succesResult = {0};


                    document.update(USER_NAME, App.instance.getAccountManager().getUser().getName())
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

                    document.update(USER_PHONE, App.instance.getAccountManager().getUser().getPhone())
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

                    document.update(USER_GENDER, App.instance.getAccountManager().getUser().getGender())
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

                    String email = App.instance.getAccountManager().getUser().getEmail();

                    App.instance.getAccountManager().updateUserByEmail(email);

                }
            });
            thread.start();

        } else if (tag.equalsIgnoreCase(KEY_UPDATE_LOCATION)) {


            document.update(USER_LOCATION, App.instance.getAccountManager().getUser().getGeopoint())
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

        } else if (tag.equalsIgnoreCase(KEY_UPDATE_NET_STATUS)) {

            document.update(USER_NET_STATUS, true)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            App.instance.getAccountManager().updateUserByEmail(App.instance.getAccountManager().getUser().getEmail());

                            Log.d(LOG_TAG, task + " => " + task.getResult());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            e.printStackTrace();
                        }
                    });
        } else if (tag.equalsIgnoreCase(KEY_UPDATE_DISCOVERED_QR_POINTS)) {

            document.update(USER_DISCOVERED_QR_POINTS, App.instance.getAccountManager().getUser().getDiscoveredQrPointIDs())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            App.instance.getAccountManager().updateUserByEmail(App.instance.getAccountManager().getUser().getEmail());
                            Log.d(LOG_TAG, task + " => " + task.getResult());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            e.printStackTrace();
                        }
                    });

            document.update(USER_SUM_OF_DISCOVERED_POINTS, App.instance.getAccountManager().getUser().getSumOfFondedPoints() + 1)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            App.instance.getAccountManager().updateUserByEmail(App.instance.getAccountManager().getUser().getEmail());
                            Log.d(LOG_TAG, task + " => " + task.getResult());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            e.printStackTrace();
                        }
                    });


        } else if (tag.equalsIgnoreCase(KEY_UPDATE_BONUS)) {
            document.update(USER_BONUS, App.instance.getAccountManager().getUser().getBonus())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            App.instance.getAccountManager().updateUserByEmail(App.instance.getAccountManager().getUser().getEmail());
                            Log.d(LOG_TAG, task + "Updated user bonus => " + task.getResult());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            e.printStackTrace();
                        }
                    });

            document.update(USER_TOTAL_BONUS, App.instance.getAccountManager().getUser().getTotalBonus())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            App.instance.getAccountManager().updateUserByEmail(App.instance.getAccountManager().getUser().getEmail());
                            Log.d(LOG_TAG, task + "Updated user total bonus => " + task.getResult());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            e.printStackTrace();
                        }
                    });

        } else if (tag.equalsIgnoreCase(KEY_UPDATE_FONDED_QR_POINTS)) {
            document.update(USER_FONDED_QR_POINTS, App.instance.getAccountManager().getUser().getFondedQrPointsIDs())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            App.instance.getAccountManager().updateUserByEmail(App.instance.getAccountManager().getUser().getEmail());
                            Log.d(LOG_TAG, task + " => " + task.getResult());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            e.printStackTrace();
                        }
                    });

            document.update(USER_SUM_OF_FONDED_POINTS, App.instance.getAccountManager().getUser().getSumOfFondedPoints() + 1)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            App.instance.getAccountManager().updateUserByEmail(App.instance.getAccountManager().getUser().getEmail());
                            Log.d(LOG_TAG, task + " => " + task.getResult());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            e.printStackTrace();
                        }
                    });
        } else if (tag.equalsIgnoreCase(KEY_UPDATE_TOURNAMENT)) {
            document.update(USER_TOURNAMENT_ID, App.instance.getAccountManager().getUser().getTournamentID())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            Log.d(LOG_TAG, task + "Обновление турниров => " + task.getResult());


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            e.printStackTrace();
                        }
                    });

            document.update(USER_TEAM_ID, App.instance.getAccountManager().getUser().getTeamID())
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

        document = mFirebaseFirestore.collection(KEY_USERS_PATH).document(App.instance.getAccountManager().getUser().getID());
        document.update(USER_NET_STATUS, status);


        Log.d(LOG_TAG, "changeUserNetStatus() User " + App.instance.getAccountManager().getUser().getID());

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

                                App.instance.getTeamManager().addTeam(
                                        new Team(document.getString(KEY_TEAM_TOURNAMENT_ID),
                                                (List<String>) document.get(KEY_TEAM_PLAYER_IDS),
                                                document.getId(),
                                                document.getString(KEY_TEAM_NAME),
                                                document.getLong(KEY_TEAM_BONUS)));
                                Log.d(LOG_TAG, document.getId() + " => " + document.getData());
                            }
                        }
                    } else {
                        Log.w(LOG_TAG, "Error getting documents.", task.getException());
                    }
                });

    }

    public void updateTeams() {

        App.instance.getTeamManager().clearTeams();

        FirebaseFirestore.getInstance().collection(KEY_TEAMS_PATH).get()
                .addOnFailureListener(e -> Log.w(LOG_TAG, "Error getting documents. Failure"))
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful() && task.getResult() != null) {
                        for (QueryDocumentSnapshot document : task.getResult()) {

                            if (document.exists()) {

                                App.instance.getTeamManager().addTeam(
                                        new Team(document.getString(KEY_TEAM_TOURNAMENT_ID),
                                                (List<String>) document.get(KEY_TEAM_PLAYER_IDS),
                                                document.getId(),
                                                document.getString(KEY_TEAM_NAME),
                                                document.getLong(KEY_TEAM_BONUS)));
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


                                App.instance.getTournamentManager().addTournament(
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

                                Log.d(LOG_TAG, document.getId() + "getTournaments() => " + document.getData());
                            }

                            FinditBus.getInstance().post(new UpdateTournamentsEvent());

                        }
                    } else {
                        Log.w(LOG_TAG, "Error getting documents.", task.getException());
                    }
                });
    }


    public void createUser(String email, String password, String name, String photoUrl) {

        // Create a new user with a first and last name
        // Инициализируем остальные значения по умолчанию
        Map<String, Object> user = new HashMap<>();
        user.put(USER_EMAIL, email);
        user.put(USER_PASSWORD, password);
        user.put(USER_NET_STATUS, true);
        user.put(USER_GENDER, 0);
        user.put(USER_BONUS, 0);
        user.put(USER_PHOTO, photoUrl);
        user.put(USER_LOCATION, new GeoPoint(0, 0));
        user.put(USER_ACCOUNT_TYPE, "Free");
        user.put(USER_NAME, name);
        user.put(USER_PHONE, "");
        user.put(USER_TEAM_ID, "");
        user.put(USER_TOTAL_BONUS, 0);
        user.put(USER_TOURNAMENT_ID, "");
        user.put(USER_DISCOVERED_QR_POINTS, new ArrayList<String>());
        user.put(USER_FONDED_QR_POINTS, new ArrayList<String>());
        user.put(USER_SUM_OF_FONDED_POINTS, 0);
        user.put(USER_SUM_OF_DISCOVERED_POINTS, 0);


        // Add a new document with a generated ID
        mFirebaseFirestore.collection(KEY_USERS_PATH)
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        App.instance.getAccountManager().updateUserByEmail(email);
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


    public void initUser(String email) {


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
                                App.instance.getAccountManager().initUser(new User(document.getString(USER_PHONE),
                                        document.getString(USER_NAME),
                                        document.getId(),
                                        document.getString(USER_EMAIL),
                                        document.getLong(USER_BONUS) == null ? 0 : document.getLong(USER_BONUS),
                                        document.getString(USER_PHOTO),
                                        document.getString(USER_PASSWORD),
                                        document.getLong(USER_GENDER) == null ? 0 : document.getLong(USER_GENDER),
                                        document.getString(USER_TOURNAMENT_ID),
                                        document.getString(USER_TEAM_ID),
                                        document.getLong(USER_TOTAL_BONUS) == null ? 0 : document.getLong(USER_TOTAL_BONUS),
                                        (ArrayList<String>) document.get(USER_DISCOVERED_QR_POINTS),
                                        (ArrayList<String>) document.get(USER_FONDED_QR_POINTS),
                                        document.getLong(USER_SUM_OF_FONDED_POINTS) == null ? 0 : document.getLong(USER_SUM_OF_FONDED_POINTS),
                                        document.getLong(USER_SUM_OF_DISCOVERED_POINTS) == null ? 0 : document.getLong(USER_SUM_OF_DISCOVERED_POINTS)

                                ));


                                Log.d(LOG_TAG, "initUser() User " + App.instance.getAccountManager().getUser().getID());


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
                            if (document.getId().equalsIgnoreCase(App.instance.getAccountManager().getUser().getID()))
                                continue;

                            // Обновляем значение по ID что эти точки уже нашли другие пользователи
                            // Карта при обновлени автоматически подхватит измененения
                            App.instance.getPlayersManager().addPlayer(new Player(
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
                                    document.getLong(USER_GENDER),
                                    document.getLong(USER_SUM_OF_FONDED_POINTS) == null ? 0 : document.getLong(USER_SUM_OF_FONDED_POINTS),
                                    document.getLong(USER_SUM_OF_DISCOVERED_POINTS) == null ? 0 : document.getLong(USER_SUM_OF_FONDED_POINTS)));

                            Log.d(LOG_TAG, document.getId() + "getPlayers() => " + document.getData());
                        }


                    } else {
                        Log.w(LOG_TAG, "Error getting documents.", task.getException());
                    }
                });
    }


    public void updatePlayers() {


        mFirebaseFirestore.collection(KEY_USERS_PATH).get()
                .addOnFailureListener(e -> Log.w(LOG_TAG, "Error getting documents. Failure"))
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {

                        App.instance.getPlayersManager().clearPlayers();

                        for (QueryDocumentSnapshot document : task.getResult()) {


                            GeoPoint geoPoint = document.getGeoPoint(USER_LOCATION);

                            // Ставим ограничение, если ID равен нашему аккаунту, то игнорим
                            if (App.instance.getAccountManager().getUser() != null)
                                if (document.getId().equalsIgnoreCase(App.instance.getAccountManager().getUser().getID()))
                                    continue;

                            // Обновляем значение по ID что эти точки уже нашли другие пользователи
                            // Карта при обновлени автоматически подхватит измененения
                            App.instance.getPlayersManager().addPlayer(new Player(
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
                                    document.getLong(USER_GENDER),
                                    document.getLong(USER_SUM_OF_FONDED_POINTS) == null ? 0 : document.getLong(USER_SUM_OF_FONDED_POINTS),
                                    document.getLong(USER_SUM_OF_DISCOVERED_POINTS) == null ? 0 : document.getLong(USER_SUM_OF_FONDED_POINTS))

                            );

                            Log.d(LOG_TAG, document.getId() + "updatePlayers() => " + document.getData());
                        }


                    } else {
                        Log.w(LOG_TAG, "Error getting documents.", task.getException());
                    }
                });
    }


    public void updateQrPointByID(String id, String mark) {


        document = mFirebaseFirestore.collection(KEY_QRPOINTS_PATH).document(id);
        document.update(QRPOINT_MARK, mark).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                getQrPlaces();
            }
        });

    }

    public void getQrPlaces() {


        mFirebaseFirestore.collection(KEY_QRPOINTS_PATH).get()
                .addOnFailureListener(e -> Log.w(LOG_TAG, "Error getting documents. Failure"))
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {

                        App.instance.getQrPointManager().clearQrPoints();

                        for (QueryDocumentSnapshot document : task.getResult()) {


                            // Ставим ограничение, если не равна нашему турниру, то откллоняем
                            if (App.instance.getAccountManager().getUser() != null)
                                if (!document.getString(QRPOINT_TOURNAMENT_ID).equalsIgnoreCase(App.instance.getAccountManager().getUser().getTournamentID()))
                                    continue;

                            GeoPoint geoPoint = document.getGeoPoint(QRPOINT_LOCATION);

                            // Обновляем значение по ID что эти точки уже нашли другие пользователи
                            // Карта при обновлени автоматически подхватит измененения
                            App.instance.getQrPointManager().addQrPoint(new QrPoint(
                                    document.getLong(QRPOINT_BONUS),
                                    document.getBoolean(QRPOINT_TYPE),
                                    document.getString(QRPOINT_MARK),
                                    document.getLong(QRPOINT_QUEST_BONUS),
                                    (HashMap<String, ArrayList<String>>) document.get(QRPOINT_QUESTS),
                                    document.getString(QRPOINT_TIP_FOR_NEXT),
                                    document.getString(QRPOINT_TIP_FOR_CURRENT),
                                    document.getString(QRPOINT_TOURNAMENT_ID),
                                    document.getBoolean(QRPOINT_IS_MAIN),
                                    document.getString(QRPOINT_TIP_PHOTO),
                                    geoPoint.getLatitude(),
                                    geoPoint.getLongitude(),
                                    document.getLong(QRPOINT_DISTANCE),
                                    document.getLong(QRPOINT_DIFFICULTY),
                                    document.getId())
                            );

                            Log.d(LOG_TAG, document.getId() + "getQrPlaces()  => " + document.getData());
                        }

                        FinditBus.getInstance().post(new UpdateAllQrPoints());

                    } else {
                        Log.w(LOG_TAG, "Error getting documents.", task.getException());
                    }
                });

    }


    public void resetQrPlaceBonus(String code) {
        document = mFirebaseFirestore.collection(KEY_QRPOINTS_PATH).document(code);
        document.update(QRPOINT_BONUS, 0);
    }

    public void updateTournament(String id, String tag) {

        document = mFirebaseFirestore.collection(KEY_TOURNAMENTS_PATH).document(id);


        document.update(TOURNAMENTS_PLAYERS_IDS, App.instance.getTournamentManager().getTournament(id).getPlayersIDs())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {


                        ServerManager.getInstance().getTournaments();

                        IActionHelper.getInstance().action();

                        ServerManager.getInstance().updateUserOnServer(KeyCommonUpdateUserRequests.KeysField.KEY_UPDATE_TOURNAMENT);


                        Log.d(LOG_TAG, task + " => " + task.getResult());
                    }
                });


    }

    public void checkProfile(String email, DisposableObserver<Boolean> observable) {

        final boolean[] result = {false};

        mFirebaseFirestore.collection(KEY_USERS_PATH).get()
                .addOnFailureListener(e -> Log.w(LOG_TAG, "Error getting documents. Failure"))
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        for (QueryDocumentSnapshot document : task.getResult()) {

                            Log.d(LOG_TAG, task + " => " + document.getString(USER_EMAIL));

                            // Ставим ограничение, если ID равен нашему аккаунту, то игнорим
                            if (document.getString(USER_EMAIL).equalsIgnoreCase(email)) {
                                result[0] = true;
                                observable.onNext(result[0]);
                                observable.dispose();
                                return;
                            }
                        }

                        observable.onNext(result[0]);
                        observable.dispose();


                    } else {
                        Log.w(LOG_TAG, "Error getting documents.", task.getException());
                    }
                });


    }

    public void getBonusByCode(String code, IAction iAction) {

        // Получаем список всех бонусных кодов, если одна из них не найденна, то сообщаем об ошибке
        mFirebaseFirestore.collection(KEY_BONUS_CODE_PATH).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {


                if (task.isSuccessful() && task.getResult() != null) {

                    boolean success = false;

                    for (QueryDocumentSnapshot document : task.getResult()) {

                        Log.d(LOG_TAG, task + " => " + document.getString(USER_EMAIL));

                        // Если совпала, то получаем информацию по бонусам, добавляем и удаляем этот код
                        if (document.getId().equals(code)) {

                            if (iAction != null) {
                                iAction.action();
                            }

                            success = true;

                            App.instance.getAccountManager().getUser().addBonus(document.getLong(KEY_BONUS_CODE_GIFT_BONUS));

                            Toast.makeText(App.instance.getApplicationContext(),
                                    App.instance.getApplicationContext().getResources().getString(R.string.bonuses_added_succes), Toast.LENGTH_LONG).show();

                            ServerManager.getInstance().updateUserOnServer(KEY_UPDATE_BONUS);

                            mFirebaseFirestore.collection(KEY_BONUS_CODE_PATH).document(code).delete();

                            break;

                        }

                    }

                    if (success == false) {
                        Toast.makeText(App.instance.getApplicationContext(),
                                App.instance.getApplicationContext().getResources().getString(R.string.bonuses_added_error), Toast.LENGTH_LONG).show();
                    }


                } else {
                    Log.w(LOG_TAG, "Error getting documents.", task.getException());
                }


            }
        });

    }
}
