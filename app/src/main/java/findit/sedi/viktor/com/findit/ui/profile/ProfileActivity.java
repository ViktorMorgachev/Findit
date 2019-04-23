package findit.sedi.viktor.com.findit.ui.profile;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.appcompat.app.AppCompatActivity;
import findit.sedi.viktor.com.findit.R;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    // View
    private TextView mTextViewBonus;
    private TextView mTextViewSignOut;
    private EditText mEditTextName;
    private EditText mEditTextEmail;
    private EditText mEditTextPassword;

    //Logic
    private FirebaseUser mFirebaseUser;

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

        mTextViewSignOut = findViewById(R.id.tv_sign_out);
        mEditTextEmail = findViewById(R.id.et_email);
        mEditTextPassword = findViewById(R.id.et_password);

        mTextViewSignOut.setOnClickListener(this::onClick);

        mEditTextEmail.setText(mFirebaseUser.getEmail());
        mEditTextPassword.setText(mFirebaseUser.getUid());


    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.tv_sign_out) {
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(this, "Пользователь вышел", Toast.LENGTH_LONG).show();
            // После нужно выйти в главное окно регистрации
        }

    }
}
