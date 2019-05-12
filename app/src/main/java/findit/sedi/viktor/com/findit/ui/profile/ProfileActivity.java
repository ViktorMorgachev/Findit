package findit.sedi.viktor.com.findit.ui.profile;

import android.arch.lifecycle.Lifecycle;
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
import com.squareup.otto.Subscribe;

import findit.sedi.viktor.com.findit.App;
import findit.sedi.viktor.com.findit.R;
import findit.sedi.viktor.com.findit.common.ManagersFactory;
import findit.sedi.viktor.com.findit.data_providers.cloud.myserver.ServerManager;
import findit.sedi.viktor.com.findit.data_providers.data.User;
import findit.sedi.viktor.com.findit.presenter.otto.FinditBus;
import findit.sedi.viktor.com.findit.presenter.otto.events.UpdateUsersEvent;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

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
    private Intent mRestartIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.profile_layout);

        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        initUI();


        FinditBus.getInstance().register(this);


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

        mTextViewSignOut.setOnClickListener(this::onClick);
        mButtonSave.setOnClickListener(this::onClick);

        mEditTextEmail.setText(mFirebaseUser.getEmail());
        mEditTextPassword.setText(user.getPassword());
        mTextViewBonus.setText(getResources().getString(R.string.bonus) + ": " + String.valueOf((int) user.getBonus()));
        mEditTextName.setText(user.getName());
        mEditTextPhone.setText(user.getPhone());


        mSpinnerGender.setSelection((int) user.getGender());


    }


    @Subscribe
    public void updateUser(UpdateUsersEvent updateUsersEvent) {

        // Инициализируем значения
        if (this.getLifecycle().getCurrentState() == Lifecycle.State.RESUMED) {

            mEditTextEmail.setText(mFirebaseUser.getEmail());
            mEditTextPassword.setText(ManagersFactory.getInstance().getAccountManager().getUser().getPassword());
            mTextViewBonus.setText(getResources().getString(R.string.bonus) + ": " + (int) ManagersFactory.getInstance().getAccountManager().getUser().getBonus());
            mEditTextName.setText(ManagersFactory.getInstance().getAccountManager().getUser().getName());
            mEditTextPhone.setText(ManagersFactory.getInstance().getAccountManager().getUser().getPhone());
            mSpinnerGender.setSelection((int) ManagersFactory.getInstance().getAccountManager().getUser().getGender());
        }

        Toast.makeText(App.instance, "Обновленно успешно", Toast.LENGTH_SHORT).show();

    }


    @Override
    public void onClick(View v) {

        // Отправить на сервер при выходе об измененении статуса онлайн Users
        if (v.getId() == R.id.tv_sign_out) {
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(this, "Пользователь вышел", Toast.LENGTH_LONG).show();

            ServerManager.getInstance().changeUserNetStatus(false);
        }

        if (v.getId() == R.id.btn_save) {

            // Получаем изменененияе полей которые нужно изменить на сервере


            ManagersFactory.getInstance().getAccountManager().getUser().setPhone(mEditTextPhone.getText().toString());
            ManagersFactory.getInstance().getAccountManager().getUser().setGender(mSpinnerGender.getSelectedItemPosition());
            ManagersFactory.getInstance().getAccountManager().getUser().setName(mEditTextName.getText().toString());

            ServerManager.getInstance().updateUserOnServer("profile");


        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        FinditBus.getInstance().unregister(this);
    }
}
