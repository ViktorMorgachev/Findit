package findit.sedi.viktor.com.findit.ui.find_tainik.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import findit.sedi.viktor.com.findit.App;
import findit.sedi.viktor.com.findit.R;
import findit.sedi.viktor.com.findit.data_providers.Prefs;
import findit.sedi.viktor.com.findit.data_providers.data.QrPoint;
import findit.sedi.viktor.com.findit.interactors.KeyPrefs;

public class TipTainikFragment extends Fragment implements View.OnClickListener {

    // Views
    private ImageView mImageView;
    private Button mButton;
    private QrPoint mQrPoint;
    private Button mButtonBonus;
    private TextView mTextViewTip;
    private TextView mTextViewTipForNext, mTextViewDiscribe;

    private QuestTainikFragment.OnButtonClickListener mCallBackClickListener;

    static final String KEY_BONUS = "bonus";
    static final String KEY_QR_POINT_ID = "qr_point_id";
    static final String KEY_SUCCESS = "key_success";


    // Передам позицию и ссылку на вьюшку в виде строки
    public static TipTainikFragment newInstance(String QrPointID, int bonus, boolean success) {
        TipTainikFragment tipTainikFragment = new TipTainikFragment();
        Bundle arguments = new Bundle();
        arguments.putString(KEY_QR_POINT_ID, QrPointID);
        arguments.putInt(KEY_BONUS, bonus);
        arguments.putBoolean(KEY_SUCCESS, success);


        tipTainikFragment.setArguments(arguments);
        return tipTainikFragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tip_for_tainik, null);


        mQrPoint = App.instance.getQrPointManager().getQrPlaceByID(getArguments().getString(KEY_QR_POINT_ID));

        mImageView = view.findViewById(R.id.iv_tip_photo);
        mButton = view.findViewById(R.id.btn_ok);
        mTextViewTipForNext = view.findViewById(R.id.tv_tip_for_next);
        mButtonBonus = view.findViewById(R.id.btn_bonuses);
        mImageView.setVisibility(View.GONE);
        mTextViewTip = view.findViewById(R.id.tv_tip);
        mTextViewDiscribe = view.findViewById(R.id.tv_discribe);
        mButtonBonus.setText(String.valueOf(getArguments().getInt(KEY_BONUS)));

        // Отправляем в преференсы, информацию о  том что мы уже ответили на все вопросы, отправим ID тайника

        Prefs prefs = Prefs.getInstance();
        prefs.setContext(getContext());


        initView();

        mTextViewTipForNext.setText(getResources().getString(R.string.next_tainik) + ": " + mQrPoint.getTipForNextQrPoint());

        // Если уже раньше успешно отвечали на этот вопрос
        if (prefs.getStringSetByKey(KeyPrefs.KeysField.KEY_GET_BONUS_FROM_DISCOVERED_QRPOINT).contains(mQrPoint.getID())) {
            mButtonBonus.setVisibility(View.GONE);
            mTextViewDiscribe.setText(getResources().getString(R.string.sorry_but_you_get_bonus));

        } else {


            if (getArguments().getBoolean(KEY_SUCCESS) == false) {
                mTextViewTip.setText("");
                mTextViewTip.setVisibility(View.GONE);
                mTextViewDiscribe.setText(getResources().getString(R.string.sorry_but_you_get_some_tips));
            } else {

                Prefs.getInstance().addStringValue(KeyPrefs.KeysField.KEY_GET_BONUS_FROM_DISCOVERED_QRPOINT, mQrPoint.getID());

                mTextViewTip.setText(mTextViewTip.getText().toString() + ": " + mQrPoint.getTip());
            }


        }


        return view;
    }

    private void initView() {

        mButton.setOnClickListener(this::onClick);


    }

    @Override
    public void onClick(View v) {

        // Тут нам нужно убрать слушатель с карты

        mCallBackClickListener.onBackPress();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallBackClickListener = (QuestTainikFragment.OnButtonClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnButtonClickListener");
        }
    }

}
