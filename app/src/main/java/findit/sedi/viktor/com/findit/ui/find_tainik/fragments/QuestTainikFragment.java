package findit.sedi.viktor.com.findit.ui.find_tainik.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

import findit.sedi.viktor.com.findit.R;

public class QuestTainikFragment extends Fragment implements View.OnClickListener {


    // Views
    private TextView mTextView;
    private Button mButton;
    private View mView;
    private RadioGroup mRadioGroup;


    private String mSelectedAnwer;
    private String mRightAnswer;
    private ArrayList<String> mAnswers;
    private String mQuestion;

    private OnButtonClickListener mCallBackClickListener;

    static final String KEY_QUESTION = "question_key";
    static final String KEY_ANSWERS = "answers_key";
    static final String KEY_RIGHT_ANSWER = "right_answer";


    // Передам позицию и ссылку на вьюшку в виде строки
    public static QuestTainikFragment newInstance(String question, ArrayList<String> answers) {
        QuestTainikFragment guideSectionFragment = new QuestTainikFragment();
        Bundle arguments = new Bundle();
        arguments.putString(KEY_QUESTION, question);
        arguments.putString(KEY_RIGHT_ANSWER, answers.get(0));
        Collections.shuffle(answers);
        arguments.putStringArrayList(KEY_ANSWERS, answers);
        guideSectionFragment.setArguments(arguments);
        return guideSectionFragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.quest_tainik_layout, null);


        mRightAnswer = getArguments().getString(KEY_RIGHT_ANSWER);
        mAnswers = getArguments().getStringArrayList(KEY_ANSWERS);
        mQuestion = getArguments().getString(KEY_QUESTION);


        mTextView = view.findViewById(R.id.tv_question);
        mButton = view.findViewById(R.id.btn_next);
        mRadioGroup = view.findViewById(R.id.rg_answers);

        if (mAnswers.size() == 3) {
            ((RadioButton) view.findViewById(R.id.rb_1)).setText(mAnswers.get(0));
            ((RadioButton) view.findViewById(R.id.rb_2)).setText(mAnswers.get(1));
            ((RadioButton) view.findViewById(R.id.rb_3)).setText(mAnswers.get(2));
        }

        mTextView.setText(mQuestion);

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId){
                    case R.id.rb_1:
                        mSelectedAnwer = mAnswers.get(0);
                        break;
                    case R.id.rb_2:
                        mSelectedAnwer = mAnswers.get(1);
                        break;
                    case R.id.rb_3:
                        mSelectedAnwer = mAnswers.get(2);
                        break;
                }

            }
        });



        initView();

        mView = view;

        return view;
    }

    private void initView() {

        mButton.setOnClickListener(this::onClick);


    }

    @Override
    public void onClick(View v) {

        if (mSelectedAnwer == null || mSelectedAnwer.equalsIgnoreCase("")) {
            Toast.makeText(getContext(), "Выберите хотя - бы один ответ", Toast.LENGTH_SHORT).show();
            return;
        }

        // Если ответ верный
        if (((RadioButton) mView.findViewById(mRadioGroup.getCheckedRadioButtonId())).getText().toString().equalsIgnoreCase(mRightAnswer)) {
            mCallBackClickListener.onRightAnswer();
            Toast.makeText(getContext(), "Верно", Toast.LENGTH_SHORT).show();
        } else mCallBackClickListener.onWrongAnswer();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallBackClickListener = (OnButtonClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnButtonClickListener");
        }
    }

    public interface OnButtonClickListener {
        void onRightAnswer();
        void onBackPress();
        void onWrongAnswer();
    }


}
