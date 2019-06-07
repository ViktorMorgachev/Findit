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

public class TipTainikFragment extends Fragment implements View.OnClickListener {

    // Views
    private ImageView mImageView;
    private Button mButton;
    private Button mButtonBonus;
    private TextView mTextViewTip;
    private TextView mTextViewTipForNext;

    private QuestTainikFragment.OnButtonClickListener mCallBackClickListener;

    static final String KEY_BONUS = "bonus";
    static final String KEY_IMAGE = "image_url";
    static final String KEY_TIP_FOR_NEXT = "tip_for_next";
    static final String KEY_TIP_FOR_CURRENT = "tip_for_current";


    // Передам позицию и ссылку на вьюшку в виде строки
    public static TipTainikFragment newInstance(String tipForNext, String tipForCurrent, String ImageUrl, int bonus) {
        TipTainikFragment tipTainikFragment = new TipTainikFragment();
        Bundle arguments = new Bundle();
        arguments.putInt(KEY_BONUS, bonus);
        arguments.putString(KEY_IMAGE, ImageUrl);
        arguments.putString(KEY_TIP_FOR_CURRENT, tipForCurrent);
        arguments.putString(KEY_TIP_FOR_NEXT, tipForNext);


        tipTainikFragment.setArguments(arguments);
        return tipTainikFragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tip_for_tainik, null);


        mImageView = view.findViewById(R.id.iv_tip_photo);
        mButton = view.findViewById(R.id.btn_ok);
        mTextViewTip = view.findViewById(R.id.tv_tip);
        mTextViewTipForNext = view.findViewById(R.id.tv_tip_for_next);
        mButtonBonus = view.findViewById(R.id.btn_bonuses);


        mImageView.setVisibility(View.GONE);

        mTextViewTip.setText(getResources().getString(R.string.tip_for_current) + " " + getArguments().getString(KEY_TIP_FOR_CURRENT));
        mTextViewTipForNext.setText(getResources().getString(R.string.next_tainik) + " " + getArguments().getString(KEY_TIP_FOR_NEXT));
        mButtonBonus.setText(String.valueOf(getArguments().getInt(KEY_BONUS)));


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
