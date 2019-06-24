package findit.sedi.viktor.com.findit.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;

import findit.sedi.viktor.com.findit.R;
import findit.sedi.viktor.com.findit.ui.preloader.PreviewActivity;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int RC_UPDATE_APP = 201;
    private static final int RC_SIGN_IN = 202;

    // View
    private ImageView mImageViewGoogleEnter;

    // Logic
    private GoogleSignInClient mGoogleSignInClient;
    private boolean googleApiAvailable = false;
    private AppUpdateManager mAppUpdateManager;
    private InstallStateUpdatedListener mInstallStateUpdatedListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mAppUpdateManager = AppUpdateManagerFactory.create(this);


        mAppUpdateManager.getAppUpdateInfo()
                .addOnFailureListener(e -> {

                })
                .addOnSuccessListener(appUpdateInfo -> {
                    if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                            && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {

                        mInstallStateUpdatedListener = state -> {
                        };

                        mAppUpdateManager.registerListener(mInstallStateUpdatedListener);

                        try {
                            mAppUpdateManager.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.FLEXIBLE, this, RC_UPDATE_APP);
                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                        }
                    }
                });

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
        GoogleSignInAccount googleSignInAccount = GoogleSignIn.getLastSignedInAccount(this);


        updateUI(googleSignInAccount);

    }


    private void updateUI(GoogleSignInAccount googleSignInAccount) {


        googleApiAvailable = isGooglePlayServicesAvailable(this);

        // Если пользователь зарегестрированн  в системе, вытаскиваем из преференсов данные
        // То сразу переходим в
        if (googleSignInAccount == null) {

            setContentView(R.layout.register_layout);

            Toast.makeText(this, "Не зарегестрированный", Toast.LENGTH_LONG).show();

            mImageViewGoogleEnter = findViewById(R.id.iv_google_enter);


            setUIListeners();


            mImageViewGoogleEnter.setOnClickListener(this::onClick);

        }


        if (googleSignInAccount != null) {
            finditAuthWithGoogle(googleSignInAccount);
            return;
        }


    }

    private void startNextActivity(Intent intent) {

        if (mInstallStateUpdatedListener != null)
            mAppUpdateManager.unregisterListener(mInstallStateUpdatedListener);

        startActivity(intent);

    }

    private void setUIListeners() {
        mImageViewGoogleEnter.setOnClickListener(this::onClick);

    }

    @Override
    public void onClick(View v) {


        if (!googleApiAvailable) return;


        if (v.getId() == R.id.iv_google_enter) {

            signIn();
        }
    }

    private void signIn() {

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

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

        if (requestCode == RC_UPDATE_APP) {
            if (resultCode == RESULT_OK) {

            }
        }
    }


    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {

            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            finditAuthWithGoogle(account);


        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Toast.makeText(getApplicationContext(), R.string.registration_error, Toast.LENGTH_SHORT).show();
        }
    }


    private void finditAuthWithGoogle(GoogleSignInAccount acct) {


        String photoUrl = acct.getPhotoUrl() != null ? acct.getPhotoUrl().toString() : "";

        Intent intent = PreviewActivity.getIntent(RegisterActivity.this, "byGoogle", acct.getEmail(), acct.getDisplayName(), photoUrl);
        startNextActivity(intent);

    }


}
