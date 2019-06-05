package findit.sedi.viktor.com.findit.ui.find_tainik;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;

import findit.sedi.viktor.com.findit.R;
import findit.sedi.viktor.com.findit.common.ManagersFactory;
import findit.sedi.viktor.com.findit.data_providers.data.QrPoint;
import findit.sedi.viktor.com.findit.ui.find_tainik.fragments.QuestTainikFragment;

import static findit.sedi.viktor.com.findit.ui.find_tainik.DiscoveredTainikActivity.POINT_ID;

public class QuestTainikActivity extends AppCompatActivity implements QuestTainikFragment.OnButtonClickListener {


    private Fragment mFragment;
    private FragmentManager mFragmentManager;
    private QrPoint mQrPoint;
    static int count = 0;
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
        Intent intent = new Intent(context, DiscoveredTainikActivity.class);
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

        ManagersFactory.getInstance().getAccountManager().getUser().setBonus(ManagersFactory.getInstance().getAccountManager().getUser().getBonus() + mQrPoint.getQuestBonus());
        showFragment();

    }

    private void showFragment() {


        if (count > mQrPoint.getQuests().size()) {
            Toast.makeText(getApplicationContext(), "Вы ответили на все вопросы", Toast.LENGTH_SHORT).show();
            // Обновляем пользователя на сервере, его бонусы

            return;
        }


        mFragment = mFragmentManager.findFragmentById(R.id.fragment);
        if (mFragment == null) {
            mFragment = QuestTainikFragment.newInstance(mArrayListAnswers.get(count), Objects.requireNonNull(mQrPoint.getQuests().get(mArrayListAnswers.get(count))));
            mFragmentManager.beginTransaction()
                    .add(R.id.fragment, mFragment)
                    .commit();


        } else {
            mFragment = QuestTainikFragment.newInstance(mArrayListAnswers.get(count), Objects.requireNonNull(mQrPoint.getQuests().get(mArrayListAnswers.get(count))));
            mFragmentManager.beginTransaction()
                    .replace(R.id.fragment, mFragment)
                    .commit();
        }

        count++;

    }


    @Override
    public void onWrongAnswer() {

        // Обновляем бонусы пользователя не сервере, сколько он получил

    }
}
