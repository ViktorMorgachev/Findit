package findit.sedi.viktor.com.findit.ui.profile_info;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import findit.sedi.viktor.com.findit.App;
import findit.sedi.viktor.com.findit.R;
import findit.sedi.viktor.com.findit.common.background_services.MyService;
import findit.sedi.viktor.com.findit.common.dialogs.DialogManager;
import findit.sedi.viktor.com.findit.data_providers.cloud.myserver.ServerManager;
import findit.sedi.viktor.com.findit.data_providers.data.User;
import findit.sedi.viktor.com.findit.presenter.otto.FinditBus;
import findit.sedi.viktor.com.findit.ui.RegisterActivity;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import ru.terrakok.cicerone.Navigator;
import ru.terrakok.cicerone.commands.Command;

import static findit.sedi.viktor.com.findit.interactors.KeyCommonUpdateUserRequests.KeysField.KEY_UPDATE_PROFILE;

public class ProfileInfoActivity extends AppCompatActivity implements View.OnClickListener {

    // View
    private TextView mTextViewBonus;
    private TextView mTextViewSignOut;
    private TextView mTextViewRating;
    private EditText mEditTextName;
    private EditText mEditTextEmail;
    private Spinner mSpinnerGender;
    private EditText mEditTextPassword;
    private EditText mEditTextPhone;
    private Button mButtonSave;
    private ImageView mImageView;


    //Logic
    private DisposableObserver<User> mUserObserver;
    private GoogleSignInClient mGoogleSignInClient;
    private Navigator mNavigator = new Navigator() {
        @Override
        public void applyCommands(Command[] commands) {

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.profile_layout);


        DialogManager.getInstance().setContext(this);


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        initUI();


    }

    // Добавить вкладки (табы) в одной он видит свой профиль, в другой видит список мест в которых он побывал
    private void initUI() {

        User user =  App.instance.getAccountManager().getUser();

        mTextViewBonus = findViewById(R.id.tv_bonus_info);
        mSpinnerGender = findViewById(R.id.sp_gender);
        mEditTextPhone = findViewById(R.id.et_phone);
        mTextViewSignOut = findViewById(R.id.tv_sign_out);
        mEditTextEmail = findViewById(R.id.et_email);
        mEditTextPassword = findViewById(R.id.et_password);
        mButtonSave = findViewById(R.id.btn_save);
        mEditTextName = findViewById(R.id.et_name);
        mImageView = findViewById(R.id.iv_profile);

        mEditTextEmail.setText(user.getEmail());
        mEditTextPassword.setText(user.getPassword());
        mTextViewBonus.setText(getResources().getString(R.string.bonus) + ": " + String.valueOf((int) user.getBonus()));
        mEditTextName.setText(user.getName());
        mEditTextPhone.setText(user.getPhone());
        mSpinnerGender.setSelection((int) user.getGender());

        Glide.with(this).load(user.getPhotoUrl()).apply(RequestOptions.circleCropTransform()).into(mImageView);


        mTextViewSignOut.setOnClickListener(this::onClick);
        mButtonSave.setOnClickListener(this::onClick);

    }

    @Override
    protected void onResume() {
        super.onResume();

        mUserObserver =  App.instance.getAccountManager().getChanges()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<User>() {
                    @Override
                    public void onNext(User user) {
                        mEditTextEmail.setText(user.getEmail());
                        mEditTextPassword.setText(user.getPassword());
                        mTextViewBonus.setText(getResources().getString(R.string.bonus) + ": " + String.valueOf((int) user.getBonus()));
                        mEditTextName.setText(user.getName());
                        mEditTextPhone.setText(user.getPhone());
                        mSpinnerGender.setSelection((int) user.getGender());
                        Toast.makeText(App.instance, "Обновленно успешно", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

        App.instance.getNavigationHolder().setNavigator(mNavigator);

    }

    @Override
    public void onClick(View v) {

        // Отправить на сервер при выходе об измененении статуса онлайн Users
        if (v.getId() == R.id.tv_sign_out) {


            mGoogleSignInClient.signOut();

            stopService(new Intent(this, MyService.class));

            ServerManager.getInstance().changeUserNetStatus(false);

            App.instance.getAccountManager().clearUser();
            App.instance.getTeamManager().clearTeams();
            App.instance.getTournamentManager().clearTournaments();
            App.instance.getQrPointManager().clearQrPoints();
            App.instance.getPlayersManager().clearPlayers();

            Toast.makeText(this, "Пользователь вышел", Toast.LENGTH_LONG).show();

            startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
        }

        if (v.getId() == R.id.btn_save) {
            // Получаем изменененияе полей которые нужно изменить на сервере

            User user = App.instance.getAccountManager().getUser();

            user.setName(mEditTextName.getText().toString());
            user.setPhone(mEditTextPhone.getText().toString());
            user.setGender(mSpinnerGender.getSelectedItemPosition());


            ServerManager.getInstance().updateUserOnServer(KEY_UPDATE_PROFILE);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        App.instance.getNavigationHolder().removeNavigator();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FinditBus.getInstance().unregister(this);
        if (!mUserObserver.isDisposed()) {
            mUserObserver.dispose();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!mUserObserver.isDisposed()) {
            mUserObserver.dispose();
        }
    }
}
