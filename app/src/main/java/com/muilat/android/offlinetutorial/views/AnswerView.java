package com.muilat.android.offlinetutorial.views;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Checkable;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.muilat.android.offlinetutorial.R;
import com.muilat.android.offlinetutorial.data.Quiz;

public class AnswerView extends RadioGroup implements RadioGroup.OnCheckedChangeListener {

    /**
     * Callback to report selection change events by returning
     * whether the correct or incorrect answer was selected.
     */
    public interface OnAnswerSelectedListener {
        /* Correct answer choice was selected */
        void onCorrectAnswerSelected();

        /* Incorrect answer choice was selected */
        void onWrongAnswerSelected();
    }

    private String mCorrectAnswer;
    private int correctIndex;
    private OnAnswerSelectedListener mSelectedListener;

    public AnswerView(Context context) {
        super(context);
        init(context);
    }

    public AnswerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        //Setup default parameters
        setGravity(Gravity.CENTER_VERTICAL);

        //Listen internally for change events
        setOnCheckedChangeListener(this);
    }

    /**
     * Attach on {@link OnAnswerSelectedListener} callback to this view.
     *
     * @param listener Callback to invoke when selections change.
     */
    public void setOnAnswerSelectedListener(OnAnswerSelectedListener listener) {
        mSelectedListener = listener;
    }

    /**
     * Set up the choices view with new data. Each option is given a
     * {@link RadioButton} to select. The correct answer is saved and
     * compared against the user choice.
     *
     * @param quizCursor List of possible answer choices. Correct answer should be in this list.
     */
    public void loadAnswers(Cursor quizCursor) {
//        quizCursor.moveToNext();
        Quiz quiz = new Quiz(quizCursor);
        mCorrectAnswer = quiz.getAnswer();
        String[] answers = quiz.getOptions();

        LayoutInflater inflater = LayoutInflater.from(getContext());
        removeAllViews();
        for (int i=0; i<answers.length;i++) {
            if(answers[i].equals(mCorrectAnswer)){
                correctIndex = i;
            }
            RadioButton button = (RadioButton) inflater.inflate(R.layout.quiz_item, this, false);
            button.setText(answers[i]);

            addView(button);
        }
    }

    /**
     * Returns whether the current selection matches the correct answer.
     */
    public boolean isCorrectAnswerSelected() {
        int checkedId = getCheckedRadioButtonId();
        RadioButton selected = findViewById(checkedId);

        return (selected != null) && TextUtils.equals(selected.getText(), mCorrectAnswer);
    }

    /**
     * Returns the index of the currently checked item.
     */
    public int getCheckedIndex() {
        View checked = findViewById(getCheckedRadioButtonId());
        return indexOfChild(checked);
    }

    /**
     * Set the currently checked item by index, rather than id.
     *
     * @param index View index to check.
     */
    public void setCheckedIndex(int index) {
        View child = getChildAt(index);
        if (child != null && child instanceof Checkable) {
            check(child.getId());
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (mSelectedListener != null) {
            if (isCorrectAnswerSelected()) {
                mSelectedListener.onCorrectAnswerSelected();
            } else {
                //check right Answer
                setCheckedIndex(correctIndex);
                mSelectedListener.onWrongAnswerSelected();
                group.setEnabled(false);
            }
        }
    }
}
