package findit.sedi.viktor.com.findit.ui.find_tainik;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import findit.sedi.viktor.com.findit.R;
import findit.sedi.viktor.com.findit.common.ManagersFactory;
import findit.sedi.viktor.com.findit.data_providers.data.QrPoint;
import io.opencensus.tags.TagValue;

// Сюда передадим обьект QrPlace или QrPlace и будем использовать либо инстансы либо интерфейсы необходимые
// в зависимости от типа тайника
public class DiscoveredTainikActivity extends AppCompatActivity {


    // Views
    private RatingBar mRatingBar;
    private TextView mTextView;

    public static final String POINT_ID = "pointID";
    private QrPoint mQrPoint;
    public static final int LAYOUT_RES_ID = R.layout.nearby_tainik_layout;

    public static Intent getIntent(Context context, String qrPointID) {
        Intent intent = new Intent(context, DiscoveredTainikActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.putExtra(POINT_ID, qrPointID);
        return intent;
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT_RES_ID);

        mRatingBar = findViewById(R.id.rt_dificulty);
        mTextView = findViewById(R.id.tv_bonus);


        readIntentExtras();


        mTextView.setText(mTextView.getText() + " " + (int) mQrPoint.getBonus());
        mRatingBar.setRating(mQrPoint.getDifficulty());
    }


    public void onClick(View view) {

        startActivity(new Intent(this, QuestTainikActivity.class));

    }

    private void readIntentExtras() {
        if (getIntent().getExtras() != null) {
            String mQrPointID = getIntent().getExtras().getString(POINT_ID);
            mQrPoint = ManagersFactory.getInstance().getQrPointManager().getQrPlaceByID(mQrPointID);
        }

    }
}