package findit.sedi.viktor.com.findit.ui.find_tainik;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;

import findit.sedi.viktor.com.findit.R;
import findit.sedi.viktor.com.findit.common.ManagersFactory;
import findit.sedi.viktor.com.findit.data_providers.cloud.myserver.ServerManager;
import findit.sedi.viktor.com.findit.data_providers.data.QrPoint;
import findit.sedi.viktor.com.findit.data_providers.data.User;
import findit.sedi.viktor.com.findit.interactors.KeyCommonUpdateUserRequests;
import findit.sedi.viktor.com.findit.ui.find_tainik.fragments.QuestTainikFragment;
import findit.sedi.viktor.com.findit.ui.find_tainik.fragments.TipTainikFragment;

import static findit.sedi.viktor.com.findit.ui.find_tainik.NearbyTainikActivity.POINT_ID;

public class QuestTainikActivity extends AppCompatActivity implements QuestTainikFragment.OnButtonClickListener {


    private Fragment mFragment;
    private FragmentManager mFragmentManager;
    private QrPoint mQrPoint;
    private User mUser = ManagersFactory.getInstance().getAccountManager().getUser();
    static int count = 0;
    public int bonus = 0;
    private ArrayList<String> mArrayListAnswers = new ArrayList<>();

    public static final int LAYOUT_RES_ID = R.layout.fragment_layout;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT_RES_ID);

        readIntentExtras();

        mFragmentManager = getSupportFragmentManager();


        showFragment();

    }

    public static Intent getIntent(Context context, String qrPointID) {
        Intent intent = new Intent(context, NearbyTainikActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.putExtra(POINT_ID, qrPointID);
        return intent;
    }

    private void readIntentExtras() {
        if (getIntent().getExtras() != null) {
            String mQrPointID = getIntent().getExtras().getString(POINT_ID);
            mQrPoint = ManagersFactory.getInstance().getQrPointManager().getQrPlaceByID(mQrPointID);

            mArrayListAnswers.addAll(mQrPoint.getQuests().keySet());
        }

    }


    @Override
    public void onRightAnswer() {

        bonus = (int) (bonus + mQrPoint.getQuestBonus());

        ManagersFactory.getInstance().getAccountManager().getUser().addBonus(mQrPoint.getQuestBonus());

        ServerManager.getInstance().updateUserOnServer(KeyCommonUpdateUserRequests.KeysField.KEY_UPDATE_BONUS);

        showFragment();

    }

    @Override
    public void onBackPress() {
        onBackPressed();
    }


    @Override
    public void onWrongAnswer() {

        showResultAndTipFragmant(false);

    }


    private void showFragment() {


        if (count == mQrPoint.getQuests().size()) {
            Toast.makeText(getApplicationContext(), "Вы ответили на все вопросы", Toast.LENGTH_SHORT).show();
            // Обновляем пользователя на сервере, его бонусы

            showResultAndTipFragmant(true);
            return;
        }

        mFragment = QuestTainikFragment.newInstance(mArrayListAnswers.get(count), Objects.requireNonNull(mQrPoint.getQuests().get(mArrayListAnswers.get(count))));
        if (mFragmentManager.getFragments().size() == 0) {
            mFragmentManager.beginTransaction()
                    .add(R.id.fragment, mFragment)
                    .commit();
        } else {
            mFragmentManager.beginTransaction()
                    .replace(R.id.fragment, mFragment)
                    .commit();
        }


        count++;

    }

    private void showResultAndTipFragmant(boolean success) {


        mFragment = TipTainikFragment.newInstance(mQrPoint.getID(), bonus, success);
        mFragmentManager.beginTransaction()
                .replace(R.id.fragment, mFragment)
                .commit();

    }


}
