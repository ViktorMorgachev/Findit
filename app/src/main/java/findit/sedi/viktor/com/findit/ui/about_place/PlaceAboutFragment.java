package findit.sedi.viktor.com.findit.ui.about_place;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.fragment.app.Fragment;
import findit.sedi.viktor.com.findit.R;

public class PlaceAboutFragment extends Fragment {

    private ImageView mImageView;
    private TextView mTextView;
    public static final String KEY_PLACE_INFO = PlaceAboutFragment.class.getCanonicalName() + "KEY_PLACE_INFO";
    public static final String KEY_URL = PlaceAboutFragment.class.getCanonicalName() + "KEY_PLACE_INFO";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.information_about_places, container, false);
        initUI(rootView);


        return rootView;
    }

    private void initUI(View rootView) {

        mImageView = rootView.findViewById(R.id.iv_place_image);
        mTextView = rootView.findViewById(R.id.tv_place_info);
        mTextView.setText(getArguments().getString(KEY_PLACE_INFO));

      //  Glide.with(this).load(getArguments().getString(KEY_URL)).into(mImageView);

    }

    // Передам позицию и ссылку на вьюшку в виде строки
    public static PlaceAboutFragment newInstance(String placeInfo, String imageURL) {
        // Уcё, вопросов нет, осталcя один вопрос, как сохранить view
        PlaceAboutFragment guideSectionFragment = new PlaceAboutFragment();
        Bundle arguments = new Bundle();
        arguments.putString(KEY_PLACE_INFO, placeInfo);
        arguments.putString(KEY_URL, imageURL);
        guideSectionFragment.setArguments(arguments);
        return guideSectionFragment;
    }
}
