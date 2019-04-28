package findit.sedi.viktor.com.findit.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import findit.sedi.viktor.com.findit.R;
import findit.sedi.viktor.com.findit.interactors.KeyCommonSettings;
import findit.sedi.viktor.com.findit.ui.preloader.PreviewActivity;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 201;
    private FirebaseAuth mAuth;

    // View
    private Button mButtonOk;
    private TextView mTextViewEmail, mTextViewPassword, mTextViewPasswordRepeat;
    private TextInputEditText mEditEmail;
    private TextInputEditText mEditPassword;
    private TextInputEditText mEditPasswordRepeat;
    private SwitchCompat mSwitchCompat;

    // Logic
    private boolean isRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


    }

    private boolean checkPlayServices() {
        GoogleApiAvailability gApi = GoogleApiAvailability.getInstance();
        int resultCode = gApi.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (gApi.isUserResolvableError(resultCode)) {
                gApi.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(this, getResources().getString(R.string.update_google_play_services), Toast.LENGTH_LONG).show();
                finish();
            }
            return false;
        }
        return true;
    }


    @Override
    public void onStart() {
        super.onStart();

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);

    }

    private void updateUI(FirebaseUser currentUser) {


        // Если пользователь зарегестрированн  в системе, вытаскиваем из преференсов данные
        // То сразу переходим в
        if (currentUser == null) {

            setContentView(R.layout.register_layout);

            Toast.makeText(this, "Не зарегестрированный", Toast.LENGTH_LONG).show();

            mButtonOk = findViewById(R.id.btn_register);
            mEditEmail = findViewById(R.id.et_email);
            mEditPassword = findViewById(R.id.et_password);
            mEditPasswordRepeat = findViewById(R.id.et_password_repeat);
            mTextViewPasswordRepeat = findViewById(R.id.tv_info_password_repeat);
            mSwitchCompat = findViewById(R.id.swRegistration);


            setUIListeners();

            mButtonOk.setOnClickListener(this::onClick);

        }

        if (currentUser != null) {
            startNextActivity();
        }


    }

    private void startNextActivity() {

        checkPlayServices();

        startActivity(new Intent(this, PreviewActivity.class));
    }

    private void setUIListeners() {
        mSwitchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isRegister = isChecked;
                if (isRegister) {
                    mTextViewPasswordRepeat.setVisibility(View.VISIBLE);
                    mEditPasswordRepeat.setVisibility(View.VISIBLE);
                    mButtonOk.setText("Регистрация");
                } else {
                    mTextViewPasswordRepeat.setVisibility(View.GONE);
                    mEditPasswordRepeat.setVisibility(View.GONE);
                    mButtonOk.setText("Войти");
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_register) {
            registerOrEnterUser();
        }
    }

    private void registerOrEnterUser() {

        if (isRegister) {
            registerUser();
        } else {
            enterUser();
        }

    }

    private void enterUser() {
        mAuth.signInWithEmailAndPassword(mEditEmail.getText().toString(), mEditPassword.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(RegisterActivity.this, "Аутентификация успешно пройдена.",
                                    Toast.LENGTH_SHORT).show();

                            startNextActivity();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(RegisterActivity.this, "Ошибка аутентификации",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    private void registerUser() {

        // Проверка полей ввода на кооректность данных
        mAuth.createUserWithEmailAndPassword(mEditEmail.getText().toString(), mEditPassword.getText().toString())
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegisterActivity.this, "Ошибка регистрации " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                })
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "Регистрация пройденна успешно", Toast.LENGTH_LONG).show();
                            startNextActivity();
                        }

                    }
                });
    }


}
