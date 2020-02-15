package com.yasdalteam.yasdalege;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AnswerSection extends ViewHolder
{
    private final int messageRightStrokeColor = Color.parseColor("#D5FF2F");
    private final int messageWrongStrokeColor = Color.parseColor("#F17357");
    private final int messageRightAnswerStrokeColor = Color.parseColor("#f2f2f2");
    private final int messageStrokeWidth = 4;

    @BindView(R.id.etAnswerField) EditText answerFieldView;
    @BindView(R.id.btnShowAnswer) Button btnShowAnswer;
    @BindView(R.id.btnAnswer) Button btnAnswer;
    @BindView(R.id.tvAnswerMessage) TextView tvAnswerMessage;

    private boolean isAnswerRight = false;
    private boolean isRightAnswerShown = false;

    private GradientDrawable tvAnswerMessageBackground;

    public AnswerSection(View view, ExercisesListAdapter.Listener listener)
    {
        super(view, listener);
        ButterKnife.bind(this, view);
    }

    @Override
    public void bindExercise(Exercise exercise)
    {
        super.bindExercise(exercise);

        tvAnswerMessageBackground = ((GradientDrawable)tvAnswerMessage.getBackground().mutate());

        if (tvAnswerMessage.getVisibility() == View.VISIBLE)
        {
            tvAnswerMessage.setVisibility(View.INVISIBLE);
        }
        if (exercise.getAnswer() != null)
        {
            answerFieldView.setText(exercise.getAnswer());
            answer(exercise.getAnswer());
        }
        else
        {
            answerFieldView.setText("");
        }
    }

    @OnClick(R.id.btnShowAnswer)
    public void onShowAnswerClick()
    {
        showOrHideRightAnswer();
    }
    @OnClick(R.id.btnAnswer)
    public void onAnswerClick()
    {
        answer(answerFieldView.getText().toString());
    }

    public boolean isAnswerRight()
    {
        return isAnswerRight;
    }
    public boolean isRightAnswerShown()
    {
        return isRightAnswerShown;
    }

    private void setIsRightAnswerShown(boolean isRightAnswerShown)
    {
        if (!isRightAnswerShown)
        {
            btnShowAnswer.setHint("Показать ответ");
        }
        else
        {
            btnShowAnswer.setHint("Скрыть ответ");
        }
        this.isRightAnswerShown = isRightAnswerShown;
    }

    public void showOrHideRightAnswer()
    {
        if (!isRightAnswerShown())
        {
            tvAnswerMessage.setVisibility(View.VISIBLE);
            tvAnswerMessage.setHint("Правильный ответ: " + exercise.getRightAnswer());
            tvAnswerMessageBackground.setStroke(messageStrokeWidth, messageRightAnswerStrokeColor);
            setIsRightAnswerShown(true);
        }
        else
        {
            tvAnswerMessage.setVisibility(View.INVISIBLE);
            setIsRightAnswerShown(false);
        }
    }

    public void answer(String answer)
    {
        if (checkAnswer(answer, exercise.getRightAnswer(), exercise.getAnswerType()))
        {
            tvAnswerMessage.setVisibility(View.VISIBLE);
            tvAnswerMessage.setHint("Верно!");
            tvAnswerMessageBackground.setStroke(messageStrokeWidth, messageRightStrokeColor);
            isAnswerRight = true;
            if (!exercise.isCompleted() && App.getInstance().getUser().isAuthorized())
            {
                onAddToCompletedClick();
            }
        }
        else
        {
            tvAnswerMessage.setVisibility(View.VISIBLE);
            tvAnswerMessage.setHint("Не верно!");
            tvAnswerMessageBackground.setStroke(messageStrokeWidth, messageWrongStrokeColor);
            isAnswerRight = false;
        }

        exercise.setAnswer(answer);
        exercise.setAnsweredRight(isAnswerRight);

        setIsRightAnswerShown(false);
    }

    private boolean checkAnswer(String answer, String rightAnswer, String answerType)
    {
        switch (answerType)
        {
            case "ordered":
                return answer.equals(rightAnswer);
            case "unordered":
                Set<Character> answerSet = StringUtils.stringToCharacterSet(answer);
                Set<Character> rightAnswerSet = StringUtils.stringToCharacterSet(rightAnswer);
                return StringUtils.containsAllChars(answer, rightAnswer) && answer.length() == rightAnswer.length();
            default:
                return false;
        }
    }
}