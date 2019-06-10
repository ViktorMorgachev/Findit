package findit.sedi.viktor.com.findit.ui.find_tainik.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import findit.sedi.viktor.com.findit.R;
import findit.sedi.viktor.com.findit.common.ManagersFactory;
import findit.sedi.viktor.com.findit.data_providers.data.QrPoint;

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


        mQrPoint = ManagersFactory.getInstance().getQrPointManager().getQrPlaceByID(getArguments().getString(KEY_QR_POINT_ID));

        mImageView = view.findViewById(R.id.iv_tip_photo);
        mButton = view.findViewById(R.id.btn_ok);
        mTextViewTipForNext = view.findViewById(R.id.tv_tip_for_next);
        mButtonBonus = view.findViewById(R.id.btn_bonuses);
        mImageView.setVisibility(View.GONE);
        mTextViewTip = view.findViewById(R.id.tv_tip);
        mButtonBonus.setText(String.valueOf(getArguments().getInt(KEY_BONUS)));


        if (getArguments().getBoolean(KEY_SUCCESS) == false) {
            mTextViewTip.setText("");
            mTextViewDiscribe.setText(getResources().getString(R.string.sorry_but_you_get_some_tips));
        }

        mTextViewTipForNext.setText(getResources().getString(R.string.next_tainik) + " " + mQrPoint.getTipForNextQrPoint());


        initView();


        return view;
    }

    private void initView() {

        mButton.setOnClickListener(this::onClick);


    }

    @Override
    public void onClick(View v) {

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
