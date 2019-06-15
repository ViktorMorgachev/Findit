package findit.sedi.viktor.com.findit.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import findit.sedi.viktor.com.findit.R;
import findit.sedi.viktor.com.findit.data_providers.cloud.myserver.ServerManager;
import findit.sedi.viktor.com.findit.interactors.KeyAccountOfType;
import findit.sedi.viktor.com.findit.ui.preloader.PreviewActivity;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 201;
    private static final int RC_SIGN_IN = 1;

    // View
    private Button mButtonOk;
    private TextView mTextViewEmail, mTextViewPassword, mTextViewPasswordRepeat, mTextViewName;
    private TextInputEditText mEditEmail;
    private TextInputEditText mEditPassword;
    private ImageView mImageViewGoogleEnter;
    private TextInputEditText mEditName;
    private TextInputEditText mEditPasswordRepeat;
    private SwitchCompat mSwitchCompat;


    // Logic
    private GoogleSignInClient mGoogleSignInClient;
    private boolean isRegister;
    private FirebaseAuth mAuth;
    private boolean googleApiAvailable = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mAuth = FirebaseAuth.getInstance();

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

    }

    public boolean isGooglePlayServicesAvailable(Activity activity) {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(activity);
        if (status != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(status)) {
                googleApiAvailability.getErrorDialog(activity, status, 2404).show();
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
        GoogleSignInAccount googleSignInAccount = GoogleSignIn.getLastSignedInAccount(this);


        updateUI(currentUser, googleSignInAccount);

    }


    private void updateUI(FirebaseUser currentUser, GoogleSignInAccount googleSignInAccount) {


        googleApiAvailable = isGooglePlayServicesAvailable(this);

        // Если пользователь зарегестрированн  в системе, вытаскиваем из преференсов данные
        // То сразу переходим в
        if (currentUser == null && googleSignInAccount == null) {

            setContentView(R.layout.register_layout);

            Toast.makeText(this, "Не зарегестрированный", Toast.LENGTH_LONG).show();

            mButtonOk = findViewById(R.id.btn_register);
            mEditEmail = findViewById(R.id.et_email);
            mEditPassword = findViewById(R.id.et_password);
            mEditPasswordRepeat = findViewById(R.id.et_password_repeat);
            mTextViewPasswordRepeat = findViewById(R.id.tv_info_password_repeat);
            mSwitchCompat = findViewById(R.id.swRegistration);
            mEditName = findViewById(R.id.et_name);
            mTextViewName = findViewById(R.id.tv_info_name);
            mImageViewGoogleEnter = findViewById(R.id.iv_google_enter);


            setUIListeners();


            mImageViewGoogleEnter.setOnClickListener(this::onClick);
            mButtonOk.setOnClickListener(this::onClick);

        }


        if (currentUser != null) {
            ServerManager.getInstance().updateUser(currentUser.getEmail());
            return;
        }

        if (googleSignInAccount != null) {
            firebaseAuthWithGoogle(googleSignInAccount);
            return;
        }


    }

    private void startNextActivity(Intent intent) {


        startActivity(intent);

    }

    private void setUIListeners() {
        mSwitchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isRegister = isChecked;
                if (isRegister) {
                    mTextViewPasswordRepeat.setVisibility(View.VISIBLE);
                    mEditPasswordRepeat.setVisibility(View.VISIBLE);
                    mEditName.setVisibility(View.VISIBLE);
                    mTextViewName.setVisibility(View.VISIBLE);
                    mButtonOk.setText("Регистрация");
                } else {
                    mTextViewPasswordRepeat.setVisibility(View.GONE);
                    mEditPasswordRepeat.setVisibility(View.GONE);
                    mEditName.setVisibility(View.GONE);
                    mTextViewName.setVisibility(View.GONE);
                    mButtonOk.setText("Войти");
                }
            }
        });

        mImageViewGoogleEnter.setOnClickListener(this::onClick);

    }

    @Override
    public void onClick(View v) {


        if (!googleApiAvailable) return;

        if (v.getId() == R.id.btn_register) {

            if (isRegister) {
                if (mEditName.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.please_input_name), Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            registerOrEnterUser();

        } else if (v.getId() == R.id.iv_google_enter) {

            signIn();
        }
    }

    private void signIn() {

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

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

                            Toast.makeText(RegisterActivity.this, "Аутентификация успешно пройдена.",
                                    Toast.LENGTH_SHORT).show();

                            // Создаеём пользователя получив информармацию из БД Firebase чтобы проинициализировать пользователя
                            // Для этого придётся перебрать список всех пользователей в БД Firestore для того чтобы проинициализировать и дать необходимый айдишник
                            FirebaseUser user = mAuth.getCurrentUser();
                            ServerManager.getInstance().updateUser(user.getEmail());

                            Intent intent = PreviewActivity.getIntent(RegisterActivity.this, KeyAccountOfType.KeysField.KEY_FIREBASE_EMAIL_ACCOUNT);
                            startNextActivity(intent);

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(RegisterActivity.this, "Ошибка аутентификации " + task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();

                            GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
                            googleApiAvailability.makeGooglePlayServicesAvailable(RegisterActivity.this);

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
                        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
                        googleApiAvailability.makeGooglePlayServicesAvailable(RegisterActivity.this);
                    }
                })
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "Регистрация пройденна успешно", Toast.LENGTH_LONG).show();

                            // Создаём нового пользователя как на устройстве так и в Firebase и обращаемся с ним по ID, которое получим
                            FirebaseUser user = mAuth.getCurrentUser();
                            ServerManager.getInstance().createNewUser(user.getEmail(), mEditPassword.getText().toString(), mEditName.getText().toString());

                            Intent intent = PreviewActivity.getIntent(RegisterActivity.this, KeyAccountOfType.KeysField.KEY_FIREBASE_EMAIL_ACCOUNT);
                            startNextActivity(intent);
                        }

                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }


    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {

            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            firebaseAuthWithGoogle(account);

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Toast.makeText(getApplicationContext(), R.string.registration_error, Toast.LENGTH_SHORT).show();
        }
    }


    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {


        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Intent intent = PreviewActivity.getIntent(RegisterActivity.this, KeyAccountOfType.KeysField.KEY_GOOGLE_ACCOUNT);
                            startNextActivity(intent);


                        } else {

                            mAuth.signOut();

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(RegisterActivity.this, "Ошибка аутентификации: " + task.getException().getMessage(),
                                            Toast.LENGTH_SHORT).show();

                                }
                            });

                            GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
                            googleApiAvailability.makeGooglePlayServicesAvailable(RegisterActivity.this);


                            mGoogleSignInClient.signOut();

                        }

                    }
                });
    }


}
