package findit.sedi.viktor.com.findit.ui.preloader;

import androidx.lifecycle.Lifecycle;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import findit.sedi.viktor.com.findit.App;
import findit.sedi.viktor.com.findit.R;
import findit.sedi.viktor.com.findit.data_providers.cloud.myserver.ServerManager;
import findit.sedi.viktor.com.findit.data_providers.data.User;
import findit.sedi.viktor.com.findit.presenter.ProgressLoder;
import findit.sedi.viktor.com.findit.ui.main.MainActivity;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import pl.droidsonroids.gif.GifImageView;

import static findit.sedi.viktor.com.findit.interactors.KeyAccountRegistration.KeysField.KEY_ACCOUNT_TYPE;
import static findit.sedi.viktor.com.findit.interactors.KeyAccountRegistration.KeysField.KEY_EMAIL;
import static findit.sedi.viktor.com.findit.interactors.KeyAccountRegistration.KeysField.KEY_PHOTO_URL;
import static findit.sedi.viktor.com.findit.interactors.KeyAccountRegistration.KeysField.KEY_USER_NAME;
import static findit.sedi.viktor.com.findit.interactors.KeyCommonSettings.KeysField.LOG_TAG;
import static findit.sedi.viktor.com.findit.interactors.KeyCommonUpdateUserRequests.KeysField.KEY_UPDATE_NET_STATUS;

public class PreviewActivity extends AppCompatActivity {


    //  User Data
    private String email;
    private String name;
    private String photoUrl;

    private Handler mDataLoader = new Handler();
    private Thread mThread;
    private DisposableObserver<User> mUserObserver;
    private DisposableObserver<Boolean> mBooleanGoogleRegistrationObserver;

    private GifImageView mGifImageView;
    private ProgressLoder mProgressLoder;

    // View


    public static Intent getIntent(Context context, String accountType, String email, String displayName, String photoUrl) {
        Intent intent = new Intent(context, PreviewActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.putExtra(KEY_ACCOUNT_TYPE, accountType);
        intent.putExtra(KEY_EMAIL, email);
        intent.putExtra(KEY_USER_NAME, displayName);
        intent.putExtra(KEY_PHOTO_URL, photoUrl);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(LOG_TAG, "PreviewActivity: onCreate()");


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        initUserObserver();

        email = getIntent().getStringExtra(KEY_EMAIL);
        photoUrl = getIntent().getStringExtra(KEY_PHOTO_URL);
        name = getIntent().getStringExtra(KEY_USER_NAME);

        if (getIntent().getStringExtra(KEY_ACCOUNT_TYPE).equalsIgnoreCase("byGoogle"))
            observeGoogleAuth();


        setContentView(R.layout.preview_layout);

        mGifImageView = findViewById(R.id.gif_image);
        mGifImageView.setImageResource(R.drawable.loader_spinner_1s_200px);
        mGifImageView.animate();

        //     loadDataFromServer();

    }

    private void observeGoogleAuth() {


        mBooleanGoogleRegistrationObserver = new DisposableObserver<Boolean>() {
            @Override
            public void onNext(Boolean aBoolean) {

                if (!aBoolean) {
                    Toast.makeText(PreviewActivity.this, "Регистрация успешно пройдена.",
                            Toast.LENGTH_SHORT).show();

                    User user = new User();
                    user.setName(name);
                    user.setEmail(email);
                    user.setPhotoUrl(photoUrl);

                    App.instance.getGoogleStore().initUser(new User());

                    ServerManager.getInstance().createNewUser(email, "byGoogle", name, photoUrl);
                } else {
                    Toast.makeText(PreviewActivity.this, "Аутентификация успешно пройдена.",
                            Toast.LENGTH_SHORT).show();
                    ServerManager.getInstance().updateUser(email);
                }

                mBooleanGoogleRegistrationObserver.dispose();

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };

        // Смотрим есть ли такой пользователь на сервере с таким email, если нет, то регистрация, иначе аутентификация
        ServerManager.getInstance().checkProfile(email, mBooleanGoogleRegistrationObserver);


    }

    private void initUserObserver() {

        mUserObserver =  App.instance.getAccountManager().getChanges()
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<User>() {
                    @Override
                    public void onNext(User user) {

                        if (PreviewActivity.this.getLifecycle().getCurrentState() == Lifecycle.State.DESTROYED) {
                            this.onComplete();
                            mUserObserver.dispose();
                        }

                        Toast.makeText(PreviewActivity.this, "Информация о пользователе синхронизирована",
                                Toast.LENGTH_SHORT).show();
                        Log.d(LOG_TAG, "RegisterActivity User  " + user.getName());

                        if (user.getName() != null) {
                            ServerManager.getInstance().updateUserOnServer(KEY_UPDATE_NET_STATUS);
                            loadDataFromServer();
                            mUserObserver.dispose();
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        Log.d(LOG_TAG, "RegisterActivity onComplete  " + App.instance.getAccountManager().getUser());
                    }
                });

    }


    private void startNextActivity() {

        //checkPlayServices();

        startActivity(new Intent(this, MainActivity.class));
    }


    // Нужно будет по максимуму всё получить с сервера
    // Самую основную информацию кроме самого пользователя
    private void loadDataFromServer() {

        // Защита, чтобы много потоков не пошло в сеть загружать данные
        if (mThread == null || !mThread.isAlive()) {
            mThread = new Thread(new Runnable() {


                @Override
                public void run() {


                    if (mThread.isInterrupted()) {
                        ProgressLoder.getInstance().breakProgress(true);
                        return;
                    }


                    Thread getTeams = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            ServerManager.getInstance().getTeams();
                        }
                    });

                    Thread getTournaments = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            ServerManager.getInstance().getTournaments();
                        }
                    });

                    Thread getPlayers = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            ServerManager.getInstance().getPlayers();
                        }
                    });


                    // ServerManager.getInstance().getQrPlaces();
                    // Очередь нам не важна, просто получаем значения и инициализируем пока в наши менеджеры
                    getTeams.start();
                    getTournaments.start();

                    getPlayers.start();


                    try {
                        getPlayers.join();
                        getTeams.join();
                        getTournaments.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    mDataLoader.post(new Runnable() {
                        @Override
                        public void run() {

                            Toast.makeText(PreviewActivity.this, "Данные загруженны " + "\n" + "Открытие карты", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(PreviewActivity.this, MainActivity.class));

                        }
                    });


                }
            });
            mThread.start();
        }

    }




    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mUserObserver != null && !mUserObserver.isDisposed()) {
            mUserObserver.dispose();
        }
        if (mBooleanGoogleRegistrationObserver != null && !mBooleanGoogleRegistrationObserver.isDisposed()) {
            mBooleanGoogleRegistrationObserver.dispose();
        }
    }
}
