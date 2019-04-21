package findit.sedi.viktor.com.findit.ui.about_place;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import findit.sedi.viktor.com.findit.R;
import findit.sedi.viktor.com.findit.data.Place;

public class PlaceAboutActivity extends AppCompatActivity {

    // После того как запустили активность с местоположением. нужно будет отметить у себя (и возможно на сервере?),
    // Координаты где эта активность запустилась и показывать только пользователю информацию об этом, с возможностью скрыть эти точки на карте
    // И просмотреть в списке найденных мест в меню
    // Что пользователь набрёл на место чтобы эта активность не показывалась больше пока он снова в это место не придёт

    private ImageView mImageView;
    private TextView mTextView;
    private Place mPlace;
    public static final String KEY_PLACE_ID = PlaceAboutActivity.class.getCanonicalName() + "KEY_PLACE_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.information_about_places);

        initUI();


        if (getIntent() != null) {

          /*  if (getIntent().getLongExtra(KEY_PLACE_ID) != null)
                mTextView.setText(getIntent().getStringExtra(KEY_PLACE_ID));*/

            //       Glide.with(this).load(getIntent().getStringExtra(KEY_URL)).into(mImageView);
            // else mImageView.setImageDrawable(getDrawable(R.drawable.googleg_standard_color_18));
        }

    }

    private void initUI() {
        mImageView = findViewById(R.id.iv_place_image);
        mTextView = findViewById(R.id.tv_place_info);
    }


}
