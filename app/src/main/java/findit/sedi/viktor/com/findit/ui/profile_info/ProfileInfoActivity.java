package findit.sedi.viktor.com.findit.ui.profile_info;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import findit.sedi.viktor.com.findit.App;
import findit.sedi.viktor.com.findit.R;
import findit.sedi.viktor.com.findit.common.ManagersFactory;
import findit.sedi.viktor.com.findit.data_providers.cloud.myserver.ServerManager;
import findit.sedi.viktor.com.findit.data_providers.data.User;
import findit.sedi.viktor.com.findit.presenter.otto.FinditBus;
import findit.sedi.viktor.com.findit.ui.RegisterActivity;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;

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


    //Logic
    private FirebaseUser mFirebaseUser;
    private DisposableObserver<User> mUserObserver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.profile_layout);

        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        initUI();


    }

    // Добавить вкладки (табы) в одной он видит свой профиль, в другой видит список мест в которых он побывал
    private void initUI() {

        User user = ManagersFactory.getInstance().getAccountManager().getUser();

        mTextViewBonus = findViewById(R.id.tv_bonus_info);
        mSpinnerGender = findViewById(R.id.sp_gender);
        mEditTextPhone = findViewById(R.id.et_phone);
        mTextViewSignOut = findViewById(R.id.tv_sign_out);
        mEditTextEmail = findViewById(R.id.et_email);
        mEditTextPassword = findViewById(R.id.et_password);
        mButtonSave = findViewById(R.id.btn_save);
        mEditTextName = findViewById(R.id.et_name);

        mEditTextEmail.setText(mFirebaseUser.getEmail());
        mEditTextPassword.setText(user.getPassword());
        mTextViewBonus.setText(getResources().getString(R.string.bonus) + ": " + String.valueOf((int) user.getBonus()));
        mEditTextName.setText(user.getName());
        mEditTextPhone.setText(user.getPhone());
        mSpinnerGender.setSelection((int) user.getGender());


        mTextViewSignOut.setOnClickListener(this::onClick);
        mButtonSave.setOnClickListener(this::onClick);

    }

    @Override
    protected void onResume() {
        super.onResume();

        mUserObserver = ManagersFactory.getInstance().getAccountManager().getChanges()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<User>() {
                    @Override
                    public void onNext(User user) {
                        mEditTextEmail.setText(mFirebaseUser.getEmail());
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

    }

    @Override
    public void onClick(View v) {

        // Отправить на сервер при выходе об измененении статуса онлайн Users
        if (v.getId() == R.id.tv_sign_out) {
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(this, "Пользователь вышел", Toast.LENGTH_LONG).show();
            ServerManager.getInstance().changeUserNetStatus(false);
            startActivity(new Intent(this, RegisterActivity.class));
        }

        if (v.getId() == R.id.btn_save) {
            // Получаем изменененияе полей которые нужно изменить на сервере

            User user = ManagersFactory.getInstance().getAccountManager().getUser();

            user.setName(mEditTextName.getText().toString());
            user.setPhone(mEditTextPhone.getText().toString());
            user.setGender(mSpinnerGender.getSelectedItemPosition());


            ServerManager.getInstance().updateUserOnServer(KEY_UPDATE_PROFILE);
        }

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
